package com.biyoex.app.market.fragment


import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import com.biyoex.app.R
import com.biyoex.app.VBTApplication.FALL_COLOR
import com.biyoex.app.VBTApplication.RISE_COLOR
import com.biyoex.app.common.base.RxBus
import com.biyoex.app.common.base.RxBusData
import com.biyoex.app.common.mvpbase.BaseFragment
import com.biyoex.app.common.okhttp.SocketUtils
import com.biyoex.app.common.utils.MoneyUtils
import com.biyoex.app.common.utils.log.Log
import com.biyoex.app.common.widget.MyViewPager
import com.biyoex.app.home.bean.CoinTradeRankBean
import com.biyoex.app.market.presenter.NowMarketPersenter
import com.biyoex.app.market.view.NowMarketView
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_now_market.*
import org.json.JSONArray
import org.json.JSONException
import java.math.BigDecimal
import kotlin.collections.MutableList as MutableList1

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *    tag 用来区分是k线调用或者交易中心调用，xml布局不一样 type是用来viewpager区分高度
 */
@SuppressLint("ValidFragment")
class NowMarketFragment() : BaseFragment<NowMarketPersenter>(), NowMarketView, SocketUtils.OnReceiveMessageListener {
    private var soldRecordAdapter: BaseQuickAdapter<List<String>, BaseViewHolder>? = null
    private var soldRecordList: MutableList1<List<String>> = mutableListOf()
    private var priceDecimal: Int = 0
    private var amonutDecimal: Int = 0
    private var subscribe: Disposable? = null
    private var mType:Int = 0
    var myViewPager: MyViewPager?=null
    var coin: CoinTradeRankBean.DealDatasBean?=null
    var mtag: Int = 0
    override fun getCoinMarketInfoSuccess(sellList: MutableList1<MutableList1<String>>) {
        soldRecordAdapter!!.setNewData(sellList as List<List<String>>)
    }
    constructor( mType: Int,  myViewPager: MyViewPager,  coin: CoinTradeRankBean.DealDatasBean,  mtag: Int):this(){
        this.mtag = mtag
        this.mType = mType
        this.myViewPager = myViewPager
        this.coin = coin
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = super.onCreateView(inflater, container, savedInstanceState)
        myViewPager?.setObjectForPosition(rootView, mType)
        return rootView
    }

    override fun initImmersionBar() {
        if (mtag != 0) {
            immersionBar!!.statusBarDarkFont(false).init()
        }
    }
    override fun createPresent(): NowMarketPersenter = NowMarketPersenter()
    override fun getLayoutId(): Int = if (mtag == 0) R.layout.fragment_now_market else R.layout.fragment_new_deal
    override fun initComp() {
        soldRecordAdapter = object : BaseQuickAdapter<List<String>, BaseViewHolder>(if (mtag == 0) R.layout.item_market_sold_record else R.layout.item_sold_record, soldRecordList) {
            override fun convert(helper: BaseViewHolder, item: List<String>) {
                if (item[3] == "2") {
                    helper.setTextColor(R.id.tv_buy_and_sell, FALL_COLOR)
                    helper.setText(R.id.tv_buy_and_sell, R.string.sell)
                } else {
                    helper.setTextColor(R.id.tv_buy_and_sell, RISE_COLOR)
                    helper.setText(R.id.tv_buy_and_sell, R.string.buy)
                }
                coin?.let {
                    helper.setText(R.id.tv_deal_price, "${MoneyUtils.decimalByUp(it.priceDecimals, BigDecimal(item[0]))}")
                    helper.setText(R.id.tv_volume, "${MoneyUtils.decimalByUp(it.amountDecimals, BigDecimal(item[1]))}")
                }
                helper.setText(R.id.tv_timer, item[2].split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1])
            }
        }
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = soldRecordAdapter
        soldRecordAdapter!!.emptyView = View.inflate(context, R.layout.view_empty, null);
    }

    @SuppressLint("CheckResult")
    override fun initData() {
        SocketUtils.getIns().setOnReceiveMessageListener(this)
        mPresent?.requestMarketRefresh("${coin!!.fid}")
        subscribe = RxBus.get().toFlowable().map { o -> o as RxBusData }.subscribe { rxBusData ->
            if (rxBusData.msgName == if (mtag == 0) "CoinData" else "KlineData") {
                Log.e("NowMarketFragment" + rxBusData.msgName)
                coin = rxBusData.msgData as CoinTradeRankBean.DealDatasBean
                priceDecimal = coin!!.priceDecimals
                amonutDecimal = coin!!.priceDecimals
                mPresent?.requestMarketRefresh("${coin!!.fid}")
            }
        }
    }

    /**
     * 刷新历史记录
     *
     * @param response
     * @throws JSONException
     */
    fun entrustLog(response: String) {
        soldRecordList.clear()
        val data = Gson().fromJson<MutableList1<MutableList1<String>>>(response, object : TypeToken<MutableList1<MutableList1<String>>>() {
        }.type)
        soldRecordList.addAll(data)
        if (isDetached) {
            return
        }
        soldRecordAdapter!!.setNewData(soldRecordList)
    }

    override fun onMessage(text: String?) {
        var jsonObject = JSONArray(text)
        if (jsonObject[0] == "entrust-log") {
            val log = jsonObject.getString(1)
            activity!!.runOnUiThread { entrustLog(log) }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        SocketUtils.getIns().removeListener(this)
        if (subscribe != null) {
            subscribe!!.dispose()
        }
    }
}

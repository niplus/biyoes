package com.biyoex.app.market.fragment


import android.annotation.SuppressLint
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

import com.biyoex.app.R
import com.biyoex.app.VBTApplication.FALL_COLOR
import com.biyoex.app.VBTApplication.RISE_COLOR
import com.biyoex.app.common.base.RxBus
import com.biyoex.app.common.base.RxBusData
import com.biyoex.app.common.data.CoinLiveData
import com.biyoex.app.common.data.SessionLiveData
import com.biyoex.app.common.mvpbase.BaseFragment
import com.biyoex.app.common.okhttp.SocketUtils
import com.biyoex.app.common.utils.MoneyUtils
import com.biyoex.app.common.widget.MyViewPager
import com.biyoex.app.common.widget.RatioLinearLayout
import com.biyoex.app.home.bean.CoinTradeRankBean
import com.biyoex.app.market.presenter.MineEntrustOrderPersenter
import com.biyoex.app.market.view.MineEntrustOrderView
import com.biyoex.app.trading.bean.RefreshUserInfoBean
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_mine_entrust_order.*
import org.json.JSONArray
import java.math.BigDecimal
import java.text.SimpleDateFormat

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * 交易页面当前委托
 *
 */
@SuppressLint("ValidFragment")
class MineEntrustOrderFragment() : BaseFragment<MineEntrustOrderPersenter>(), MineEntrustOrderView, SocketUtils.OnReceiveMessageListener {
    private var mCurrentEntrustAdapter: BaseQuickAdapter<List<String>, BaseViewHolder>? = null
    private var mUnfinishedList: MutableList<List<String>> = mutableListOf()
    lateinit var subscribe: Disposable
    override fun createPresent(): MineEntrustOrderPersenter = MineEntrustOrderPersenter()
    override fun getLayoutId(): Int = R.layout.fragment_mine_entrust_order
    var mType: Int = 0
    var myViewPager: MyViewPager? = null
    lateinit var coin: CoinTradeRankBean.DealDatasBean

    constructor(mType: Int, myViewPager: MyViewPager, coin: CoinTradeRankBean.DealDatasBean) : this() {
        this.mType = mType
        this.myViewPager = myViewPager
        this.coin = coin
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = super.onCreateView(inflater, container, savedInstanceState)
        myViewPager?.setObjectForPosition(rootView, mType)
        return rootView
    }

    override fun initComp() {

        CoinLiveData.getIns(context!!).observe(this, Observer {
            coin = it
            coin.let {
                mPresent?.getRefreshUserInfo(it.fid)
            }
        })
//        coin = CoinLiveData.getIns(context!!).value


        mCurrentEntrustAdapter = object : BaseQuickAdapter<List<String>, BaseViewHolder>(R.layout.item_market_entrust, mUnfinishedList) {
            override fun convert(helper: BaseViewHolder, item: List<String>) {
                //                helper.setText(R.id.tv_currency_abbreviated, coin.getFname_sn().replaceAll(" ", ""));
                //0:买卖，1:委托价格，2:未成交数量，3:主键ID，4:创建时间，5:状态（1未成交 ，2 部分成交，3完全成交，4已取消），6:委托数量，7:成交均价
                val ratioRightLayout = helper.itemView.findViewById<RatioLinearLayout>(R.id.item_market_entrust_progressbar)
                ratioRightLayout.setStartRight(false)
                if (item[0] == "1") {
                    helper.setTextColor(R.id.item_market_entrust_isbuy, FALL_COLOR)
                    helper.setText(R.id.item_market_entrust_isbuy, R.string.sell)
                            .setImageDrawable(R.id.item_market_entrust_buyandsell, resources.getDrawable(R.mipmap.icon_small_sell))
                    ratioRightLayout.setPaintColor(FALL_COLOR)
                } else {
                    helper.setText(R.id.item_market_entrust_isbuy, R.string.buy)
                    helper.setImageDrawable(R.id.item_market_entrust_buyandsell, resources.getDrawable(R.mipmap.icon_small_buy))
                    ratioRightLayout.setPaintColor(RISE_COLOR)
                }

                //                    int status = dataJson.getInt(5);
                //                helper.setVisible(R.id.tv_cancel, true);
                //                helper.setVisible(R.id.tv_status, false);
                //                arr[0]买卖类型 0买，1卖
                //                arr[1] 单价
                //                arr[2] 剩余未成交数量
                //                arr[3] 订单id
                //                arr[4] 时间戳
                //                arr[5] 状态 1未成交 ，2 部分成交，3完全成交，4已取消
                //                arr[6] 挂单总数量
                //                arr[7]成交均价
                //成交总量
                val success_total = BigDecimal(item[6]).subtract(BigDecimal(item[2])).setScale(coin.amountDecimals, BigDecimal.ROUND_DOWN).toDouble()
                val user_Number = MoneyUtils.decimalByUp(coin.amountDecimals, BigDecimal(item[6])).setScale(coin.amountDecimals, BigDecimal.ROUND_DOWN).toPlainString()
                val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                helper.setText(R.id.item_market_entrust_time, simpleDateFormat.format(java.lang.Long.valueOf(item[4])))
                //判断是否有成交
                if (item[7] == null) {
                    helper.setText(R.id.item_market_entrust_success_price, "--")
                            .setText(R.id.item_market_entrust_success_total, "--")
                } else {
                    helper.setText(R.id.item_market_entrust_success_price, MoneyUtils.decimalByUp(coin.priceDecimals, BigDecimal(item[7])).toPlainString())
                            .setText(R.id.item_market_entrust_success_total, MoneyUtils.mul(success_total, item[7].toDouble()).toString())//成交总量
                }
                helper.apply {
                    //委托数量
                    setText(R.id.item_market_entrust_number, user_Number)
                            .setText(R.id.item_market_entrust_number_name, mContext.getString(R.string.deal_number) + "(" + coin.fShortName + ")")
                    //成交单价
                    setText(R.id.item_deal_single_price, mContext.getString(R.string.deal_success_single_price) + "(" + coin.group + ")")
                    //成交数量
                    setText(R.id.item_entrust_success_sumber_name, mContext.getString(R.string.success_number) + "(" + coin.fShortName + ")")

                    //委托单价
                    setText(R.id.item_market_entrust_user_price, MoneyUtils.decimalByUp(coin.priceDecimals, BigDecimal(item[1])).toPlainString())
                            .setText(R.id.item_user_price_name, mContext.getString(R.string.deal_price) + "(" + coin.fShortName + ")")
                    //成交总量
                    setText(R.id.item_market_entrust_success_total_name, mContext.getString(R.string.deal_success_sum) + "(" + coin.group + ")")

                    setVisible(R.id.item_market_entrust_cancel, true)//是否可以撤销
                            .setText(R.id.item_market_entrust_coinname, coin.fname_sn)//币种名称
                            .setText(R.id.item_market_entrust_success_number, "$success_total")//成交总量
                            .setOnClickListener(R.id.item_market_entrust_cancel) { v ->
                                mPresent?.cancelOrder(item[3])
                            }
                }

                ratioRightLayout.setRatio(BigDecimal(BigDecimal(item[6]).subtract(BigDecimal(item[2])).toPlainString()).divide(BigDecimal(item[6]), 4, BigDecimal.ROUND_DOWN).setScale(2, BigDecimal.ROUND_DOWN).toDouble())


            }
        }

        rv_current_entrust.layoutManager = LinearLayoutManager(context)
        rv_current_entrust.adapter = mCurrentEntrustAdapter
        mCurrentEntrustAdapter!!.emptyView = View.inflate(context, R.layout.view_empty, null)
    }

    @SuppressLint("CheckResult")
    override fun initData() {
        //用来接收market传来的币种ID
        subscribe = RxBus.get().toFlowable().map { o -> o as RxBusData }.subscribe { rxBusData ->
            if (rxBusData.msgName == "CoinData") {
                coin = rxBusData.msgData as CoinTradeRankBean.DealDatasBean
                if (SessionLiveData.getIns().value != null)
                    mPresent?.getRefreshUserInfo(coin.fid)
                else
                    mCurrentEntrustAdapter?.setNewData(mutableListOf())

            }
        }
    }

    override fun onMessage(text: String?) {
        var jsonObject = JSONArray(text)
        if (jsonObject[0] == "entrust-log") {

        }
    }

    //撤销挂单成功回调
    override fun UserOrderCancelSuccess() {
        coin.let {
            mPresent?.getRefreshUserInfo(it.fid)
        }
        var data = RxBusData()
        data.msgName = "refreshUserInfo"
//        RxBus.get().post(data)
    }

    //获得币种ID
    override fun getMarketUserInfoSuccess(t: RefreshUserInfoBean) {

        if (mType != 2) {
            mUnfinishedList = t.entrustList
        } else {
            mUnfinishedList = t.entrustListLog
        }
        mCurrentEntrustAdapter!!.setNewData(mUnfinishedList)
    }

    override fun onDestroy() {
        super.onDestroy()
        SocketUtils.getIns().removeListener(this)
        subscribe.dispose()
    }
}

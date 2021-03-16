package com.biyoex.app.trading.fragmnet


import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.biyoex.app.R
import com.biyoex.app.common.base.RxBus
import com.biyoex.app.common.base.RxBusData
import com.biyoex.app.common.depth.Depth
import com.biyoex.app.common.mvpbase.BaseFragment
import com.biyoex.app.common.okhttp.SocketUtils
import com.biyoex.app.common.utils.log.Log
import com.biyoex.app.common.widget.MyViewPager
import com.biyoex.app.home.bean.CoinTradeRankBean
import com.biyoex.app.trading.adapter.BuyTrendChartAdapter
import com.biyoex.app.trading.adapter.SellTrendChartAdapter
import com.biyoex.app.trading.persenter.KlineChildPresenter
import com.biyoex.app.trading.view.KlineChildView
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_kline_entrust.*
import org.json.JSONArray
import java.util.ArrayList


@SuppressLint("ValidFragment")
class KlineEntrustFragment(var mType: Int, var coin: CoinTradeRankBean.DealDatasBean, var myViewPager: MyViewPager) : BaseFragment<KlineChildPresenter>(), KlineChildView, SocketUtils.OnReceiveMessageListener {
    override fun getCoinMarketInfoSuccess(sellList: MutableList<MutableList<String>>) {

    }
    override fun onMessage(text: String?) {
        var jsonData: JSONArray? = null
        jsonData = JSONArray(text)
        val strName = jsonData.get(0) as String
        if (strName == "entrust-buy") {
            val buy = jsonData.getString(1)
            mPresent?.getBuyDaata(context as Activity, buy)
            //
            //最新交易卖单
        } else if (strName == "entrust-sell") {
            val sell = jsonData.get(1) as String
            activity?.let { mPresent?.getSellData(it, sell) }
        }
    }

    override fun initImmersionBar() {
        super.initImmersionBar()
        immersionBar?.statusBarDarkFont(false)?.init()

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = super.onCreateView(inflater, container, savedInstanceState)
        myViewPager.setObjectForPosition(rootView, mType)
        return rootView
    }

    override fun createPresent(): KlineChildPresenter = KlineChildPresenter()
    override fun getLayoutId(): Int = R.layout.fragment_kline_entrust
    lateinit var mBuyAdapter: BuyTrendChartAdapter
    lateinit var mSellAdapter: SellTrendChartAdapter
    private var subscribe: Disposable? = null
    @SuppressLint("CheckResult")
    override fun initComp() {
        //设置数据详情的数量说明
        depthView.setDetailVolumeTitle(getString(R.string.all_market_number))
        //设置数据详情的价钱说明
        depthView.setDetailPriceTitle(getString(R.string.rate) + "( " + coin.fShortName + ")：")
        rv_buy_trendchart.layoutManager = LinearLayoutManager(context)
        rv_sell_trendchart.layoutManager = LinearLayoutManager(context)
        mBuyAdapter = BuyTrendChartAdapter(context!!, coin.priceDecimals, coin.amountDecimals)
        mSellAdapter = SellTrendChartAdapter(context!!, coin.priceDecimals, coin.amountDecimals)
        rv_buy_trendchart.adapter = mBuyAdapter
        rv_buy_trendchart.isFocusable = false
        rv_sell_trendchart.isFocusable = false
        rv_sell_trendchart.adapter = mSellAdapter
        Log.e("新的coin" + coin.fShortName)
        mPresent?.requestMarketRefresh("${coin.fid}")
        subscribe = RxBus.get().toFlowable().map { o -> o as RxBusData }.subscribe { rxBusData ->
            if (rxBusData.msgName == "KlineData") {
                if (isAdded) {
                    coin = rxBusData.msgData as CoinTradeRankBean.DealDatasBean
                    mBuyAdapter.amountDecimals = coin.amountDecimals
                    mBuyAdapter.priceDecimasl = coin.priceDecimals
                    mSellAdapter.amountDecimals = coin.amountDecimals
                    mSellAdapter.priceDecimasl = coin.priceDecimals
                    mBuyAdapter.notifyDataSetChanged()
                    mSellAdapter.notifyDataSetChanged()
                    mPresent?.requestMarketRefresh("${coin.fid}")
                }
            }
        }
    }

    override fun initData() {
        SocketUtils.getIns().setOnReceiveMessageListener(this)
    }

    override fun getCoinMarketInfoData(sellList: MutableList<MutableList<String>>, totalSell: Double, maxSell: Double) {
        mSellAdapter.setNewData(sellList)
        mSellAdapter.setMaxBuy(maxSell, totalSell)
        setDepthView()
    }

    override fun getCoinMarketBuyData(buyList: MutableList<MutableList<String>>, totalbuy: Double, maxbuy: Double) {
        mBuyAdapter.setNewData(buyList)
        mBuyAdapter.setMaxBuy(maxbuy, totalbuy)
        setDepthView()
    }


    private fun setDepthView() {
        val sellDepthList = ArrayList<Depth>()
        val buyDepthList = ArrayList<Depth>()
        for (i in mSellAdapter.data) {
            //            Log.i("計算數量", "setDepthView: " + Double.valueOf(sellList.get(i).get(1)));
            sellDepthList.add(Depth(java.lang.Double.valueOf(i[0])!!, java.lang.Double.valueOf(i[1])!!, 1))
        }
        //        depthView.setSellDataList(depthList);
        for (i in mBuyAdapter.data) {
            buyDepthList.add(Depth(java.lang.Double.valueOf(i.get(0))!!, java.lang.Double.valueOf(i.get(1))!!, 0))
        }
        depthView.resetAllData(buyDepthList, sellDepthList)
        if (sellDepthList.isNotEmpty() && buyDepthList.isNotEmpty()) {
            val centerprice = (buyDepthList[buyDepthList.size - 1].price + sellDepthList[0].price) / 2
            depthView.setAbscissaCenterPrice(centerprice)
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

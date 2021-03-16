package com.biyoex.app.trading.view

import com.github.fujianlian.klinechart.KLineEntity
import com.biyoex.app.common.mvpbase.BaseView
import com.biyoex.app.trading.bean.MarketRealBean
import com.biyoex.app.trading.bean.RefreshUserInfoBean

interface TrendChartView : BaseView {
    fun getCoinMarketInfoSuccess(sellList: MutableList<MutableList<String>>)

    fun getCoinMarketInfoData(sellList: MutableList<MutableList<String>>, totalSell: Double, maxSell: Double)

    fun getCoinMarketBuyData(buyList: MutableList<MutableList<String>>, totalbuy: Double, maxbuy: Double)


    fun getCoinNewKlineData(klineList: MutableList<KLineEntity>)

    fun getUserMarketData(response: RefreshUserInfoBean)

    fun getCoinData(marketRealBean:MarketRealBean)

    fun retrunFons(isFons:Boolean)

}
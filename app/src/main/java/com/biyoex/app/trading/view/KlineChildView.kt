package com.biyoex.app.trading.view

import com.biyoex.app.common.mvpbase.BaseView

interface KlineChildView : BaseView {
    fun getCoinMarketInfoSuccess(sellList: MutableList<MutableList<String>>)

    fun getCoinMarketInfoData(sellList: MutableList<MutableList<String>>, totalSell: Double, maxSell: Double)

    fun getCoinMarketBuyData(buyList: MutableList<MutableList<String>>, totalbuy: Double, maxbuy: Double)

}
package com.biyoex.app.market.view

import com.biyoex.app.common.mvpbase.BaseView

interface NowMarketView : BaseView {
    fun getCoinMarketInfoSuccess(sellList: MutableList<MutableList<String>>)
}
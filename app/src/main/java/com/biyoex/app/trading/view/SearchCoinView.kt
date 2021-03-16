package com.biyoex.app.trading.view

import com.biyoex.app.common.mvpbase.BaseView
import com.biyoex.app.home.bean.CoinTradeRankBean

interface SearchCoinView : BaseView {

    fun getCoinData(mCoinData: MutableList<CoinTradeRankBean.DealDatasBean>)
}
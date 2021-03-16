package com.biyoex.app.home.view

import com.biyoex.app.common.bean.CoinPosBean
import com.biyoex.app.common.bean.PosHoldRateProfitBean
import com.biyoex.app.common.mvpbase.BaseView

interface CoinPosView : BaseView {

    fun getCoinData(mData: CoinPosBean)

    fun getWeekEarnings(mListData: MutableList<MutableList<String>>)

    fun getHoldRateProfit(mListData: MutableList<PosHoldRateProfitBean>)

    fun getWeekIncomeCurves(mListData: MutableList<MutableList<String>>)
}
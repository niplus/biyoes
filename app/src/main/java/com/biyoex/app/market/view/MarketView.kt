package com.biyoex.app.market.view

import com.biyoex.app.common.mvpbase.BaseView
import com.biyoex.app.trading.bean.MarketRealBean
import com.biyoex.app.trading.bean.RefreshUserInfoBean

interface MarketView : BaseView {
    fun getMarketUserInfoSuccess(infoBean: RefreshUserInfoBean)
    fun postUserBuyOrderSuccess()
    fun marketError()
    fun marketUserNoPassword()
    fun marketCancelOrderSuccess()
    fun getCoinMarketInfoSuccess(sellList: MutableList<MutableList<String>>)

    fun getCoinMarketInfoData(sellList: MutableList<MutableList<String>>, totalSell: Double, maxSell: Double, type: Int)

    fun getCoinMarketBuyData(buyList: MutableList<MutableList<String>>, totalbuy: Double, maxbuy: Double)

    fun getCoinData(data: MarketRealBean)
//    fun getMarketUserInfoSuccess(token:String,.infoBean: MutableList<MutableList<String>>, entrustList: MutableList<MutableList<String>>, s: String, s1: String, strRecommendedPrice: String, strSellOutPrice: String,isLogin:Boolean,needPassWord:Boolean)
}
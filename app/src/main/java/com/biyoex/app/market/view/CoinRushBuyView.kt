package com.biyoex.app.market.view

import com.biyoex.app.common.mvpbase.BaseView
import com.biyoex.app.common.bean.RushBuyBean
import com.biyoex.app.common.bean.RushBuyListData

interface CoinRushBuyView :BaseView {
     fun getRushBuySuccess(mData: RushBuyBean)

     fun getRushBuyListSuccess(mListData:MutableList<RushBuyListData>, page:Int, pageSize:Int)

     fun getRushBuyError(msg:String)
}
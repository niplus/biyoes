package com.biyoex.app.property.view

import com.biyoex.app.common.mvpbase.BaseView
import com.biyoex.app.property.datas.CoinRecordBean

interface PropertyDetailsView :BaseView {
   fun getRecordDataSuccess(mCoinRecordBean: CoinRecordBean,page:Int,pageSize:Int)
   fun getRecordDataError()
}
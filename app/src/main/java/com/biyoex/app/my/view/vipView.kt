package com.biyoex.app.my.view

import com.biyoex.app.common.mvpbase.BaseView
import com.biyoex.app.common.bean.VipPriceBean

interface VipView : BaseView {

    fun getVipPriceSuccess(mData: VipPriceBean)

    fun getBuyVipPriceSuccess(mPrice:String)
}
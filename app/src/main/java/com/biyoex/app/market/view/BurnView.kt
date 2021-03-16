package com.biyoex.app.market.view

import com.biyoex.app.common.bean.BurnInfoBean
import com.biyoex.app.common.mvpbase.BaseView

interface BurnView :BaseView {

    fun getBurnSuccess(data:BurnInfoBean)
}
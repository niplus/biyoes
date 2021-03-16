package com.biyoex.app.my.view

import com.biyoex.app.common.mvpbase.BaseView
import com.biyoex.app.common.bean.MyInviteVipBean

interface MyInviteVipView :BaseView {
    fun getMyInviteSuccess(mData: MyInviteVipBean)
}
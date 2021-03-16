package com.biyoex.app.market.view

import com.biyoex.app.common.mvpbase.BaseView
import com.biyoex.app.trading.bean.RefreshUserInfoBean

interface MineEntrustOrderView : BaseView {
    fun getMarketUserInfoSuccess(t: RefreshUserInfoBean)

    fun UserOrderCancelSuccess()
}
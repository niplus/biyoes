package com.biyoex.app.market.view

import com.biyoex.app.common.mvpbase.BaseView
import com.biyoex.app.common.bean.RushBuyInviteBean
import com.biyoex.app.common.bean.WeekRewardBean

interface RushBuyInviteRecordView : BaseView {
    fun getUserRecordSuccess(mData: MutableList<RushBuyInviteBean>, type: Int)
    fun getUserRecordRewardSuccess(mWeekRewardBean: WeekRewardBean)
}
package com.biyoex.app.my.view

import com.biyoex.app.common.mvpbase.BaseView
import com.biyoex.app.common.bean.WeekRankBean

interface MyRankView : BaseView {

    fun getWeekRankSuccess(weekRankBean: WeekRankBean)
}
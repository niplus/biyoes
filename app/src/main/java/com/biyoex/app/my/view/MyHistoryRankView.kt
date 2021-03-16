package com.biyoex.app.my.view

import com.biyoex.app.common.mvpbase.BaseView
import com.biyoex.app.common.bean.MyHistoryBean

interface MyHistoryRankView :BaseView {
    fun getHistoryRankSuccess(myHistoryBean: MyHistoryBean)
}
package com.biyoex.app.property.view

import com.biyoex.app.common.bean.RequestResult
import com.biyoex.app.common.mvpbase.BaseView
import com.biyoex.app.property.datas.LockHistoryBean

interface PropertyHistoryView :BaseView {
   fun getLockHistorySuccess(datas:RequestResult<LockHistoryBean>)
   fun getLockHistoryError()
}
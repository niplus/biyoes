package com.biyoex.app.property.view

import com.biyoex.app.common.mvpbase.BaseView
import com.biyoex.app.property.datas.LockDataBean

interface LockPropertyView :BaseView{
    fun getLockDataSuccess( lockDataBean: LockDataBean)
    fun updateSuccess()
}
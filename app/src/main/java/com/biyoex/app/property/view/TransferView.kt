package com.biyoex.app.property.view

import com.biyoex.app.common.mvpbase.BaseView
import com.biyoex.app.property.datas.LockDataBean

interface TransferView :BaseView{
    fun HttpSuccess(datas:LockDataBean)
    fun updateDataSuccess()
}
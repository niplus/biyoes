package com.biyoex.app.my.view

import com.biyoex.app.common.mvpbase.BaseView
import com.biyoex.app.my.bean.ZGRechargeCoinBean

interface CurrencyAddressListView :BaseView {
    fun getCurrencyAddressSuccess(mutableList: MutableList<ZGRechargeCoinBean.AddressBean>)
    fun deleteAddressSuccess()
}
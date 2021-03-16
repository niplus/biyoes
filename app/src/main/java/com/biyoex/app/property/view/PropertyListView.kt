package com.biyoex.app.property.view

import com.biyoex.app.common.mvpbase.BaseView
import com.biyoex.app.my.bean.RechargeCoinBean

interface PropertyListView :BaseView{
     fun httpSuccess(result: MutableList<RechargeCoinBean>?, coinname: String, totalValue: String, usdtValue: String){
    }
    fun httpErrorView(code:Int){
    }
}
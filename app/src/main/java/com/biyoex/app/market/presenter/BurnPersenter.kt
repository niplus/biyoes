package com.biyoex.app.market.presenter

import com.biyoex.app.common.bean.BurnInfoBean
import com.biyoex.app.common.bean.RequestResult
import com.biyoex.app.common.http.RetrofitHelper
import com.biyoex.app.common.mvpbase.BaseObserver
import com.biyoex.app.common.mvpbase.BasePresent
import com.biyoex.app.market.view.BurnView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class BurnPersenter : BasePresent<BurnView>() {

    fun getBurnRate(id:Int){
        RetrofitHelper.getIns().zgtopApi
                .getBurnInfo(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object :BaseObserver<RequestResult<BurnInfoBean>>(){
                    override fun success(t: RequestResult<BurnInfoBean>) {
                        mView.getBurnSuccess(t!!.data)
                    }
                })
    }
}
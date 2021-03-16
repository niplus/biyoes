package com.biyoex.app.property.presenter

import com.biyoex.app.common.bean.RequestResult
import com.biyoex.app.common.http.RetrofitHelper
import com.biyoex.app.common.mvpbase.BaseObserver
import com.biyoex.app.common.mvpbase.BasePresent
import com.biyoex.app.property.datas.LockHistoryBean
import com.biyoex.app.property.view.PropertyHistoryView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class PropertyHistoryPresenter :BasePresent<PropertyHistoryView>(){
    fun getLockHistory(conidId:String,page:Int,pageSum:Int){
//        mView.showLoadingDialog()
        RetrofitHelper
                .getIns()
                .zgtopApi
                .getLockHistory(conidId,page,pageSum)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object :BaseObserver<RequestResult<LockHistoryBean>>(){
                    override fun success(t: RequestResult<LockHistoryBean>) {
//                        mView.hideLoadingDialog()
                        mView.getLockHistorySuccess(t!!)
                    }

                    override fun failed(code: Int, data: String?, msg: String?) {
                        super.failed(code, data, msg)
//                        mView.hideLoadingDialog()
                        mView.showToast(msg)

                    }

                });
    }
}
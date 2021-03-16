package com.biyoex.app.property.presenter

import androidx.lifecycle.Lifecycle
import androidx.appcompat.app.AppCompatActivity
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.kotlin.autoDisposable
import com.biyoex.app.common.bean.RequestResult
import com.biyoex.app.common.http.RetrofitHelper
import com.biyoex.app.common.mvpbase.BaseObserver
import com.biyoex.app.common.mvpbase.BasePresent
import com.biyoex.app.property.datas.LockDataBean
import com.biyoex.app.property.view.TransferView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class TransferPresenter :BasePresent<TransferView>() {
    fun getTransferData(coinId:String){
            RetrofitHelper
                    .getIns()
                    .zgtopApi
                    .getTransferData(coinId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .autoDisposable(AndroidLifecycleScopeProvider.from(mView.context as AppCompatActivity, Lifecycle.Event.ON_DESTROY))//OnDestory时自动解绑
                    .subscribe(object :BaseObserver<RequestResult<LockDataBean>>(){
                        override fun success(t: RequestResult<LockDataBean>) {
                           mView.HttpSuccess(t!!.data)
                        }
                    })

    }
    fun postTransferData(coidId:String,amount:String){
        mView.showLoadingDialog()
    RetrofitHelper
            .getIns()
            .zgtopApi
            .postTransfer(coidId,amount)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .autoDisposable(AndroidLifecycleScopeProvider.from(mView.context as AppCompatActivity, Lifecycle.Event.ON_DESTROY))//OnDestory时自动解绑
            .subscribe(object :BaseObserver<RequestResult<Any>>() {
                override fun success(t: RequestResult<Any>) {
                    if(t!!.code==200){
                        mView.updateDataSuccess()
                        mView.hideLoadingDialog()
                    }
                    else{
                        mView.hideLoadingDialog()
                        mView.showToast(t!!.msg)
                    }
                }

                override fun failed(code: Int, data: String?, msg: String?) {
                    super.failed(code, data, msg)
                    mView.hideLoadingDialog()
                    mView.showToast(msg)
                }
            })

    }
}
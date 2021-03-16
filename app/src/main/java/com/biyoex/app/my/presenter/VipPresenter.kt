package com.biyoex.app.my.presenter

import androidx.lifecycle.Lifecycle
import androidx.appcompat.app.AppCompatActivity
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.kotlin.autoDisposable
import com.biyoex.app.common.bean.RequestResult
import com.biyoex.app.common.http.RetrofitHelper
import com.biyoex.app.common.mvpbase.BaseObserver
import com.biyoex.app.common.mvpbase.BasePresent
import com.biyoex.app.common.bean.VipBuyBean
import com.biyoex.app.common.bean.VipPriceBean
import com.biyoex.app.my.view.VipView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class VipPresenter : BasePresent<VipView>() {

    fun getVipPriceData() {
        mView.showLoadingDialog()
        RetrofitHelper.getIns().zgtopApi
                .vipPrice
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .autoDisposable(AndroidLifecycleScopeProvider.from(mView.context as AppCompatActivity, Lifecycle.Event.ON_DESTROY))//OnDestory时自动解绑
                .subscribe(object : BaseObserver<RequestResult<VipPriceBean>>() {
                    override fun success(t: RequestResult<VipPriceBean>) {
                        mView.hideLoadingDialog()
                        mView.getVipPriceSuccess(t!!.data)
                    }

                    override fun failed(code: Int, data: String?, msg: String?) {
                        super.failed(code, data, msg)
                        mView.hideLoadingDialog()
                    }
                })
    }

    fun getBuyVipPrice(grade: Int) {
        RetrofitHelper.getIns().zgtopApi
                .getBuyVipPrice(grade)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .autoDisposable(AndroidLifecycleScopeProvider.from(mView.context as AppCompatActivity, Lifecycle.Event.ON_DESTROY))//OnDestory时自动解绑
                .subscribe(object : BaseObserver<RequestResult<VipBuyBean>>() {
                    override fun success(t: RequestResult<VipBuyBean>) {
                        mView.getBuyVipPriceSuccess(t!!.data.ducePrices)
                    }

                    override fun failed(code: Int, data: String?, msg: String?) {
                        super.failed(code, data, msg)
                        mView.showToast(msg)
                    }
                })
    }

    fun postBuyVip(garde: Int) {
        mView.showLoadingDialog()
        RetrofitHelper.getIns().zgtopApi
                .postBuyVip(garde)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .autoDisposable(AndroidLifecycleScopeProvider.from(mView.context as AppCompatActivity, Lifecycle.Event.ON_DESTROY))//OnDestory时自动解绑
                .subscribe(object : BaseObserver<RequestResult<Any>>() {
                    override fun success(t: RequestResult<Any>) {
                        mView.showToast("开通成功")
                        getVipPriceData()
                        mView.hideLoadingDialog()
                    }

                    override fun failed(code: Int, data: String?, msg: String?) {
                        super.failed(code, data, msg)
                        mView.showToast(msg)
                        mView.hideLoadingDialog()
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        mView.hideLoadingDialog()
                    }
                })
    }
}
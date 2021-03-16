package com.biyoex.app.my.presenter

import androidx.lifecycle.Lifecycle
import androidx.appcompat.app.AppCompatActivity
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.kotlin.autoDisposable
import com.biyoex.app.common.bean.RequestResult
import com.biyoex.app.common.http.RetrofitHelper
import com.biyoex.app.common.mvpbase.BaseObserver
import com.biyoex.app.common.mvpbase.BasePresent
import com.biyoex.app.my.bean.ZGRechargeCoinBean
import com.biyoex.app.my.view.CurrencyAddressListView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CurrencyAddressListPresenter : BasePresent<CurrencyAddressListView>() {

    fun getCoinAddress(id: String) {
        mView.showLoadingDialog()
        RetrofitHelper.getIns().zgtopApi
                .getParamsByCoin(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .autoDisposable(AndroidLifecycleScopeProvider.from(mView.context as AppCompatActivity, Lifecycle.Event.ON_DESTROY))//OnDestory时自动解绑
                .subscribe(object : BaseObserver<RequestResult<ZGRechargeCoinBean>>() {
                    override fun success(t: RequestResult<ZGRechargeCoinBean>) {
                        mView.hideLoadingDialog()
                        mView.getCurrencyAddressSuccess(t!!.data.addresses)

                    }

                    override fun failed(code: Int, data: String?, msg: String?) {
                        mView.hideLoadingDialog()
                        super.failed(code, data, msg)
                        mView.showToast(msg)
                    }
                })
    }

    fun deleteAddress(id: Int) {
        RetrofitHelper.getIns().zgtopApi
                .delectAddress(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .autoDisposable(AndroidLifecycleScopeProvider.from(mView.context as AppCompatActivity, Lifecycle.Event.ON_DESTROY))//OnDestory时自动解绑
                .subscribe(object : BaseObserver<RequestResult<Any>>() {
                    override fun success(t: RequestResult<Any>) {
                        mView.deleteAddressSuccess()
                    }

                })
    }
}
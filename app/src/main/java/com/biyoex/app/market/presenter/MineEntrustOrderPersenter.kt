package com.biyoex.app.market.presenter

import androidx.lifecycle.Lifecycle
import androidx.appcompat.app.AppCompatActivity
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.kotlin.autoDisposable
import com.biyoex.app.R
import com.biyoex.app.common.http.RetrofitHelper
import com.biyoex.app.common.mvpbase.BaseObserver
import com.biyoex.app.common.mvpbase.BasePresent
import com.biyoex.app.market.view.MineEntrustOrderView
import com.biyoex.app.trading.bean.RefreshUserInfoBean
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MineEntrustOrderPersenter : BasePresent<MineEntrustOrderView>() {
    //刷新用户信息
    fun getRefreshUserInfo(sytmol: Int) {
        if(mView==null) return
//        Log.e("刷新用户信息")
        RetrofitHelper.getIns().zgtopApi
                .getMarketUserInfo(sytmol, "${System.currentTimeMillis()}")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .autoDisposable(AndroidLifecycleScopeProvider.from(mView.context as AppCompatActivity, Lifecycle.Event.ON_DESTROY))//OnDestory时自动解绑
                .subscribe(object : BaseObserver<RefreshUserInfoBean>(mView.context as AppCompatActivity) {
                    override fun success(t: RefreshUserInfoBean) {
                        if (t != null) {
                            if (t.isLogin != "0") {
//
                                mView.getMarketUserInfoSuccess(t)
                            }
                        } else {
                            mView.showToast(mView.context.getString(R.string.net_error))
                        }
                    }
                })
    }

//    //撤销当前所有委托
//    fun cancelAllOrder(sytmol: Int) {
//        mView.showLoadingDialog()
//        RetrofitHelper.getIns().zgtopApi
//                .cancelAllOrder(sytmol)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .autoDisposable(AndroidLifecycleScopeProvider.from(mView.context as AppCompatActivity, Lifecycle.Event.ON_DESTROY))//OnDestory时自动解绑
//                .subscribe(object : BaseObserver<RequestResult<Any>>() {
//                    override fun success(t: RequestResult<Any>?) {
//                        Log.e("撤销成功")

//                        mView.hideLoadingDialog()
//                        mView.UserOrderCancelSuccess()
//                        getRefreshUserInfo(sytmol)
////                        requestMarketRefresh("$sytmol")
//                    }
//
//                    override fun failed(code: Int, data: String?, msg: String?) {
//                        Log.e("撤销失败")
//                        super.failed(code, data, msg)
//                        mView.showToast(msg)
//                        mView.hideLoadingDialog()
//                    }
//
//                })
//    }

    fun cancelOrder(id: String) {
        mView.showLoadingDialog()
        RetrofitHelper
                .getIns().zgtopApi
                .cancelEntrush(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .autoDisposable(AndroidLifecycleScopeProvider.from(mView.context as AppCompatActivity, Lifecycle.Event.ON_DESTROY))//OnDestory时自动解绑
                .subscribe(object : BaseObserver<String>() {
                    override fun success(t: String) {
                        mView.showToast(mView.context.getString(R.string.already_revoke))
                        mView.UserOrderCancelSuccess()
                        mView.hideLoadingDialog()
                    }

                    override fun onError(e: Throwable) {
                        mView.showToast(mView.context.getString(R.string.revoke_failed))
                        mView.hideLoadingDialog()
                    }
                })
    }
}
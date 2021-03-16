package com.biyoex.app.my.presenter

import androidx.lifecycle.Lifecycle
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.kotlin.autoDisposable
import com.biyoex.app.R
import com.biyoex.app.common.bean.RequestResult
import com.biyoex.app.common.http.RetrofitHelper
import com.biyoex.app.common.mvpbase.BaseObserver
import com.biyoex.app.common.mvpbase.BasePresent
import com.biyoex.app.common.bean.GoogleSecret
import com.biyoex.app.my.view.BindGoogleView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class BindGooglePresenter(var mcontext: Context) : BasePresent<BindGoogleView>() {

    fun getGooglePass() {
        mView.showLoadingDialog()
        RetrofitHelper.getIns().zgtopApi
                .googlePass
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .autoDisposable(AndroidLifecycleScopeProvider.from(mView.context as AppCompatActivity, Lifecycle.Event.ON_DESTROY))//OnDestory时自动解绑
                .subscribe(object : BaseObserver<RequestResult<GoogleSecret>>(mView.context as AppCompatActivity) {
                    override fun success(t: RequestResult<GoogleSecret>) {
                        mView.hideLoadingDialog()
                        t?.data?.let { mView.getGoogleSuccess(it) }
                    }
                })
    }

    fun sendSmsCode() {
        mView.showLoadingDialog()
        RetrofitHelper.getIns().zgtopApi.getGoogleCode(3)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .autoDisposable(AndroidLifecycleScopeProvider.from(mView.context as AppCompatActivity, Lifecycle.Event.ON_DESTROY))//OnDestory时自动解绑
                .subscribe(object : BaseObserver<RequestResult<Any>>(mView.context as AppCompatActivity) {
                    override fun success(t: RequestResult<Any>) {
                        mView.hideLoadingDialog()
                        mView.showToast(mcontext.getString(R.string.send_sms_success))
                    }
                })
    }

    fun bindGoogle(password: String, emailCode: String, code: String) {
        mView.showLoadingDialog()
        RetrofitHelper.getIns().zgtopApi
                .authGoogle(password, emailCode, code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .autoDisposable(AndroidLifecycleScopeProvider.from(mView.context as AppCompatActivity, Lifecycle.Event.ON_DESTROY))//OnDestory时自动解绑
                .subscribe(object : BaseObserver<RequestResult<Any>>(mView.context as AppCompatActivity) {
                    override fun success(t: RequestResult<Any>) {
                        mView.hideLoadingDialog()
                        mView.bindGoogleSuccess()
                    }

                    override fun failed(code: Int, data: String?, msg: String?) {
                        super.failed(code, data, msg)
                        when (code) {
                            -2 -> {
                                mView.showToast(mcontext.getString(R.string.alread_bind_google))
                            }
                            -3 -> {
                                mView.showToast(mcontext.getString(R.string.wrong_login_passowrd))
                            }
                            -4 -> {
                                mView.showToast(mcontext.getString(R.string.google_code_error))
                            }
                            else -> {
                                mView.showToast(msg)
                            }
                        }
                        mView.hideLoadingDialog()
                    }
                })
    }

    fun unBindGoogle(password: String, code: String) {
        mView.showLoadingDialog()
        RetrofitHelper.getIns().zgtopApi.unbindGoogle(password, code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .autoDisposable(AndroidLifecycleScopeProvider.from(mView.context as AppCompatActivity, Lifecycle.Event.ON_DESTROY))//OnDestory时自动解绑
                .subscribe(object : BaseObserver<RequestResult<Any>>(lifecycleOwner) {
                    override fun success(t: RequestResult<Any>) {
                        mView.hideLoadingDialog()
                        mView.bindGoogleSuccess()
                    }
                    override fun failed(code: Int, data: String?, msg: String?) {
                        super.failed(code, data, msg)
                        when (code) {
                            -2 -> {
                                mView.showToast(mcontext.getString(R.string.wrong_login_passowrd))
                            }
                            -3 -> {
                                mView.showToast(mcontext.getString(R.string.no_google_certify))
                            }
                            -4 -> {
                                mView.showToast(mcontext.getString(R.string.google_code_error))
                            }
                            else -> {
                                mView.showToast(msg)
                            }
                        }
                        mView.hideLoadingDialog()
                    }
                })
    }
}
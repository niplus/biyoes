package com.biyoex.app.my.presenter

import androidx.lifecycle.Lifecycle
import androidx.appcompat.app.AppCompatActivity
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.kotlin.autoDisposable
import com.biyoex.app.common.bean.RequestResult
import com.biyoex.app.common.http.RetrofitHelper
import com.biyoex.app.common.mvpbase.BasePresent
import com.biyoex.app.common.bean.MyInviteVipBean
import com.biyoex.app.my.view.MyInviteVipView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import com.biyoex.app.common.mvpbase.BaseObserver as BaseObserver1

class MyInviteVipPresenter : BasePresent<MyInviteVipView>() {
    fun getMyInviteVip(page: Int, PageSize: Int) {
        mView.showLoadingDialog()
        RetrofitHelper.getIns().zgtopApi
                .getMyInviteVip(page, PageSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .autoDisposable(AndroidLifecycleScopeProvider.from(mView.context as AppCompatActivity, Lifecycle.Event.ON_DESTROY))//OnDestory时自动解绑
                .subscribe(object : BaseObserver1<RequestResult<MyInviteVipBean>>() {
                    override fun success(t: RequestResult<MyInviteVipBean>) {
                        mView.hideLoadingDialog()
                        mView.getMyInviteSuccess(t!!.data)
                    }

                    override fun failed(code: Int, data: String?, msg: String?) {
                        mView.hideLoadingDialog()
                        super.failed(code, data, msg)
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        mView.hideLoadingDialog()
                    }
                })
    }
}
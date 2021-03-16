package com.biyoex.app.my.presenter

import androidx.lifecycle.Lifecycle
import androidx.appcompat.app.AppCompatActivity
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.kotlin.autoDisposable
import com.biyoex.app.common.bean.RequestResult
import com.biyoex.app.common.http.RetrofitHelper
import com.biyoex.app.common.mvpbase.BaseObserver
import com.biyoex.app.common.mvpbase.BasePresent
import com.biyoex.app.common.bean.MyHistoryBean
import com.biyoex.app.my.view.MyHistoryRankView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MyHistoryRankPresenter : BasePresent<MyHistoryRankView>() {
    fun getMyHistoryRankData(page: Int, pageSize: Int) {
        mView.showLoadingDialog()
        RetrofitHelper.getIns().zgtopApi
                .getUserHistoryRank(page, pageSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .autoDisposable(AndroidLifecycleScopeProvider.from(mView.context as AppCompatActivity, Lifecycle.Event.ON_DESTROY))//OnDestory时自动解绑
                .subscribe(object : BaseObserver<RequestResult<MyHistoryBean>>() {
                    override fun success(t: RequestResult<MyHistoryBean>) {
                        mView.getHistoryRankSuccess(t!!.data)
                    }

                    override fun failed(code: Int, data: String?, msg: String?) {
                        super.failed(code, data, msg)
                        mView.httpError()
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        mView.httpError()
                    }
                })
    }
}
package com.biyoex.app.my.presenter

import androidx.lifecycle.Lifecycle
import androidx.appcompat.app.AppCompatActivity
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.kotlin.autoDisposable
import com.biyoex.app.common.bean.RequestResult
import com.biyoex.app.common.http.RetrofitHelper
import com.biyoex.app.common.mvpbase.BaseObserver
import com.biyoex.app.common.mvpbase.BasePresent
import com.biyoex.app.common.bean.WeekRankBean
import com.biyoex.app.my.view.MyRankView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MyRankPresenter : BasePresent<MyRankView>() {
    fun getWeekRank(index: Int, page: Int, pageSize: Int) {
        RetrofitHelper.getIns().zgtopApi
                .getWeekRank(index)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .autoDisposable(AndroidLifecycleScopeProvider.from(mView.context as AppCompatActivity, Lifecycle.Event.ON_DESTROY))//OnDestory时自动解绑
                .subscribe(object : BaseObserver<RequestResult<WeekRankBean>>() {
                    override fun success(t: RequestResult<WeekRankBean>) {
                        mView.getWeekRankSuccess(t!!.data)
                    }

                    override fun failed(code: Int, data: String?, msg: String?) {
                        super.failed(code, data, msg)
                        mView.showToast(msg)
                    }
                })
    }
}
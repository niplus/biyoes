package com.biyoex.app.market.presenter

import androidx.lifecycle.Lifecycle
import androidx.appcompat.app.AppCompatActivity
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.kotlin.autoDisposable
import com.biyoex.app.common.bean.RequestResult
import com.biyoex.app.common.http.RetrofitHelper
import com.biyoex.app.common.mvpbase.BaseObserver
import com.biyoex.app.common.mvpbase.BasePresent
import com.biyoex.app.common.bean.RushBuyInviteBean
import com.biyoex.app.common.bean.WeekRewardBean
import com.biyoex.app.market.view.RushBuyInviteRecordView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class RushBuyInviteRecordPresenter : BasePresent<RushBuyInviteRecordView>() {

    fun getUserRushInviteData(type: Int, page: Int, pageSize: Int) {
        mView.showLoadingDialog()
        RetrofitHelper
                .getIns()
                .zgtopApi
                .getUserInviteData(type, page, pageSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .autoDisposable(AndroidLifecycleScopeProvider.from(mView.context as AppCompatActivity, Lifecycle.Event.ON_DESTROY))//OnDestory时自动解绑
                .subscribe(object : BaseObserver<RequestResult<MutableList<RushBuyInviteBean>>>() {
                    override fun success(t: RequestResult<MutableList<RushBuyInviteBean>>) {
                        mView.getUserRecordSuccess(t!!.data, type)
//                        mView.hideLoadingDialog()
                    }

                    override fun failed(code: Int, data: String?, msg: String?) {
                        super.failed(code, data, msg)
//                        mView.hideLoadingDialog()
                        mView.httpError()
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        mView.httpError()
                    }
                })
    }

    fun getRewardWeek() {

        mView.showLoadingDialog()
        RetrofitHelper.getIns().zgtopApi.rewardRecordWeek
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .autoDisposable(AndroidLifecycleScopeProvider.from(mView.context as AppCompatActivity, Lifecycle.Event.ON_DESTROY))//OnDestory时自动解绑
                .subscribe(object : BaseObserver<RequestResult<WeekRewardBean>>() {
                    override fun success(t: RequestResult<WeekRewardBean>) {
//                        mView.hideLoadingDialog()
                        mView.getUserRecordRewardSuccess(t!!.data)
                    }
                    override fun failed(code: Int, data: String?, msg: String?) {
                        super.failed(code, data, msg)
//                        mView.hideLoadingDialog()
                        mView.httpError()
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        mView.httpError()
                    }
                })
    }
}
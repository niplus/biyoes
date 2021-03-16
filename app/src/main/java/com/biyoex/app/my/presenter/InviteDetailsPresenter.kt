package com.biyoex.app.my.presenter

import androidx.lifecycle.Lifecycle
import androidx.appcompat.app.AppCompatActivity
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.kotlin.autoDisposable
import com.biyoex.app.common.bean.InviteReturnRankBean
import com.biyoex.app.common.bean.MineInviteReturnBean
import com.biyoex.app.common.bean.RequestResult
import com.biyoex.app.common.http.RetrofitHelper
import com.biyoex.app.common.mvpbase.BaseObserver
import com.biyoex.app.common.mvpbase.BasePresent
import com.biyoex.app.my.view.InviteDetailsView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


/**
 * time = 2019/8/22 0022
 * CreatedName =
 */
class InviteDetailsPresenter : BasePresent<InviteDetailsView>() {

    fun getInviteRankData() {
        mView.hideLoadingDialog()
        RetrofitHelper.getIns().zgtopApi.inviteRankData
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .autoDisposable(AndroidLifecycleScopeProvider.from(mView.context as AppCompatActivity, Lifecycle.Event.ON_DESTROY))//OnDestory时自动解绑
                .subscribe(object : BaseObserver<RequestResult<MutableList<InviteReturnRankBean>>>() {
                    override fun success(t: RequestResult<MutableList<InviteReturnRankBean>>) {
                        mView.hideLoadingDialog()
                        mView.getInviteRankDataSuccess(t!!.data)
                    }

                })
    }

    fun getMyInviteReturnData(page: Int, pageSize: Int) {
        mView.hideLoadingDialog()
        RetrofitHelper.getIns().zgtopApi.getMyInviteReturn(page, pageSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .autoDisposable(AndroidLifecycleScopeProvider.from(mView.context as AppCompatActivity, Lifecycle.Event.ON_DESTROY))//OnDestory时自动解绑
                .subscribe(object : BaseObserver<RequestResult<MutableList<MineInviteReturnBean>>>() {
                    override fun success(t: RequestResult<MutableList<MineInviteReturnBean>>) {
                        mView.getMyInviteSuccess(t!!.data)
                    }
                })
    }
}
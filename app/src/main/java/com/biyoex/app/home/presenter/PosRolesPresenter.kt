package com.biyoex.app.home.presenter

import com.biyoex.app.common.bean.PosInvitationNumberBean
import com.biyoex.app.common.bean.PosRoleListBean
import com.biyoex.app.common.bean.RequestResult
import com.biyoex.app.common.http.RetrofitHelper
import com.biyoex.app.common.mvpbase.BaseObserver
import com.biyoex.app.common.mvpbase.BasePresent
import com.biyoex.app.common.utils.ToastUtils
import com.biyoex.app.home.view.PosRolesView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class PosRolesPresenter : BasePresent<PosRolesView>() {

    //POS角色列表
    fun getSwiPosRoleListData() {
        mView.showLoadingDialog()
        RetrofitHelper.getIns().zgtopApi
                .posRoleList
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : BaseObserver<RequestResult<MutableList<PosRoleListBean>>>() {
                    override fun success(t: RequestResult<MutableList<PosRoleListBean>>) {
                        mView.hideLoadingDialog()
                        t!!.data?.let {
                            mView.getRoleListData(it)
                        }
                    }

                    override fun failed(code: Int, data: String?, msg: String?) {
                        super.failed(code, data, msg)
                        mView.hideLoadingDialog()
                    }
                })

    }

    //创建角色
    fun getPosCreatePosRoleData(name: String, inviteCode: String) {
        RetrofitHelper.getIns().zgtopApi
                .getPosCreatePosRole(name,inviteCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : BaseObserver<RequestResult<PosInvitationNumberBean>>() {
                    override fun success(t: RequestResult<PosInvitationNumberBean>) {
                        ToastUtils.showToast("创建角色成功")
                        getSwiPosRoleListData()
                    }

                    override fun failed(code: Int, data: String?, msg: String?) {
                        super.failed(code, data, msg)
                        ToastUtils.showToast(msg)
                    }
                })

    }

}
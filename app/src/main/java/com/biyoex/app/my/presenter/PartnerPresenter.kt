package com.biyoex.app.my.presenter

import android.content.Context
import android.widget.Toast
import com.biyoex.app.R
import com.biyoex.app.common.bean.InvitationPartnerBean
import com.biyoex.app.common.bean.PartnerCode
import com.biyoex.app.common.bean.RequestResult
import com.biyoex.app.common.http.RetrofitHelper
import com.biyoex.app.common.mvpbase.BaseObserver
import com.biyoex.app.common.mvpbase.BasePresent
import com.biyoex.app.my.view.PartnerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class PartnerPresenter(var context:Context) :BasePresent<PartnerView>() {

    fun  getPartner(){
        RetrofitHelper.getIns().zgtopApi.partner
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object :BaseObserver<RequestResult<PartnerCode>>(){
                    override fun success(t: RequestResult<PartnerCode>) {
                        mView.getPartner(t!!.data.type)
                    }

                    override fun failed(code: Int, data: String?, msg: String?) {
                        super.failed(code, data, msg)
                        if(code==-1){
                            Toast.makeText(context,"账号不存在",Toast.LENGTH_LONG).show()
                        }
                        else{
                            data?.let {
                                mView.showToast(it)
                            }
                        }
                    }

                })
    }

    fun postShareCode(shareCode:String){
        mView.showLoadingDialog()
        RetrofitHelper.getIns().zgtopApi.postShareCode(shareCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object :BaseObserver<RequestResult<String>>(){
                    override fun success(t: RequestResult<String>) {
                        getBuyMessage()
                    }
                    override fun failed(code: Int, data: String?, msg: String?) {
                        super.failed(code, data, msg)
                        mView.hideLoadingDialog()
                       setmessage(code,data)
                    }

                })
    }

    private fun setmessage(code: Int,data:String?) {
        when(code){
            -1 -> mView.showToast(context.getString(R.string.account_not_exist))
            -2 -> mView.showToast("不能输入此账号ID")
            -6 -> mView.showToast("邀请人,不能是您已邀请过的用户")
            else -> data?.let {
                mView.showToast(data)
            }

        }
    }

    fun getBuyMessage(){
        RetrofitHelper.getIns().zgtopApi.imnvitation
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object :BaseObserver<RequestResult<InvitationPartnerBean>>(){
                    override fun success(t: RequestResult<InvitationPartnerBean>) {
                        mView.postShareIdSuccess(t!!.data)
                        mView.hideLoadingDialog()
                    }
                })
    }
}
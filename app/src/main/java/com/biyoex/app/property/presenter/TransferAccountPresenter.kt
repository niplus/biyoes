package com.biyoex.app.property.presenter

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
import com.biyoex.app.common.utils.log.Log
import com.biyoex.app.property.view.TransferAccountView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import java.lang.Exception
import java.math.BigDecimal

class TransferAccountPresenter : BasePresent<TransferAccountView>() {

    fun getUserBalance(coidId: Int) {
        RetrofitHelper
                .getIns().zgtopApi
                .getTransferBanlance(coidId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .autoDisposable(AndroidLifecycleScopeProvider.from(mView.context as AppCompatActivity, Lifecycle.Event.ON_DESTROY))//OnDestory时自动解绑
                .subscribe(object : BaseObserver<String>() {
                    override fun success(response: String) {
                        try {
                            var jsonObject = JSONObject(response)
                            val jsonObject1 = jsonObject.getJSONObject("data")
                            mView.getUserBalance(BigDecimal(jsonObject1.getString("total")).setScale(4, BigDecimal.ROUND_HALF_UP).toString(), jsonObject1.getString("coinName"))
                        } catch (e: Exception) {
                        }
                    }
                })
    }

    fun postUserTransfer(id: Int, googleCode: String, uid: String, amount: String, code: String, safeWord: String) {
        RetrofitHelper
                .getIns()
                .zgtopApi
                .postTransferToUse(id, googleCode, uid, amount, code, safeWord)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .autoDisposable(AndroidLifecycleScopeProvider.from(mView.context as AppCompatActivity, Lifecycle.Event.ON_DESTROY))//OnDestory时自动解绑
                .subscribe(object : BaseObserver<String>() {
                    override fun success(t: String) {
                        var jsonObject = JSONObject(t)
                        val string = jsonObject.getString("code")
                        if (string == "200") {
                            mView.postTransferSuccess()
                        } else {
                            mView.showToast(jsonObject.getString("msg"))
                        }
                    }

                })
    }

    fun postSms(sendType: Int, picCode: String) {
        RetrofitHelper.getIns()
                .zgtopApi
                .sendAccountCode(sendType, picCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : BaseObserver<RequestResult<*>>() {
                    public override fun success(requestResult: RequestResult<*>) {
                        mView.hideLoadingDialog()
                        mView.postSmsSuccess()
                        mView.showToast("发送成功")
                    }


                    public override fun failed(code: Int, data: String?, msg: String?) {
                        Log.i("send code failed : $code")
                        mView.showToast(sendAuthCodeMessage(mView.context.getString(R.string.phone_email), code, mView.context))
                        mView.hideLoadingDialog()
                    }
                })
    }

    /**
     * 处理响应码，把响应码变成中文并且返回出去给ui界面去提示
     */
    fun sendAuthCodeMessage(hint: String, resultCode: Int, context: Context): String {
        var messag = ""
        when (resultCode) {
            100 -> messag = "图形验证码为空"
            101 -> messag = context.getString(R.string.please_input) + hint
            102 -> messag = context.getString(R.string.not_vip) + hint
            103 -> messag = context.getString(R.string.not_bind) + hint
            4 -> messag = context.getString(R.string.already_regist)
            105 -> messag = hint + context.getString(R.string.opera_too_much)
            3 -> messag = context.getString(R.string.pic_code_error)
            2 -> messag = context.getString(R.string.wrong_phone_or_email)
            5 -> messag = context.getString(R.string.account_not_exit)
            6 -> messag = context.getString(R.string.contact_admin)
            1 -> messag = context.getString(R.string.illegal_opera)
            406 -> messag = "图形验证码错误"
            else -> messag = context.getString(R.string.send_code_error)
        }
        return messag
    }
}
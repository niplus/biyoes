package com.biyoex.app.my.presenter

import androidx.appcompat.app.AppCompatActivity
import com.biyoex.app.common.bean.RequestResult
import com.biyoex.app.common.http.RetrofitHelper
import com.biyoex.app.common.mvpbase.BaseObserver
import com.biyoex.app.common.mvpbase.BasePresent
import com.biyoex.app.my.view.OCRView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject

/**
 *
 * @ProjectName:    VBT-android$
 * @Package:        com.vbt.app.my.presenter$
 * @ClassName:      OCRPresenter$
 * @Description:    实名认证P层
 * @Author:         赵伟国
 * @CreateDate:     2020-04-10$ 12:46$
 * @Version:        1.0
 */
class OCRPresenter : BasePresent<OCRView>() {

    //请求4要素验证姓名银行卡是否正确
    fun checkBankAndName(name: String, cardId: String, bankID: String) {
        RetrofitHelper.getIns()
                .zgtopApi
                .checkBankAndName(name, cardId, bankID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : BaseObserver<String>(mView.context as AppCompatActivity) {
                    override fun success(t: String) {
                        t?.let {
                            var json = JSONObject(t)
                            if (!t.contains("error")) {
                                val string = json.getString("biz_token")
                                mView.checkBankSuccess(string)
                            } else {
                                mView.showToast("信息有误，请重新输入")
                                mView.hideLoadingDialog()
                            }
                        }
                    }
                })
    }

    fun postRealName(name: String, cardID: String, bankID: String, type: Int, strPositiveUrl: String, strReverseSideUrl: String, strHoldPositiveUrl: String) {
        mView.showLoadingDialog()
        RetrofitHelper.getIns()
                .zgtopApi
                .postAuth(name, cardID, bankID, type, strPositiveUrl, strReverseSideUrl, strHoldPositiveUrl)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : BaseObserver<RequestResult<Any>>() {
                    override fun success(t: RequestResult<Any>) {
                        mView.hideLoadingDialog()
                        mView.postAuthSuccess()

                    }
                    override fun failed(code: Int, data: String?, msg: String?) {
                        super.failed(code, data, msg)
                        mView.hideLoadingDialog()
                        msg?.let {
                            mView.showToast(msg)
                        }
                    }
                })
    }

}
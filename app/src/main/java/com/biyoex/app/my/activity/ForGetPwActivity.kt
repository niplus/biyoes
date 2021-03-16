package com.biyoex.app.my.activity

import android.text.InputType
import com.biyoex.app.R
import com.biyoex.app.common.base.BaseActivity
import com.biyoex.app.common.bean.RequestResult
import com.biyoex.app.common.http.RetrofitHelper
import com.biyoex.app.common.mvpbase.BaseObserver
import com.biyoex.app.common.utils.RegexUtils
import com.biyoex.app.common.utils.TimeCount
import com.biyoex.app.common.utils.ToastUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_for_get_pw.*
import kotlinx.android.synthetic.main.activity_for_get_pw.iv_back
import kotlinx.android.synthetic.main.activity_for_get_pw.tv_send_code

/**
     * 新的修改登录密码页面
     * */
class ForGetPwActivity : BaseActivity() {
    override fun getLayoutId(): Int = R.layout.activity_for_get_pw

    override fun initData() {
        iv_new_eyes.tag = true
        iv_again_eves.tag = true
    }

    override fun initView() {
        iv_back.setOnClickListener { finish()
        }

        tv_send_code.setOnClickListener {
            if(et_account.text.isNullOrEmpty()){
                showToast(getString(R.string.input_phone_email))
            }
            else if(!RegexUtils.isEmail(et_account.text)&&!RegexUtils.isTel(et_account.text)){
                ToastUtils.showToast(getString(R.string.account_format_error))
            }
            else{
                sendCode()
            }
        }

        //新密码明文判断
        iv_new_eyes.setOnClickListener {
            if(iv_new_eyes.getTag() as Boolean){//明文
                //明文
                edit_new_pw.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                iv_new_eyes.setImageResource(R.mipmap.e_open)
                iv_new_eyes.tag = false
                edit_new_pw.setSelection(edit_new_pw.text.toString().length)
            }
            else{//秘文
                edit_new_pw.setInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)
                iv_new_eyes.tag = true
                iv_new_eyes.setImageResource(R.mipmap.e_close)
                edit_new_pw.setSelection(edit_new_pw.text.toString().length)
            }
        }

        //再次输入密码明文判断
        iv_again_eves.setOnClickListener {
            if(iv_again_eves.getTag() as Boolean){//明文
                //明文
                ed_again_pw.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                iv_again_eves.setImageResource(R.mipmap.e_open)
                iv_again_eves.tag = false
                ed_again_pw.setSelection(ed_again_pw.text.toString().length)
            }
            else{
                ed_again_pw.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                iv_again_eves.tag = true
                iv_again_eves.setImageResource(R.mipmap.e_close)
                ed_again_pw.setSelection(ed_again_pw.text.toString().length)
            }
        }

        btn_confirm.setOnClickListener {
            if(et_account.text.isNullOrEmpty()){
                showToast(getString(R.string.input_phone_email))
            }
            else if(!RegexUtils.isEmail(et_account.text)&&!RegexUtils.isTel(et_account.text)){
                ToastUtils.showToast(getString(R.string.account_format_error))
            }
            else if(ed_code.text.isNullOrEmpty()){
                showToast(getString(R.string.code_empty))
            }
            else if(edit_new_pw.text.isNullOrEmpty()){
                showToast(getString(R.string.input_new_password))
            }
            else if(ed_again_pw.text.isNullOrEmpty()){
                showToast(getString(R.string.repeat_password))
            }
            else{
                updatePassword()
            }
        }

    }

    private fun updatePassword() {

        RetrofitHelper.getIns().zgtopApi
                .forgetPassword(et_account.text.toString(),ed_code.text.toString(),edit_new_pw.text.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object :BaseObserver<RequestResult<Any>>(){
                    override fun success(t: RequestResult<Any>) {
                        ToastUtils.showToast(getString(R.string.reset_password_success))
                        finish()
                    }
                })
    }

    //发送验证码
    private fun sendCode() {
        RetrofitHelper.getIns().zgtopApi
                .sendFindCode(et_account.text.toString(),"")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object :BaseObserver<RequestResult<Any>>(){
                    override fun success(t: RequestResult<Any>) {
                        showToast(getString(R.string.send_sms_success))
                        TimeCount(60000, 1000, tv_send_code).start()
                    }

                    override fun failed(code: Int, data: String?, msg: String?) {
                        super.failed(code, data, msg)
                      msg?.let {
                          showToast(it)
                      }
                    }
                })
    }

}

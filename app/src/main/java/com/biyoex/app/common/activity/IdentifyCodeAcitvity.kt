package com.biyoex.app.common.activity

import android.text.TextUtils
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import butterknife.BindView
import butterknife.OnClick
import com.uber.autodispose.AutoDispose
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.biyoex.app.R
import com.biyoex.app.common.Constants
import com.biyoex.app.common.base.BaseAppCompatActivity
import com.biyoex.app.common.bean.RequestResult
import com.biyoex.app.common.data.SessionLiveData
import com.biyoex.app.common.data.SessionLiveData.OnRequestSessionSuccessListener
import com.biyoex.app.common.http.RetrofitHelper
import com.biyoex.app.common.mvpbase.BaseObserver
import com.biyoex.app.common.okhttp.OkHttpUtils
import com.biyoex.app.common.okhttp.callback.StringCallback
import com.biyoex.app.common.utils.MessagDealUtils
import com.biyoex.app.common.utils.SharedPreferencesUtils
import com.biyoex.app.common.utils.ToastUtils
import com.biyoex.app.common.utils.log.Log
import com.biyoex.app.common.widget.CodeInputView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.Call
import org.json.JSONException
import org.json.JSONObject
import java.util.*

//输入验证码
class IdentifyCodeAcitvity : BaseAppCompatActivity() {
    @JvmField
    @BindView(R.id.code_input)
    var codeInputView: CodeInputView? = null

    @JvmField
    @BindView(R.id.tv_title)
    var tvTitle: TextView? = null

    @JvmField
    @BindView(R.id.tv_message)
    var tvMessage: TextView? = null
    private var type = 0
    override fun initView() {
        if (type == VALIDATE_DEVICE) {
            tvTitle!!.setText(R.string.unusual_login_code)
            tvMessage!!.text = """
                ${getString(R.string.not_usual_login_need_certify)}${starNum(intent!!.getStringExtra("name"))}
                ${getString(R.string.please_input_code)}
                """.trimIndent()
        } else {
            if (intent!!.getStringExtra("name").contains("@")) {
                tvTitle!!.setText(R.string.email_code)
            } else {
                tvTitle!!.setText(R.string.message_code)
            }
            tvMessage!!.text = """
                ${getString(R.string.already_send_code_to)}${starNum(intent!!.getStringExtra("name"))}
                ${getString(R.string.please_input_code)}
                """.trimIndent()
        }
        codeInputView!!.setOnCodeCompleteListener { code: String? ->
            when (type) {
                FORGET_PASSWORD -> requestFindPassword(intent!!.getStringExtra("name"), code, intent!!.getStringExtra("pwd"))
                VALIDATE_DEVICE -> requestLoginValidate(code)
                REGISTER -> requestRegister(intent!!.getStringExtra("name"), intent!!.getStringExtra("pwd"), code, intent!!.getStringExtra("invite"))
            }
        }
    }

    override fun initData() {
        intent = getIntent()
        type = intent.getIntExtra("type", 0)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_identify_code_acitvity
    }

    @OnClick(R.id.iv_menu)
    override fun onBackPressed() {
        super.onBackPressed()
    }

    /**
     * 忘記密碼
     */
    fun requestFindPassword(name: String?, code: String?, pwd: String?) {
        showLoadingDialog()
        RetrofitHelper
                .getIns()
                .zgtopApi
                .forgetPassword(name, code, pwd)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .`as`(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this, Lifecycle.Event.ON_DESTROY)))
                .subscribe(object : BaseObserver<RequestResult<Any>>() {
                    protected override fun success(requestResult: RequestResult<Any>) {
                        val code1 = requestResult.code
                        when (code1) {
                            200 -> {
                                ToastUtils.showToast(getString(R.string.reset_password_success))
                                setResult(RESULT_OK)
                                finish()
                            }
                            1 -> ToastUtils.showToast(getString(R.string.empty_account))
                            2 -> ToastUtils.showToast(getString(R.string.code_empty))
                            3 -> ToastUtils.showToast(getString(R.string.password_empty))
                            4 -> ToastUtils.showToast(getString(R.string.account_not_exist))
                            100 -> ToastUtils.showToast(getString(R.string.never_send_code))
                            101 -> ToastUtils.showToast(getString(R.string.send_too_much))
                            102 -> ToastUtils.showToast(getString(R.string.code_error))
                        }
                        hideLoadingDialog()
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        hideLoadingDialog()
                    }
                })
    }

    /**
     * 二次验证
     */
    fun requestLoginValidate(code: String?) {
        showLoadingDialog()
        OkHttpUtils
                .post()
                .url(Constants.BASE_URL + "v1/secLogin")
                .addHeader("X-Requested-With", "XMLHttpRequest")
                .addParams("code", code)
                .build().execute(object : StringCallback() {
                    override fun onError(call: Call, e: Exception, id: Int) {
                        Log.e(e, "requestLoginValidate")
                        call.cancel()
                        hideLoadingDialog()
                    }

                    @Throws(JSONException::class)
                    override fun onResponse(response: String, id: Int) {
                        val dataJson = JSONObject(response)
                        var code = ""
                        if (dataJson.has("code")) {
                            code = dataJson.getString("code")
                            Log.i("requestLoginValidate result :$code")
                            //验证成功
                            if (code == "200") {
                                SessionLiveData.getIns().startTimer(object : OnRequestSessionSuccessListener {
                                    override fun onRequestSessionSuccess() {
                                        successLogin()
                                    }

                                    override fun onRequestSessionFailed() {
                                        hideLoadingDialog()
                                        ToastUtils.showToast(getString(R.string.relogin))
                                    }
                                })
                            } else {
                                ToastUtils.showToast(MessagDealUtils.loginValidateMessage(code, this@IdentifyCodeAcitvity))
                            }
                            hideLoadingDialog()
                        }
                    }
                })
    }

    /**
     * 成功之后的跳转
     */
    fun successLogin() {
        ToastUtils.showToast(getString(R.string.certify_success))
        setResult(RESULT_OK)
        finish()
    }

    /**
     * 请求注册
     */
    fun requestRegister(name: String?, pwd: String?, code: String?, inviteCode: String?) {
        showLoadingDialog()
        val mapParams: MutableMap<String, String?> = HashMap()
        mapParams["name"] = name
        mapParams["pwd"] = pwd
        mapParams["code"] = code
        if (!TextUtils.isEmpty(inviteCode)) {
            mapParams["inviteCode"] = inviteCode
        }
        OkHttpUtils
                .post()
                .url(Constants.BASE_URL + "v1/register")
                .addHeader("X-Requested-With", "XMLHttpReques")
                .params(mapParams)
                .build().execute(object : StringCallback() {
                    override fun onError(call: Call, e: Exception, id: Int) {
                        Log.e(e, "requestRegister")
                        hideLoadingDialog()
                    }

                    @Throws(JSONException::class)
                    override fun onResponse(response: String, id: Int) {
                        val dataJson = JSONObject(response)
                        var code = ""
                        var leftCount: String? = ""
                        if (dataJson.has("code")) {
                            code = dataJson.getString("code")
                        }
                        Log.i("requestRegister result :$code")
                        if (dataJson.has("data")) {
                            leftCount = dataJson.getString("data")
                        }
                        if (code == "200") {
                            ToastUtils.showToast(getString(R.string.regist_success))
                            SharedPreferencesUtils.getInstance().saveString("loginName", name)
                            setResult(RESULT_OK)
                            finish()
                        } else {
                            if (code == "106") {
                                ToastUtils.showToast(getString(R.string.code_error_too_much))
                            } else if (code == "1") {
                                ToastUtils.showToast(getString(R.string.input_phone_email))
                            } else if (code == "2") {
                                ToastUtils.showToast(getString(R.string.account_already_regist))
                            } else if (code == "3") {
                                ToastUtils.showToast(getString(R.string.password_empty))
                            } else if (code == "100") {
                                ToastUtils.showToast(getString(R.string.code_error))
                            } else if (code == "101") {
                                ToastUtils.showToast(getString(R.string.opera_too_much))
                            } else if (code == "102") {
                                if (leftCount != null && leftCount != "") ToastUtils.showToast(String.format(getString(R.string.certify_error_remains), leftCount))
                            } else if (code == "-1") {
                                ToastUtils.showToast(getString(R.string.unknown_error))
                            }
                        }
                        hideLoadingDialog()
                    }
                })
    }

    private fun starNum(num: String): String {
        return if (num.contains("@")) {
            num.replace("(\\w?)(\\w+)(\\w)(@\\w+\\.[a-z]+(\\.[a-z]+)?)".toRegex(), "$1****$3$4")
        } else {
            num.replace("(\\d{3})\\d{4}(\\d{4})".toRegex(), "$1****$2")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        SessionLiveData.getIns().setOnRequestSessionSuccessListener(null)
    }

    companion object {
        /**
         * 忘记密码
         */
        const val FORGET_PASSWORD = 0x1

        /**
         * 异常设备登录
         */
        const val VALIDATE_DEVICE = 0x2
        const val REGISTER = 0x3
    }
}
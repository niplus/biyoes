package com.biyoex.app.my.activity

import android.view.View
import com.biyoex.app.R
import com.biyoex.app.common.data.SessionLiveData
import com.biyoex.app.common.mvpbase.BaseActivity
import com.biyoex.app.common.utils.ActivityManagerUtils
import com.biyoex.app.common.utils.TimeCount
import com.biyoex.app.common.bean.GoogleSecret
import com.biyoex.app.my.presenter.BindGooglePresenter
import com.biyoex.app.my.view.BindGoogleView
import kotlinx.android.synthetic.main.activity_bind_google_next.*

    /**
     * 绑定google第二步
     * */
class BindGoogleNextActivity : BaseActivity<BindGooglePresenter>(), BindGoogleView {
    var tag = 0
    override fun bindGoogleSuccess() {
        SessionLiveData.getIns().getSeesionInfo()
        ActivityManagerUtils.getInstance().finishActivity(BindGoogleActivity::class.java)
        setResult(55)
        finish()
    }

    override fun getLayoutId(): Int = R.layout.activity_bind_google_next
    override fun createPresent(): BindGooglePresenter = BindGooglePresenter(this)
    override fun initComp() {
        tv_send_code.setOnClickListener {
            mPresent.sendSmsCode()
            TimeCount(60000, 1000, tv_send_code).start()
        }
        btn_submit.setOnClickListener {
            if (edit_login_password.text.isNullOrEmpty()) {
                showToast(getString(R.string.input_login_password))
            } else if (edit_googlecode.text.isNullOrEmpty()) {
                showToast(getString(R.string.input_google_code))
            } else if (tag == 0 && edit_sms_code.text.isNullOrEmpty()) {
                showToast(getString(R.string.input_certify_code))
            } else {
                if (tag == 0) {
                    mPresent.bindGoogle(edit_login_password.text.toString(), edit_googlecode.text.toString(), edit_sms_code.text.toString())
                } else {
                    mPresent.unBindGoogle(edit_login_password.text.toString(), edit_googlecode.text.toString())
                }
            }
        }
    }

    override fun initData() {
        iv_back.setOnClickListener { finish() }
        tag = intent.getIntExtra("tag", 0)
        if (tag == 1) {
            edit_sms_code.visibility = View.GONE
            tv_send_code.visibility = View.GONE
            view6.visibility = View.GONE
            textView6.text = getString(R.string.unbindgoogle)
        }

    }

    override fun getGoogleSuccess(mdata: GoogleSecret) {
    }

}

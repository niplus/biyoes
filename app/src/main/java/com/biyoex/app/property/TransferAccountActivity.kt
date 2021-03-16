package com.biyoex.app.property

import android.content.Intent
import android.view.View
import com.biyoex.app.R
import com.biyoex.app.common.activity.LoginActivity
import com.biyoex.app.common.data.SessionLiveData
import com.biyoex.app.common.mvpbase.BaseActivity
import com.biyoex.app.common.utils.TimerUtils
import com.biyoex.app.common.utils.ToastUtils
import com.biyoex.app.my.activity.RevisePayPasswordActivity
import com.biyoex.app.my.bean.ZGSessionInfoBean
import com.biyoex.app.property.presenter.TransferAccountPresenter
import com.biyoex.app.property.view.TransferAccountView
import kotlinx.android.synthetic.main.activity_transfer_account.*
    /*
    * 转账
    * */
class TransferAccountActivity : BaseActivity<TransferAccountPresenter>(), TransferAccountView {
    override fun postSmsSuccess() {
        timerUtils = TimerUtils.getInitialise()
        timerUtils!!.resetTimer()
        if (timerUtils!!.canOnClick) {
            timerUtils!!.startTimer(tv_send_code, 61)
        }
    }

    /**
     * 计时器类对象
     */
    private var timerUtils: TimerUtils? = null
    private var sessionInfo: ZGSessionInfoBean? = null
    override fun getUserBalance(userBalance: String, coidNmae: String) {
        tv_user_account.text = "${getString(R.string.useful)} $userBalance"
        tv_coin_name.text = coidNmae
    }

    override fun postTransferSuccess() {
        showToast(getString(R.string.transfer_success))
        var intent = Intent(this, PropertyRecordActivity::class.java)
        intent.putExtra("coidId", getIntent().getIntExtra("coidId", 0))
        startActivity(intent)
    }
    override fun createPresent(): TransferAccountPresenter = TransferAccountPresenter()
    override fun getLayoutId(): Int = R.layout.activity_transfer_account

    override fun initComp() {

    }

    override fun initData() {
        mPresent.getUserBalance(intent.getIntExtra("coidId", 0))
        sessionInfo = SessionLiveData.getIns().value
        if (sessionInfo != null) {
            rl_google_code.visibility = if (sessionInfo!!.isGoogleBind) View.VISIBLE else View.GONE
        }
        iv_back.setOnClickListener { finish() }
        //提交转账
        btn_confirm.setOnClickListener {
            if (edit_account.text.isNullOrEmpty()) {
                showToast(getString(R.string.transfer_address))
            } else if (edit_transfer_number.text.isNullOrEmpty()) {
                showToast(getString(R.string.transfer_number))
            } else if (edit_trade_pass.text.isNullOrEmpty()) {
                showToast(getString(R.string.input_trade_password))
            } else {
                sessionInfo = SessionLiveData.getIns().value
                if (sessionInfo != null) {
                    if (sessionInfo!!.isSafeword) {
                        mPresent.postUserTransfer(intent.getIntExtra("coidId", 0), ed_google_code.text.toString(), edit_account.text.toString(), edit_transfer_number.text.toString(), ed_verification_code.text.toString(), edit_trade_pass.text.toString())
                    } else {
                        ToastUtils.showToast(getString(R.string.need_set_trade_password))
                        val itPersonalInfo = Intent(this@TransferAccountActivity, RevisePayPasswordActivity::class.java)
                        itPersonalInfo.putExtra("revise", getString(R.string.setting))
                        startActivity(itPersonalInfo)
                    }
                } else {
                    val itLogin = Intent(this, LoginActivity::class.java)
                    itLogin.putExtra("type", "info")
                    startActivity(itLogin)
                }
            }
        }

        tv_send_code.setOnClickListener {
            if (edit_account.text.isNullOrEmpty()) {
                showToast(getString(R.string.transfer_address))
            } else if (edit_transfer_number.text.isNullOrEmpty()) {
                showToast(getString(R.string.transfer_number))
            } else if (edit_trade_pass.text.isNullOrEmpty()) {
                showToast(getString(R.string.input_trade_password));
            } else {
                mPresent.postSms(4, "")
            }
        }
    }


}

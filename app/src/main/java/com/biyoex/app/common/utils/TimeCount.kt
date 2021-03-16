package com.biyoex.app.common.utils

import android.os.CountDownTimer
import android.widget.Button

class TimeCount(var millisInFuture:Long,var countDownInterval:Long,var btn_count:Button) : CountDownTimer(millisInFuture,countDownInterval) {
    override fun onTick(millisUntilFinished: Long) {
        btn_count.setEnabled(false)
        btn_count.text = (millisUntilFinished / 1000).toString() + "秒"
    }

    override fun onFinish() {
        btn_count.isEnabled = true
        btn_count.text = "发送验证码"
    }
}
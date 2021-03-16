package com.biyoex.app.home.view

import android.app.Activity
import android.view.Gravity
import android.view.WindowManager
import android.widget.TextView
import com.biyoex.app.R
import com.biyoex.app.common.widget.loopview.TopDialog

class MoreDialog(context: Activity, var listener: DialogMorePartner) : TopDialog(context, R.layout.dialog_pos_more) {
    init {
        val window = window
        window!!.decorView.setPadding(0, 0, 0, 0)
        // 获取Window的LayoutParams
        val attributes = window.attributes
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT
        attributes.gravity = Gravity.TOP
        // 一定要重新设置, 才能生效
        window.attributes = attributes
        findViewById<TextView>(R.id.tv_pos_record).setOnClickListener {
            listener.onRecord()
        }

        findViewById<TextView>(R.id.tv_icon_switch_account).setOnClickListener {
            listener.onSwitchAccount()
        }
    }
}

interface DialogMorePartner {
    fun onRecord()
    fun onSwitchAccount()
}
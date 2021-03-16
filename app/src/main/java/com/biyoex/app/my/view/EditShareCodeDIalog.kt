package com.biyoex.app.my.view

import android.app.Activity
import android.view.Gravity
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import com.biyoex.app.R
import com.biyoex.app.common.widget.BottomDialog

class EditShareCodeDIalog(context: Activity,var listener:DialogIsPartner) :BottomDialog(context, R.layout.dialog_edit_share_code) {
    init {
        val window = window
        window!!.decorView.setPadding(100, 0, 100, 0)
        // 获取Window的LayoutParams
        val attributes = window.attributes
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT
        attributes.gravity = Gravity.CENTER
        // 一定要重新设置, 才能生效
        window.attributes = attributes
        findViewById<TextView>(R.id.tv_cancel).setOnClickListener {
            listener.onCancel()
        }

        findViewById<TextView>(R.id.tv_sure).setOnClickListener {
            listener.OnNextClickListener(findViewById<EditText>(R.id.edit_shareCode).text.toString())
        }
    }
}

interface DialogIsPartner{
    fun OnNextClickListener(userId:String)

    fun onCancel()
}
package com.biyoex.app.common.widget

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import com.biyoex.app.R

class BaseDialog (context: Context,listener:OnClicklisteners):BottomDialog(context,R.layout.base_dialogs) {

        init {
            val window = window
            window!!.decorView.setPadding(100, 0, 100, 0)
            // 获取Window的LayoutParams
            val attributes = window.attributes
            attributes.width = WindowManager.LayoutParams.MATCH_PARENT
            attributes.gravity = Gravity.CENTER
            // 一定要重新设置, 才能生效
            window.attributes = attributes

            findViewById<TextView>(R.id.tv_sure).setOnClickListener {
                listener.sureOnclicklistener()
            }
            findViewById<TextView>(R.id.tv_dialog_title).visibility = View.GONE
            findViewById<TextView>(R.id.dialog_ms).text = "确认是否成为合伙人"
            findViewById<TextView>(R.id.tv_sure).text = "${context.getString(R.string.sure)}"
            findViewById<TextView>(R.id.tv_cancel).visibility = View.VISIBLE
            findViewById<TextView>(R.id.tv_cancel).setOnClickListener { dismiss() }
        }
}

interface  OnClicklisteners{
     fun   sureOnclicklistener()
}
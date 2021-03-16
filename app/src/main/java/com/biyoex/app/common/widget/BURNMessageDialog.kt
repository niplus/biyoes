package com.biyoex.app.common.widget

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import com.biyoex.app.R

class BURNMessageDialog (context: Context):BottomDialog(context, R.layout.dialog_baseview) {

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
            dismiss()
        }
        findViewById<TextView>(R.id.dialog_ms).text = "${context.getString(R.string.burn_info)}"
        findViewById<TextView>(R.id.tv_sure).text = "${context.getString(R.string.sure)}"
        findViewById<TextView>(R.id.tv_cancel).visibility = View.GONE
    }
}
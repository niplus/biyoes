package com.biyoex.app.home.view

import android.content.Context
import android.view.Gravity
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import com.biyoex.app.R
import com.biyoex.app.common.bean.SignToCoin
import com.biyoex.app.common.widget.BottomDialog

class HomedetailsDialog(context: Context,var data:SignToCoin) :BottomDialog(context, R.layout.dialog_lucky_details) {
    init {
        val window = window
        window!!.decorView.setPadding(0, 0, 0, 0)
        // 获取Window的LayoutParams
        val attributes = window.attributes
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT
        attributes.gravity = Gravity.CENTER
        // 一定要重新设置, 才能生效
        window.attributes = attributes
        var tvname = findViewById<TextView>(R.id.tv_title);
        tvname.text = if(data.amount.isNullOrEmpty()) data.name else "${data.amount}${data.name}"
        findViewById<ImageView>(R.id.iv_close).setOnClickListener {
            dismiss()
        }
        findViewById<TextView>(R.id.tv_btn).setOnClickListener {
          dismiss()
        }
    }
}
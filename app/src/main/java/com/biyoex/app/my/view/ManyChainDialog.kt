package com.biyoex.app.my.view

import android.app.Activity
import android.view.Gravity
import android.view.WindowManager
import com.biyoex.app.R
import com.biyoex.app.common.widget.BottomDialog

/**
 * Created by mac on 20/9/2.
 * 支持多链说明
 */


class ManyChainDialog(context: Activity) : BottomDialog(context, R.layout.dialog_many_chain) {
    init {
        val window = window
        window!!.decorView.setPadding(100, 0, 100, 0)
        // 获取Window的LayoutParams
        val attributes = window.attributes
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT
        attributes.gravity = Gravity.CENTER
        // 一定要重新设置, 才能生效
        window.attributes = attributes
    }
}

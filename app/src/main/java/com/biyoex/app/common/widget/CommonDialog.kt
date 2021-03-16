package com.biyoex.app.common.widget

import android.content.Context
import android.widget.TextView
import com.biyoex.app.R

class CommonDialog(context: Context) :BottomDialog(context, R.layout.dialog_baseview){
    init {
        findViewById<TextView>(R.id.dialog_ms)
                .text = "确定成为合伙人"
    }
}
package com.biyoex.app.common.utils

import android.content.Context
import android.widget.LinearLayout
import android.widget.TextView
import com.biyoex.app.R
import com.biyoex.app.common.widget.BottomDialog

class ShareWechatDialog(context: Context,url:String,title:String,message:String) : BottomDialog(context, R.layout.dialog_share_wechat) {
    init {
        findViewById<LinearLayout>(R.id.layout_send_firends).setOnClickListener {
            ShareWechat.ShareUrlToWechat(url,title,message,context,true)
            dismiss()
        }
        findViewById<LinearLayout>(R.id.tv_send_friend).setOnClickListener {
            ShareWechat.ShareUrlToWechat(url,title,message,context,false)
            dismiss()
        }
        findViewById<TextView>(R.id.tv_cancel).setOnClickListener {
            dismiss()
        }
    }
}
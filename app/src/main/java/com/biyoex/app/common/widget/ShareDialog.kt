package com.biyoex.app.common.widget

import android.app.Activity
import android.content.Context
import android.graphics.*
import com.biyoex.app.R
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.elvishew.xlog.XLog
import com.github.fujianlian.klinechart.utils.ViewUtil
import com.biyoex.app.common.utils.ShareWechat
import com.biyoex.app.common.utils.SharedUtils


class ShareDialog(context: Context) : BottomDialog(context, R.layout.dialog_share) {
    init {
//        setContentView(R.layout.dialog_share)
        var imageView = findViewById<ImageView>(R.id.iv_shareimage)
        val decodeResource = BitmapFactory.decodeResource(context.resources, R.mipmap.share_bottom)
        val createBitmap = createBitmap(SharedUtils.captureWithStatusBar(context as Activity), decodeResource)
        //显示截图文件
        imageView.setImageBitmap(createBitmap)
        findViewById<LinearLayout>(R.id.layout_send_firends).setOnClickListener {
            ShareWechat.shareImageToWechat(createBitmap, "", context, true)
            dismiss()
        }
        findViewById<LinearLayout>(R.id.tv_send_friend).setOnClickListener {
            ShareWechat.shareImageToWechat(createBitmap, "", context, false)
            dismiss()
        }
        findViewById<TextView>(R.id.tv_cancel).setOnClickListener {
            dismiss()
        }
    }

    private fun createBitmap(src: Bitmap?, watermark: Bitmap): Bitmap? {
        val tag = "createBitmap"
        XLog.Log.d(tag, "create a new bitmap")
        if (src == null) {
            return null
        }
        val w = src!!.width
        val h = src!!.height
        val wh = watermark.height
        val newBitmap = ViewUtil.getNewBitmap(watermark, w, wh)
        val newb = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_4444)
        val cv = Canvas(newb)
        cv.drawBitmap(src, 0f, 0f, null)
        cv.drawBitmap(newBitmap, 0f, h.toFloat() - wh, null)
        //save all clip
        cv.save()
        //store
        cv.restore()
        return newb
    }

}
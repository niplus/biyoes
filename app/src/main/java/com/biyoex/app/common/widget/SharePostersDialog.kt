package com.biyoex.app.common.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.elvishew.xlog.XLog
import com.biyoex.app.R
import com.biyoex.app.common.Constants
import com.biyoex.app.common.data.SessionLiveData
import com.biyoex.app.common.utils.CommonUtil
import com.biyoex.app.common.utils.ShareWechat
class SharePostersDialog(context: Context) : BottomDialog(context, R.layout.dialog_share_posters) {
    init {
        var ivOnePosters = findViewById<ImageView>(R.id.iv_one_posters)
        var ivTwoPosters = findViewById<ImageView>(R.id.iv_two_posters)
        var ivThreePosters = findViewById<ImageView>(R.id.iv_three_posters)
        var ivOneselect = findViewById<ImageView>(R.id.iv_one_posters_select)
        var ivTwoSelect = findViewById<ImageView>(R.id.iv_two_posters_select)
        var ivThreeSelect = findViewById<ImageView>(R.id.iv_three_posters_select)
        var tvWechatFriend = findViewById<LinearLayout>(R.id.tv_send_friend)
        var tvWEchatFriends = findViewById<LinearLayout>(R.id.layout_send_firends)
        var tv_cancel = findViewById<TextView>(R.id.tv_cancel)
        var tv_save_phone = findViewById<LinearLayout>(R.id.layout_download)
        var zXingBitmap: Bitmap?
        var bitmapOnePosters :Bitmap?
        var bitmapTowPosters:Bitmap?
        var bitmapThreePosters:Bitmap?
        var shareBitmap:Bitmap?

        if (SessionLiveData.getIns().value != null) {
            val path = Constants.REALM_NAME + "/user/register?intro=" + SessionLiveData.getIns().value!!.id.toLong()
            zXingBitmap = CommonUtil.createQRCode(path, 150)
            bitmapOnePosters = createBitmap(BitmapFactory.decodeResource(context.resources, R.mipmap.one_posters), zXingBitmap)
            bitmapTowPosters = createBitmap(BitmapFactory.decodeResource(context.resources, R.mipmap.two_posters), zXingBitmap)
            bitmapThreePosters = createBitmap(BitmapFactory.decodeResource(context.resources, R.mipmap.three_posters), zXingBitmap)
            ivOnePosters.setImageBitmap(bitmapOnePosters)
            ivTwoPosters.setImageBitmap(bitmapTowPosters)
            ivThreePosters.setImageBitmap(bitmapThreePosters)
            shareBitmap = bitmapOnePosters
            //判断选择海报图片 默认选择第一张
            ivOneselect.setOnClickListener {
                ivOneselect.setImageDrawable(context.resources.getDrawable(R.mipmap.icon_vip_select))
                ivTwoSelect.setImageDrawable(context.resources.getDrawable(R.drawable.share_image_select))
                ivThreeSelect.setImageDrawable(context.resources.getDrawable(R.drawable.share_image_select))
                shareBitmap = bitmapOnePosters!!
            }
            ivTwoSelect.setOnClickListener {
                ivTwoSelect.setImageDrawable(context.resources.getDrawable(R.mipmap.icon_vip_select))
                ivOneselect.setImageDrawable(context.resources.getDrawable(R.drawable.share_image_select))
                ivThreeSelect.setImageDrawable(context.resources.getDrawable(R.drawable.share_image_select))
                shareBitmap = bitmapTowPosters
            }
            ivThreeSelect.setOnClickListener {
                ivThreeSelect.setImageDrawable(context.resources.getDrawable(R.mipmap.icon_vip_select))
                ivTwoSelect.setImageDrawable(context.resources.getDrawable(R.drawable.share_image_select))
                ivOneselect.setImageDrawable(context.resources.getDrawable(R.drawable.share_image_select))
                shareBitmap = bitmapThreePosters
            }
            tvWEchatFriends.setOnClickListener {
                if (zXingBitmap != null) {
//                ivOnePosters.setImageBitmap(createBitmap)
                    ShareWechat.shareImageToWechat(shareBitmap!!, "", context, true)
                    dismiss()
//                CommonUtil.saveBitmap2file(createBitmap,)
                }
            }
            tvWechatFriend.setOnClickListener {
                if (zXingBitmap != null) {
//                    val createBitmap = createBitmap(postersBItmap, zXingBitmap!!)
                    ShareWechat.shareImageToWechat(shareBitmap, "", context, false)
                    dismiss()
                }
            }

            tv_save_phone.setOnClickListener {

                if (zXingBitmap != null) {
//                    val createBitmap = createBitmap(postersBItmap, zXingBitmap!!)
                    CommonUtil.saveBitmap2file(shareBitmap, context)
                    dismiss()
                }
            }

        }
        tv_cancel.setOnClickListener {
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
        val ww = watermark.width
        val wh = watermark.height
        val copy = src.copy(Bitmap.Config.ARGB_8888, true)
        val cv = Canvas(copy)
        cv.drawBitmap(src, 0f, 0f, null)
        cv.drawBitmap(watermark, w - ww.toFloat()-10, h - wh.toFloat()-10, null)
        //save all clip
        cv.save()
        //store
        cv.restore()
        return copy
    }
}
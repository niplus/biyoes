package com.biyoex.app.my

import android.content.ClipData
import android.content.ClipboardManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.os.Environment
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder
import com.biyoex.app.R
import com.biyoex.app.common.base.BaseAppCompatActivity
import com.biyoex.app.common.data.SessionLiveData
import com.biyoex.app.common.utils.ToastUtils
import com.biyoex.app.common.utils.log.Log
import com.biyoex.app.databinding.ActivityRegistInviteBinding
import kotlinx.android.synthetic.main.layout_title_bar.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.*


class RegistInviteActivity : AppCompatActivity() {

    lateinit var binding: ActivityRegistInviteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityRegistInviteBinding>(this, R.layout.activity_regist_invite)

        binding.apply {

            lifecycleScope.launch {
                tv_title.text ="注册邀请"
                tv_title.setTextColor(0xffFFFFFF.toInt())
                iv_menu.setImageResource(R.mipmap.arrow_back_white)
                iv_menu.setOnClickListener {
                    finish()
                }
                val bitmap = QRCodeEncoder.syncEncodeQRCode("https://biyoex.com/mobile/#register?intro=${tvInviterId.text}",  BGAQRCodeUtil.dp2px(this@RegistInviteActivity, 106f))
                ivQrcode.setImageBitmap(bitmap)
                tvInviterId.text = SessionLiveData.getIns().value?.id.toString()

                tvCopy.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG )
                tvCopy.setOnClickListener {
                    val clipboard = getSystemService(BaseAppCompatActivity.CLIPBOARD_SERVICE) as ClipboardManager
                    val clipData = ClipData.newPlainText(null, tvInviterId.text)
                    clipboard.setPrimaryClip(clipData)
                    ToastUtils.showToast("已复制")
                }
                btnCopy.setOnClickListener {
                    val clipboard = getSystemService(BaseAppCompatActivity.CLIPBOARD_SERVICE) as ClipboardManager
                    val clipData = ClipData.newPlainText(null, "https://biyoex.com/mobile/#register?intro=${tvInviterId.text}")
                    clipboard.setPrimaryClip(clipData)
                    ToastUtils.showToast("已复制")
                }

                btnSave.setOnClickListener {
                    GlobalScope.launch {
                        viewSaveToImage(parentContent)
                    }
                }
            }
        }
    }


    private fun viewSaveToImage(view: View) {
        view.setDrawingCacheEnabled(true)
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH)
        view.setDrawingCacheBackgroundColor(Color.WHITE)

        // 把一个View转换成图片
        val cachebmp: Bitmap? = loadBitmapFromView(view)
        val fos: FileOutputStream
        var imagePath = ""
        try {
            // 判断手机设备是否有SD卡
            val isHasSDCard = Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)
            if (isHasSDCard) {
                // SD卡根目录
                val sdRoot: File = Environment.getExternalStorageDirectory()
                val file = File(sdRoot, Calendar.getInstance().getTimeInMillis().toString() + ".png")
                fos = FileOutputStream(file)
                imagePath = file.getAbsolutePath()
            } else throw Exception("nidongliang 创建文件失败!")
            cachebmp?.compress(Bitmap.CompressFormat.PNG, 90, fos)
            fos.flush()
            fos.close()
        } catch (e: Exception) {
            e.printStackTrace()

        }
        Log.i("nidongliang imagePath=$imagePath")
        view.destroyDrawingCache()
    }

    private fun loadBitmapFromView(v: View): Bitmap? {
        val w = v.width
        val h = v.height
        val bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val c = Canvas(bmp)
        c.drawColor(Color.WHITE)
        /** 如果不设置canvas画布为白色，则生成透明  */
        v.layout(0, 0, w, h)
        v.draw(c)
        return bmp
    }




}
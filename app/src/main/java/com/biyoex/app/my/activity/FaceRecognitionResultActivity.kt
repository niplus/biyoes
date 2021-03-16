package com.biyoex.app.my.activity

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.biyoex.app.R
import com.biyoex.app.databinding.ActivityFaceRecognitionResultBinding

class FaceRecognitionResultActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityFaceRecognitionResultBinding>(this, R.layout.activity_face_recognition_result)

        with(binding){
            val isSuccess = intent.getBooleanExtra("isSuccess", false)
            if (isSuccess){
                ivHint.setImageResource(R.mipmap.face_success)
                tvHint.text = "恭喜！ 视频认证成功"
                btnConfirm.text = "确定"
                setResult(Activity.RESULT_OK)
            }else{
                ivHint.setImageResource(R.mipmap.face_failed)
                tvHint.text = "很抱歉！您的认证失败"
            }
            btnConfirm.setOnClickListener {
                finish()
            }
        }
    }
}
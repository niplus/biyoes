package com.v

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.media.MediaPlayer.OnPreparedListener
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import butterknife.ButterKnife
import butterknife.Unbinder
import com.isseiaoki.simplecropview.util.Logger
import com.tbruyelle.rxpermissions2.RxPermissions
import com.biyoex.app.MainActivity
import com.biyoex.app.R
import com.biyoex.app.VBTApplication
import com.biyoex.app.common.Constants
import com.biyoex.app.common.bean.BaseUrlBean
import com.biyoex.app.common.http.RetrofitHelper
import com.biyoex.app.common.mvpbase.BaseObserver
import com.biyoex.app.common.utils.SharedPreferencesUtils
import com.biyoex.app.common.utils.log.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_start.*
import org.greenrobot.eventbus.EventBus
import pl.droidsonroids.gif.GifDrawable
import java.util.*

class StartActivity :AppCompatActivity(){

    private var timerTask:TimerTask?= null
    private var  timer:Timer?=null
    private var restTime = 3
    private var unbinder:Unbinder?=null

    private var loadPosterFailed = false

    private var  duration = 0
    private var  instart = 0

    private var readyCount = 0

    private var handler = @SuppressLint("HandlerLeak")
    object : Handler(){
        override fun handleMessage(msg: Message) {
            val what = msg.what
            if(what==0){
                if(loadPosterFailed){
                    startAcitivy()
                }
            }
        }

    }


    //请求动态ip
    private fun startAcitivy() {
        RetrofitHelper.getIns().zgtopApi
                .baseUrl
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : BaseObserver<BaseUrlBean?>() {
                    override fun failed(code: Int, data: String?, msg: String?) {
                        super.failed(code, data, msg)
                        openMainActivity()
                    }

                    override fun success(t: BaseUrlBean?) {
                        t?.let {
                            //切换测试服务器得把这三个注释掉
                            Constants.BASE_URL = it.http_protocol + it.domain_name + "/api/"
                            Constants.WEBSOCKET_URL = it.socket_protocol + it.domain_name + "/socket.io/?"
                            Constants.REALM_NAME = it.http_protocol + it.domain_name
                            RetrofitHelper.getIns().setOkHttpClient()
                            readyCount++
                            if (readyCount == 2)
                                openMainActivity()
                        }
                    }
                })
    }

    private fun openMainActivity() {
        val mainIntent = Intent(this@StartActivity, MainActivity::class.java)
        //说明mainactivity不存在，需要重新打开
        startActivity(mainIntent)
        finish()
    }


    override fun onResume() {
        val decorView = window.decorView
        val uiOptions = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        decorView.systemUiVisibility = uiOptions
//        if (video_view != null && !video_view.isPlaying) {
//            video_view.resume()
//        }
        super.onResume()
    }

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val rxPermissions = RxPermissions(this)
        rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CALL_PHONE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .subscribe { aBoolean: Boolean ->
                    if (aBoolean) {
                        setContentView(getLayoutId())
                        initData()
                    } else {
                        Toast.makeText(this@StartActivity, "未授权权限，部分功能不能使用", Toast.LENGTH_SHORT).show()
                        setContentView(getLayoutId())
                        initData()
                    }
                }
    }
     fun getLayoutId(): Int  = R.layout.activity_start

     fun initData() {
//        RateLivedata.getIns().init();
        initDatas()
        //注解绑定
        unbinder = ButterKnife.bind(this)
//        val uri = "android.resource://" + packageName + "/" + R.raw.start
//        val parse = Uri.parse(uri)
//        if (parse != null) {
//            video_view.setVideoURI(parse)
//            video_view.start()
//        }
//        video_view.setOnPreparedListener(OnPreparedListener { mp: MediaPlayer -> mp.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING) })
//        video_view.setOnCompletionListener(OnCompletionListener {
//            loadPosterFailed = true
//            handler.sendEmptyMessageDelayed(0,
//                    duration + 500.toLong())
//        })

//         video_view.set
         val gifDrawable = video_view.drawable as GifDrawable
         gifDrawable.loopCount = 1
         startAcitivy()
         gifDrawable.addAnimationListener {
             readyCount++
             if(readyCount == 2)
                openMainActivity()
         }
    }


     fun initDatas() {
        Log.i("startactivity init data")
        VBTApplication.APP_STATUS = 1
        SharedPreferencesUtils.getInstance().saveData("JSESSIONID", "")
        SharedPreferencesUtils.getInstance().saveData("route", "")
        //        SessionLiveData.getIns().getSeesionInfo();
        //部分手机退出到后台再进入会重新实例化这个activity
        if (!this.isTaskRoot) { //判断该Activity是不是任务空间的源Activity，“非”也就是说是被系统重新实例化出来
            //如果你就放在launcher Activity中话，这里可以直接return了
            val startIntent = intent
            val action = startIntent.action
            if (startIntent.hasCategory(Intent.CATEGORY_LAUNCHER) && action == Intent.ACTION_MAIN) {
                Logger.e("startactivity has exits")
                finish()
                return  //加return避免可能的exception
            }
        }
//
    }


    override fun onDestroy() {
        timer?.let {
          it.cancel()
            timer = null
        }
        timerTask?.let {
            it.cancel()
            timerTask =null
        }
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }


}
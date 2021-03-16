package com.biyoex.app.common.present

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.uber.autodispose.AutoDispose
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.biyoex.app.R
import com.biyoex.app.common.Constants
import com.biyoex.app.common.SendCodeView
import com.biyoex.app.common.bean.RequestResult
import com.biyoex.app.common.http.RetrofitHelper
import com.biyoex.app.common.mvpbase.BaseObserver
import com.biyoex.app.common.mvpbase.BasePresent
import com.biyoex.app.common.utils.log.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * 发送验证码业务
 */
class SendCodePresent : BasePresent<SendCodeView?> {
    private var picCode: String? = null
    private var type: Int
    private var sendType = 0

    /**
     * 手机或者邮箱
     */
    private var name: String? = null
    private var dialog: Dialog? = null
    private var ivCode: ImageView? = null
    private var edtCode: EditText? = null

    constructor(type: Int) {
        this.type = type
    }

    constructor(type: Int, sendType: Int) {
        this.type = type
        this.sendType = sendType
    }

    private fun sendAccountCode() {
        mView!!.showLoadingDialog()
        RetrofitHelper.getIns()
                .zgtopApi
                .sendAccountCode(sendType, picCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : BaseObserver<RequestResult<Any>>((mView!!.context as LifecycleOwner)) {
                    override fun success(requestResult: RequestResult<Any>) {
                        mView!!.hideLoadingDialog()
                        mView!!.showToast("发送成功")
                        mView!!.sendCodeSuccess()
                    }

                    public override fun failed(code: Int, data: String?, msg: String?) {
                        Log.i("send code failed : $code")
                        sendAuthCodeMessage(mView!!.context.getString(R.string.phone_email), code, mView!!.context)
                        mView!!.hideLoadingDialog()
                    }
                })
    }

    private fun sendLoginCode() {
        mView!!.showLoadingDialog()
        RetrofitHelper.getIns()
                .zgtopApi
                .sendLoginCode(picCode, name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .`as`(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(mView!!.context as AppCompatActivity, Lifecycle.Event.ON_DESTROY)))
                .subscribe(object : BaseObserver<RequestResult<Any>>((mView!!.context as AppCompatActivity)) {
                    override fun success(requestResult: RequestResult<Any>) {
                        mView!!.hideLoadingDialog()
                        mView!!.sendCodeSuccess()
                        mView!!.showToast("发送成功")
                    }

                    public override fun failed(code: Int, data: String?, msg: String?) {
                        Log.i("send login code failed : $code")
                        mView!!.showToast(sendAuthCodeMessage(mView!!.context.getString(R.string.phone_email), code, mView!!.context))
                        mView!!.hideLoadingDialog()
                    }
                })
    }

    private fun sendRegCode() {
        mView!!.showLoadingDialog()
        RetrofitHelper.getIns()
                .zgtopApi
                .sendRegCode(name, picCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .`as`(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(mView!!.context as AppCompatActivity, Lifecycle.Event.ON_DESTROY)))
                .subscribe(object : BaseObserver<RequestResult<Any>>((mView!!.context as AppCompatActivity)) {
                    override fun success(requestResult: RequestResult<Any>) {
                        mView!!.hideLoadingDialog()
                        mView!!.sendCodeSuccess()
                        mView!!.showToast("发送成功")
                    }

                    public override fun failed(code: Int, data: String?, msg: String?) {
                        Log.i("send reg code failed : $code")
                        mView!!.showToast(sendAuthCodeMessage(mView!!.context.getString(R.string.phone_email), code, mView!!.context))
                        mView!!.hideLoadingDialog()
                    }
                })
    }

    private fun sendBindPayCode() {
        mView!!.showLoadingDialog()
        RetrofitHelper.getIns()
                .zgtopApi
                .sendBindPayCode(name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .`as`(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(mView!!.context as AppCompatActivity, Lifecycle.Event.ON_DESTROY)))
                .subscribe(object : BaseObserver<RequestResult<Any>>((mView!!.context as AppCompatActivity)) {
                    override fun success(requestResult: RequestResult<Any>) {
                        mView!!.hideLoadingDialog()
                        mView!!.sendCodeSuccess()
                        mView!!.showToast("发送成功")
                    }

                    public override fun failed(code: Int, data: String?, msg: String?) {
                        mView!!.hideLoadingDialog()
                        when (code) {
                            -4 -> {
                                mView!!.showToast("手机号格式不正确")
                                return
                            }
                            -3 -> {
                                mView!!.showToast("操作频繁,请稍候2分钟再试")
                                return
                            }
                        }
                        mView!!.showToast(sendAuthCodeMessage(mView!!.context.getString(R.string.phone_email), code, mView!!.context))
                    }
                })
    }

    private fun sendFindCode() {
        mView!!.showLoadingDialog()
        RetrofitHelper.getIns()
                .zgtopApi
                .sendFindCode(name, picCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : BaseObserver<RequestResult<Any>>((mView!!.context as AppCompatActivity)) {
                    override fun success(requestResult: RequestResult<Any>) {
                        mView!!.hideLoadingDialog()
                        mView!!.sendCodeSuccess()
                        mView!!.showToast("发送成功")
                    }

                    public override fun failed(code: Int, data: String?, msg: String?) {
                        Log.i("send find code failed : $code")
                        mView!!.showToast(sendAuthCodeMessage(mView!!.context.getString(R.string.phone_email), code, mView!!.context))
                        mView!!.hideLoadingDialog()
                    }
                })
    }

    fun startSend() {
        when (type) {
            REGIST -> sendRegCode()
            ACCOUNT -> sendAccountCode()
            BIND_PHONE -> sendBindPayCode()
            FIND_PASS -> sendFindCode()
            LOGIN -> sendLoginCode()
        }
    }

    fun sendCode(name: String?) {
        this.name = name
        if (type == REGIST) {
            imageCode
        } else {
            startSend()
            //            RetrofitHelper
//                    .getIns()
//                    .getZgtopApi()
//                    .getCount()
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new BaseObserver<String>() {
//                        @Override
//                        public void success(String s) {
//                            try {
//                                JSONObject jsonObject = new JSONObject(s);
//                                int code = jsonObject.getInt("code");
//
//                                if (code == 200) {
//                                    int count = jsonObject.getJSONObject("data").getInt("count");
//                                    if (count < 5) {
//                                        startSend();
//                                    } else {
//                                        getImageCode();
//                                    }
//                                } else {
//                                    mView.hideLoadingDialog();
//                                    mView.showToast("发送验证码失败");
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//                        @Override
//                        public void failed(int code, String data, String msg) {
//
//                        }
//                    });
        }
    }

    /**
     * 处理响应码，把响应码变成中文并且返回出去给ui界面去提示
     */
    fun sendAuthCodeMessage(hint: String, resultCode: Int, context: Context): String {
        var messag = ""
        messag = when (resultCode) {
            100 -> "图形验证码为空"
            101 -> context.getString(R.string.please_input) + hint
            102 -> context.getString(R.string.not_vip) + hint
            103 -> context.getString(R.string.not_bind) + hint
            4 -> context.getString(R.string.already_regist)
            105 -> hint + context.getString(R.string.opera_too_much)
            3 -> context.getString(R.string.pic_code_error)
            2 -> context.getString(R.string.wrong_phone_or_email)
            5 -> context.getString(R.string.account_not_exit)
            6 -> context.getString(R.string.contact_admin)
            1 -> context.getString(R.string.illegal_opera)
            406 -> "图形验证码错误"
            else -> context.getString(R.string.send_code_error)
        }
        return messag
    }
    //        Glide.with(mView.getContext()).load(Constants.BASE_URL + "v1/servlet/ImageCode?" + System.currentTimeMillis())
//                .listener(new RequestListener<String, GlideDrawable>() {
//                    @Override
//                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//
//                    }
//
//                    @Override
//                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                        ivCode.setVisibility(View.VISIBLE);
//                        return false;
//                    }
//                })
//                .into(ivCode);

    //        RetrofitHelper
//                .getIns()
//                .getZgtopApi()
//                .getImageCode("v1/servlet/ImageCode?" + System.currentTimeMillis())
//                .subscribeOn(Schedulers.io())
//                .map(new Function<ResponseBody, Bitmap>() {
//                    @Override
//                    public Bitmap apply(ResponseBody responseBody) throws Exception {
//                        if (responseBody == null){
//                            return null;
//                        }
//                        Bitmap bitmap = null;
//                        try {
//                            bitmap = BitmapFactory.decodeStream(responseBody.byteStream());
//                        }catch (Exception e){
//                            Log.e(e, "nidongliang");
//                        }finally {
//                            return bitmap;
//                        }
//
//
//
//                    }
//                })
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new BaseObserver<Bitmap>() {
//                    @Override
//                    public void success(Bitmap bitmap) {
//                        if (bitmap == null){
//                            mView.showToast("图形验证码获取失败");
//                        }else {
//                            ivCode.setImageBitmap(bitmap);
//                            edtCode.setText("");
//                            dialog.show();
//                        }
//                    }
//
//                    @Override
//                    public void failed(int code, String data, String msg) {
//
//                    }
//                });
    val imageCode: Unit
        get() {
            if (dialog == null) {
                dialog = Dialog(mView!!.context, R.style.Dialog)
                dialog!!.setContentView(R.layout.dialog_pic_code)
                dialog!!.setCancelable(false)
                dialog!!.setCanceledOnTouchOutside(false)
                ivCode = dialog!!.findViewById(R.id.iv_pic_code)
                val tvCancel = dialog!!.findViewById<TextView>(R.id.tv_cancel)
                val tvConfirm = dialog!!.findViewById<TextView>(R.id.tv_confirm)
                edtCode = dialog!!.findViewById(R.id.edt_code)
                tvCancel.setOnClickListener { v: View? -> dialog!!.dismiss() }
                tvConfirm.setOnClickListener { v: View? ->
                    if (TextUtils.isEmpty(edtCode!!.getText())) {
                        mView!!.showToast("请输入图片验证码")
                    } else {
                        picCode = edtCode!!.getText().toString().replace(" ".toRegex(), "")
                        if (picCode!!.length > 4) {
                            mView!!.showToast("当前输入图形验证码长度大于4位")
                            return@setOnClickListener
                        }
                        startSend()
                        dialog!!.dismiss()
                    }
                }
                ivCode!!.setOnClickListener(View.OnClickListener { v: View? -> imageCode })
                dialog!!.setOnDismissListener { dialog: DialogInterface? -> edtCode!!.setText("") }
            }
            edtCode!!.setText("")
            dialog!!.show()
            ivCode!!.visibility = View.GONE
            val options = RequestOptions()
                    .override(Target.SIZE_ORIGINAL)
            Glide.with(mView!!.context)
                    .load(Constants.BASE_URL + "v1/servlet/ImageCode?" + System.currentTimeMillis())
                    .apply(options)
                    .listener(object : RequestListener<Drawable?> {
                        override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable?>, isFirstResource: Boolean): Boolean {
                            mView!!.showToast("图形验证码获取失败")
                            return false
                        }

                        override fun onResourceReady(resource: Drawable?, model: Any, target: Target<Drawable?>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                            ivCode!!.visibility = View.VISIBLE
                            return false
                        }
                    })
                    .into(ivCode!!)
            //        Glide.with(mView.getContext()).load(Constants.BASE_URL + "v1/servlet/ImageCode?" + System.currentTimeMillis())
//                .listener(new RequestListener<String, GlideDrawable>() {
//                    @Override
//                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//
//                    }
//
//                    @Override
//                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                        ivCode.setVisibility(View.VISIBLE);
//                        return false;
//                    }
//                })
//                .into(ivCode);

//        RetrofitHelper
//                .getIns()
//                .getZgtopApi()
//                .getImageCode("v1/servlet/ImageCode?" + System.currentTimeMillis())
//                .subscribeOn(Schedulers.io())
//                .map(new Function<ResponseBody, Bitmap>() {
//                    @Override
//                    public Bitmap apply(ResponseBody responseBody) throws Exception {
//                        if (responseBody == null){
//                            return null;
//                        }
//                        Bitmap bitmap = null;
//                        try {
//                            bitmap = BitmapFactory.decodeStream(responseBody.byteStream());
//                        }catch (Exception e){
//                            Log.e(e, "nidongliang");
//                        }finally {
//                            return bitmap;
//                        }
//
//
//
//                    }
//                })
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new BaseObserver<Bitmap>() {
//                    @Override
//                    public void success(Bitmap bitmap) {
//                        if (bitmap == null){
//                            mView.showToast("图形验证码获取失败");
//                        }else {
//                            ivCode.setImageBitmap(bitmap);
//                            edtCode.setText("");
//                            dialog.show();
//                        }
//                    }
//
//                    @Override
//                    public void failed(int code, String data, String msg) {
//
//                    }
//                });
        }

    companion object {
        /**
         * 注册
         */
        const val REGIST = 1

        /**
         * 帐号相关验证码
         */
        const val ACCOUNT = 2

        /**
         * 绑定手机验证码
         */
        const val BIND_PHONE = 3

        /**
         * 忘记密码
         */
        const val FIND_PASS = 4

        /**
         * 异常登录
         */
        const val LOGIN = 5
    }
}
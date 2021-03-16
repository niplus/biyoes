package com.biyoex.app.my.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import butterknife.BindView
import butterknife.OnClick
import com.bumptech.glide.Glide
import com.esandinfo.zoloz.ZolozManager
import com.google.gson.Gson
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureMimeType
import com.lzy.imagepicker.ImagePicker
import com.biyoex.app.R
import com.biyoex.app.VBTApplication
import com.biyoex.app.common.Constants
import com.biyoex.app.common.activity.LookPictureActivity
import com.biyoex.app.common.base.BaseActivity
import com.biyoex.app.common.bean.AuthInitResult
import com.biyoex.app.common.data.SessionLiveData
import com.biyoex.app.common.http.RetrofitHelper
import com.biyoex.app.common.okhttp.OkHttpUtils
import com.biyoex.app.common.okhttp.callback.StringCallback
import com.biyoex.app.common.utils.GlideEngine
import com.biyoex.app.common.utils.MPermissionUtils
import com.biyoex.app.common.utils.MPermissionUtils.OnPermissionListener
import com.biyoex.app.common.utils.ToastUtils
import com.biyoex.app.common.utils.log.Log
import com.esandinfo.zoloz.constants.ZolozErrCode
import id.zelory.compressor.Compressor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import me.nereo.multi_image_selector.MultiImageSelectorActivity
import okhttp3.Call
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.util.*
import kotlin.jvm.Throws

/**
 * Created by LG on 2017/7/20.
 */
class NewsCertificateUploadActivity : BaseActivity() {
    @JvmField
    @BindView(R.id.tv_title)
    var tvTitle: TextView? = null

    @JvmField
    @BindView(R.id.btn_confirm)
    var btnConfirm: Button? = null

    @JvmField
    @BindView(R.id.iv_front)
    var ivFrong: ImageView? = null

    @JvmField
    @BindView(R.id.iv_back)
    var ivBack: ImageView? = null

    //    @BindView(R.id.tv_positive)
    //    TextView tvPositive;
    //    @BindView(R.id.tv_other_side)
    //    TextView tvOtherSide;
    //正面身份证图片
    private var strPositiveUrl = ""

    //反面省份证图片
    private var strReverseSideUrl = ""

    //手持正面省份证图片
    private var strHoldPositiveUrl = ""
    private var mPositiveImgPathList: ArrayList<String>? = null
    private var mReverseSideImgPathList: ArrayList<String>? = null
    private var mHoldPositiveImgpathlist: ArrayList<String>? = null

    //重新选择图片 1 = 正面  2 = 后面 3 = 手持
    private var status = 1

    private val session = SessionLiveData.getIns().value
    override fun initView() {
        tvTitle!!.text = ""
    }

    override fun initData() {
//        ivHoldPositive.setOnLongClickListener(view -> {
//            status = 3;
//            showNormalDialog("","是否重新选择");
//            return false;
//        });

        if (session!!.isAuthDeepPost && !session.isAuthDeep){
            showLoadingDialog()
            val zolozManager = ZolozManager(this@NewsCertificateUploadActivity)
            Log.i("nidongliang, no is ${session.no}, realname: ${session.realName}")
            val result = zolozManager.authInit(null, session.no, session.realName)
            getAuthInitMsg(result.data)
        }
        ivBack!!.setOnLongClickListener {
            status = 2
            showNormalDialog("", "是否重新选择")
            false
        }
        ivFrong!!.setOnLongClickListener { view: View? ->
            status = 1
            showNormalDialog("", "是否重新选择")
            false
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_news_certificate_upload1
    }

    @OnClick(R.id.iv_menu)
    override fun onBackPressed() {
        super.onBackPressed()
    }

    @OnClick(R.id.btn_confirm, R.id.iv_front, R.id.iv_back)
    fun onClick(view: View) {
        when (view.id) {
            R.id.btn_confirm -> if (strPositiveUrl == "" && strReverseSideUrl == "" && strHoldPositiveUrl == "") {
                ToastUtils.showToast(getString(R.string.please_choose_pic))
            } else {
                showLoadingDialog()
                requestUploadIdentifyPic()
            }
            R.id.iv_front -> MPermissionUtils.requestPermissionsResult(this@NewsCertificateUploadActivity, object : OnPermissionListener {
                override fun onPermissionGranted() {
                    if (strPositiveUrl == "") {
                        PictureSelector.create(this@NewsCertificateUploadActivity)
                                .openGallery(PictureMimeType.ofImage())
                                .isWeChatStyle(true)
                                .loadImageEngine(GlideEngine.createGlideEngine())
                                .maxSelectNum(1)
                                .forResult(RESULT_CODE_POSITIVE)
                    } else {
                        val itLookPicture = Intent(this@NewsCertificateUploadActivity, LookPictureActivity::class.java)
                        itLookPicture.putExtra("type", "Positive")
                        itLookPicture.putExtra("picture_url", strPositiveUrl)
                        startActivity(itLookPicture)
                    }
                }

                override fun onPermissionDenied() {
                    MPermissionUtils.showTipsDialog(this@NewsCertificateUploadActivity)
                }
            }, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            R.id.iv_back -> MPermissionUtils.requestPermissionsResult(this@NewsCertificateUploadActivity, object : OnPermissionListener {
                override fun onPermissionGranted() {
                    if (strReverseSideUrl == "") {
                        PictureSelector.create(this@NewsCertificateUploadActivity)
                                .openGallery(PictureMimeType.ofImage())
                                .isWeChatStyle(true)
                                .loadImageEngine(GlideEngine.createGlideEngine())
                                .maxSelectNum(1)
                                .forResult(RESULT_CODE_REVERSESIDE)
                        //                            startActivityForResult(intent, RESULT_CODE_REVERSESIDE);
                    } else {
                        val itLookPicture = Intent(this@NewsCertificateUploadActivity, LookPictureActivity::class.java)
                        itLookPicture.putExtra("type", "ReverseSide")
                        itLookPicture.putExtra("picture_url", strReverseSideUrl)
                        startActivity(itLookPicture)
                    }
                }

                override fun onPermissionDenied() {
                    MPermissionUtils.showTipsDialog(this@NewsCertificateUploadActivity)
                }
            }, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            R.id.iv_hold_positive -> MPermissionUtils.requestPermissionsResult(this@NewsCertificateUploadActivity, object : OnPermissionListener {
                override fun onPermissionGranted() {
                    if (strHoldPositiveUrl == "") {
                        PictureSelector.create(this@NewsCertificateUploadActivity)
                                .openGallery(PictureMimeType.ofImage())
                                .isWeChatStyle(true)
                                .loadImageEngine(GlideEngine.createGlideEngine())
                                .maxSelectNum(1)
                                .forResult(RESULT_CODE_HOLD_POSITIVE)
                        //                            startActivityForResult(intent, RESULT_CODE_HOLD_POSITIVE);
                    } else {
                        val itLookPicture = Intent(this@NewsCertificateUploadActivity, LookPictureActivity::class.java)
                        itLookPicture.putExtra("type", "HoldPositive")
                        itLookPicture.putExtra("picture_url", strHoldPositiveUrl)
                        startActivity(itLookPicture)
                    }
                }

                override fun onPermissionDenied() {
                    MPermissionUtils.showTipsDialog(this@NewsCertificateUploadActivity)
                }
            }, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RESULT_CODE_POSITIVE -> if (requestCode == RESULT_CODE_POSITIVE) {
                val localMedia = PictureSelector.obtainMultipleResult(data)
                mPositiveImgPathList = ArrayList()
                for (mSelect in localMedia) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        mPositiveImgPathList!!.add(mSelect.androidQToPath)
                    } else {
                        mPositiveImgPathList!!.add(mSelect.path)
                    }
                }
                showLoadingDialog()
                if (mPositiveImgPathList != null && mPositiveImgPathList!!.size > 0) {
                    Compressor(this)
                            .compressToFileAsFlowable(File(mPositiveImgPathList!![0]))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ file: File -> requestUploadAuthImg(file, "positive") }) { throwable: Throwable ->
                                hideLoadingDialog()
                                throwable.printStackTrace()
                                ToastUtils.showToast(throwable.message)
                            }
                } else {
                    hideLoadingDialog()
                }
            } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
                if (data != null) {
                    val items = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT)
                    if (items != null) {
                        if (items.size == 0) {
                            strPositiveUrl = ""
                            //ivPositive.setImageResource(R.mipmap.sczm);
                        } else {
                            mPositiveImgPathList = items
                        }
                    }
                }
            }
            RESULT_CODE_REVERSESIDE -> if (requestCode == RESULT_CODE_REVERSESIDE) {
                val localMedia = PictureSelector.obtainMultipleResult(data)
                mReverseSideImgPathList = ArrayList()
                for (mSelect in localMedia) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        mReverseSideImgPathList!!.add(mSelect.androidQToPath)
                    } else {
                        mReverseSideImgPathList!!.add(mSelect.path)
                    }
                }
                showLoadingDialog()
                if (mReverseSideImgPathList != null && mReverseSideImgPathList!!.size > 0) {
                    Compressor(this)
                            .compressToFileAsFlowable(File(mReverseSideImgPathList!![0]))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ file -> requestUploadAuthImg(file, "reverse_side") }) { throwable ->
                                hideLoadingDialog()
                                throwable.printStackTrace()
                                ToastUtils.showToast(throwable.message)
                            }
                } else {
                    hideLoadingDialog()
                }
            } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
                if (data != null) {
                    val items = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT)
                    if (items != null) {
                        if (items.size == 0) {
                            strReverseSideUrl = ""
                            //                                ivOtherSide.setImageResource(R.mipmap.scbm);
                        } else {
                            mReverseSideImgPathList = items
                        }
                    }
                }
            }
            RESULT_CODE_HOLD_POSITIVE -> if (requestCode == RESULT_CODE_HOLD_POSITIVE) {
                val localMedia = PictureSelector.obtainMultipleResult(data)
                mHoldPositiveImgpathlist = ArrayList()
                for (mSelect in localMedia) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        mHoldPositiveImgpathlist!!.add(mSelect.androidQToPath)
                    } else {
                        mHoldPositiveImgpathlist!!.add(mSelect.path)
                    }
                }
                showLoadingDialog()
                if (mHoldPositiveImgpathlist != null && mHoldPositiveImgpathlist!!.size > 0) {
                    Compressor(this)
                            .compressToFileAsFlowable(File(mHoldPositiveImgpathlist!![0]))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ file: File -> requestUploadAuthImg(file, "hold_positive") }) { throwable: Throwable ->
                                hideLoadingDialog()
                                throwable.printStackTrace()
                                ToastUtils.showToast(throwable.message)
                            }
                } else {
                    hideLoadingDialog()
                }
            } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
                if (data != null) {
                    val items = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT)
                    if (items != null) {
                        if (items.size == 0) {
                            strHoldPositiveUrl = ""
                            //ivOtherSide.setImageResource(R.mipmap.scbm);
                        } else {
                            mHoldPositiveImgpathlist = items
                        }
                    }
                }
            }
        }
    }

    /**
     * 上传图片
     *
     * @param file
     */
    fun requestUploadAuthImg(file: File, state: String) {
        OkHttpUtils
                .post()
                .addHeader("X-Requested-With", "XMLHttpReques")
                .url(Constants.BASE_URL + "v1/account/upload")
                .addParams("type", "1")
                .addFile("img", file.name, file)
                .build()
                .execute(object : StringCallback() {
                    override fun onError(call: Call, e: Exception, id: Int) {
                        Log.e(e, "requestUploadAuthImg")
                        hideLoadingDialog()
                    }

                    @Throws(JSONException::class)
                    override fun onResponse(response: String, id: Int) {
                        val dataJson = JSONObject(response)
                        val code = dataJson.getInt("code")
                        Log.i("requestUploadAuthImg result :$code")
                        //正面
                        if (state == "positive") {
                            if (code == 200) {
                                strPositiveUrl = dataJson.getString("data")
                                //Log.e("sssss", strPositiveUrl);
                                Glide
                                        .with(VBTApplication.getContext())
                                        .load(VBTApplication.appPictureUrl + strPositiveUrl)
                                        .into(ivFrong!!)
                                hideLoadingDialog()

                                //GlideUtils.getInstance().displayFilletImage(NewsCertificateUploadActivity.this, Constants.BASE_URL + strPositiveUrl, ivPositive);
                            }
                            //反面
                        } else if (state == "reverse_side") {
                            if (code == 200) {
                                strReverseSideUrl = dataJson.getString("data")
                                Glide
                                        .with(VBTApplication.getContext())
                                        .load(VBTApplication.appPictureUrl + strReverseSideUrl)
                                        .into(ivBack!!)
                                hideLoadingDialog()
                                //GlideUtils.getInstance().displayFilletImage(NewsCertificateUploadActivity.this, Constants.BASE_URL + strReverseSideUrl, ivOtherSide);
                            }
                            //手持正面
                        } else {
//                            if (code == 200) {
//                                strHoldPositiveUrl = dataJson.getString("data");
//                                Glide
//                                        .with(VBTApplication.getContext())
//                                        .load(VBTApplication.appPictureUrl + strHoldPositiveUrl)
//                                        .into(ivHoldPositive);
//                                hideLoadingDialog();
//                            }
                        }
                        if (dataJson.has("max")) {
                            hideLoadingDialog()
                            ToastUtils.showToast(getString(R.string.upload_files_exceeded) + dataJson.getString("max"))
                            Log.i("file is too large : " + dataJson.getString("max"))
                        }
                    }
                })
    }

    /**
     * 确认上传图片
     */
    fun requestUploadIdentifyPic() {
        OkHttpUtils
                .post()
                .addHeader("X-Requested-With", "XMLHttpRequest")
                .url(Constants.BASE_URL + "v1/account/uploadAuth")
                .addParams("fIdentityPath1", strPositiveUrl)
                .addParams("fIdentityPath2", strReverseSideUrl) //                .addParams("fIdentityPath3", strHoldPositiveUrl)
                .build()
                .execute(object : StringCallback() {
                    override fun onError(call: Call, e: Exception, id: Int) {
                        Log.e(e, "requestUploadIdentifyPic")
                        hideLoadingDialog()
                    }

                    @Throws(JSONException::class)
                    override fun onResponse(response: String, id: Int) {
                        val code = JSONObject(response).getString("code")
                        Log.i("requestUploadIdentifyPic result :$code")
                        if (code == "200") {
                            ToastUtils.showToast(getString(R.string.upload_success))
                            //                                    setResult(RESULT_OK);
                            finish()
                            SessionLiveData.getIns().getSeesionInfo()
                            val zolozManager = ZolozManager(this@NewsCertificateUploadActivity)
                            val result = zolozManager.authInit(null, session!!.no, session.realName)
                            getAuthInitMsg(result.data)
                        } else {
                            ToastUtils.showToast(getString(R.string.upload_failed))
                        }
                        hideLoadingDialog()
                    }
                })
    }

    override fun setDialogOnClick() {
        when (status) {
            1 -> MPermissionUtils.requestPermissionsResult(this@NewsCertificateUploadActivity, object : OnPermissionListener {
                override fun onPermissionGranted() {
//
//                            MultiImageSelector.create(NewsCertificateUploadActivity.this)
//                                    .showCamera(true)
//                                    .single()
//                                    .start(NewsCertificateUploadActivity.this, RESULT_CODE_POSITIVE);
                    PictureSelector.create(this@NewsCertificateUploadActivity)
                            .openGallery(PictureMimeType.ofImage())
                            .isWeChatStyle(true)
                            .loadImageEngine(GlideEngine.createGlideEngine())
                            .maxSelectNum(1)
                            .forResult(RESULT_CODE_POSITIVE)
                    //                            startActivityForResult(intent, RESULT_CODE_POSITIVE);
                }

                override fun onPermissionDenied() {
                    MPermissionUtils.showTipsDialog(this@NewsCertificateUploadActivity)
                }
            }, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            2 -> MPermissionUtils.requestPermissionsResult(this@NewsCertificateUploadActivity, object : OnPermissionListener {
                override fun onPermissionGranted() {
//                            MultiImageSelector.create(NewsCertificateUploadActivity.this)
//                                    .showCamera(true)
//                                    .single()
//                                    .start(NewsCertificateUploadActivity.this, RESULT_CODE_REVERSESIDE);
//                            startActivityForResult(intent, RESULT_CODE_REVERSESIDE);
                    PictureSelector.create(this@NewsCertificateUploadActivity)
                            .openGallery(PictureMimeType.ofImage())
                            .isWeChatStyle(true)
                            .loadImageEngine(GlideEngine.createGlideEngine())
                            .maxSelectNum(1)
                            .forResult(RESULT_CODE_REVERSESIDE)
                }

                override fun onPermissionDenied() {
                    MPermissionUtils.showTipsDialog(this@NewsCertificateUploadActivity)
                }
            }, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            3 -> MPermissionUtils.requestPermissionsResult(this@NewsCertificateUploadActivity, object : OnPermissionListener {
                override fun onPermissionGranted() {
                    PictureSelector.create(this@NewsCertificateUploadActivity)
                            .openGallery(PictureMimeType.ofImage())
                            .isWeChatStyle(true)
                            .loadImageEngine(GlideEngine.createGlideEngine())
                            .maxSelectNum(1)
                            .forResult(RESULT_CODE_HOLD_POSITIVE)
                    //                            MultiImageSelector.create(NewsCertificateUploadActivity.this)
//                                    .showCamera(true)
//                                    .single()
//                                    .start(NewsCertificateUploadActivity.this, RESULT_CODE_HOLD_POSITIVE);
//                            startActivityForResult(intent, RESULT_CODE_HOLD_POSITIVE);
                }

                override fun onPermissionDenied() {
                    MPermissionUtils.showTipsDialog(this@NewsCertificateUploadActivity)
                }
            }, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    private fun getAuthInitMsg(result: String){
        val authInitResult = Gson().fromJson(result, AuthInitResult::class.java)

        lifecycleScope.launch {
            try {
                val result = RetrofitHelper.getIns().zgtopApi.getAuthInitInfo(
                        authInitResult.appName,
                        authInitResult.appSign,
                        authInitResult.bizId,
                        authInitResult.packageName,
                        authInitResult.identityParam,
                        authInitResult.metaInfo,
                        authInitResult.platform)
                hideLoadingDialog()
                val zolozManager = ZolozManager(this@NewsCertificateUploadActivity)
                Log.i("nidongliang auth init : ${result.data}, code: ${result.code}")
                zolozManager.auth(Gson().toJson(result.data)) { p0 ->
                    Log.i("nidongliang p0 : ${p0.message}, isSuccess: ${p0.isSuccess}, code: ${p0.code}")
                    if (p0 == null){
                        startActivityForResult(Intent(this@NewsCertificateUploadActivity, FaceRecognitionResultActivity::class.java).putExtra("isSuccess", false), 0x1)
                    } else if(p0.code == ZolozErrCode.ZOLOZ_CANCEL){
                        finish()
                    }else if (p0.isSuccess) {
                        val reasultBean = JSONObject(p0.message)
                        lifecycleScope.launch {
                            val upLoadResult = RetrofitHelper.getIns().zgtopApi.uploadResult(authInitResult.bizId, reasultBean.getString("certifyId"))
                            if (upLoadResult.code == 200){
                                startActivityForResult(Intent(this@NewsCertificateUploadActivity, FaceRecognitionResultActivity::class.java).putExtra("isSuccess", true), 0x1)
                                finish()
                            }else{
                                startActivityForResult(Intent(this@NewsCertificateUploadActivity, FaceRecognitionResultActivity::class.java).putExtra("isSuccess", false), 0x1)
                            }
                        }

                    }
                }


//                finish()

            }catch (e: java.lang.Exception){
                Log.i("nidongliang http error: ${e.message}")
            }

        }



    }



    companion object {
        //正面回调
        const val RESULT_CODE_POSITIVE = 0x1455

        //反面回调
        const val RESULT_CODE_REVERSESIDE = 0x1511

        //手持正面回调
        const val RESULT_CODE_HOLD_POSITIVE = 0x1512
    }
}
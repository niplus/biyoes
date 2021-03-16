package com.biyoex.app.my.activity

import android.Manifest
import android.content.Intent
import android.view.View
import com.bumptech.glide.Glide
import com.lzy.imagepicker.ImagePicker
import com.biyoex.app.MainActivity
import com.biyoex.app.R
import com.biyoex.app.VBTApplication
import com.biyoex.app.common.Constants
import com.biyoex.app.common.activity.LookPictureActivity
import com.biyoex.app.common.base.BaseActivity
import com.biyoex.app.common.bean.RequestResult
import com.biyoex.app.common.http.RetrofitHelper
import com.biyoex.app.common.mvpbase.BaseObserver
import com.biyoex.app.common.okhttp.OkHttpUtils
import com.biyoex.app.common.okhttp.callback.StringCallback
import com.biyoex.app.common.utils.MPermissionUtils
import com.biyoex.app.common.utils.ToastUtils
import com.biyoex.app.common.utils.log.Log
import id.zelory.compressor.Compressor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_authen_tication.*
import kotlinx.android.synthetic.main.layout_newtitle_bar.*
import kotlinx.android.synthetic.main.layout_title_bar.*
import kotlinx.android.synthetic.main.layout_title_bar.iv_menu
import kotlinx.android.synthetic.main.layout_title_bar.tv_title
import me.nereo.multi_image_selector.MultiImageSelector
import me.nereo.multi_image_selector.MultiImageSelectorActivity
import okhttp3.Call
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.util.ArrayList

class AuthenTicationActivity : BaseActivity() {


    //正面身份证图片
    private var strPositiveUrl = ""
    //反面省份证图片
    private var strReverseSideUrl = ""
    //手持正面省份证图片
    private var strHoldPositiveUrl = ""

    //正面回调
    val RESULT_CODE_POSITIVE = 0x1455
    //反面回调
    val RESULT_CODE_REVERSESIDE = 0x1511
    //手持正面回调
    val RESULT_CODE_HOLD_POSITIVE = 0x1512

    private var mPositiveImgPathList: ArrayList<String>? = null
    private var mReverseSideImgPathList: ArrayList<String>? = null
    private var mHoldPositiveImgpathlist: ArrayList<String>? = null

    //重新选择图片 1 = 正面  2 = 后面 3 = 手持
    private var status = 1

    override fun getLayoutId(): Int = R.layout.activity_authen_tication

    override fun initData() {
        tv_title .text = "Authentication"
        tv_right.visibility = View.GONE
    }

    override fun initView() {
        iv_menu.setOnClickListener { finish() }
        //正面身份证
        iv_positive.setOnClickListener {
            postPositive()
        }
        iv_positive.setOnLongClickListener {
            status = 1
            showNormalDialog("", "是否重新选择")
            return@setOnLongClickListener false
        }
        iv_other_side.setOnLongClickListener {
            status = 2
            showNormalDialog("", "是否重新选择")
            return@setOnLongClickListener false
        }
        iv_hold_positive.setOnLongClickListener {
            status = 3
            showNormalDialog("", "是否重新选择")
            return@setOnLongClickListener false
        }

        //身份证背面click
        iv_other_side.setOnClickListener {
            postOtherSide()
        }

        //手持身份证照片

        iv_hold_positive.setOnClickListener {
            postHoldPositve()
        }

        //提交验证
        btn_confirm.setOnClickListener {
            if (edit_card_id.text.isNullOrEmpty()) { //验证是否输入身份证号
                showToast(getString(R.string.input_id_card))
            } else if (edit_name.text.isNullOrEmpty()) {//姓名是否为null
                showToast(getString(R.string.input_id_card_name))
            } else if (strPositiveUrl.isNullOrEmpty()) {
                showToast("请上传身份证正面照")
            } else if (strHoldPositiveUrl.isNullOrEmpty()) {
                showToast("请上传身份证手持照")
            } else if (strReverseSideUrl.isNullOrEmpty()) {
                showToast("请上传身份证背面照")
            } else {
                showLoadingDialog()
                //请求4要素
                postAuth()
            }
        }

    }

    private fun postAuth() {
        showLoadingDialog()
        RetrofitHelper.getIns()
                .zgtopApi
                .postAuth(edit_name.text.toString(), edit_card_id.text.toString(),"", 1, strPositiveUrl, strReverseSideUrl, strHoldPositiveUrl)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : BaseObserver<RequestResult<Any>>(this) {
                    override fun success(t: RequestResult<Any>) {
                        showToast("认证成功")
                        startActivity(Intent(this@AuthenTicationActivity,MainActivity::class.java))
                    }

                    override fun failed(code: Int, data: String?, msg: String?) {
                        super.failed(code, data, msg)
                        hideLoadingDialog()
                        msg?.let {
                            showToast(msg)
                        }
                    }
                })
    }
    //上传手持照片
    private fun postHoldPositve() {
        MPermissionUtils.requestPermissionsResult(this@AuthenTicationActivity, object : MPermissionUtils.OnPermissionListener {
            override fun onPermissionGranted() {
                if (strHoldPositiveUrl == "") {
                    MultiImageSelector.create(this@AuthenTicationActivity)
                            .showCamera(true)
                            .single()
                            .start(this@AuthenTicationActivity, RESULT_CODE_HOLD_POSITIVE)
                    //                            startActivityForResult(intent, RESULT_CODE_HOLD_POSITIVE);
                } else {
                    val itLookPicture = Intent(this@AuthenTicationActivity, LookPictureActivity::class.java)
                    itLookPicture.putExtra("type", "HoldPositive")
                    itLookPicture.putExtra("picture_url", strHoldPositiveUrl)
                    startActivity(itLookPicture)
                }
            }

            override fun onPermissionDenied() {
                MPermissionUtils.showTipsDialog(this@AuthenTicationActivity)
            }
        }, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
    }
    //上传身份证背面照片
    private fun postOtherSide() {
        MPermissionUtils.requestPermissionsResult(this@AuthenTicationActivity, object : MPermissionUtils.OnPermissionListener {
            override fun onPermissionGranted() {
                if (strReverseSideUrl == "") {
                    MultiImageSelector.create(this@AuthenTicationActivity)
                            .showCamera(true)
                            .single()
                            .start(this@AuthenTicationActivity, RESULT_CODE_REVERSESIDE)
                    //                            startActivityForResult(intent, RESULT_CODE_REVERSESIDE);
                } else {
                    val itLookPicture = Intent(this@AuthenTicationActivity, LookPictureActivity::class.java)
                    itLookPicture.putExtra("type", "ReverseSide")
                    itLookPicture.putExtra("picture_url", strReverseSideUrl)
                    startActivity(itLookPicture)
                }
            }

            override fun onPermissionDenied() {
                MPermissionUtils.showTipsDialog(this@AuthenTicationActivity)
            }
        }, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
    }
    //上传身份证正面照片
    private fun postPositive() {
        MPermissionUtils.requestPermissionsResult(this@AuthenTicationActivity, object : MPermissionUtils.OnPermissionListener {
            override fun onPermissionGranted() {
                //GalleryFinal.openGallerySingle(1001, mOnReverseSideResultCallback);
                if (strPositiveUrl == "") {
                    MultiImageSelector.create(this@AuthenTicationActivity)
                            .showCamera(true)
                            .single()
                            .start(this@AuthenTicationActivity, RESULT_CODE_POSITIVE)
                    //                            startActivityForResult(intent, RESULT_CODE_POSITIVE);
                } else {
                    val itLookPicture = Intent(this@AuthenTicationActivity, LookPictureActivity::class.java)
                    itLookPicture.putExtra("type", "Positive")
                    itLookPicture.putExtra("picture_url", strPositiveUrl)
                    startActivity(itLookPicture)
                }
            }

            override fun onPermissionDenied() {
                MPermissionUtils.showTipsDialog(this@AuthenTicationActivity)
            }
        }, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    //拍照回传
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            //正面
            RESULT_CODE_POSITIVE -> if (requestCode == RESULT_CODE_POSITIVE) {
                mPositiveImgPathList = data!!.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT)
                showLoadingDialog()
                mPositiveImgPathList?.let {
                    Compressor(this)
                            .compressToFileAsFlowable(File(it.get(0)))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ file -> requestUploadAuthImg(file, "positive") }, { throwable ->
                                hideLoadingDialog()
                                throwable.printStackTrace()
                                ToastUtils.showToast(throwable.message)
                            })

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
            //反面
            RESULT_CODE_REVERSESIDE -> if (requestCode == RESULT_CODE_REVERSESIDE) {
                mReverseSideImgPathList = data!!.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT)
                showLoadingDialog()
                mReverseSideImgPathList?.let {
                    Compressor(this)
                            .compressToFileAsFlowable(File(it.get(0)))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ file -> requestUploadAuthImg(file, "reverse_side") }, { throwable ->
                                hideLoadingDialog()
                                throwable.printStackTrace()
                                ToastUtils.showToast(throwable.message)
                            })
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
                mHoldPositiveImgpathlist = data!!.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT)
                showLoadingDialog()
                mHoldPositiveImgpathlist?.let{
                    Compressor(this)
                            .compressToFileAsFlowable(File(it.get(0)))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ file -> requestUploadAuthImg(file, "hold_positive") }, { throwable ->
                                hideLoadingDialog()
                                throwable.printStackTrace()
                                ToastUtils.showToast(throwable.message)
                            })
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
                                        .into(iv_positive)
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
                                        .into(iv_other_side)
                                hideLoadingDialog()
                                //GlideUtils.getInstance().displayFilletImage(NewsCertificateUploadActivity.this, Constants.BASE_URL + strReverseSideUrl, ivOtherSide);
                            }
                            //手持正面
                        } else {
                            if (code == 200) {
                                strHoldPositiveUrl = dataJson.getString("data")
                                Glide
                                        .with(VBTApplication.getContext())
                                        .load(VBTApplication.appPictureUrl + strHoldPositiveUrl)
                                        .into(iv_hold_positive)
                                hideLoadingDialog()
                                //GlideUtils.getInstance().displayFilletImage(NewsCertificateUploadActivity.this, Constants.BASE_URL + strReverseSideUrl, ivOtherSide);
                            }
                        }
                        if (dataJson.has("max")) {
                            hideLoadingDialog()
                            ToastUtils.showToast(getString(R.string.upload_files_exceeded) + dataJson.getString("max"))
                            Log.i("file is too large : " + dataJson.getString("max"))
                        }
                    }
                })


    }
    //重新选择照片dialog回调
    override fun setDialogOnClick() {
        when (status) {
            //正面
            1 ->{
                strPositiveUrl = ""
                postPositive()
            }
            //反面
            2 ->{
                strReverseSideUrl = ""
                postOtherSide()
            }
            //手持正面
            3 -> {
                strHoldPositiveUrl = ""
                postHoldPositve()
            }
        }
    }



}


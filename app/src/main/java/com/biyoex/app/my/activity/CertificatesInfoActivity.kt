package com.biyoex.app.my.activity

import butterknife.BindView
import com.biyoex.app.R
import com.biyoex.app.common.utils.LanguageUtils
import butterknife.OnClick
import android.text.TextUtils
import android.view.View
import android.widget.*
import com.biyoex.app.common.Constants
import com.biyoex.app.common.base.BaseActivity
import com.biyoex.app.common.okhttp.OkHttpUtils
import com.biyoex.app.common.okhttp.callback.StringCallback
import kotlin.Throws
import com.biyoex.app.common.data.SessionLiveData
import com.biyoex.app.common.utils.RegexUtils
import com.biyoex.app.common.utils.log.Log
import okhttp3.Call
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception
import java.util.ArrayList
import java.util.HashMap

/**
 * Created by LG on 2017/6/9.
 */
class CertificatesInfoActivity : BaseActivity() {
    @JvmField
    @BindView(R.id.tv_title)
    var tvTitle: TextView? = null

    @JvmField
    @BindView(R.id.iv_menu)
    var ivMenu: ImageView? = null

    @JvmField
    @BindView(R.id.rl_menu)
    var rlMenu: RelativeLayout? = null

    @JvmField
    @BindView(R.id.tv_title_certificates_type)
    var tvTitleCertificatesType: TextView? = null

    @JvmField
    @BindView(R.id.sp_certificates_type)
    var spCertificatesType: Spinner? = null

    @JvmField
    @BindView(R.id.rl_layout_certificates_type)
    var rlLayoutCertificatesType: RelativeLayout? = null

    @JvmField
    @BindView(R.id.tv_title_name)
    var tvTitleName: TextView? = null

    @JvmField
    @BindView(R.id.ed_name)
    var edName: EditText? = null

    @JvmField
    @BindView(R.id.rl_layout_passport)
    var rlLayoutPassport: RelativeLayout? = null

    @JvmField
    @BindView(R.id.tv_title_credential_no)
    var tvTitleCredentialNo: TextView? = null

    @JvmField
    @BindView(R.id.ed_credential_no)
    var edCredentialNo: EditText? = null

    @JvmField
    @BindView(R.id.rl_layout_credential_no)
    var rlLayoutCredentialNo: RelativeLayout? = null

    @JvmField
    @BindView(R.id.btn_confirm)
    var btnConfirm: Button? = null

    @JvmField
    @BindView(R.id.iv_arrow)
    var ivArrow: ImageView? = null
    private var mCertificatesTypeList: MutableList<Any?>? = null
    override fun initView() {
        tvTitle!!.setText(R.string.certification)
    }

    override fun initData() {
        mCertificatesTypeList = ArrayList()
        if (LanguageUtils.currentLanguage == 1) {
            mCertificatesTypeList?.add(getString(R.string.id_card))
        }
        mCertificatesTypeList?.add(getString(R.string.passport))
        val certificatesTypeAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(this, R.layout.item_simple_list, mCertificatesTypeList!!)
        spCertificatesType!!.adapter = certificatesTypeAdapter
        spCertificatesType!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {}
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_certificates_info
    }

    @OnClick(R.id.iv_menu, R.id.btn_confirm)
    fun onClick(view: View) {
        when (view.id) {
            R.id.iv_menu -> finish()
            R.id.btn_confirm -> if (TextUtils.isEmpty(edName!!.text)) {
//                  ToastUtils.showToast(getString(R.string.input_cardholder_name));
                showToast(getString(R.string.input_cardholder_name))
            } else if (TextUtils.isEmpty(edCredentialNo!!.text.toString())) {
//                    ToastUtils.showToast(getString(R.string.input_id_card_number));
                showToast(getString(R.string.input_id_card_number))
            } else if (spCertificatesType!!.selectedItem.toString() == getString(R.string.id_card) && !RegexUtils.isIDCard18(edCredentialNo!!.text.toString())) {
//                    ToastUtils.showToast(getString(R.string.wrong_id_card_number));
                showToast(getString(R.string.wrong_id_card_number))
            } else {
                showLoadingDialog()
                requestAccountAuth()
            }
        }
    }

    fun requestAccountAuth() {
        val mapParams: MutableMap<String, String> = HashMap()
        mapParams["no"] = edCredentialNo!!.text.toString()
        val type = spCertificatesType!!.selectedItem.toString()
        if (type == getString(R.string.id_card)) {
            mapParams["type"] = "0"
        } else {
            mapParams["type"] = "1"
        }
        mapParams["name"] = edName!!.text.toString()
        showLoadingDialog()



        OkHttpUtils
                .post()
                .addHeader("X-Requested-With", "XMLHttpReques")
                .url(Constants.BASE_URL + "v1/account/auth.html")
                .params(mapParams)
                .build()
                .execute(object : StringCallback() {
                    override fun onError(call: Call, e: Exception, id: Int) {
                        Log.e(e, "requestAccountAuth")
                        //ToastUtils.showToast(e.getMessage());
                        hideLoadingDialog()
                    }

                    @Throws(JSONException::class)
                    override fun onResponse(response: String, id: Int) {
                        val dataJson = JSONObject(response)
                        var code: String? = null
                        if (dataJson.has("code")) {
                            code = dataJson.getString("code")
                            Log.i("requestAccountAuth result :$code")
                            if (code == "200") {
                                SessionLiveData.getIns().getSeesionInfo()
                                showToast(getString(R.string.save_success))
                                finish()
                            } else if (code == "4") {
//                                ToastUtils.showToast(getString(R.string.id_already_used));
                                showToast(getString(R.string.id_already_used))
                            } else {
//                                ToastUtils.showToast(getString(R.string.save_failed));
                                showToast(getString(R.string.save_failed))
                            }
                        }
                        hideLoadingDialog()
                    }
                })
    }


}


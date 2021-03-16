package com.biyoex.app.my.activity

import android.app.Activity
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.biyoex.app.R
import com.biyoex.app.common.Constants
import com.biyoex.app.common.base.BaseActivity
import com.biyoex.app.common.okhttp.OkHttpUtils
import com.biyoex.app.common.okhttp.callback.StringCallback
import com.biyoex.app.common.utils.ToastUtils
import com.biyoex.app.my.bean.ZGRechargeCoinBean
import kotlinx.android.synthetic.main.activity_setting_address.*
import kotlinx.android.synthetic.main.layout_newtitle_bar.*
import okhttp3.Call
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

/**
 * 设置提现地址
 * */
class SettingAddressActivity : BaseActivity() {
    private var addressBean: ZGRechargeCoinBean.AddressBean? = null
    private var strSymbol: String? = null
    private var mapParameter: MutableMap<String, String>? = null
    private var strUrl: String? = null
    private var strId: String? = null
    private var isUSDT:Boolean = false
    override fun getLayoutId(): Int = R.layout.activity_setting_address
    override fun initView() {
        iv_right.setOnClickListener {
            deleteWithdrawBtcAddr("${addressBean!!.id}")
        }
        iv_menu.setOnClickListener {
            finish()
        }

    }

    /**
     * 删除地址
     * @param id
     */
    fun deleteWithdrawBtcAddr(id: String) {
        OkHttpUtils
                .post()
                .addHeader("X-Requested-With", "XMLHttpRequest")
                .url(Constants.BASE_URL + "v1/account/deleteAddress")
                .addParams("id", id)
                .build()
                .execute(object : StringCallback() {
                    override fun onError(call: Call, e: Exception, id: Int) {
                        e.printStackTrace()
                        hideLoadingDialog()
                    }
                    @Throws(JSONException::class)
                    override fun onResponse(response: String, id: Int) {
                        val code = JSONObject(response).getString("code")
                        if (code == "200") {
                            finish()
                        }
                        hideLoadingDialog()
                    }
                })
    }

    override fun initData() {
        addressBean = intent.getSerializableExtra("address") as ZGRechargeCoinBean.AddressBean?
        iv_right.visibility = if (addressBean == null) View.GONE else View.VISIBLE
        isUSDT = intent.getBooleanExtra("isUSDT",false)
        tv_address.hint = if(isUSDT) getString(R.string.add_address) else getString(R.string.please_input_address)
        mapParameter = HashMap()
        if (addressBean == null) {
            tv_title.setText(R.string.set_withdraw_address)
            strUrl = "v1/account/addAddress"
            strSymbol = intent.getStringExtra("strSymbol")
        } else {
            tv_title.setText(R.string.edit_withdraw_address)
            strUrl = "v1/account/updateFlag"
            strId = addressBean!!.id.toString() + ""
            tv_name.setText(addressBean!!.flag)
            tv_address.setText(addressBean!!.address)
        }
        btn_sure.setOnClickListener {
            if (TextUtils.isEmpty(tv_name.text.toString())) {
                ToastUtils.showToast(getString(R.string.please_input_label))
            } else if (TextUtils.isEmpty(tv_address.text)) {
                ToastUtils.showToast(getString(R.string.please_input_address))
            } else {
                if (addressBean == null) {
                    mapParameter!!["id"] = "$strSymbol"
                } else {
                    mapParameter!!["id"] = addressBean!!.id.toString() + ""
                }
                //                    mapParameter.put("code",edCode.getText().toString());
                mapParameter!!["address"] = tv_address.text.toString()
                mapParameter!!["flag"] = tv_name.text.toString()
                mapParameter!!["remark"] = edit_address_payment.text.toString()
                showLoadingDialog()
                requestModifyWithdrawBtcAddr()

            }
        }
    }
//
    /**
     * 设置提现地址
     * @param
     * @param
     * @param
     */
    fun requestModifyWithdrawBtcAddr() {
        OkHttpUtils
                .get()
                .addHeader("X-Requested-With", "XMLHttpRequest")
                .url(Constants.BASE_URL + strUrl)
                .params(mapParameter)
                .build()
                .execute(object : StringCallback() {
                    override fun onError(call: Call, e: Exception, id: Int) {
                        e.printStackTrace()
                        //ToastUtils.showToast(e.toString());
                        hideLoadingDialog()
                    }
                    @Throws(JSONException::class)
                    override fun onResponse(response: String, id: Int) {
                        hideLoadingDialog()
                        val dataJson = JSONObject(response)
                        if (dataJson.has("code")) {
                            val code = dataJson.getString("code")
                            Log.e("response", response)
                            if (code == "200") {
                                if (strSymbol != null) {
                                    ToastUtils.showToast(getString(R.string.set_withdraw_address_success))
                                } else {
                                    ToastUtils.showToast(getString(R.string.edit_withdraw_address_success))
                                }
                                setResult(Activity.RESULT_OK)
                                finish()
                            } else if (code == "-1") {
                                showToast(getString(R.string.error_address_info))
                            } else if (code == "-2") {
                                showToast(getString(R.string.error_payment))
                            } else {
                                val data = dataJson.getString("data")
                                ToastUtils.showToast(modifyWithdrawBtcAddr(code, data))
                            }

                        } else {
                            ToastUtils.showToast(getString(R.string.net_error))
                            hideLoadingDialog()
                        }
                    }
                })
    }

    /**
     * 设置地址提示语
     * @param resultCode
     * @return
     */

    fun modifyWithdrawBtcAddr(resultCode: String, data: String): String {
        var mssage = ""
        when (resultCode) {
            "100" -> mssage = getString(R.string.never_send_code)
            "101" -> mssage = getString(R.string.code_error_too_much)
            "102" -> mssage = String.format(getString(R.string.code_error_remain_times), data)
            "104" -> mssage = getString(R.string.can_not_bind_platform_address)
            "106" -> mssage = getString(R.string.max_five_address)
            "128" -> mssage = getString(R.string.address_length_too_long)
        }
        if (mssage == "") {
            mssage = getString(R.string.bind_address_failed)
        }
        return mssage
    }

}

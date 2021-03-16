package com.biyoex.app.my.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.biyoex.app.R;
import com.biyoex.app.common.Constants;
import com.biyoex.app.common.mvpbase.BaseActivity;
import com.biyoex.app.common.mvpbase.BasePresent;
import com.biyoex.app.common.okhttp.OkHttpUtils;
import com.biyoex.app.common.okhttp.callback.StringCallback;
import com.biyoex.app.common.present.SendCodePresent;
import com.biyoex.app.common.utils.RegexUtils;
import com.biyoex.app.common.utils.ToastUtils;
import com.biyoex.app.common.widget.ClearEditText;
import com.biyoex.app.my.bean.ZGRechargeCoinBean;
import com.biyoex.app.qrscan.activity.CaptureActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

import static com.biyoex.app.qrscan.activity.CaptureActivity.INTENT_EXTRA_KEY_QR_SCAN;
import static com.biyoex.app.qrscan.activity.CaptureActivity.RESULT_CODE_QR_SCAN;

/**
 * Created by LG on 2017/8/10.
 */

public class SetingCurrencyAddress extends BaseActivity<BasePresent> {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_label)
    TextView tvLabel;
    @BindView(R.id.ed_label)
    ClearEditText edLabel;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.ed_address)
    EditText edAddress;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    @BindView(R.id.ed_remark)
    EditText edRemark;

    private String strSymbol;

    private Map<String, String> mapParameter;

    private String strUrl;

    private String strId;

    private ZGRechargeCoinBean.AddressBean addressBean;

    @Override
    public void initData() {
        mapParameter = new HashMap<>();
        addressBean = (ZGRechargeCoinBean.AddressBean) getIntent().getSerializableExtra("address");
    }

    @Override
    protected SendCodePresent createPresent() {
        return new SendCodePresent(SendCodePresent.ACCOUNT);
    }

    @Override
    public void initComp() {
        if (addressBean == null) {
            tvTitle.setText(R.string.set_withdraw_address);
            strUrl = "v1/account/addAddress";
            strSymbol = getIntent().getStringExtra("strSymbol");
        } else {
            tvTitle.setText(R.string.edit_withdraw_address);
            strUrl = "v1/account/updateFlag";
            strId = addressBean.getId() + "";
            edLabel.setText(addressBean.getFlag());
            edAddress.setText(addressBean.getAddress());
            edRemark.setText(addressBean.getRemark());
        }
//
//        if (getIntent().getBooleanExtra("isUSDT", false)){
//            edAddress.setHint(R.string.usdt_setAddress_hint);
//        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_seting_currey_newaddress;
    }


    @OnClick({R.id.iv_menu, R.id.btn_confirm, R.id.iv_scan_qr})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_menu:
                finish();
                break;
            case R.id.btn_confirm:
                if (TextUtils.isEmpty(edLabel.getText().toString())) {
//                    ToastUtils.showToast(getString(R.string.please_input_label));
                    showToast(getString(R.string.please_input_label));
                } else if (TextUtils.isEmpty(edAddress.getText())) {
//                    ToastUtils.showToast(getString(R.string.please_input_address));
                    showToast(getString(R.string.please_input_address));
                } else if (RegexUtils.isZh(edRemark.getText().toString().trim())) {
//                    ToastUtils.showToast(getString(R.string.label_not_support_chinese));
                    showToast(getString(R.string.label_not_support_chinese));
                } else {

                    if (addressBean == null) {
                        mapParameter.put("id", strSymbol);
                    } else {
                        mapParameter.put("id", addressBean.getId() + "");
                    }
//                    mapParameter.put("code",edCode.getText().toString());
                    mapParameter.put("address", edAddress.getText().toString());
                    mapParameter.put("flag", edLabel.getText().toString());
                    if (!TextUtils.isEmpty(edRemark.getText().toString().trim())) {
                        mapParameter.put("remark", edRemark.getText().toString().trim());
                    }
                    showLoadingDialog();
                    requestModifyWithdrawBtcAddr();

                }
                break;
            case R.id.iv_scan_qr:
                Intent intent = new Intent(this, CaptureActivity.class);
                startActivityForResult(intent, 0x1);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0x1 && resultCode == RESULT_CODE_QR_SCAN) {
            String address = data.getStringExtra(INTENT_EXTRA_KEY_QR_SCAN);
            edAddress.setText(address);
        }
    }


    /**
     * 设置提现地址
     *
     * @param
     * @param
     * @param
     */
    public void requestModifyWithdrawBtcAddr() {

        OkHttpUtils
                .get()
                .addHeader("X-Requested-With", "XMLHttpRequest")
                .url(Constants.BASE_URL + strUrl)
                .params(mapParameter)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        //ToastUtils.showToast(e.toString());
                        hideLoadingDialog();
                    }

                    @Override
                    public void onResponse(String response, int id) throws JSONException {
                        hideLoadingDialog();
                        JSONObject dataJson = new JSONObject(response);
                        if (dataJson.has("code")) {
                            String code = dataJson.getString("code");
                            Log.e("response", response);
                            if (code.equals("200")) {
                                if (strSymbol != null) {
//                                 ToastUtils.showToast(getString(R.string.set_withdraw_address_success));
                                    showToast(getString(R.string.set_withdraw_address_success));
//                                 if (isDrawPage)
//                                 {
//                                     EventBus.getDefault().postSticky(new CallBackAddressMessage());
//                                 }
                                } else {
//                                    ToastUtils.showToast(getString(R.string.edit_withdraw_address_success));
                                    showToast(getString(R.string.edit_withdraw_address_success));
                                }
//                                if (!isDrawPage) {
//                                    EventBus.getDefault().postSticky(new SetingAddressMessage());
//                                }
                                setResult(RESULT_OK);
                                finish();
                            } else {
                                String data = dataJson.getString("data");
                                ToastUtils.showToast(modifyWithdrawBtcAddr(code, data));
                            }

                        } else {
//                            ToastUtils.showToast(getString(R.string.net_error));
                            showToast(getString(R.string.net_error));
                            hideLoadingDialog();
                        }
                    }
                });
    }

    /**
     * 设置地址提示语
     *
     * @param resultCode
     * @return
     */
    public String modifyWithdrawBtcAddr(String resultCode, String data) {
        String mssage = "";
        switch (resultCode) {
            case "100":
                mssage = getString(R.string.never_send_code);
                break;
            case "101":
                mssage = getString(R.string.code_error_too_much);
                break;
            case "102":
                mssage = String.format(getString(R.string.code_error_remain_times), data);
                break;
            case "104":
                mssage = getString(R.string.can_not_bind_platform_address);
                break;
            case "106":
                mssage = getString(R.string.max_five_address);
                break;
            case "128":
                mssage = getString(R.string.address_length_too_long);
                break;
        }

        if (mssage.equals("")) {
            mssage = getString(R.string.bind_address_failed);
        }
        return mssage;
    }


}

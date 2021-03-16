package com.biyoex.app.common.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.biyoex.app.R;
import com.biyoex.app.common.Constants;
import com.biyoex.app.common.SendCodeView;
import com.biyoex.app.common.mvpbase.BaseActivity;
import com.biyoex.app.common.okhttp.OkHttpUtils;
import com.biyoex.app.common.okhttp.callback.StringCallback;
import com.biyoex.app.common.present.SendCodePresent;
import com.biyoex.app.common.utils.NetUtils;
import com.biyoex.app.common.utils.RegexUtils;
import com.biyoex.app.common.utils.SendCodeTimeUtils;
import com.biyoex.app.common.utils.SharedPreferencesUtils;
import com.biyoex.app.common.utils.TimeCount;
import com.biyoex.app.common.utils.ToastUtils;
import com.biyoex.app.common.utils.log.Log;
import com.biyoex.app.common.widget.AutoButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;


/**
 * 注册页面
 * Created by LG on 2017/5/16.
 */

public class RegisterActivity extends BaseActivity<SendCodePresent> implements SendCodeView {
    @BindView(R.id.et_account)
    EditText edPhone;
    @BindView(R.id.iv_new_eyes)
    ImageView ivLookOver;
    @BindView(R.id.iv_again_eves)
    ImageView ivRepeatLookOver;
    @BindView(R.id.ed_set_password)
    EditText edPassword;
    @BindView(R.id.ed_again_pw)
    EditText edRepeatPass;
    @BindView(R.id.btn_confirm)
    AutoButton btnConfirm;
//    @BindView(R.id.tv_risk)
//    TextView tvRisk;
    @BindView(R.id.ed_code)
    EditText edInviteCode;
//    @BindView(R.id.iv_select)
//    CustomeToggleButton customeToggleButton;
    @BindView(R.id.tv_send_code)
    Button tvSendCode;
    @BindView(R.id.edit_shareCode)
    EditText edit_inviteCode;
//    @BindView(R.id.tv_title)
//    TextView tvTitle;
    @BindView(R.id.tv_server_message)
    TextView tvServer;
    private SendCodeTimeUtils sendCodeTimeUtils;


    @Override
    public void initData() {
        SpannableStringBuilder spannableString = new SpannableStringBuilder(tvServer.getText().toString());
        ForegroundColorSpan colorSpan =new  ForegroundColorSpan(getResources().getColor(R.color.my_theme));
        spannableString.setSpan(colorSpan,spannableString.length()-5,spannableString.length()-1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvServer.setText(spannableString);
        sendCodeTimeUtils = new SendCodeTimeUtils();
    }

    @Override
    protected SendCodePresent createPresent() {
        return new SendCodePresent(SendCodePresent.REGIST);
    }

    @Override
    public void initComp() {
        edPhone.setOnFocusChangeListener((v, hasFocus) -> {
            if (!TextUtils.isEmpty(edPhone.getText().toString().trim()) && !hasFocus) {
                requestIsRegister();
            }
        });

        ivLookOver.setTag(true);
        ivRepeatLookOver.setTag(true);
//
//        tvLogin.setText(Html.fromHtml("<fonts color=\"#333333\">" + getString(R.string.already_register) + "</fonts>" + getString(R.string.login)));

        btnConfirm.setEditTexts(edPhone, edPassword, edRepeatPass);

//        tvTitle.setText("注册");
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_register_new;
    }


    @OnClick({R.id.btn_confirm, R.id.iv_new_eyes, R.id.iv_again_eves,R.id.tv_send_code,R.id.tv_server_message})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_send_code:
                if (NetUtils.isNetworkAvalible(this)) {
                    if (TextUtils.isEmpty(edPhone.getText().toString().trim())) {
                        ToastUtils.showToast(getString(R.string.input_phone_email));
                    } else if (!RegexUtils.isTel(edPhone.getText().toString().trim()) && !RegexUtils.isEmail(edPhone.getText().toString().trim())) {
                        ToastUtils.showToast(getString(R.string.account_format_error));
                    } else {
                        if (sendCodeTimeUtils.needSend) {
                            mPresent.sendCode(edPhone.getText().toString().trim());
//                            requestSendSMSAuthCode();
                        }
                    }
                }
                else {
                    ToastUtils.showToast(getString(R.string.net_error));
                }
                break;
            //注册
            case R.id.btn_confirm:
                if (NetUtils.isNetworkAvalible(this)) {
                    if (TextUtils.isEmpty(edPhone.getText().toString().trim())) {
                        ToastUtils.showToast(getString(R.string.input_phone_email));
                    } else if (!RegexUtils.isTel(edPhone.getText().toString().trim()) && !RegexUtils.isEmail(edPhone.getText().toString().trim())) {
                        ToastUtils.showToast(getString(R.string.account_format_error));
                    } else if (TextUtils.isEmpty(edPassword.getText().toString())) {
                        ToastUtils.showToast(getString(R.string.password_hint));
                    } else if (!RegexUtils.isPassword(edPassword.getText().toString())) {
                        ToastUtils.showToast(getString(R.string.password_regex));
                    } else if (TextUtils.isEmpty(edRepeatPass.getText().toString())) {
                        ToastUtils.showToast(getString(R.string.please_repeat_pass));
                    } else if (!edRepeatPass.getText().toString().equals(edPassword.getText().toString())) {
                        ToastUtils.showToast(getString(R.string.password_not_same));
                    } else if (RegexUtils.inputFilter(edPassword.getText().toString())) {
                        ToastUtils.showToast(R.string.illegal_charaters);
                    } else if (TextUtils.isEmpty(edInviteCode.getText().toString().trim())) {
                        ToastUtils.showToast(R.string.code_error);
                    } else {
//                        if (sendCodeTimeUtils.needSend) {
//                            mPresent.sendCode(edPhone.getText().toString().trim());
////                            requestSendSMSAuthCode();
//                        }else {
//                            startCodeInputAcitivity();
//                        }
                        requestRegister(edPhone.getText().toString().trim(), edPassword.getText().toString(), edInviteCode.getText().toString(), edit_inviteCode.getText().toString());
                    }
                } else {
                    ToastUtils.showToast(getString(R.string.net_error));
                }
                break;
            //风险提示
//            case R.id.tv_risk:
//                Intent itWebPageLoading = new Intent(this, WebPageLoadingActivity.class);
//                itWebPageLoading.putExtra("title", getString(R.string.risk_hint));
////                if (LanguageUtils.currentLanguage == 1) {
//                itWebPageLoading.putExtra("url", " https://vbt.zendesk.com/hc/zh-sg/articles/360024102254");
//                itWebPageLoading.putExtra("type", "url");
////                }else {
////                    itWebPageLoading.putExtra("url", "http://www.zg.top/news/appservice");
////                    itWebPageLoading.putExtra("type", "url");
////                }
//                startActivity(itWebPageLoading);
//                break;
            case R.id.iv_new_eyes:
                if ((boolean) ivLookOver.getTag()) {
                    //明文
                    edPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    ivLookOver.setImageResource(R.mipmap.e_open);
                    ivLookOver.setTag(false);
                    edPassword.setSelection(edPassword.getText().toString().length());
                } else {
                    //密文
                    edPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    ivLookOver.setTag(true);
                    ivLookOver.setImageResource(R.mipmap.e_close);
                    edPassword.setSelection(edPassword.getText().toString().length());
                }
                break;
            case R.id.iv_again_eves:
                if ((boolean) ivRepeatLookOver.getTag()) {
                    //明文
                    edRepeatPass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    ivRepeatLookOver.setImageResource(R.mipmap.e_open);
                    ivRepeatLookOver.setTag(false);
                    edRepeatPass.setSelection(edRepeatPass.getText().toString().length());
                } else {
                    //密文
                    edRepeatPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    ivRepeatLookOver.setTag(true);
                    ivRepeatLookOver.setImageResource(R.mipmap.e_close);
                    edRepeatPass.setSelection(edRepeatPass.getText().toString().length());
                }
                break;
//
            case R.id.tv_server_message:
                Intent itWebPageLoading = new Intent(this, WebPageLoadingActivity.class);
                itWebPageLoading.putExtra("title", getString(R.string.risk_hint));
//                if (LanguageUtils.currentLanguage == 1) {
                itWebPageLoading.putExtra("url", "https://biyoex.com/mobile/#/detail?type=6&id=1");
                itWebPageLoading.putExtra("type", "url");
                startActivity(itWebPageLoading);
                break;
        }
    }

//    public void startCodeInputAcitivity() {
//        Intent intent = new Intent(this, IdentifyCodeAcitvity.class);
//        intent.putExtra("type", IdentifyCodeAcitvity.REGISTER);
//        intent.putExtra("name", edPhone.getText().toString());
//        intent.putExtra("pwd", edPassword.getText().toString());
//        intent.putExtra("invite", edInviteCode.getText().toString());
//        startActivityForResult(intent, 0x1);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x1 && resultCode == RESULT_OK) {
            finish();
        }
    }

    public void requestIsRegister() {
        if (TextUtils.isEmpty(edPhone.getText().toString().trim())) {
            ToastUtils.showToast(getString(R.string.input_phone_email));
            return;
        }
        if (!RegexUtils.isTel(edPhone.getText().toString().trim()) && !RegexUtils.isEmail(edPhone.getText().toString().trim())) {
            ToastUtils.showToast(getString(R.string.account_format_error));
            return;
        }
        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("name", edPhone.getText().toString());
        mapParams.put("pwd", "123");
        mapParams.put("code", "123");
        OkHttpUtils
                .post()
                .url(Constants.BASE_URL + "v1/register")
                .addHeader("X-Requested-With", "XMLHttpReques")
                .params(mapParams)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(String response, int id) throws JSONException {
                JSONObject dataJson = new JSONObject(response);
                int code = dataJson.getInt("code");
                if (code == 2) {
                    AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this).create();
                    alertDialog.setMessage(getString(R.string.already_regist));
                    alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.confirm), (dialog, which) -> dialog.dismiss());
                    alertDialog.show();
                }
            }
        });
    }

    /**
     * 请求注册
     */
    public void requestRegister(final String name, String pwd, String code, String inviteCode) {
        showLoadingDialog();
        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("name", name);
        mapParams.put("pwd", pwd);
        mapParams.put("code", code);
//        mapParams.put("invi")
        if (!TextUtils.isEmpty(inviteCode)) {
            mapParams.put("inviteCode", inviteCode);
        }
        OkHttpUtils
                .post()
                .url(Constants.BASE_URL + "v1/register")
                .addHeader("X-Requested-With", "XMLHttpReques")
                .params(mapParams)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e(e, "requestRegister");
                hideLoadingDialog();
            }

            @Override
            public void onResponse(String response, int id) throws JSONException {
                JSONObject dataJson = new JSONObject(response);
                String code = "";
                String leftCount = "";
                String msg = "";
                if (dataJson.has("msg")) {
                    msg = dataJson.getString("msg");
                }
                if (dataJson.has("code")) {
                    code = dataJson.getString("code");
                }

                Log.i("requestRegister result :" + code);

                if (dataJson.has("data")) {
                    leftCount = dataJson.getString("data");
                }

                if (code.equals("200")) {
                    ToastUtils.showToast(getString(R.string.regist_success));
                    SharedPreferencesUtils.getInstance().saveString("loginName", name);
                    setResult(RESULT_OK);
                    finish();
                } else {
                    if (code.equals("106")) {
                        ToastUtils.showToast(getString(R.string.code_error_too_much));
                    } else if (code.equals("1")) {
                        ToastUtils.showToast(getString(R.string.input_phone_email));
                    } else if (code.equals("2")) {
                        ToastUtils.showToast(getString(R.string.account_already_regist));
                    } else if (code.equals("3")) {
                        ToastUtils.showToast(getString(R.string.password_empty));
                    } else if (code.equals("100")) {
                        ToastUtils.showToast(getString(R.string.code_error));
                    } else if (code.equals("101")) {
                        ToastUtils.showToast(getString(R.string.opera_too_much));
                    } else if (code.equals("102")) {
                        if (leftCount != null && !leftCount.equals(""))
                            ToastUtils.showToast(String.format(getString(R.string.certify_error_remains), leftCount));
                    } else if (code.equals("-1")) {
                        ToastUtils.showToast(getString(R.string.unknown_error));
                    } else {
                        showToast(msg);
                    }
                }
                hideLoadingDialog();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        OkHttpUtils.getInstance().cancelTag(this);
        sendCodeTimeUtils.reset();
    }

    @OnClick({R.id.iv_back})
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void sendCodeSuccess() {
//        startCodeInputAcitivity();
//        sendCodeTimeUtils.start();
        new TimeCount(60000, 1000, tvSendCode).start();
    }


    @Override
    protected void setStatusBar() {
        super.setStatusBar();
        immersionBar.statusBarDarkFont(true);
    }
}

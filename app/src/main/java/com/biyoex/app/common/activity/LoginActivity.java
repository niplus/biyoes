package com.biyoex.app.common.activity;

import androidx.lifecycle.Lifecycle;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.text.InputType;
import android.text.Spannable;

import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;
import com.biyoex.app.BuildConfig;
import com.biyoex.app.R;
import com.biyoex.app.common.Constants;
import com.biyoex.app.common.SendCodeView;
import com.biyoex.app.common.data.SessionLiveData;
import com.biyoex.app.common.http.RetrofitHelper;
import com.biyoex.app.common.mvpbase.BaseActivity;
import com.biyoex.app.common.mvpbase.BaseObserver;
import com.biyoex.app.common.present.SendCodePresent;
import com.biyoex.app.common.utils.ActivityManagerUtils;
import com.biyoex.app.common.utils.CryptUtils;
import com.biyoex.app.common.utils.LollipopFixedWebView;
import com.biyoex.app.common.utils.NetUtils;
import com.biyoex.app.common.utils.RegexUtils;
import com.biyoex.app.common.utils.SendCodeTimeUtils;
import com.biyoex.app.common.utils.SharedPreferencesUtils;
import com.biyoex.app.common.utils.ToastUtils;
import com.biyoex.app.common.utils.log.Log;
import com.biyoex.app.my.activity.ForGetPwActivity;
import com.biyoex.app.my.activity.GestureLoginActivity;
import com.biyoex.app.my.activity.GestureSettingActivity;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.uber.autodispose.AutoDispose.autoDisposable;

/**
 * ????????????
 * Created by LG on 2017/5/16.
 */

public class LoginActivity extends BaseActivity<SendCodePresent> implements SendCodeView {
    @BindView(R.id.edit_account)
    EditText edUser;
    @BindView(R.id.iv_eyes)
    ImageView ivLookOver;
    @BindView(R.id.edit_login_password)
    EditText edPassword;
    @BindView(R.id.tv_login)
    TextView btnConfirm;
    @BindView(R.id.webView)
    LollipopFixedWebView wvDetails;
    @BindView(R.id.tv_register)
    TextView tvRegister;
    /**
     * ??????????????????????????????true????????????false?????????
     */
    private boolean isDisplay = true;
    private String slideSession = "";
    private String slideToken = "";
    private String slideSig = "";
    //??????????????????
    private String strErrorNum;
    private SendCodeTimeUtils sendCodeTimeUtils;
    public static int count = 0;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void initData() {
        boolean isGestureOn = SharedPreferencesUtils.getInstance().isUserGestureOn();
        String pwd = SharedPreferencesUtils.getInstance().getString("accessWord", "");
        if (isGestureOn && !TextUtils.isEmpty(pwd)) {
            //?????????????????????????????????????????????
            if (count == 0) {
                Intent intent = new Intent(this, GestureLoginActivity.class);
                startActivityForResult(intent, 0x2);
                count++;//??????????????? ????????????????????????????????????count?????????????????????
            }
        }
        SpannableStringBuilder spannableString = new SpannableStringBuilder(tvRegister.getText().toString());
        ForegroundColorSpan colorSpan =new  ForegroundColorSpan(getResources().getColor(R.color.my_theme));
        spannableString.setSpan(colorSpan,5,spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvRegister.setText(spannableString);
        sendCodeTimeUtils = new SendCodeTimeUtils();
//        Log.i("nidongliang loadUrl : " + Constants.REALM_NAME + "/user/mobileLogin");
//        wvDetails.loadUrl(Constants.REALM_NAME + "/user/mobileLogin");
        wvDetails.loadUrl("https://hk.biyocn.com/nocaptcha/mobile.html");
        wvDetails.getSettings().setLoadWithOverviewMode(true);
        wvDetails.getSettings().setJavaScriptEnabled(true);
        wvDetails.getSettings().setBlockNetworkImage(false);//?????????????????????
        wvDetails.getSettings().setLoadsImagesAutomatically(true);
        wvDetails.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        wvDetails.getSettings().setUseWideViewPort(true);//???????????????
        wvDetails.addJavascriptInterface(new JSWebView(), "testInterface");
        wvDetails.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(url.contains("broadcast")){

                }
                else{

                }
                wvDetails.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }
        });

        //????????????????????????????????????????????????
        wvDetails.getSettings().setDomStorageEnabled(true);
    }

    @Override
    protected SendCodePresent createPresent() {
        return new SendCodePresent(SendCodePresent.LOGIN);
    }

    @Override
    public void initComp() {
        //??????????????????
        edPassword.clearFocus();
        edUser.clearFocus();
//        btnConfirm.setEditTexts(edUser, edPassword);

//        tvTitle.setText(R.string.regist_login);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //??????????????????????????????????????????
        edUser.setText(SharedPreferencesUtils.getInstance().getString("loginName", ""));
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login_n;
    }

    @OnClick({R.id.iv_eyes, R.id.tv_login, R.id.tv_forget_pad, R.id.tv_register})
    public void onClick(View view) {
        switch (view.getId()) {
            //??????????????????
            case R.id.iv_eyes:
                if (isDisplay) {
                    //??????
                    edPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    isDisplay = false;
                    ivLookOver.setImageResource(R.mipmap.e_open);
                    edPassword.setSelection(edPassword.getText().toString().length());
                } else {
                    //??????
                    edPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    isDisplay = true;
                    ivLookOver.setImageResource(R.mipmap.e_close);
                    edPassword.setSelection(edPassword.getText().toString().length());
                }
                break;
            //????????????
            case R.id.tv_login:
                if (NetUtils.isNetworkAvalible(this)) {
                    if (TextUtils.isEmpty(edUser.getText().toString().trim())) {
//                        ToastUtils.showToast(getString(R.string.username_can_not_empty));\
                        showToast(getString(R.string.username_can_not_empty));
                    } else if (TextUtils.isEmpty(edPassword.getText().toString())) {
//                        ToastUtils.showToast(getString(R.string.password_hint));
                        showToast(getString(R.string.password_hint));
                    } else if (RegexUtils.inputFilter(edPassword.getText().toString()) || RegexUtils.inputFilter(edUser.getText().toString())) {
//                        ToastUtils.showToast(R.string.illegal_charaters);
                        showToast(getString(R.string.illegal_charaters));
                    }  else {
                        requestUserLogin(edPassword.getText().toString());
                    }
                } else {
//                    ToastUtils.showToast(getString(R.string.net_not_work));
                    showToast(getString(R.string.net_not_work));
                }
                break;
            //????????????
            case R.id.tv_register:
                ActivityManagerUtils.getInstance().addActivity(this);
                Intent itRegister = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(itRegister);
                break;
            //????????????
            case R.id.tv_forget_pad:
                Intent itForgetPassword = new Intent(LoginActivity.this, ForGetPwActivity.class);
                startActivity(itForgetPassword);
                break;
        }
    }

    /**
     * ??????????????????????????????
     */
    public void requestUserLogin2(String password) {
        showLoadingDialog();
        RetrofitHelper
                .getIns()
                .getZgtopApi()
                .login(edUser.getText().toString().trim(), password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(autoDisposable(AndroidLifecycleScopeProvider.from(this, Lifecycle.Event.ON_DESTROY)))
                .subscribe(new BaseObserver<String>(this) {

                    @Override
                    protected void error() {
                        Log.e("requestUserLogin error");
                        hideLoadingDialog();
                        ToastUtils.showToast(getString(R.string.relogin));
                    }

                    @Override
                    protected void success(String s) {
                        JSONObject dataJson;
                        try {
                            dataJson = new JSONObject(s);
                            String code = dataJson.getString("code");
                            Log.i("requestUserLogin result :" + code);
                            String data = dataJson.getString("data");

                            if (dataJson.has("errorNum")) {
                                strErrorNum = dataJson.getString("errorNum");
                            }
                            getDealMessage(code, data);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    /**
     * ??????????????????
     */
    public void requestUserLogin(String password) {
        showLoadingDialog();
        RetrofitHelper
                .getIns()
                .getZgtopApi()
                .login2(edUser.getText().toString().trim(), password, slideSession, slideToken, slideSig)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(autoDisposable(AndroidLifecycleScopeProvider.from(this, Lifecycle.Event.ON_DESTROY)))
                .subscribe(new BaseObserver<String>(this) {
                    @Override
                    protected void error() {
                        Log.e("requestUserLogin error");
                        hideLoadingDialog();
                        ToastUtils.showToast(getString(R.string.relogin));
                    }

                    @Override
                    protected void success(String s) {
                        JSONObject dataJson;
                        try {
                            dataJson = new JSONObject(s);
                            String code = dataJson.getString("code");
                            Log.i("requestUserLogin result :" + code);

                            String data = dataJson.getString("data");

                            if (dataJson.has("errorNum")) {
                                strErrorNum = dataJson.getString("errorNum");
                            }
//                            wvDetails.loadUrl(Constants.REALM_NAME + "/user/mobileLogin");
                            wvDetails.loadUrl("https://hk.biyocn.com/nocaptcha/mobile.html");
                            getDealMessage(code, data);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @OnClick(R.id.iv_back)
    @Override
    public void onBackPressed() {
//        Intent intent = getIntent();
//        String tag = intent.getStringExtra("tag");
//        if (tag != null && tag.equals("property")) {
//            ActivityManagerUtils.getInstance().getMainActivity().turnPage(4);
        finish();
//        } else {
        super.onBackPressed();
//        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);

    }

    /**
     * ??????????????????????????????
     *
     * @param resultCode
     */
    public void getDealMessage(String resultCode, String data) {

        switch (resultCode) {
            //?????????????????????????????????????????????
            case "7":
                //?????????????????????????????????????????????
            case "6":
                SharedPreferencesUtils.getInstance().saveString("loginName", edUser.getText().toString());
                if (sendCodeTimeUtils.needSend) {
                    mPresent.sendCode(edUser.getText().toString());
                } else {
                    finish();
                    startCodeInputAcitivity();
                }
                break;
            //?????????????????????????????????????????????????????????
            case "5":
//                ToastUtils.showToast(getString(R.string.account_exception));
                showToast(getString(R.string.account_exception));
                break;
            //????????????????????????(????????????)
            case "4":
//                ToastUtils.showToast(String.format(getString(R.string.login_failed_remain_time), data));
                showToast(String.format(getString(R.string.login_failed_remain_time), data));
                break;
            //?????????????????????????????????????????????????????????????????????
            case "3":
//                ToastUtils.showToast(getString(R.string.pass_error_too_many_times));
                showToast(getString(R.string.pass_error_too_many_times));
                break;
            //???????????????????????? ?????????????????????????????????
            case "-3":
//                ToastUtils.showToast(String.format(getString(R.string.login_failed_remain_time), strErrorNum));
                showToast(String.format(getString(R.string.login_failed_remain_time), strErrorNum));
                break;
            //????????????
            case "200":
                //?????????????????????
                successLogin();
                return;
            case "1":
//                ToastUtils.showToast(getString(R.string.username_can_not_empty));
                showToast(getString(R.string.username_can_not_empty));
                break;
            case "2":
//                ToastUtils.showToast(getString(R.string.password_empty));
                showToast(getString(R.string.password_empty));
                break;
            default:
//                    ToastUtils.showToast("????????????");
                showToast(getString(R.string.relogin));

        }
        hideLoadingDialog();
    }

    public void startCodeInputAcitivity() {
        Intent intent = new Intent(this, IdentifyCodeAcitvity.class);
        intent.putExtra("type", IdentifyCodeAcitvity.VALIDATE_DEVICE);
        intent.putExtra("name", edUser.getText().toString());
        startActivityForResult(intent, 0x1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x1 && resultCode == RESULT_OK) {
            boolean isGestureOn = SharedPreferencesUtils.getInstance().isUserGestureOn();
            String pwd = SharedPreferencesUtils.getInstance().getString("accessWord", "");

            if (!isGestureOn && !TextUtils.isEmpty(pwd)) {
                //?????????????????????????????????????????????
                Intent intent = new Intent(this, GestureSettingActivity.class);
                startActivityForResult(intent, 0x2);
            } else {
                savePwd();
                finish();
            }
        } else if (requestCode == 0x2) {
            Log.i("resultCode : " + resultCode);
            if (resultCode == RESULT_CANCELED) {
                finish();
            } else if (resultCode == RESULT_OK) {
                try {
                    String password = CryptUtils.decrypt(BuildConfig.salt, SharedPreferencesUtils.getInstance().getString("accessWord", ""));
                    if (!TextUtils.isEmpty(password)) {
                        requestUserLogin2(password);
                    } else {
//                        ToastUtils.showToast("??????????????????");
                        showToast(getString(R.string.Gesture_login_failed));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * ?????????????????????
     */
    public void successLogin() {
        RetrofitHelper.getIns().getZgtopApi().getUserSelfToken()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(autoDisposable(AndroidLifecycleScopeProvider.from(this, Lifecycle.Event.ON_DESTROY)))
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void success(String s) {

                    }
                });
        if (!TextUtils.isEmpty(edUser.getText()))
            SharedPreferencesUtils.getInstance().saveString("loginName", edUser.getText().toString());
        savePwd();

        SessionLiveData.getIns().startTimer(new SessionLiveData.OnRequestSessionSuccessListener() {
            @Override
            public void onRequestSessionSuccess() {
                boolean isGestureOn = SharedPreferencesUtils.getInstance().isUserGestureOn();
                String pwd = SharedPreferencesUtils.getInstance().getString("accessWord", "");
                if (!isGestureOn && !TextUtils.isEmpty(pwd)) {
                    //?????????????????????????????????????????????
                    Intent intent = new Intent(LoginActivity.this, GestureSettingActivity.class);
                    startActivityForResult(intent, 0x2);
                } else {
                    Intent intent = getIntent();
                    String tag = intent.getStringExtra("tag");
                    if (tag != null && tag.equals("property")) {
                        ActivityManagerUtils.getInstance().getMainActivity().turnPage(4);
                        finish();
                    } else {
                        showToast(getString(R.string.login_success));
                        hideLoadingDialog();
                        finish();
                    }
                }
//                ToastUtils.showToast(getString(R.string.login_success));
//                Intent intent = getIntent();
//                String tag = intent.getStringExtra("tag");
//                if (tag != null && tag.equals("property")) {
//                    ActivityManagerUtils.getInstance().getMainActivity().turnPage(4);
//                    finish();
//                } else {
//                    showToast(getString(R.string.login_success));
//                    hideLoadingDialog();
//                    finish();
//                }
            }

            @Override
            public void onRequestSessionFailed() {
                hideLoadingDialog();
//                ToastUtils.showToast("????????????");
                showToast(getString(R.string.relogin));
            }
        });
    }

    private void savePwd() {
        try {
            if (!TextUtils.isEmpty(edPassword.getText())) {
                String encrypt = CryptUtils.encrypt(BuildConfig.salt, edPassword.getText().toString());
                android.util.Log.i("encrypt", "savePwd: ");
                SharedPreferencesUtils.getInstance().saveString("accessWord", encrypt);
            }
        } catch (Exception e) {
            Log.e(e, "success login");
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
//        Log.i("onDestroy");
        SessionLiveData.getIns().setOnRequestSessionSuccessListener(null);
        sendCodeTimeUtils.reset();
    }

    @Override
    public void sendCodeSuccess() {
        startCodeInputAcitivity();
        sendCodeTimeUtils.start();
    }

    class JSWebView {
        @JavascriptInterface
        public void getSlideData(String json) {
            try {
                Log.d("getslidedata" + json);
                JSONObject jsonObject = new JSONObject(json);
                slideSession = jsonObject.getString("sessionId");
                slideSig = jsonObject.getString("sig");
                slideToken = jsonObject.getString("token");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}

package com.biyoex.app.my.activity;

import android.content.Intent;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.biyoex.app.MainActivity;
import com.biyoex.app.R;
import com.biyoex.app.common.SendCodeView;
import com.biyoex.app.common.activity.LoginActivity;
import com.biyoex.app.common.bean.RequestResult;
import com.biyoex.app.common.data.SessionLiveData;
import com.biyoex.app.common.http.RetrofitHelper;
import com.biyoex.app.common.mvpbase.BaseActivity;
import com.biyoex.app.common.mvpbase.BaseObserver;
import com.biyoex.app.common.present.SendCodePresent;
import com.biyoex.app.common.utils.RegexUtils;
import com.biyoex.app.common.utils.SharedPreferencesUtils;
import com.biyoex.app.common.utils.TimerUtils;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 修改登录密码
 * Created by LG on 2017/6/5.
 */

public class ReviseLoginPasswordActivity extends BaseActivity<SendCodePresent> implements SendCodeView {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ed_used_password)
    EditText edUsedPassword;
    @BindView(R.id.ed_news_password)
    EditText edNewsPassword;
    @BindView(R.id.ed_repeat_password)
    EditText edRepeatPassword;
    @BindView(R.id.tv_timer)
    TextView tvTimer;
    @BindView(R.id.ed_verification_code)
    EditText edVerificationCode;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    @BindView(R.id.iv_new_eyes)
    ImageView ivNewsEyes;
    @BindView(R.id.iv_old_eyes)
    ImageView ivOldEyes;
    @BindView(R.id.iv_repeat_eyes)
    ImageView ivRepeatEyes;

    /**
     * 计时器类对象
     */
    private TimerUtils timerUtils;


    @Override
    public void initData() {

    }

    @Override
    protected SendCodePresent createPresent() {
        return new SendCodePresent(SendCodePresent.ACCOUNT, 5);
    }

    @Override
    public void initComp() {
        tvTimer.setText(getString(R.string.get_code));
//        tvTitle.setText(R.string.change_login_password);
        //false为闭眼
        ivNewsEyes.setTag(false);
        ivOldEyes.setTag(false);
        ivRepeatEyes.setTag(false);
    }

    @Override
    public int getLayoutId() {

        return R.layout.activity_revise_login_password;

    }

    @OnClick({R.id.iv_menu, R.id.tv_timer, R.id.btn_confirm, R.id.iv_repeat_eyes, R.id.iv_old_eyes, R.id.iv_new_eyes})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_menu:
                if (timerUtils != null) {
                    timerUtils.resetTimer();
                }
                finish();
                break;
            //发送短信验证码
            case R.id.tv_timer:
                if (tvTimer.getText().toString().indexOf(getString(R.string.second)) == -1) {
                    showLoadingDialog();
                    mPresent.sendCode(null);
                }
                break;
            case R.id.btn_confirm:
                if (TextUtils.isEmpty(edUsedPassword.getText().toString())) {
//                   ToastUtils.showToast(getString(R.string.input_old_password));
                    showToast(getString(R.string.input_old_password));
                } else if (TextUtils.isEmpty(edNewsPassword.getText().toString())) {
//                   ToastUtils.showToast(getString(R.string.input_new_password));
                    showToast(getString(R.string.input_new_password));
                } else if (!RegexUtils.isPassword(edNewsPassword.getText().toString())) {
//                   ToastUtils.showToast(getString(R.string.password_regex));
                    showToast(getString(R.string.password_regex));
                } else if (TextUtils.isEmpty(edRepeatPassword.getText().toString())) {
//                   ToastUtils.showToast(getString(R.string.repeat_password));
                    showToast(getString(R.string.repeat_password));
                } else if (!edNewsPassword.getText().toString().equals(edRepeatPassword.getText().toString())) {
//                   ToastUtils.showToast(getString(R.string.password_is_not_same));
                    showToast(getString(R.string.password_is_not_same));
                } else if (TextUtils.isEmpty(edVerificationCode.getText().toString())) {
//                   ToastUtils.showToast(getString(R.string.input_certify_code));
                    showToast(getString(R.string.input_certify_code));
                } else if (RegexUtils.inputFilter(edUsedPassword.getText().toString())
                        || RegexUtils.inputFilter(edNewsPassword.getText().toString())
                        || RegexUtils.inputFilter(edRepeatPassword.getText().toString())) {
//                   ToastUtils.showToast(R.string.illegal_charaters);
                    showToast(getString(R.string.illegal_charaters));
                } else {
                    showLoadingDialog();
                    requestModLoginPassword();
                }
                break;
            case R.id.iv_new_eyes:
                switchEyes(edNewsPassword, ivNewsEyes);
                break;
            case R.id.iv_old_eyes:
                switchEyes(edUsedPassword, ivOldEyes);
                break;
            case R.id.iv_repeat_eyes:
                switchEyes(edRepeatPassword, ivRepeatEyes);
                break;
        }
    }

    private void switchEyes(EditText editText, ImageView imageView) {
        boolean isDisplay = (boolean) imageView.getTag();
        if (!isDisplay) {
            //明文
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            imageView.setImageResource(R.mipmap.e_open);
            editText.setSelection(editText.getText().toString().length());
        } else {
            //密文
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            imageView.setImageResource(R.mipmap.e_close);
            editText.setSelection(editText.getText().toString().length());
        }
        imageView.setTag(!isDisplay);
    }


    /**
     * 重置登录密码
     */
    public void requestModLoginPassword() {
        RetrofitHelper.getIns().getZgtopApi()
                .updagePassword(edNewsPassword.getText().toString(), edVerificationCode.getText().toString(), edUsedPassword.getText().toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<RequestResult>() {
                    @Override
                    protected void success(RequestResult requestResult) {
                        hideLoadingDialog();
                        showToast(getString(R.string.change_login_pass_success));
                        setResult(RESULT_OK);
                        SharedPreferencesUtils.getInstance().saveData("JSESSIONID", "");
                        SharedPreferencesUtils.getInstance().saveData("route", "");
                        SharedPreferencesUtils.getInstance().saveData("accessWord", "");
                        SessionLiveData.getIns().postValue(null);
                        //回到首页
                        Intent mainActivity = new Intent(ReviseLoginPasswordActivity.this, MainActivity.class);
                        startActivity(mainActivity);
                        Intent itLogin = new Intent(ReviseLoginPasswordActivity.this, LoginActivity.class);
                        itLogin.putExtra("type", "noinfo");
                        startActivity(itLogin);
                        finish();
                    }

                    @Override
                    protected void failed(int code, String data, String msg) {
                        hideLoadingDialog();
                        super.failed(code, data, msg);
                        if (code == 101) {
//                              ToastUtils.showToast(getString(R.string.send_code_too_much));
                            showToast(getString(R.string.send_code_too_much));
                        } else if (code == 102) {
//                               ToastUtils.showToast(getString(R.string.code_error));
                            showToast(getString(R.string.code_error));
                        } else if (code == 104) {
//                               ToastUtils.showToast(getString(R.string.old_pass_error));
                            showToast(getString(R.string.old_pass_error));
                        } else if (code == 105) {
//                               ToastUtils.showToast(getString(R.string.same_login_trade_pass));
                            showToast(getString(R.string.same_login_trade_pass));
                        } else if (code == 100) {
//                               ToastUtils.showToast(getString(R.string.never_send_code));
                            showToast(getString(R.string.never_send_code));
                        } else if (code == 103) {
//                               ToastUtils.showToast(getString(R.string.same_old_new_pass));
                            showToast(getString(R.string.same_old_new_pass));
                        } else {
//                               ToastUtils.showToast(getString(R.string.change_pass_failed));
                            showToast(getString(R.string.change_pass_failed));
                        }
                    }
                });
    }

    @Override
    public void sendCodeSuccess() {
        timerUtils = TimerUtils.getInitialise();
        timerUtils.setCanOnClick(true);
        if (timerUtils.getCanOnClick()) {
            timerUtils.startTimer(tvTimer, 61);
        }
    }
}

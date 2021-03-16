package com.biyoex.app.my.activity;

import android.content.Intent;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.biyoex.app.R;
import com.biyoex.app.common.SendCodeView;
import com.biyoex.app.common.bean.RequestResult;
import com.biyoex.app.common.data.SessionLiveData;
import com.biyoex.app.common.http.RetrofitHelper;
import com.biyoex.app.common.mvpbase.BaseActivity;
import com.biyoex.app.common.mvpbase.BaseObserver;
import com.biyoex.app.common.present.SendCodePresent;
import com.biyoex.app.common.utils.RegexUtils;
import com.biyoex.app.common.utils.TimerUtils;
import com.biyoex.app.common.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 修改支付密码
 * Created by LG on 2017/6/5.
 */

public class RevisePayPasswordActivity extends BaseActivity<SendCodePresent> implements SendCodeView {
    @BindView(R.id.tv_title)
    TextView tvTitle;
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
    @BindView(R.id.iv_repeat_eyes)
    ImageView ivRepeatEyes;


    /**
     * 计时器类对象
     */
    private TimerUtils timerUtils;

    private String strPrompt;

    private String type;

    @Override
    public void initData() {
        Intent itRevise = getIntent();
        type = itRevise.getStringExtra("revise");
        if (type.equals(getString(R.string.setting))) {
//            tvTitle.setText(R.string.set_trade_password);
            strPrompt = getString(R.string.set_trade_password_success);
        } else {
//            tvTitle.setText(R.string.edit_trade_password);
            strPrompt = getString(R.string.edit_trade_password_success);
        }

    }

    @Override
    protected SendCodePresent createPresent() {
        return new SendCodePresent(SendCodePresent.ACCOUNT, 6);
    }

    @Override
    public void initComp() {
        tvTimer.setText(getString(R.string.get_code));

        //false为闭眼
        ivNewsEyes.setTag(false);
        ivRepeatEyes.setTag(false);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_revise_pay_password;
    }

    @OnClick({R.id.iv_menu, R.id.btn_confirm, R.id.tv_timer, R.id.iv_new_eyes, R.id.iv_repeat_eyes})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_menu:
                if (timerUtils != null) {
                    timerUtils.resetTimer();
                }
                finish();
                break;
            //修改
            case R.id.btn_confirm:
                if (TextUtils.isEmpty(edNewsPassword.getText().toString())) {
//                    ToastUtils.showToast(getString(R.string.input_new_password));
                    showToast(getString(R.string.input_new_password));
                } else if (!RegexUtils.isPassword(edNewsPassword.getText().toString())) {
//                    ToastUtils.showToast(getString(R.string.password_regex));
                    showToast(getString(R.string.password_regex));
                } else if (TextUtils.isEmpty(edRepeatPassword.getText().toString())) {
//                    ToastUtils.showToast(getString(R.string.repeat_password));
                    showToast(getString(R.string.repeat_password));
                } else if (!edNewsPassword.getText().toString().equals(edRepeatPassword.getText().toString())) {
//                    ToastUtils.showToast(getString(R.string.password_not_same));
                    showToast(getString(R.string.password_not_same));
                } else if (TextUtils.isEmpty(edVerificationCode.getText().toString())) {
//                    ToastUtils.showToast(getString(R.string.input_certify_code));
                    showToast(getString(R.string.input_certify_code));
                } else if (RegexUtils.inputFilter(edNewsPassword.getText().toString())) {
//                    ToastUtils.showToast(R.string.illegal_charaters);
                    showToast(getString(R.string.illegal_charaters));
                } else {
                    showLoadingDialog();
                    requestModLoginPassword();
                }
                break;
            //发送短信验证码
            case R.id.tv_timer:
                if (tvTimer.getText().toString().indexOf(getString(R.string.second)) == -1) {
                    showLoadingDialog();
                    mPresent.sendCode(null);
                }
                break;
            case R.id.iv_new_eyes:
                switchEyes(edNewsPassword, ivNewsEyes);
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
     * 设置密码
     */
    public void requestModLoginPassword() {
//        Map<String, String> mapParams = new HashMap<>();
//        mapParams.put("pwd", edNewsPassword.getText().toString());
//        mapParams.put("code", edVerificationCode.getText().toString());
        RetrofitHelper.getIns().getZgtopApi().savePassword(edNewsPassword.getText().toString(), edVerificationCode.getText().toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<RequestResult>() {
                    @Override
                    protected void success(RequestResult requestResult) {
                        ToastUtils.showToast(strPrompt);
                        hideLoadingDialog();
                        finish();
                        SessionLiveData.getIns().getSeesionInfo();
                    }

                    @Override
                    protected void failed(int code, String data, String msg) {
                        super.failed(code, data, msg);
                        hideLoadingDialog();
                        if (code == 100) {
//                                ToastUtils.showToast(getString(R.string.code_not_send));
                            showToast(getString(R.string.code_not_send));
                        } else if (code == 105) {
//                                ToastUtils.showToast(getString(R.string.trade_pass_need_diff_login_pass));
                            showToast(getString(R.string.trade_pass_need_diff_login_pass));
                        } else if (code == 102) {
//                                ToastUtils.showToast(getString(R.string.code_error));
                            showToast(getString(R.string.code_error));
                        } else if (code == 1) {
//                                ToastUtils.showToast(getString(R.string.same_old_new_pass));
                            showToast(getString(R.string.same_old_new_pass));
                        } else {
//                                ToastUtils.showToast(getString(R.string.change_pass_failed));
                            showToast(getString(R.string.change_pass_failed));
                        }
                    }
                });
    }
    @Override
    public void sendCodeSuccess() {
        if (timerUtils == null) {
            timerUtils = new TimerUtils();
        }

        timerUtils.resetTimer();
        if (timerUtils.getCanOnClick()) {
            timerUtils.startTimer(tvTimer, 61);
        }
    }
}

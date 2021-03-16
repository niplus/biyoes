package com.biyoex.app.common.activity;

import android.content.Intent;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.biyoex.app.R;
import com.biyoex.app.common.SendCodeView;
import com.biyoex.app.common.mvpbase.BaseActivity;
import com.biyoex.app.common.present.SendCodePresent;
import com.biyoex.app.common.utils.NetUtils;
import com.biyoex.app.common.utils.RegexUtils;
import com.biyoex.app.common.utils.SendCodeTimeUtils;
import com.biyoex.app.common.utils.ToastUtils;
import com.biyoex.app.common.widget.AutoButton;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 找回密码
 * Created by LG on 2017/5/16.
 */

public class ForgetPasswordActivity extends BaseActivity<SendCodePresent> implements SendCodeView {
    @BindView(R.id.ed_phone)
    EditText edPhone;
    @BindView(R.id.btn_confirm)
    AutoButton btnConfirm;
    @BindView(R.id.ed_set_password)
    EditText edSetPass;
    @BindView(R.id.ed_repeat_password)
    EditText edRepeatPass;
    @BindView(R.id.iv_password)
    ImageView ivPassword;
    @BindView(R.id.iv_repeat_password)
    ImageView ivRepeatPassword;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    /**
     * 计时器类对象
     */
    private SendCodeTimeUtils sendCodeTimeUtils;


    @Override
    public void initData() {
        sendCodeTimeUtils = new SendCodeTimeUtils();
    }

    @Override
    protected SendCodePresent createPresent() {
        return new SendCodePresent(SendCodePresent.FIND_PASS);
    }

    @Override
    public void initComp() {
        ivPassword.setTag(true);
        ivRepeatPassword.setTag(true);
        btnConfirm.setEditTexts(edPhone, edSetPass, edRepeatPass);
        tvTitle.setText(getString(R.string.forget_password));
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_forget_password;
    }


    @OnClick(R.id.iv_menu)
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @OnClick({R.id.btn_confirm, R.id.iv_password, R.id.iv_repeat_password})
    public void onClick(View view) {
        switch (view.getId()) {
            //下一步按钮
            case R.id.btn_confirm:
                if (NetUtils.isNetworkAvalible(this)) {
                    if (TextUtils.isEmpty(edPhone.getText().toString())) {
//                        ToastUtils.showToast(getString(R.string.input_phone_email));
                        showToast(getString(R.string.input_phone_email));
                    } else if (TextUtils.isEmpty(edSetPass.getText().toString())) {
//                        ToastUtils.showToast(getString(R.string.password_hint));
                        showToast(getString(R.string.password_hint));
                    } else if (!RegexUtils.isPassword(edSetPass.getText().toString())) {
                        ToastUtils.showToast(getString(R.string.password_regex));
                        showToast(getString(R.string.password_regex));
                    } else if (TextUtils.isEmpty(edRepeatPass.getText().toString())) {
//                        ToastUtils.showToast(getString(R.string.please_repeat_pass));
                        showToast(getString(R.string.please_repeat_pass));
                    } else if (!edRepeatPass.getText().toString().equals(edSetPass.getText().toString())) {
//                        ToastUtils.showToast(getString(R.string.password_not_same));
                        showToast(getString(R.string.password_not_same));
                    } else if (RegexUtils.inputFilter(edPhone.getText().toString())
                            || RegexUtils.inputFilter(edSetPass.getText().toString())
                            || RegexUtils.inputFilter(edRepeatPass.getText().toString())) {
//                        ToastUtils.showToast(R.string.illegal_charaters);
                        showToast(getString(R.string.illegal_charaters));
                    } else {
                        if (sendCodeTimeUtils.needSend) {
                            mPresent.sendCode(edPhone.getText().toString());
                        } else {

                        }
                    }
                } else {
//                    ToastUtils.showToast(getString(R.string.net_error));
                    showToast(getString(R.string.net_error));
                }

                break;

            case R.id.iv_password:
                if ((boolean) ivPassword.getTag()) {
                    //明文
                    edSetPass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    ivPassword.setImageResource(R.mipmap.e_open);
                    ivPassword.setTag(false);
                    edSetPass.setSelection(edSetPass.getText().toString().length());
                } else {
                    //密文
                    edSetPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    ivPassword.setTag(true);
                    ivPassword.setImageResource(R.mipmap.e_close);
                    edSetPass.setSelection(edSetPass.getText().toString().length());
                }
                break;
            case R.id.iv_repeat_password:
                if ((boolean) ivRepeatPassword.getTag()) {
                    //明文
                    edRepeatPass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    ivRepeatPassword.setImageResource(R.mipmap.e_open);
                    ivRepeatPassword.setTag(false);
                    edRepeatPass.setSelection(edRepeatPass.getText().toString().length());
                } else {
                    //密文
                    edRepeatPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    ivRepeatPassword.setTag(true);
                    ivRepeatPassword.setImageResource(R.mipmap.e_close);
                    edRepeatPass.setSelection(edRepeatPass.getText().toString().length());
                }
                break;
        }
    }

    public void startCodeInputAcitivity() {
        Intent intent = new Intent(ForgetPasswordActivity.this, IdentifyCodeAcitvity.class);
        intent.putExtra("type", IdentifyCodeAcitvity.FORGET_PASSWORD);
        intent.putExtra("name", edPhone.getText().toString());
        intent.putExtra("pwd", edSetPass.getText().toString());
        startActivityForResult(intent, 0x1);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        sendCodeTimeUtils.reset();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x1 && resultCode == RESULT_OK) {
            finish();
        }
    }


    @Override
    public void sendCodeSuccess() {
        startCodeInputAcitivity();
        sendCodeTimeUtils.start();
    }

    @Override
    protected void setStatusBar() {
        super.setStatusBar();
        immersionBar
                .transparentStatusBar();
        immersionBar.statusBarDarkFont(true);
    }


}

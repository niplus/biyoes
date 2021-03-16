package com.biyoex.app.my.activity;

import android.content.Intent;
import androidx.appcompat.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.biyoex.app.BuildConfig;
import com.biyoex.app.R;
import com.biyoex.app.common.base.BaseAppCompatActivity;
import com.biyoex.app.common.data.SessionLiveData;
import com.biyoex.app.common.utils.ActivityManagerUtils;
import com.biyoex.app.common.utils.CryptUtils;
import com.biyoex.app.common.utils.SharedPreferencesUtils;
import com.biyoex.app.common.utils.ToastUtils;
import com.biyoex.app.common.utils.log.Log;
import com.biyoex.app.common.widget.PasswordDialog;
import com.biyoex.app.my.bean.ZGSessionInfoBean;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LG on 2017/6/9.
 * 安全中心
 */

public class RealNameCertificationActivity extends BaseAppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_real_name_certifcation)
    TextView tvRealNameCertifcation;
    @BindView(R.id.rl_layout_real_name_certifcation)
    RelativeLayout rlLayoutRealNameCertifcation;
    @BindView(R.id.tv_upload_documents)
    TextView tvUploadDocuments;
    @BindView(R.id.rl_layout_upload_documents)
    RelativeLayout rlLayoutUploadDocuments;
    @BindView(R.id.tv_reset_pwd)
    TextView tvResetPwd;
    //    @BindView(R.id.iv_gesture)
//    ImageView ivGesture;
    @BindView(R.id.sc_gesture)
    SwitchCompat scGesture;
    @BindView(R.id.tv_safety_rank)
    TextView tvSafety;
    @BindView(R.id.google_gesture)
    SwitchCompat googleGesture;
    private ZGSessionInfoBean sessionInfo;
    //    private FrontSessionBean frontSessionBean;
    private boolean isGestureOn;

    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener;

    @Override
    protected void initView() {
        tvTitle.setText(R.string.security_center);
        googleGesture.setOnCheckedChangeListener(this);
        onCheckedChangeListener = (buttonView, isChecked) -> {
            final PasswordDialog passwordDialog = new PasswordDialog(RealNameCertificationActivity.this, getString(R.string.safety_certification), getString(R.string.input_login_password));
            passwordDialog.setOnConfirmListener(pwd -> {
                if (TextUtils.isEmpty(pwd)) {
                    ToastUtils.showToast(getString(R.string.input_login_password));
                    return;
                }
                try {
                    String password = CryptUtils.decrypt(BuildConfig.salt, SharedPreferencesUtils.getInstance().getString("accessWord", ""));
                    if (pwd.equals(password)) {
                        if (isGestureOn) {
                            SharedPreferencesUtils.getInstance().setUserGestureSwitch(false);
                            isGestureOn = false;
                            ToastUtils.showToast(R.string.gesture_password_closed);
                        } else {
                            //不要触发switch 关闭
                            passwordDialog.setOnDismissListener(null);
                            Intent gestureIntent = new Intent(RealNameCertificationActivity.this, GestureSettingActivity.class);
                            startActivity(gestureIntent);
                        }
                    } else {
                        ToastUtils.showToast(getString(R.string.message_pass_error));
                    }

                    passwordDialog.dismiss();
                } catch (Exception e) {
                    Log.e(e, "gesture code setting");
                }
            });
            passwordDialog.setOnDismissListener(dialog -> setSwitchCheck(isGestureOn));
            passwordDialog.show();
        };
        scGesture.setOnCheckedChangeListener(onCheckedChangeListener);
    }

    @Override
    protected void initData() {
        SessionLiveData.getIns().observe(this, userinfoBean -> {
            if (userinfoBean != null) {
                sessionInfo = userinfoBean;
//                    frontSessionBean = userinfoBean.getFrontSessionBean();
                googleGesture.setOnCheckedChangeListener(null);
                googleGesture.setChecked(userinfoBean.isGoogleBind());
                googleGesture.setOnCheckedChangeListener(this);
                tvRealNameCertifcation.setTextColor(getResources().getColor(R.color.text_weak_light));
                if (sessionInfo.isAuth()) {
                    tvRealNameCertifcation.setTextColor(getResources().getColor(R.color.price_green));
                    tvRealNameCertifcation.setText(R.string.certified);
                } else {
                    tvRealNameCertifcation.setText(R.string.go_certify);
                }

                if (!sessionInfo.isAuthDeepPost() && !sessionInfo.isAuthDeep()) {
                    tvUploadDocuments.setText(R.string.go_upload);
                } else if (sessionInfo.isAuthDeepPost() && !sessionInfo.isAuthDeep()) {
                    tvUploadDocuments.setText(R.string.to_be_audited);
                } else if (sessionInfo.isAuthDeep()) {
                    tvUploadDocuments.setText(R.string.already_passed);
                    tvUploadDocuments.setTextColor(getResources().getColor(R.color.price_green));
                }
                if (sessionInfo.isSafeword()) {
                    tvResetPwd.setText(getString(R.string.edit_trade_password));
                } else {
                    tvResetPwd.setText(getString(R.string.set_trade_password));
                }
            }
            if (sessionInfo!=null&&sessionInfo.isAuth() && sessionInfo.isAuthDeep()) {
                if (isGestureOn) {
                    tvSafety.setText(R.string.security_level_high);
                } else {
                    tvSafety.setText(R.string.security_level_center);
                }
            } else {
                tvSafety.setText(R.string.security_level_weak);
            }
 //                tvBindGoogleStatus.setText(sessionInfo.isGoogleBind() ? getString(R.string.already_opened) : getString(R.string.go_opened));
//                tvBindGoogleStatus.setTextColor(getResources().getColor(sessionInfo.isGoogleBind() ? R.color.price_green : R.color.text_weak_light));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        isGestureOn = SharedPreferencesUtils.getInstance().isUserGestureOn();
        SessionLiveData.getIns().getSeesionInfo();
        setSwitchCheck(isGestureOn);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_real_name_certification;
    }


    @OnClick({R.id.iv_menu, R.id.rl_layout_real_name_certifcation, R.id.rl_layout_upload_documents,
            R.id.ll_bind_google, R.id.rl_layout_pay_password, R.id.rl_layout_login_password})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_menu:
                finish();
                break;
            case R.id.rl_layout_real_name_certifcation:
                if (sessionInfo != null) {
                    if (sessionInfo.isAuth()) {
                        ToastUtils.showToast(getString(R.string.certified));
                    } else {
                        Intent itCertificatesInfo2 = new Intent(this, CertificatesInfoActivity.class);
                        startActivity(itCertificatesInfo2);
                    }
                }
                break;

            case R.id.rl_layout_upload_documents:
                if (tvRealNameCertifcation.getText().toString().equals(getString(R.string.go_certify))) {
                    ToastUtils.showToast(getString(R.string.please_certify_first));
                } else {
                    if (tvUploadDocuments.getText().toString().equals(getString(R.string.go_upload))) {
                        Intent itNewsCertificateUpload = new Intent(this, NewsCertificateUploadActivity.class);
                        startActivity(itNewsCertificateUpload);
                    } else if (tvUploadDocuments.getText().toString().equals(getString(R.string.to_be_audited))) {
                        if (sessionInfo.isAuthDeepPost() && !sessionInfo.isAuthDeep()) {
                            Intent itNewsCertificateUpload = new Intent(this, NewsCertificateUploadActivity.class);
                            startActivity(itNewsCertificateUpload);
                        }
                    } else {
                        ToastUtils.showToast(getString(R.string.already_passed));
                    }
                }
                break;
            case R.id.ll_bind_google:
                Intent google = new Intent(this, BindGoogleActivity.class);
                startActivity(google);
                break;
            //修改支付密码
            case R.id.rl_layout_pay_password:
                //实名认证判断
                if (sessionInfo != null && sessionInfo.isAuth()) {
                    if (sessionInfo.isSafeword()) {
                        Intent itPersonalInfo = new Intent(this, RevisePayPasswordActivity.class);
                        itPersonalInfo.putExtra("revise", getString(R.string.modify));
                        startActivity(itPersonalInfo);
                    } else {
                        Intent itPersonalInfo = new Intent(this, RevisePayPasswordActivity.class);
                        itPersonalInfo.putExtra("revise", getString(R.string.setting));
                        startActivity(itPersonalInfo);
                    }
                } else {
                    Intent itCertificatesInfo = new Intent(this, CertificatesInfoActivity.class);
                    startActivity(itCertificatesInfo);
                }
                break;
            //修改登录密码
            case R.id.rl_layout_login_password:
                ActivityManagerUtils.getInstance().addActivity(this);
                Intent itReviseLoginPassword = new Intent(this, ReviseLoginPasswordActivity.class);
                startActivityForResult(itReviseLoginPassword, 0x1);
                break;
        }
    }

    private void setSwitchCheck(boolean isCheck) {
        scGesture.setOnCheckedChangeListener(null);
        scGesture.setChecked(isCheck);
        scGesture.setOnCheckedChangeListener(onCheckedChangeListener);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            startActivity(new Intent(this, BindGoogleActivity.class));
        } else {
            startActivity(new Intent(this, BindGoogleNextActivity.class).putExtra("tag", 1));
        }
    }
}

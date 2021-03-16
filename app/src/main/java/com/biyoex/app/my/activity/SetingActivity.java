package com.biyoex.app.my.activity;

import android.content.Intent;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.biyoex.app.R;
import com.biyoex.app.common.Constants;
import com.biyoex.app.common.activity.LoginActivity;
import com.biyoex.app.common.base.BaseAppCompatActivity;
import com.biyoex.app.common.data.SessionLiveData;
import com.biyoex.app.common.okhttp.OkHttpUtils;
import com.biyoex.app.common.okhttp.callback.StringCallback;
import com.biyoex.app.common.utils.ActivityManagerUtils;
import com.biyoex.app.common.utils.FileCacheUtils;
import com.biyoex.app.common.utils.SharedPreferencesUtils;
import com.biyoex.app.common.utils.log.Log;
import com.biyoex.app.common.widget.IconMessageDialog;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 设置
 * Created by LG on 2017/6/5.
 */

public class SetingActivity extends BaseAppCompatActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.tv_clean_cache)
    TextView tvCleanCache;
    @BindView(R.id.btn_confirm)
    TextView btnConfirm;

    @Override
    protected void initView() {
        tvTitle.setText("");
        try {
            tvCleanCache.setText(FileCacheUtils.getCacheSize(SetingActivity.this));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @OnClick(R.id.iv_menu)
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void initData() {
        ActivityManagerUtils.getInstance().addActivity(this);
        SessionLiveData.getIns().observe(this, userinfoBean -> {
            if (userinfoBean == null) {
                btnConfirm.setVisibility(View.GONE);
            } else {
                btnConfirm.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_seting;
    }

    @OnClick({R.id.rl_layout_clean_cache, R.id.btn_confirm
            , R.id.rl_layout_about_us,R.id.rl_layout_set_launcher})
    public void onClick(View view) {
        switch (view.getId()) {
            //清除缓存
            case R.id.rl_layout_clean_cache:
                new IconMessageDialog.Builder(this)
                        .setTitle(R.string.hint_message)
                        .setContent(R.string.confirm_clean_cache)
                        .setNegativeMessage(R.string.cancel)
                        .setPositiveMessage(R.string.confirm)
                        .setNegativeButtonListener((dialog, which) -> dialog.dismiss())
                        .setPositiveButtonListener((dialog, which) -> {
                            dialog.dismiss();
                            FileCacheUtils.cleanInternalCache(getApplicationContext());
                            FileCacheUtils.cleanExternalCache(getApplicationContext());
                            try {
                                tvCleanCache.setText(FileCacheUtils.getCacheSize(SetingActivity.this));
//                                    ToastUtils.showToast(getString(R.string.clean_cache_success));
                                showToast(getString(R.string.clean_cache_success));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        })
                        .build()
                        .show();

                break;
            case R.id.rl_layout_set_launcher:
//                changeAppLanguage(Locale.US);
                    startActivity(new Intent(this,ChooseLanguageActivity.class));
                break;
            //退出登录
            case R.id.btn_confirm:
                new IconMessageDialog.Builder(this)
                        .setTitle(R.string.hint_message)
                        .setContent(R.string.confirm_logout)
                        .setNegativeMessage(R.string.cancel)
                        .setPositiveMessage(R.string.confirm)
                        .setNegativeButtonListener((dialog, which) -> dialog.dismiss())
                        .setPositiveButtonListener((dialog, which) -> {
                            showLoadingDialog();
                            requestLogOut();
                            dialog.dismiss();
                        })
                        .build()
                        .show();
                break;
            //关于我们
            case R.id.rl_layout_about_us:
                Intent itAppEdition = new Intent(this, AppEditionActivity.class);
                startActivity(itAppEdition);
                break;
        }
    }

    public void requestLogOut() {
        Log.i("requestLogOut");
        OkHttpUtils
                .post()
                .addHeader("X-Requested-With", "XMLHttpReques")
                .url(Constants.BASE_URL + "v1/logout")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(e, "logout");
//                        ToastUtils.showToast(getString(R.string.logout_failed));
                        showToast(getString(R.string.logout_failed));
                        hideLoadingDialog();
                    }

                    @Override
                    public void onResponse(String response, int id) throws JSONException {
                        String code = new JSONObject(response).getString("code");
                        Log.i("requestLogOut result :" + code);
                        if (code.equals("200")) {
                            SharedPreferencesUtils.getInstance().saveData("JSESSIONID", "");
                            SharedPreferencesUtils.getInstance().saveData("route", "");
                            SharedPreferencesUtils.getInstance().saveString("accessWord", "");
                            SessionLiveData.getIns().postValue(null);
                            hideLoadingDialog();
                            Intent itLogin = new Intent(SetingActivity.this, LoginActivity.class);
                            itLogin.putExtra("type", "info");
                            startActivity(itLogin);
                            finish();
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0x1 && resultCode == RESULT_OK) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}

package com.biyoex.app.my.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.biyoex.app.R;
import com.biyoex.app.VBTApplication;
import com.biyoex.app.common.Constants;
import com.biyoex.app.common.activity.WebPageLoadingActivity;
import com.biyoex.app.common.base.BaseActivity;
import com.biyoex.app.common.okhttp.OkHttpUtils;
import com.biyoex.app.common.okhttp.callback.GenericsCallback;
import com.biyoex.app.common.okhttp.callback.JsonGenericsSerializator;
import com.biyoex.app.common.utils.MobilePhoneDeviceInfo;
import com.biyoex.app.common.widget.UpdateDialog;
import com.biyoex.app.home.bean.UploadApkBean;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by LG on 2017/6/26.
 */

public class AppEditionActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(R.id.tv_versionname)
    TextView tvVersionName;
    //版本号
    private String strVersionName;


    private UploadApkBean mUploadApkBean;
    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        strVersionName = MobilePhoneDeviceInfo.getVersionName(this);
        tvVersionName.setText(strVersionName);
        tvTitle.setText("");
        getAppVersion();
    }

    private void getAppVersion() {
        showLoadingDialog();
        OkHttpUtils
                .get()
                .url(VBTApplication.appDownloadUrl)
                .build()
                .execute(new GenericsCallback<UploadApkBean>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        hideLoadingDialog();
                        //ToastUtils.showToast(e.toString());
                    }

                    @Override
                    public void onResponse(UploadApkBean response, int id) {
                        hideLoadingDialog();
                        mUploadApkBean=response;
                        if (!strVersionName.equals(response.getVersion())) {
//                        tvVersionName.setCompoundDrawables(getDrawable(R.drawable.circle_red),null,null,null);
//                        tvVersionName.setCompoundDrawablesRelative(getDrawable(R.drawable.circle_red),null,null,null);
                        tvVersionName.setText(response.getVersion());
                        tvVersionName.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.drawable.circle_red),null,null,null);
                        }
                    }
                });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_app_edition;
    }

    @OnClick({R.id.iv_menu, R.id.rl_latest_version,R.id.tv_help,R.id.tv_private})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_menu:
                finish();
                break;
            case R.id.rl_latest_version:
            if(mUploadApkBean!=null&&mUploadApkBean.getVersion().equals(strVersionName)){
                showToast("您已是最新版本");
            }
            else{
                if(mUploadApkBean!=null){
                    //当前版本小于强制更新不版本则需要强制更新
                    UpdateDialog updateDialog = new UpdateDialog(AppEditionActivity.this, false, mUploadApkBean);
                    updateDialog.show();
                }
            }
                break;
            case R.id.tv_help:
                Intent intent = new Intent(this, WebPageLoadingActivity.class);
                intent.putExtra("url", Constants.HELP_URL);
                intent.putExtra("type","url");
                intent.putExtra("title",getString(R.string.help_center));
                startActivity(intent);
                break;
            case R.id.tv_private:
                Intent intent1 = new Intent(this, WebPageLoadingActivity.class);
                intent1.putExtra("url", "https://biyoex.com/mobile/#/detail?type=6&id=2");
                intent1.putExtra("type","url");
                intent1.putExtra("title","隐私政策");
                startActivity(intent1);
                break;
        }
    }


    /**
     * 检查版本更新
     */
    public void requestUploadApk(){

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}

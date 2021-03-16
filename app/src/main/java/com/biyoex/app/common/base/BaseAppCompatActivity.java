package com.biyoex.app.common.base;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.IBinder;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.gyf.barlibrary.ImmersionBar;
import com.v.StartActivity;
import com.biyoex.app.VBTApplication;
import com.biyoex.app.common.utils.DialogUtils;
import com.biyoex.app.common.utils.LanguageUtils;
import com.biyoex.app.common.utils.SharedPreferencesUtils;
import com.biyoex.app.common.utils.log.Log;

import butterknife.ButterKnife;


/**
 * Activity的基础类
 * Created by LG on 2017/3/7.
 */

public abstract class BaseAppCompatActivity extends AppCompatActivity {

    public ImmersionBar immersionBar;

    private Dialog loadingDialog;

    /**
     * 初始化视图
     */
    protected abstract void initView();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 加载布局文件
     *
     * @return
     */
    protected abstract int getLayoutId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
        immersionBar.init();
        immersionBar.statusBarDarkFont(true);
//        Log.i("APP status : " + VBTApplication.APP_STATUS);

        //如果應用在后台被杀死了，则重新从启动界面进入。
        if (VBTApplication.APP_STATUS != 1) {
            Intent intent = new Intent(this, StartActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            Log.i("activity start from killed");
        }
        setContentView(getLayoutId());
        //注解绑定
        ButterKnife.bind(this);
        initData();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void showLoadingDialog() {
        if (loadingDialog == null) {
            loadingDialog = DialogUtils.getLoadDialog(this);
        }
        loadingDialog.show();
    }

    public void hideLoadingDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }


    /**
     * 隐藏键盘
     */
    public void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            View localView = getCurrentFocus();
            if (localView != null && localView.getWindowToken() != null) {
                IBinder windowToken = localView.getWindowToken();
                inputMethodManager.hideSoftInputFromWindow(windowToken, 0);
            }
        }
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config=new Configuration();
        config.setToDefaults();
        config.locale = LanguageUtils.getLocale();
        res.updateConfiguration(config,res.getDisplayMetrics());
//        android.util.Log.d("baseAppCompat","getResources");
        return res;
    }
    /**
     * 显示键盘
     */
    public void showSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            View localView = getCurrentFocus();
            if (localView != null && localView.getWindowToken() != null) {
                inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    protected void setStatusBar() {
        immersionBar = ImmersionBar.with(this);
        //适配黑夜模式 判断是否
        String color = SharedPreferencesUtils.getInstance().getString("color", "0");
        immersionBar.statusBarDarkFont(color.equals("0"));
    }

    protected void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImmersionBar.with(this).destroy();
        hideLoadingDialog();
    }

}

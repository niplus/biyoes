package com.biyoex.app.common.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.gyf.barlibrary.ImmersionBar;
import com.v.StartActivity;
import com.biyoex.app.R;
import com.biyoex.app.VBTApplication;
import com.biyoex.app.common.okhttp.OkHttpUtils;
import com.biyoex.app.common.utils.DeviceUtils;
import com.biyoex.app.common.utils.DialogUtils;
import com.biyoex.app.common.utils.LanguageUtils;
import com.biyoex.app.common.utils.SharedPreferencesUtils;
import com.biyoex.app.common.utils.log.Log;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Activity的基础类
 * Created by LG on 2017/3/7.
 */

public abstract class BaseActivity extends AppCompatActivity {

    public ImmersionBar immersionBar;
    private Dialog loadingDialog;
    protected Activity context;

    /**
     * 加载布局文件
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 初始化数据
     */
    protected abstract void initData();


    /**
     * 初始化视图
     */
    protected abstract void initView();


    private Unbinder unbinder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setStatusBar();
        immersionBar.init();
        //如果應用在后台被杀死了，则重新从启动界面进入。
        if (VBTApplication.APP_STATUS != 1) {
            Intent intent = new Intent(this, StartActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            Log.i("activity start from killed");
        }

        setContentView(getLayoutId());
        //注解绑定
        unbinder = ButterKnife.bind(this);
        initData();
        initView();
    }

    protected int dip2px(int dp) {//dp转成px
        return DeviceUtils.dip2px(context, dp);
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
    protected void setStatusBar() {
        immersionBar = ImmersionBar
                .with(this);
        String color = SharedPreferencesUtils.getInstance().getString("color", "0");
        immersionBar.statusBarDarkFont(true);
    }

    protected void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void showNormalDialog(String title,String message){
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        Dialog dialog = new Dialog(this);

        View view = LayoutInflater.from(this).inflate(R.layout.layout_base_dialog,null);
        dialog.setContentView(view);
        WindowManager.LayoutParams attributes = dialog.getWindow().getAttributes();
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
        attributes.gravity = Gravity.CENTER;
        // 一定要重新设置, 才能生效
        dialog.getWindow().setAttributes(attributes);
        TextView mTextView =  view.findViewById(R.id.tv_dialog_title);
        TextView mMessageTv = view.findViewById(R.id.tv_dialog_pos_tv);
        mMessageTv.setText(message);
        mTextView.setText(title);
        view.findViewById(R.id.tv_dialog_sure).setOnClickListener(view1 -> {
            dialog.dismiss();
            setDialogOnClick();
        });
        view.findViewById(R.id.tv_dialog_cancel).setOnClickListener(view1 ->
                dialog.dismiss()
        );
        dialog.show();
//        final AlertDialog.Builder normalDialog =
//                new AlertDialog.Builder(this);
//        normalDialog.setTitle(title);
//        normalDialog.setView(view);
//
//        normalDialog.setView(view);
//        normalDialog.setPositiveButton(R.id.tv_go_market,(dialog, which) -> {
//            dialog.dismiss();
//            setDialogOnClick();
//        });
//        normalDialog.setPositiveButton(getString(R.string.sure),
//                (dialog, which) -> {
//            dialog.dismiss();
//                setDialogOnClick();
//                });
//        normalDialog.setNegativeButton(R.string.cancel,
//                (dialog, which) -> {
//            dialog.dismiss();
//                });
//        // 显示
//        normalDialog.show();
    }

    public void setDialogOnClick(){

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImmersionBar.with(this).destroy();
        hideLoadingDialog();
        OkHttpUtils.getInstance().cancelTag(this);
    }


}

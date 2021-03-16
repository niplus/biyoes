package com.biyoex.app.common.mvpbase;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import com.gyf.barlibrary.ImmersionBar;
import com.v.StartActivity;
import com.biyoex.app.R;
import com.biyoex.app.VBTApplication;
import com.biyoex.app.common.activity.LoginActivity;
import com.biyoex.app.common.utils.DialogUtils;
import com.biyoex.app.common.utils.LanguageUtils;
import com.biyoex.app.common.utils.log.Log;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity<T extends BasePresent> extends AppCompatActivity implements BaseView {
    private Unbinder unbinder;

    protected T mPresent;

    public ImmersionBar immersionBar;

    private Dialog loadingDialog;
    protected int page = 1;
    protected int pageSize = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
        immersionBar.init();
        immersionBar.statusBarDarkFont(true);
        //如果應用在后台被杀死了，则重新从启动界面进入。
        if (VBTApplication.APP_STATUS != 1) {
            Intent intent = new Intent(this, StartActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            Log.i("activity start from killed");
        }
        setContentView(getLayoutId());
        unbinder = ButterKnife.bind(this);
        mPresent = createPresent();

        if (mPresent != null) {
            mPresent.attachView(this);
            mPresent.getLifeCycle(this);
        }
        initData();
        initComp();
    }


    public abstract int getLayoutId();

    protected abstract T createPresent();

    @Override
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void showToast(@StringRes int msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoadingDialog() {
        if (loadingDialog == null) {
            loadingDialog = DialogUtils.getLoadDialog(this);
        }
        loadingDialog.show();
    }

    @Override
    public void hideLoadingDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    public void showNormalDialog(String title, String message) {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        Dialog dialog = new Dialog(getContext());
        View view = LayoutInflater.from(this).inflate(R.layout.layout_base_dialog, null);
        dialog.setContentView(view);
        WindowManager.LayoutParams attributes = dialog.getWindow().getAttributes();
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
        attributes.gravity = Gravity.CENTER;
        // 一定要重新设置, 才能生效
        dialog.getWindow().setAttributes(attributes);
        TextView mTextView = view.findViewById(R.id.tv_dialog_title);
        TextView mMessageTv = view.findViewById(R.id.tv_dialog_pos_tv);
        mMessageTv.setText(message);
        mTextView.setText("尊敬的用户");
        view.findViewById(R.id.tv_dialog_sure).setOnClickListener(view1 -> {
            dialog.dismiss();
            setDialogOnClick();
        });
        view.findViewById(R.id.tv_dialog_cancel).setOnClickListener(view1 -> {
                    dialog.dismiss();
                    dialogCancel();
                }
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

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        config.locale = LanguageUtils.getLocale();
        res.updateConfiguration(config, res.getDisplayMetrics());
//        android.util.Log.d("baseAppCompat","getResources");
        return res;
    }

    @Override
    public Context getContext() {
        return this;
    }

    public abstract void initComp();

    public abstract void initData();

    public void setDialogOnClick() {

    }

    public void dialogCancel() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
        if (mPresent != null) {
            mPresent.detachView();
            mPresent.unBindLifeCycle();
        }

        hideLoadingDialog();

        ImmersionBar.with(this).destroy();
    }

    protected void setStatusBar() {
        immersionBar = ImmersionBar
                .with(this);
//        String color = SharedPreferencesUtils.getInstance().getString("color", "0");
        immersionBar.statusBarDarkFont(true);
    }

    /**
     * 跳转activity 带bundle
     */
    protected void openActivity(Class activity, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, activity);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    protected void openActivity(Class activity) {
        Intent intent = new Intent();
        intent.setClass(this, activity);
        startActivity(intent);
    }

    /**
     * @param type 1.no data 2. no login 3.bad network 4.no resource
     */
    public View getEmptyView(int type) {
        View emptyView = LayoutInflater.from(this).inflate(R.layout.layout_no_data, null);
        ImageView imageView = emptyView.findViewById(R.id.iv_message);
        TextView tvMessage = emptyView.findViewById(R.id.tv_message);
        TextView tvClick = emptyView.findViewById(R.id.tv_refresh);
        switch (type) {
            case 1:
                tvMessage.setVisibility(View.VISIBLE);
                imageView.setImageResource(R.mipmap.icon_empty);
                tvMessage.setText(getString(R.string.no_data));
                break;
            case 2:
                imageView.setImageResource(R.mipmap.icon_empty);
                tvMessage.setVisibility(View.GONE);
                tvClick.setText(getString(R.string.login));
                tvClick.setOnClickListener(v -> {
                    Intent loginIntent = new Intent(BaseActivity.this, LoginActivity.class);
                    startActivity(loginIntent);
                });
                break;
            case 3:
                imageView.setImageResource(R.mipmap.error_404);
                tvMessage.setText(getString(R.string.net_error));
                break;
            case 4:
                imageView.setImageResource(R.mipmap.no_resource);
                tvMessage.setText(getString(R.string.no_data));
                break;
        }

        return emptyView;
    }

    @Override
    public void httpError() {
        hideLoadingDialog();
    }
}

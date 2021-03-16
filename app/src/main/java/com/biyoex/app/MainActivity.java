package com.biyoex.app;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Lifecycle;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;

import android.provider.Settings;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;
import com.biyoex.app.common.Constants;
import com.biyoex.app.common.base.BaseAppCompatActivity;
import com.biyoex.app.common.data.SessionLiveData;
import com.biyoex.app.common.http.RetrofitHelper;
import com.biyoex.app.common.mvpbase.BaseObserver;
import com.biyoex.app.common.service.UpdateApkService;
import com.biyoex.app.common.utils.ActivityManagerUtils;
import com.biyoex.app.common.utils.CommonUtil;
import com.biyoex.app.common.utils.MobilePhoneDeviceInfo;
import com.biyoex.app.common.utils.ToastUtils;
import com.biyoex.app.common.utils.log.Log;
import com.biyoex.app.common.widget.UpdateDialog;
import com.biyoex.app.home.bean.UploadApkBean;
import com.biyoex.app.home.fragment.HomeFragment;
import com.biyoex.app.market.fragment.MarketFragment;
import com.biyoex.app.my.bean.ZGSessionInfoBean;
import com.biyoex.app.my.fragment.MyFragment;
import com.biyoex.app.property.fragment.PropertyFragment;
import com.biyoex.app.trading.fragmnet.TradingFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.uber.autodispose.AutoDispose.autoDisposable;


public class MainActivity extends BaseAppCompatActivity {
    //    @BindView(R.id.tab_navigation)
//    TabLayout tabNavigation;
    @BindView(R.id.main_bottom)
    BottomNavigationBar mainBottom;
    @BindView(R.id.main_layout)
    FrameLayout mFrameLayout;
    private boolean isKeycodeBack = true;
    //版本号
    private String strVersionName;
    private UploadApkBean mUploadApkBean;
    private List<Fragment> mFragmentList;
    private HomeFragment homeFragment;
    private TradingFragment mTradingFragment;
    private MyFragment mMyFragment;
    private int lastIndex;
    private ZGSessionInfoBean sessionInfo;
    private String filePath;
    BroadcastMain receiver;
    Intent intent = new Intent();
    //内部类，实现BroadcastReceiver
    public class BroadcastMain extends BroadcastReceiver {
        //必须要重载的方法，用来监听是否有广播发送
        @Override
        public void onReceive(Context context, Intent intent) {
             filePath = intent.getStringExtra(UpdateApkService.UPDATE_APK_FILE);
         if(Objects.requireNonNull(filePath).isEmpty()){
             VBTApplication.hasCheckVersion = false;
             Toast.makeText(MainActivity.this, "请开启VB的未知应用安装权限", Toast.LENGTH_LONG).show();
             @SuppressLint("InlinedApi") Intent intentPermission = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
             intentPermission.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
             startActivity(intentPermission);
         }
         else {
             installProcess();
         }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManagerUtils.getInstance().addActivity(this);
        EventBus.getDefault().register(this);
        receiver = new BroadcastMain();
        //新添代码，在代码中注册广播接收程序
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.updateapk");
        registerReceiver(receiver, filter);
//        Log.i("main activity onCreate");
//        immersionBar.statusBarDarkFont(true);
    }

    @Override
    protected void initView() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ActivityManagerUtils.getInstance().setMainActivity(this);
        initViewPagerAndTabLayout();
        if ("restart".equals(getIntent().getStringExtra("mode"))) {
//            vpMainContainer.setCurrentItem(4);
        } else {
            if (!VBTApplication.hasCheckVersion) {
                //版本更新
                requestUploadApk();
                VBTApplication.hasCheckVersion = true;
            }
        }
        strVersionName = MobilePhoneDeviceInfo.getVersionName(this);
        getUserSelectCoin();

        if(getIntent().getIntExtra("isToMarket",0)==1){
                turnPage(2);
        }
    }

    private void getUserSelectCoin() {
        RetrofitHelper.getIns().getZgtopApi().getUserSelfToken()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void success(String s) {

                    }
                });
    }

    /**
     * 初始化viewpager加tab
     */
    public void initViewPagerAndTabLayout() {
        mainBottom.setMode(BottomNavigationBar.MODE_FIXED)
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC)
                .setInActiveColor(R.color.color_home_ddd)
                .setActiveColor(R.color.color_home_tab)
                .addItem(new BottomNavigationItem(R.mipmap.ico_home_select,getString(R.string.home_page)).setInactiveIcon(ContextCompat.getDrawable(this, R.mipmap.ico_home_normal)))
                .addItem(new BottomNavigationItem(R.mipmap.icon_market_select, getString(R.string.market)).setInactiveIcon(ContextCompat.getDrawable(this, R.mipmap.icon_market_normal)))
                .addItem(new BottomNavigationItem(R.mipmap.icon_trade_select, getString(R.string.trade)).setInactiveIcon(ContextCompat.getDrawable(this, R.mipmap.icon_trade_normal)))
                .addItem(new BottomNavigationItem(R.mipmap.icon_property_select, getString(R.string.property)).setInactiveIcon(ContextCompat.getDrawable(this, R.mipmap.icon_property_normal)))
                .addItem(new BottomNavigationItem(R.mipmap.icon_mine_select, getString(R.string.my)).setInactiveIcon(ContextCompat.getDrawable(this, R.mipmap.icon_mine)))
                .initialise();
        mainBottom.hide();
        mainBottom.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                selectFragment(position);
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        }
        );
        CommonUtil.setBottomNavigationItem(mainBottom, 6, 26, 10);
    }

    private void selectFragment(int position) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        Fragment currentFragment = mFragmentList.get(position);
        Fragment lastFragment = mFragmentList.get(lastIndex);
        lastIndex = position;
        ft.hide(lastFragment);
        if (!currentFragment.isAdded()) {
//            getSupportFragmentManager().beginTransaction().remove(currentFragment).commitAllowingStateLoss();
            ft.add(R.id.main_layout, currentFragment);
        }
        ft.show(currentFragment);
        ft.commitAllowingStateLoss();
    }

    @Override
    protected void initData() {
        homeFragment = new HomeFragment();
        mTradingFragment = new TradingFragment();
        mMyFragment = new MyFragment();
        mFragmentList = new ArrayList<>();
        mFragmentList.add(homeFragment);
        mFragmentList.add(mTradingFragment);
        mFragmentList.add(new MarketFragment());
        mFragmentList.add(new PropertyFragment());
        mFragmentList.add(mMyFragment);
        SessionLiveData.getIns().observe(this, userinfoBean -> sessionInfo = userinfoBean);
        selectFragment(0);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

        /**
         * 检查版本更新
         */
        public void requestUploadApk() {
        RetrofitHelper
                .getIns()
                .getZgtopApi()
                .checkVersion()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(autoDisposable(AndroidLifecycleScopeProvider.from(this, Lifecycle.Event.ON_DESTROY)))
                .subscribe(new BaseObserver<UploadApkBean>() {
                    @Override
                    protected void success(UploadApkBean uploadApkBean) {
                        mUploadApkBean = uploadApkBean;
                        android.util.Log.i("测试一下", "success: ");
                        if (!strVersionName.equals(uploadApkBean.getVersion())) {
                            //当前版本小于强制更新不版本则需要强制更新
                            boolean needForcedUpdate = MobilePhoneDeviceInfo.getVersionCode(MainActivity.this) < uploadApkBean.getForceVersionCode();
                            Log.i("diff version need force update" + needForcedUpdate);
                            UpdateDialog updateDialog = new UpdateDialog(MainActivity.this, needForcedUpdate, mUploadApkBean);
                            updateDialog.show();
                        }
                    }
                    @Override
                    protected void failed(int code, String data, String msg) {
                        super.failed(code, data, msg);
                    }
                });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new Handler().postDelayed(() -> isKeycodeBack = true, 2000);
            if (isKeycodeBack) {
                ToastUtils.showToast(getResources().getString(R.string.exit_app));
                isKeycodeBack = false;
            } else {
                ActivityManagerUtils.getInstance().finishAllActivity();
                finish();
            }
        }
        return true;
    }

    public void turnPage(int page) {
//        vpMainContainer.setCurrentItem(page, false);
        mainBottom.selectTab(page);
//        selectFragment(page);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mainBottom.getCurrentSelectedPosition() == 3) {
            if (sessionInfo == null) {
                mainBottom.selectTab(4);
            }
        }
        if (!VBTApplication.hasCheckVersion) {
            //版本更新
            requestUploadApk();
            VBTApplication.hasCheckVersion = true;
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void restart(Integer eventCode) {
        if (eventCode == Constants.RESTART_EVENT) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManagerUtils.getInstance().removeActivity(this);
        EventBus.getDefault().unregister(this);
        unregisterReceiver(receiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        android.util.Log.d("权限申请", resultCode+"onActivityResult: "+requestCode);
        if (resultCode == RESULT_CANCELED && requestCode == 10086) {
            installProcess();
        }
    }
    private void installProcess() {
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //intent.setDataAndType(Uri.fromFile(myTempFile), "application/vnd.android.package-archive");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(MainActivity.this, BuildConfig.APPLICATION_ID + ".fileProvider", new File(filePath));
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Uri packageURI = Uri.parse("package:"+getPackageName());
                boolean hasInstallPermission = getPackageManager().canRequestPackageInstalls();
                if (!hasInstallPermission) {
                    Toast.makeText(this, "请开启VB的未知应用安装权限", Toast.LENGTH_SHORT).show();
                    Intent intentPermission = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                    intentPermission.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivityForResult(intentPermission,10086);
                    return;
                }
            }
        } else {
            intent.setDataAndType(Uri.fromFile(new File(filePath)), "application/vnd.android.package-archive");
        }
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
//        showNotification(this.getString(R.string.downloadDone) + "100%", pendingIntent, this.getString(R.string.downloadDone));
         startActivity(intent);
    }
    @Override
    protected void setStatusBar() {
        super.setStatusBar();
        immersionBar.transparentNavigationBar();
        immersionBar.statusBarDarkFont(true);
    }



    @Override
    public void onSaveInstanceState(@NotNull Bundle outState, @NotNull PersistableBundle outPersistentState) {
//        super.onSaveInstanceState(outState, outPersistentState);
    }
}

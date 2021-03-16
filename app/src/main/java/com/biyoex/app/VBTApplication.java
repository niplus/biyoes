package com.biyoex.app;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import androidx.appcompat.app.AppCompatDelegate;

import android.os.StrictMode;
import android.text.TextUtils;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.view.CropImageView;
import com.tencent.bugly.Bugly;
//import com.tencent.tinker.entry.ApplicationLike;
//import com.tencent.tinker.lib.listener.DefaultPatchListener;
//import com.tencent.tinker.lib.patch.UpgradePatch;
//import com.tencent.tinker.lib.reporter.DefaultLoadReporter;
//import com.tencent.tinker.lib.reporter.DefaultPatchReporter;
//import com.tencent.tinker.lib.service.PatchResult;
//import com.tinkerpatch.sdk.TinkerPatch;
//import com.tinkerpatch.sdk.loader.TinkerPatchApplicationLike;
//import com.tinkerpatch.sdk.server.callback.ConfigRequestCallback;
//import com.tinkerpatch.sdk.server.callback.RollbackCallBack;
//import com.tinkerpatch.sdk.server.callback.TinkerPatchRequestCallback;
//import com.tinkerpatch.sdk.tinker.callback.ResultCallBack;
//import com.tinkerpatch.sdk.tinker.service.TinkerServerResultService;
import com.biyoex.app.common.http.RetrofitHelper;
import com.biyoex.app.common.okhttp.OkHttpUtils;
import com.biyoex.app.common.utils.FontCustom;
import com.biyoex.app.common.utils.GlideImageLoader;

import com.biyoex.app.common.utils.MobilePhoneDeviceInfo;
import com.biyoex.app.common.utils.SharedPreferencesUtils;
import com.biyoex.app.common.utils.log.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import me.jessyan.autosize.AutoSizeConfig;

/**
 * Created by LG on 2017/5/16.
 */

public class VBTApplication extends Application {

    private static Context context;

    /**
     * 根据后期服务器图片地址修改，故用此域名访问。
     */
    public final static String appPictureUrl = "https://biyocn.oss-accelerate.aliyuncs.com/";

    /**
     * app下载地址
     */
    public final static String appDownloadUrl = "https://biyocn.oss-accelerate.aliyuncs.com/download/android/version.json";

    /**
     *
     * 身份证银行卡4要素
     */
    public final static String checkBankAndName = "http://verifycard.market.alicloudapi.com/Verification4";
    /**
     * 银行卡4要素 APPCODE
     * */
    public final static String APPCode = "08d019d04b73401db72c91e1b910171c";

    /**
     * rjax请求的key
     */
    public static String RequestedWith = "X-Requested-With";
    /**
     * rjax请求的val
     */
    public static String XmlHttpRequest = "XMLHttpRequest";

    public static int RISE_COLOR;
    public static int FALL_COLOR;

//    public static Thread.UncaughtExceptionHandler uncaughtExceptionHandler;

    /**
     * app是否正常启动 1 是正常启动
     */
    public static int APP_STATUS = 0;

    public static boolean ISDEBUG = false;

    public static boolean hasCheckVersion = false;
    private static final String TAG = "Tinker.SampleApppatch";

//    private ApplicationLike tinkerApplicationLike;
    @Override
    public void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //you must install multiDex whatever tinker is installed!
        //MultiDex.install(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        ISDEBUG = isDebug();

        //日志框架初始化
        Log.init();
        new FontCustom().replaceSystemDefaultFont(this, "fonts/Roboto-Regular.ttf");
        //打印应用及设备信息
        Log.i("===============================================\n" +
                "=                    V" + MobilePhoneDeviceInfo.getVersionName(this) + "                   =\n" +
                "=                  versionCode :" + MobilePhoneDeviceInfo.getVersionCode(this) + "             =\n" +
                "===============================================");
        AutoSizeConfig.getInstance()
                //是否让框架支持自定义 Fragment 的适配参数, 由于这个需求是比较少见的, 所以须要使用者手动开启
                //如果没有这个需求建议不开启
                .setExcludeFontScale(true)
                .setUseDeviceSize(true);
        //是否屏蔽系统字体大小对 AndroidAutoSize 的影响, 如果为 true, App 内的字体的大小将不会跟随系统设置中字体大小的改变
        //如果为 false, 则会跟随系统设置中字体大小的改变, 默认为 false
//
        RISE_COLOR = getResources().getColor(R.color.price_green);
        FALL_COLOR = getResources().getColor(R.color.price_red);
        context = getApplicationContext();
        SharedPreferencesUtils.init(getApplicationContext());
        //初始化okhttp
        OkHttpUtils.initClient(RetrofitHelper.getIns().getOkHttpClient());
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setCrop(false);        //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(false); //是否按矩形区域保存
        imagePicker.setSelectLimit(9);    //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);   //裁剪框的宽度。单 位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);//保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);//保存文件的高度。单位像素

        /*--------------集成bugly-----------------*/
        Context context = getApplicationContext();
//        // 获取当前包名
//        String packageName = context.getPackageName();
//        // 获取当前进程名
//        String processName = getProcessName(android.os.Process.myPid());
//        // 设置是否为上报进\
//        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
//        strategy.setUploadProcess(processName == null || processName.equals(packageName));
//         初始化Bugly
//        CrashReport.initCrashReport(context, "c23a4d28d6", false);
        Bugly.init(getApplicationContext(),  "c23a4d28d6", true);

//        LanguageUtils.setCurrentLanguage(SharedPreferencesUtils.getInstance().getString("language","us"));
//        UMConfigure.init(this, "5b48873db27b0a0ffc0000fe"
//                , "zgtop", UMConfigure.DEVICE_TYPE_PHONE, "");
//        PlatformConfig.setWeixin("wxf3aae87b7dad998b", "5be1c79be6527c23bc5df308f94fe780");
//        PlatformConfig.setQQZone("101485607", "8babd91cd5d6ed68be1a4fef731406c6");
//        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        SharedPreferencesUtils.getInstance().saveString("color","0");
        String color = SharedPreferencesUtils.getInstance().getString("color", "0");//判断是夜间还是白天 0=白天  1=  夜间
        AppCompatDelegate.setDefaultNightMode(color.equals("0") ? AppCompatDelegate.MODE_NIGHT_NO : AppCompatDelegate.MODE_NIGHT_NO);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
//
//        initTinkerPatch();
    }
    
    public boolean isDebug() {
        ApplicationInfo info = getApplicationInfo();

        return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
    }


    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }


    public static Context getContext() {
        if (context != null) {
            return context;
        } else {
            throw new NullPointerException("the context is null,please init App in Application first.");
        }
    }

    /**
     * 我们需要确保至少对主进程跟patch进程初始化 TinkerPatch
     */
//    private void initTinkerPatch() {
//        // 我们可以从这里获得Tinker加载过程的信息
//        if (BuildConfig.TINKER_ENABLE) {
//            tinkerApplicationLike = TinkerPatchApplicationLike.getTinkerPatchApplicationLike();
//            // 初始化TinkerPatch SDK
//            TinkerPatch.init(
//                    tinkerApplicationLike
////                new TinkerPatch.Builder(tinkerApplicationLike)
////                    .requestLoader(new OkHttp3Loader())
////                    .build()
//            )
//                    .reflectPatchLibrary()
//                    .setPatchRollbackOnScreenOff(true)
//                    .setPatchRestartOnSrceenOff(true)
//                    .setFetchPatchIntervalByHours(3)
//            ;
//            TinkerPatch.with().fetchPatchUpdate(true);
//            // 获取当前的补丁版本
//            android.util.Log.d(TAG, "Current patch version is " + TinkerPatch.with().getPatchVersion());
//
//            // fetchPatchUpdateAndPollWithInterval 与 fetchPatchUpdate(false)
//            // 不同的是，会通过handler的方式去轮询
//            TinkerPatch.with().fetchPatchUpdateAndPollWithInterval();
//        }
//    }

//    /**
//     * 在这里给出TinkerPatch的所有接口解释
//     * 更详细的解释请参考:http://tinkerpatch.com/Docs/api
//     */
//    private void useSample() {
//        TinkerPatch.init(tinkerApplicationLike)
//                //是否自动反射Library路径,无须手动加载补丁中的So文件
//                //注意,调用在反射接口之后才能生效,你也可以使用Tinker的方式加载Library
//                .reflectPatchLibrary()
//                //向后台获取是否有补丁包更新,默认的访问间隔为3个小时
//                //若参数为true,即每次调用都会真正的访问后台配置
//                .fetchPatchUpdate(false)
//                //设置访问后台补丁包更新配置的时间间隔,默认为3个小时
//                .setFetchPatchIntervalByHours(3)
//                //向后台获得动态配置,默认的访问间隔为3个小时
//                //若参数为true,即每次调用都会真正的访问后台配置
//                .fetchDynamicConfig(new ConfigRequestCallback() {
//                    @Override
//                    public void onSuccess(HashMap<String, String> hashMap) {
//
//                    }
//
//                    @Override
//                    public void onFail(Exception e) {
//                    }
//
//                }, false)
//                //设置访问后台动态配置的时间间隔,默认为3个小时
//                .setFetchDynamicConfigIntervalByHours(3)
//                //设置当前渠道号,对于某些渠道我们可能会想屏蔽补丁功能
//                //设置渠道后,我们就可以使用后台的条件控制渠道更新
//                .setAppChannel("default")
//                //屏蔽部分渠道的补丁功能
//                .addIgnoreAppChannel("googleplay")
//                //设置tinkerpatch平台的条件下发参数
//                .setPatchCondition("test", "1")
//                //设置补丁合成成功后,锁屏重启程序
//                //默认是等应用自然重启
//                .setPatchRestartOnSrceenOff(true)
//                //我们可以通过ResultCallBack设置对合成后的回调
//                //例如弹框什么
//                //注意，setPatchResultCallback 的回调是运行在 intentService 的线程中
//                .setPatchResultCallback(patchResult -> android.util.Log.i(TAG, "onPatchResult callback here"))
//                //设置收到后台回退要求时,锁屏清除补丁
//                //默认是等主进程重启时自动清除
//                .setPatchRollbackOnScreenOff(true)
//                //我们可以通过RollbackCallBack设置对回退时的回调
//                .setPatchRollBackCallback(new RollbackCallBack() {
//                    @Override
//                    public void onPatchRollback() {
//                        android.util.Log.i(TAG, "onPatchRollback callback here");
//                    }
//                });
//    }
//
//    /**
//     * 自定义Tinker类的高级用法, 使用更灵活，但是需要对tinker有更进一步的了解
//     * 更详细的解释请参考:http://tinkerpatch.com/Docs/api
//     */
//    private void complexSample() {
//        //修改tinker的构造函数,自定义类
//        TinkerPatch.Builder builder = new TinkerPatch.Builder(tinkerApplicationLike)
//                .listener(new DefaultPatchListener(this))
//                .loadReporter(new DefaultLoadReporter(this))
//                .patchReporter(new DefaultPatchReporter(this))
//                .resultServiceClass(TinkerServerResultService.class)
//                .upgradePatch(new UpgradePatch())
//                .patchRequestCallback(new TinkerPatchRequestCallback());
//        //.requestLoader(new OkHttpLoader());
//
//        TinkerPatch.init(builder.build());
//    }
}


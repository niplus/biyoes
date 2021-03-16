package com.biyoex.app.common.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import androidx.appcompat.app.AlertDialog;

import com.biyoex.app.R;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/** 动态获取权限类
 * 由于国产rom包屏蔽了原生权限回调，导致权限拒绝之后无法得到相应的结果，故改用RxPermissions来进行权限动态获取
 *
 *
 */

public class MPermissionUtils {
    private static int mRequestCode = -1;

    public static void requestPermissionsResult(Activity activity
            , final OnPermissionListener callback,String... permissions){
        requestPermissions(activity, callback,permissions);
    }

    /**
     * 请求权限处理
     * @param object        activity or fragment
     * @param
     * @param permissions   需要请求的权限
     * @param callback      结果回调
     */
    private static void requestPermissions(Object object,final OnPermissionListener callback, String... permissions){
        Observable<Boolean> request =new RxPermissions((Activity) object).request(permissions);
        request.subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Boolean aBoolean) {
                if(aBoolean){
                    callback.onPermissionGranted();
                }else{
                    callback.onPermissionDenied();
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    /**
     * 显示提示对话框
     */
    public static void showTipsDialog(final Context context) {
        new AlertDialog.Builder(context)
                .setTitle(R.string.hint_message)
                .setMessage(R.string.permission_refuse)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.confirm, (dialog, which) -> startAppSettings(context)).show();
    }

    /**
     * 启动当前应用设置页面
     */
    private static void startAppSettings(Context context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:com.zg.zhongguwang"));
        context.startActivity(intent);
    }


    public interface OnPermissionListener{
        void onPermissionGranted();
        void onPermissionDenied();
    }

    private static OnPermissionListener mOnPermissionListener;
}

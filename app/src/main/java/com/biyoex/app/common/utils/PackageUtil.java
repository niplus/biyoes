package com.biyoex.app.common.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

/**
 * Created by LG on 2017/7/27.
 */

public class PackageUtil {
    public static PackageInfo getPackageInfo(Context context){
        PackageManager pm = context.getPackageManager();
        try {
            return pm.getPackageInfo("com.zg.zhongguwang", 0);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("-----",e.getLocalizedMessage());
        }
        return  new PackageInfo();
    }
}

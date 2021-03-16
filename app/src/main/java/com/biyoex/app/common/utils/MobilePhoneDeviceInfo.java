package com.biyoex.app.common.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

/** 设备信息工具类
 * Created by LG on 2017/1/11.
 */

public class MobilePhoneDeviceInfo {
    /**
     *  wifimac地址
      * @param mContext
     * @return
     */
   public static String getWifiMac(Context mContext){
       WifiManager mWifi=(WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
       WifiInfo mConnectionInfo = mWifi.getConnectionInfo();
       String mWifiMac=mConnectionInfo.getMacAddress();
       if(!TextUtils.isEmpty(mWifiMac)){
           Log.e("mac",""+mWifiMac);
           return mWifiMac;
       }
       return "";
   }

    /**
     * 获取IMEI
     * @param mContext
     * @return
     */
    public static String getIMEI(Context mContext){
        TelephonyManager mTelephony = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        String mIMEI=mTelephony.getDeviceId();
        if(!TextUtils.isEmpty(mIMEI)){
            Log.e("IMEI",""+mIMEI);
            return mIMEI;
        }
        return "";
    }

    /**
     * 序列号
     * @param mContext
     * @return
     */
    public static String getSerialNumber(Context mContext){
        TelephonyManager mTelephony = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        String sn = mTelephony.getSimSerialNumber();
        if(!TextUtils.isEmpty(sn)){
            Log.e("sn",""+sn);
            return sn;
        }
        return "";
    }

    /**
     * 获取手机品牌
     *
     * @return
     */
    public static String getPhoneBrand() {
        Log.e("PhoneBrand",""+android.os.Build.BRAND);
        return android.os.Build.BRAND;
    }

    /**
     * 获取手机型号
     *
     * @return
     */
    public static String getPhoneModel() {
        Log.e("PhoneModel",""+android.os.Build.MODEL);
        return android.os.Build.MODEL;
    }
    /**
     * 判断当前设备是手机还是平板，代码来自 Google I/O App for Android
     * @param context
     * @return 平板返回 True，手机返回 False
     */
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    //版本号
    public static int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;

        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pi;
    }

}

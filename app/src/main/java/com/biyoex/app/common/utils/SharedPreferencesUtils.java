package com.biyoex.app.common.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.biyoex.app.BuildConfig;
import com.biyoex.app.common.utils.log.Log;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * sharedPreferences的工具类
 */
public class SharedPreferencesUtils {
    private static final String FILE_NAME = "app_data";
    private static SharedPreferences mSharedPreferences;// 单例
    private static SharedPreferencesUtils instance;// 单例

    private SharedPreferencesUtils(Context context) {
        mSharedPreferences = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
    }
    /**
     * 初始化单例
     * @param context
     */
    public static synchronized SharedPreferencesUtils init(Context context) {
        if (instance == null) {
            instance = new SharedPreferencesUtils(context);
        }
        return instance;
    }

    /**
     * 获取单例
     *
     * @return
     */
    public static SharedPreferencesUtils getInstance() {
        if (instance == null) {
            throw new RuntimeException("class should init!");

        }
        return instance;
    }

    /**
     * 保存数据
     *
     * @param key
     * @param data
     */
    public void saveData(String key, Object data) {
        String type = data.getClass().getSimpleName();

        SharedPreferences.Editor editor = mSharedPreferences.edit();

        if ("Integer".equals(type)) {
            editor.putInt(key, (Integer) data);
        } else if ("Boolean".equals(type)) {
            editor.putBoolean(key, (Boolean) data);
        } else if ("String".equals(type)) {
            editor.putString(key, (String) data);
        } else if ("Float".equals(type)) {
            editor.putFloat(key, (Float) data);
        } else if ("Long".equals(type)) {
            editor.putLong(key, (Long) data);
        }

        editor.commit();
    }

    /**
     *  传入多个数据，并且保存
     * @param map 数据集合
     */
    public void WriteDataOver(Map<String,Object> map){
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        for (String key:map.keySet()){
            String type = map.get(key).getClass().getSimpleName();
            if ("Integer".equals(type)) {
                editor.putInt(key, (Integer) map.get(key));
            } else if ("Boolean".equals(type)) {
                editor.putBoolean(key, (Boolean) map.get(key));
            } else if ("String".equals(type)) {
                editor.putString(key, (String) map.get(key));
            } else if ("Float".equals(type)) {
                editor.putFloat(key, (Float)map.get(key));
            } else if ("Long".equals(type)) {
                editor.putLong(key, (Long)map.get(key));
            }
        }
        editor.commit();
    }

    /**
     *  得到多个数据的返回
     * @param map
     * @return
     */
    public Map<String,Object> ReadDataOver(Map<String,Object> map){
        Map<String,Object> MapData= new HashMap<>();
        for (String key:map.keySet()){
            String type= map.get(key).getClass().getSimpleName();
            if ("Integer".equals(type)) {
                MapData.put(key,mSharedPreferences.getInt(key, (Integer) map.get(key)));
            } else if ("Boolean".equals(type)) {
                MapData.put(key,mSharedPreferences.getBoolean(key, (Boolean) map.get(key)));
            } else if ("String".equals(type)) {
                MapData.put(key,mSharedPreferences.getString(key, (String) map.get(key)));
            } else if ("Float".equals(type)) {
                MapData.put(key,mSharedPreferences.getFloat(key, (Float) map.get(key)));
            } else if ("Long".equals(type)) {
                MapData.put(key,mSharedPreferences.getLong(key, (Long) map.get(key)));
            }
        }
        return MapData;
    }

    /**
     * 得到数据
     *
     * @param key
     * @param defValue
     * @return
     */
    public Object getData(String key, Object defValue) {

        String type = defValue.getClass().getSimpleName();
        if ("Integer".equals(type)) {
            return mSharedPreferences.getInt(key, (Integer) defValue);
        } else if ("Boolean".equals(type)) {
            return mSharedPreferences.getBoolean(key, (Boolean) defValue);
        } else if ("String".equals(type)) {
            return mSharedPreferences.getString(key, (String) defValue);
        } else if ("Float".equals(type)) {
            return mSharedPreferences.getFloat(key, (Float) defValue);
        } else if ("Long".equals(type)) {
            return mSharedPreferences.getLong(key, (Long) defValue);
        }

        return null;
    }
    public String getString(String key, String defValue) {
        return mSharedPreferences.getString(key,defValue);

    }

    public void saveString(String key, String data) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        editor.putString(key,data).commit();
    }

    public void removeString(String key){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.remove(key);
    }
    public void removeOne(String key,String value){
        Set<String> set = mSharedPreferences.getStringSet(key, null);
        if (set == null){
           return;
        }
        set.remove(value);
        mSharedPreferences.edit().putStringSet(key,set).commit();

    }
    public void saveOne(String key,String value){
        Set<String> set = mSharedPreferences.getStringSet(key, null);
        if (set == null){
            set = new HashSet<>();
        }
        set.add(value);
        mSharedPreferences.edit().putStringSet(key,set).commit();
    }

    public boolean isContains(String key,String value){
        Set<String> set = mSharedPreferences.getStringSet(key, null);
        if (set == null){
           return false;
        }else {
            return set.contains(value);
        }
    }

    /**
     * 存储用户手势密码
     * @param gestureCode
     */
    public void setUserGesture(String gestureCode){

        String loginName = mSharedPreferences.getString("loginName","");

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        try {
            String encryptCode = CryptUtils.encrypt(BuildConfig.salt, gestureCode);
            editor.putString(loginName + "_gesture_code", encryptCode);
        } catch (Exception e) {
            Log.e(e, "set_gesture");
        }
        editor.commit();
    }

    public String getUserGesture(){
        String loginName = mSharedPreferences.getString("loginName","");

        String gestureCode = null;
        String encryptString = mSharedPreferences.getString(loginName + "_gesture_code", "");

        if (TextUtils.isEmpty(encryptString)){
            return null;
        }else {
            try {
                gestureCode =  CryptUtils.decrypt(BuildConfig.salt, encryptString);
                Log.i("gestureCode"+gestureCode);
            } catch (Exception e) {
                Log.e(e, "get_gesture");
            }finally {
                return gestureCode;
            }
        }
    }

    public void setUserGestureSwitch(boolean isGestureOn){
        String loginName = mSharedPreferences.getString("loginName","");

        SharedPreferences.Editor editor = mSharedPreferences.edit();

        Log.i(loginName + "_gesture_switch :" + isGestureOn);
        editor.putBoolean(loginName + "_gesture_switch",isGestureOn);
        editor.commit();
    }

    public boolean isUserGestureOn(){
        String loginName = mSharedPreferences.getString("loginName","");
        boolean isGestureOn = mSharedPreferences.getBoolean(loginName + "_gesture_switch", false);
        Log.i(loginName + "_gesture_switch :" + isGestureOn);
        return isGestureOn;
    }

    /**
     * 用户首次登陆到c2c交易页，需要弹出交易须知
     * @return
     */
    public boolean isNeedShowRemind(){
        String loginName = mSharedPreferences.getString("loginName","");
        boolean needShowRemind = mSharedPreferences.getBoolean(loginName + "_showRemind", false);
        Log.i(loginName + "_showRemind :" + needShowRemind);
        return needShowRemind;
    }

    public void setHasShowRemind(boolean hasShowRemind){
        String loginName = mSharedPreferences.getString("loginName","");

        SharedPreferences.Editor editor = mSharedPreferences.edit();

        Log.i(loginName + "_showRemind :" + hasShowRemind);
        editor.putBoolean(loginName + "_showRemind",hasShowRemind);
        editor.commit();
    }

    public void clearCookie(){

    }

}

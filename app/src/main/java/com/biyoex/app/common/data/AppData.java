package com.biyoex.app.common.data;

import android.app.Activity;

import com.biyoex.app.VBTApplication;
import com.biyoex.app.common.utils.SPUtils;

public class AppData {
    private static Activity context;//当前类
    private static int roleId = 0;//如果是主账号  则roleId=0 子账户 则roleId=当前roleId
    private static String user_id = "";
    private static String realName = "";

    public static String getRealName() {
        if (realName.equals("")) {
            realName = (String) SPUtils.get(VBTApplication.getContext(), "realName", "");
        }
        return realName;
    }

    public static void setRealName(String realName) {
        SPUtils.put(VBTApplication.getContext(), "realName", realName);
        AppData.realName = realName;
    }

    public static int getRoleId() {
        if (roleId != 0) {
            roleId = (int) SPUtils.get(VBTApplication.getContext(), "roleId", 0);
        }
        return roleId;
    }

    public static void setRoleId(int roleId) {
        SPUtils.put(VBTApplication.getContext(), "roleId", roleId);
        AppData.roleId = roleId;
    }

    public static String getUser_id() {
        if (user_id.equals("")) {
            user_id = (String) SPUtils.get(VBTApplication.getContext(), "user_id", "");
        }
        return user_id;
    }

    public static void setUser_id(String user_id) {
        SPUtils.put(VBTApplication.getContext(), "user_id", user_id);
        AppData.user_id = user_id;
    }

    public static Activity getContext() {
        return context;
    }

    public static void setContext(Activity context) {
        AppData.context = context;
    }

}

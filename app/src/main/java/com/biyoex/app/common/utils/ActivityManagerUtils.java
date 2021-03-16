package com.biyoex.app.common.utils;

import android.app.Activity;

import com.biyoex.app.MainActivity;

import java.util.Stack;

/**
 * Activity栈管理
 */
public class ActivityManagerUtils {

    private static Stack<Activity> activityStack;
    private static ActivityManagerUtils instance;

    private MainActivity mainActivity;

    public static ActivityManagerUtils getInstance() {
        if (instance == null) {
            instance = new ActivityManagerUtils();
        }
        return instance;
    }


    public MainActivity getMainActivity() {
        return mainActivity;
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }


    /**
     * @param activity 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * 删除堆栈中的当前Activity
     *
     * @param activity
     */
    public void removeActivity(Activity activity) {
        if (activity != null) {
            if (activityStack.contains(activity)) {
                activityStack.remove(activity);
            }
        }
    }

    /**
     * 获取当前Activity,及堆栈的最后一个
     *
     * @return
     */
    public Activity getCurrentActivity() {
        if (activityStack == null || activityStack.isEmpty()) {
            return null;
        }
        return activityStack.lastElement();
    }

    /**
     * 关闭当前的Activity
     */
    public void finishCurrentActivity() {
        if (activityStack != null && !activityStack.isEmpty()) {
            finishActivity(activityStack.lastElement());
        }

    }

    /**
     * 关闭指定Activity
     *
     * @param activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null && !activity.isFinishing()) {
            activity.finish();
            if (!stackIsNullOrEmppty()) {
                activityStack.remove(activity);
                activity.finish();
            }
            activity = null;
        }
    }
    /**
     * 根据Class关闭Activity
     *
     * @param cls
     */
    public void finishActivity(Class<?> cls) {
        if (cls == null || stackIsNullOrEmppty())
            return;
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
                break;
            }
        }
    }



    /**
     * 关闭所有Activity
     */
    public void finishAllActivity() {
        if (stackIsNullOrEmppty())
            return;
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i) && !activityStack.get(i).isFinishing()) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    public Activity getActivity(Class<?> cls) {
        if (stackIsNullOrEmppty())
            return null;
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls))
                return activity;

        }
        return null;
    }

    public boolean stackIsNullOrEmppty() {
        if (activityStack == null || activityStack.isEmpty())
            return true;
        return false;
    }

    public void closeAllExceptThis(Class<?> cls) {
        // Activity act=getActivity(cls);
        for (Activity a : activityStack) {
            if (a.getClass().equals(cls)) {
                continue;
            } else {
                finishActivity(a);
            }
        }
    }


    /**
     * 退出应用程序
     */
    public void exitApp() {
        try {
            finishAllActivity();
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

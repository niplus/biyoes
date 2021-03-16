package com.biyoex.app.common.utils;

import androidx.annotation.StringRes;

import android.annotation.SuppressLint;
import android.widget.Toast;

import com.biyoex.app.VBTApplication;

/**
 * 一个全局可以使用的toast
 * Created by Administrator on 2016/12/5.
 */

public class ToastUtils {
     private static Toast mToast;

    @SuppressLint("ShowToast")
    public static void showToast(String text){
        if(mToast==null){
            mToast=Toast.makeText(VBTApplication.getContext(),text,Toast.LENGTH_LONG);
        }else{
            mToast.setText(text);
            //mToast.setGravity(Gravity.CENTER, 0, 0);
            mToast.setDuration(Toast.LENGTH_LONG);
        }
        mToast.show();
    }

    public static void showToast(@StringRes int stringResource){
        showToast(VBTApplication.getContext().getString(stringResource));
    }

}

package com.biyoex.app.common.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.biyoex.app.R;

/**
 * Created by LG on 2017/6/14.
 */

public class DialogUtils {

    private static Dialog mDialogLoading;

    private static Activity activity;

    /**
     * 等待的对话框
     */
    public static Dialog getLoadDialog(Context context) {
        mDialogLoading = new Dialog(context, R.style.Dialog);
        mDialogLoading.setCanceledOnTouchOutside(false);
        mDialogLoading.setContentView(setViewLoading(context));
        return mDialogLoading;
    }
    /**
     * 等待对话框的view
     *
     * @param context
     * @return
     */
    public static View setViewLoading(Context context) {
        View viewLoading = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
        ImageView ivLoading = viewLoading.findViewById(R.id.iv_loading);
        Glide.with(context).load(R.drawable.image_loading).into(ivLoading);
        return viewLoading;
    }

    public static Dialog getBottomDialog() {
        return null;
    }


}

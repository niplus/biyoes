package com.biyoex.app.common.widget;

import android.app.Dialog;
import android.content.Context;
import androidx.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.biyoex.app.R;


/**
 * Created by xxx on 2018/8/16.
 */

public class BottomDialog extends Dialog {

    private View view;

    public BottomDialog(@NonNull Context context, int layoutRes) {
        super(context, R.style.BottomDialog);
        Window window = getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        // 获取Window的LayoutParams
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
        attributes.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        // 一定要重新设置, 才能生效
        window.setAttributes(attributes);
        view = LayoutInflater.from(context).inflate(layoutRes, null);
        setContentView(view);
    }

    public View getView() {
        return view;
    }


}

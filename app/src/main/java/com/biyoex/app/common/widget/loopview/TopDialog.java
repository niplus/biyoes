package com.biyoex.app.common.widget.loopview;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.biyoex.app.R;


/**
 * Created by xxx on 2020/7/27.
 */

public class TopDialog extends Dialog {

    private View view;

    public TopDialog(@NonNull Context context, int layoutRes) {
        super(context, R.style.TopDialog);
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

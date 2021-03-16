package com.biyoex.app.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/** 跑马灯效果控件
 * Created by LG on 2017/5/18.
 */

public class MarqueeTextView extends TextView{

    public MarqueeTextView(Context context) {
        super(context);
    }

    public MarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MarqueeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}

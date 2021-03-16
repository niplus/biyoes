package com.biyoex.app.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by LG on 2017/3/9.
 */

public class SquareRelatveLayout extends RelativeLayout{

    public SquareRelatveLayout(Context context) {
        super(context);
    }

    public SquareRelatveLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareRelatveLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec=widthMeasureSpec;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}

package com.biyoex.app.common.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by shupian on 2017-12-13.
 */

public class FractionTranslateLinearLayout extends LinearLayout {

    private int                       screenWidth;
    private float                     fractionX;
    private OnLayoutTranslateListener onLayoutTranslateListener;

    public FractionTranslateLinearLayout(Context context) {
        super(context);
    }

    public FractionTranslateLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FractionTranslateLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected void onSizeChanged(int w, int h, int oldW, int oldH) {

        // Assign the actual screen width to our class variable.
        screenWidth = w;

        super.onSizeChanged(w, h, oldW, oldH);
    }

    public float getFractionX() {
        return fractionX;
    }

    public void setFractionX(float xFraction) {
        this.fractionX = xFraction;

/**
 xFraction = -1 说明在屏幕的最左边,不可见
 xFraction = 0 说明在屏幕的正中间,可见
 xFraction =1 说明在屏幕的最右边,不可见

 **/
        setX((screenWidth > 0) ? (xFraction * screenWidth) : 0);

        if (xFraction == 1 || xFraction == -1) {
            setAlpha(0);
        } else if (xFraction < 1 /* enter */ || xFraction > -1 /* exit */) {
            if (getAlpha() != 1) {
                setAlpha(1);
            }
        }

        if (onLayoutTranslateListener != null) {
            onLayoutTranslateListener.onLayoutTranslate(this, xFraction);
        }
    }

    public void setOnLayoutTranslateListener(OnLayoutTranslateListener onLayoutTranslateListener) {
        this.onLayoutTranslateListener = onLayoutTranslateListener;
    }

    public static interface OnLayoutTranslateListener {
        void onLayoutTranslate(FractionTranslateLinearLayout view, float xFraction);
    }
}

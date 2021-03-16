package com.biyoex.app.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

/**
 * Created by LG on 2017/7/26.
 */

public class WebView4Scroll extends WebView {
    private float startx;
    private float starty;
    private float offsetx;
    private float offsety;
    public WebView4Scroll(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                requestDisallowInterceptTouchEvent(true);
                startx = event.getX();
                starty = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                offsetx = Math.abs(event.getX() - startx);
                offsety = Math.abs(event.getY() - starty);
                if (offsetx > offsety) {
                   requestDisallowInterceptTouchEvent(true);
                } else {
                   requestDisallowInterceptTouchEvent(false);
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }
}

package com.biyoex.app.common.widget;

import android.content.Context;

import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.biyoex.app.common.utils.log.Log;

import java.util.HashMap;
import java.util.LinkedHashMap;

//为了计算viewpager再scrollview的高度
public class MyViewPager extends ViewPager {

    private int current;
    private int height = 0;
    /**
     * 保存position与对于的View
     */
    private HashMap<Integer, View> mChildrenViews = new LinkedHashMap<Integer, View>();

    private HashMap<Integer, Integer> mchildViewHeight = new HashMap<>();
    private boolean scrollble = true;

    public MyViewPager(Context context) {
        super(context);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private int widthMeasureSpec;
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        if (mChildrenViews.size() > current) {
            View child = mChildrenViews.get(current);
            if (child != null) {
                child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                height = child.getMeasuredHeight();
            }
//        }
        this.widthMeasureSpec = widthMeasureSpec;
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    public void resetHeight(int current) {
        this.current = current;
//        if (mChildrenViews.size() > current) {
            ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams) getLayoutParams();
            if (layoutParams == null) {
                layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
            } else {
                layoutParams.height = height;
            }
            setLayoutParams(layoutParams);
//        }

        Log.i("nidongliang height : " + height);
    }

    public void initHeight(int height) {
//        this.current = current;
//        View child = getChildAt(0);
//        child.measure(widthMeasureSpec,  MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
//        int mHeight = child.getMeasuredHeight();
//        if (mChildrenViews.size() > current) {
//            ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams) getLayoutParams();
//            if (layoutParams == null) {
//                layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
//            } else {
//                layoutParams.height = height;
//            }
//            setLayoutParams(layoutParams);

            setMeasuredDimension(getMeasuredWidth(), height);
//        }
    }

    /**
     * 保存position与对于的View
     */
    public void setObjectForPosition(View view, int position) {
        mChildrenViews.put(position, view);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!scrollble) {
            return true;
        }
        return super.onTouchEvent(ev);
    }


    public boolean isScrollble() {
        return scrollble;
    }

    public void setScrollble(boolean scrollble) {
        this.scrollble = scrollble;
    }
}

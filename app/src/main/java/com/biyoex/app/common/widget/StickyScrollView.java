package com.biyoex.app.common.widget;

import android.content.Context;
import android.graphics.Canvas;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;



public class StickyScrollView extends NestedScrollView {

    //SparseArray 的key是会自动排序的
    private SparseArray<View> floatTitles;

    /**
     * 当前悬浮标题
     */
    private View currentFloatView;

    public StickyScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

//        floatTitles = new ArrayList<>();
        floatTitles = new SparseArray<>(10);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        findAllFloatTitle(getChildAt(0));
    }

    private void findAllFloatTitle(View view){
        Object tag = view.getTag();
        if ("sticky".equals(tag)){
            floatTitles.append(calcViewsTop(view), view);
        }else {
            if (view instanceof ViewGroup){
                ViewGroup root = (ViewGroup)view;
                for (int i = 0;i < root.getChildCount();i++){
                    findAllFloatTitle(root.getChildAt(i));
                }
            }
        }

    }

    private int calcViewsTop(View v){
        int top = v.getTop();

        while (v.getParent() != getChildAt(0)){
            v = (View)v.getParent();
            top += v.getTop();
        }
        return top;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
    }
}

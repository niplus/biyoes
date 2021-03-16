package com.biyoex.app.common.widget;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import java.util.List;

/**
 * 用于悬浮标题的layout，需要子child第一个是nestedScrollView，第二个是FrameLayout
 * 标题需要用FrameLayout包裹， 目前只支持两个
 */
public class FloatTitleLayout extends FrameLayout {

    private boolean isInit;
    private CustomNestedScrollView nestedScrollView;

    /**
     * 展示悬浮标题
     */
    private FrameLayout floatTitle;

    /**
     * 用于添加
     */
    private View firstFloatView;
    private View secondFloatView;


    private int currentView;

    private List<View> titles;

    public FloatTitleLayout(@NonNull Context context) {
        this(context, null);
    }

    public FloatTitleLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatTitleLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (!isInit){
            initView();
        }
    }

    private void initView(){
        isInit = true;

        nestedScrollView = (CustomNestedScrollView) getChildAt(0);
        floatTitle = (FrameLayout) getChildAt(1);


        nestedScrollView.setOnScrollChangedListener(new CustomNestedScrollView.OnScrollChangedListener() {
            @Override
            public void onScrollChanged(int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                View firstTitle = titles.get(0);
                View secondTitle = titles.get(1);

                if (secondTitle != null){
                    if (firstTitle.getY() < scrollY
                            && secondTitle.getY() >= scrollY + firstTitle.getHeight()){

                        if (currentView != 1) {
                            if (currentView != 3) {
                                floatTitle.removeAllViews();
                                if (firstFloatView != null)
                                    floatTitle.addView(firstFloatView);
                                currentView = 1;
                            }
                            floatTitle.setY(0);
                        }

                        floatTitle.setVisibility(VISIBLE);

                    }
                    //这个部分可能会导致标题只显示一部分
                    else if (secondTitle.getY() <= scrollY + firstTitle.getHeight()
                            && secondTitle.getY() > scrollY){
                        floatTitle.setY(-(floatTitle.getHeight() - (secondTitle.getY() - scrollY)));
                        if (currentView != 3) {
                            floatTitle.removeAllViews();
                            if (firstFloatView != null)
                                floatTitle.addView(firstFloatView);
                            currentView = 3;
                        }
                    }else if (secondTitle.getY() <= scrollY){
                        if (currentView != 2){
                            floatTitle.removeAllViews();
                            if (secondFloatView != null)
                                floatTitle.addView(secondFloatView);
                            currentView = 2;
                            floatTitle.setY(0);
                        }
                    }

                    if (firstTitle.getY() >= scrollY){
                        floatTitle.setVisibility(GONE);
                    }
                }
            }
        });
    }
}

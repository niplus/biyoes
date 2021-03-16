package com.biyoex.app.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import com.biyoex.app.R;

/**
 * 自定义一个指示器，因为不会横向移动，只适用数量少的时候
 */
public class PagerIndicator extends View {

    /**
     * 总共有几页
     */
    private int size = 0;

    /**
     * 小圆点之间的距离
     */
    private int pointMargin = 18;

    /**
     * 小圆点宽高
     */
    private int pointWidth = 24;
    private int pointHeight = 8;

    /**
     * 当前离两边的距离
     */
    private float distance;

    /**
     * 控件宽度
     */
    private int width;

    private int unSelectColor;

    private Paint mPaint;
    private ViewPager mViewPager;
    private int currentPosition;

    public PagerIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.PagerIndicator);

        pointWidth = array.getDimensionPixelSize(R.styleable.PagerIndicator_point_width, 24);
        pointHeight = array.getDimensionPixelSize(R.styleable.PagerIndicator_point_height, 8);
        pointMargin = array.getDimensionPixelSize(R.styleable.PagerIndicator_point_margin, 20);
        unSelectColor = array.getColor(R.styleable.PagerIndicator_unselect_color, getResources().getColor(R.color.text_normal));
        array.recycle();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
    }

    public void setViewpager(ViewPager viewpager){
        mViewPager = viewpager;
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                currentPosition = position % size;
                distance = (pointWidth + pointMargin) * positionOffset;
                invalidate();
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    public void setSize(int size) {
        this.size = size;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (size == 0){
            return;
        }

        int contentWidth = pointWidth * size + pointMargin * (size - 1);

        //如果宽度超过，则索性不进行绘制了
        if (contentWidth > width){
            return;
        }

        float startX = 0;
        startX = (width - contentWidth) / 2;


        mPaint.setColor(unSelectColor);
        for (int i = 0;i < size;i++){
            canvas.drawRoundRect(startX, 0, startX + pointWidth, pointHeight, pointHeight / 2, pointHeight / 2, mPaint);
            startX = startX + pointWidth + pointMargin;
        }

        canvas.save();

        float clipStart = (width - contentWidth) / 2;
        Path path = new Path();
        for (int i = 0;i < size;i++){
            path.addRoundRect(clipStart, 0, clipStart + pointWidth, pointHeight, pointHeight / 2, pointHeight / 2, Path.Direction.CW);
            clipStart = clipStart + pointWidth + pointMargin;
        }

        canvas.clipPath(path);
        if (currentPosition == size - 1 && distance > pointWidth){
            startX = (width - contentWidth) / 2 - ((pointWidth + pointMargin) - distance);
        }else {
            startX = (width - contentWidth) / 2 + (pointWidth + pointMargin) * currentPosition + distance;
        }

        mPaint.setColor(getResources().getColor(R.color.commonBlue));
        canvas.drawRoundRect(startX, 0, startX + pointWidth, pointHeight, pointHeight / 2, pointHeight / 2, mPaint);
        canvas.restore();

        super.onDraw(canvas);
    }

}

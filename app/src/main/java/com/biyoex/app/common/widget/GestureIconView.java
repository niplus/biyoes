package com.biyoex.app.common.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.biyoex.app.R;
import com.biyoex.app.common.utils.androidutilcode.ScreenUtils;

/**
 * Created by xxx on 2018/8/20.
 */

public class GestureIconView extends View {

    private int width = 20;
    private int height = 20;

    private Paint paint;

    /**
     * 圆的间距
     */
    private float circleMargin;

    /**
     * 半径
     */
    private float circleRadius;

    /**
     * 线的宽度
     */
    private float circleStrokeWidth;

    private String selected = "";

    public GestureIconView(Context context) {
        super(context);
    }

    public GestureIconView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GestureIconView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        circleMargin = ScreenUtils.dp2px(10);
        circleStrokeWidth = ScreenUtils.dp2px(1);

        paint = new Paint();

        paint.setAntiAlias(true);
        paint.setStrokeWidth(circleStrokeWidth);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY ||
                widthMode == MeasureSpec.AT_MOST){
            width = MeasureSpec.getSize(widthMeasureSpec);
        }

        if (heightMode == MeasureSpec.EXACTLY ||
                heightMode == MeasureSpec.AT_MOST){
            height = MeasureSpec.getSize(heightMeasureSpec);
        }

        int size = Math.min(width, height);

        super.onMeasure(MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        circleRadius = (w - circleMargin * 2) / 3 / 2 - circleStrokeWidth / 2;
    }

    public void setSelected(int selected){
        this.selected += selected;
        invalidate();
    }

    public void clear(){
        this.selected = "";
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        float startX = circleStrokeWidth / 2;
        float startY = circleStrokeWidth / 2;

        float currentX = startX + circleRadius;
        float currentY = startY + circleRadius;

        for (int i = 0;i < 9;i++){
            if (selected.contains(""+i)) {
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                paint.setColor(getResources().getColor(R.color.my_theme));
            }else {
                paint.setStyle(Paint.Style.STROKE);
                paint.setColor(getResources().getColor(R.color.text_middle_light));
            }

            canvas.drawCircle(currentX, currentY, circleRadius, paint);
            if (i % 3 == 2){
                currentX = startX + circleRadius;
                currentY += circleRadius * 2 + circleStrokeWidth + circleMargin;
            }else {
                currentX += circleRadius * 2 + circleStrokeWidth + circleMargin;
            }
        }

    }
}

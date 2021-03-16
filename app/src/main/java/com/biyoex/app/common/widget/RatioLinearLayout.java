package com.biyoex.app.common.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;


/**
 * Created by xxx on 2018/8/29.
 */

public class RatioLinearLayout extends LinearLayout {


    private Paint paint;
    private boolean isRight = true;
    /**
     * 所占比例
     */
    private float ratio;

    public RatioLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);

        init();
    }

    public RatioLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(0x6600D38B);
        paint.setStrokeCap(Paint.Cap.SQUARE);
//        paint.setStyle(Paint.Style.FILL);
//        setWillNotDraw(false);
    }

    public void setPaintColor(@ColorInt int color) {
        paint.setColor(color);
        invalidate();
    }

    public void setRatio(double ratio) {
        this.ratio = (float) ratio;
        invalidate();
    }

    public void setValues(double currentValue, double maxValue, double volume) {
        if (maxValue != volume) {
            volume = maxValue / 0.8;
        }
        ratio = (float) (currentValue / volume);
        invalidate();
    }

    public void setStartRight(boolean startRight) {
        this.isRight = startRight;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (ratio != 0) {
            if (isRight) {
                RectF rectF = new RectF(getWidth() - getWidth() * ratio, 0, getWidth(), getHeight());
                canvas.drawRoundRect(rectF, 0, 0, paint);
            } else {
                RectF rectF = new RectF(0, 0, getWidth() * ratio, getHeight());
                canvas.drawRoundRect(rectF, 0, 0, paint);
            }
        }

        super.onDraw(canvas);
    }
}

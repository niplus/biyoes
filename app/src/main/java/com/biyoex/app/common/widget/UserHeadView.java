package com.biyoex.app.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.biyoex.app.R;

/**
 * 用户头像控件，显示姓和在线状态
 */
public class UserHeadView extends View {

    private float dy;
    private Paint circlePaint;

    private Paint textPaint;
    private char lastName;

    private int textSize;

    /**
     * 是否显示在线状态
     */
    private boolean showStatus;

    public UserHeadView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.UserHeadView);
        textSize = typedArray.getDimensionPixelOffset(R.styleable.UserHeadView_nameTextSize, 30);
        showStatus = typedArray.getBoolean(R.styleable.UserHeadView_showStatus, true);
        typedArray.recycle();

        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Paint.Style.FILL);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(textSize);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextAlign(Paint.Align.CENTER);

        Paint.FontMetrics metrics = textPaint.getFontMetrics();
        //metrics.ascent为负数
        dy = -(metrics.descent + metrics.ascent) / 2;
    }

    public void setLastName(char lastName) {
        this.lastName = lastName;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int radius = getWidth() / 2;

        circlePaint.setColor(Color.parseColor("#358dfa"));
        canvas.drawCircle(radius, radius, radius, circlePaint);

        if (showStatus) {
            circlePaint.setColor(Color.WHITE);
            canvas.drawCircle(getWidth() - radius / 4, getHeight() - radius / 4, radius / 4, circlePaint);

            circlePaint.setColor(Color.parseColor("#3baf34"));
            canvas.drawCircle(getWidth() - radius / 4, getHeight() - radius / 4, radius / 4 - 2, circlePaint);
        }

        int baseLine = (int) (getHeight()/2 + dy);
        canvas.drawText(lastName+"", getWidth() / 2, baseLine, textPaint);
    }
}

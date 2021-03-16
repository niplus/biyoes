package com.biyoex.app.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.biyoex.app.R;

/**
 * Created by xxx on 2018/9/18.
 */

public class LetterNavigation extends View{

    private Paint circlePaint;
    private Paint letterPaint;

    private RectF[] rectFS;

    private String[] letters = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    /**
     * 字母大小
     */
    private int letterSize;

    /**
     * 选中圆大小
     */
    private int circleRadius;

    private int maxRadius;

    private int unSelectColor;

    /**
     * 圆之间的间距
     */
    private int letterMargin;

    private float dy;

    private int currentPosition = 0;

    private OnLetterChangeListener onLetterChangeListener;


    public LetterNavigation(Context context) {
        this(context, null);
    }

    public LetterNavigation(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        final TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.LetterNavigation);
        letterSize = a.getDimensionPixelSize(R.styleable.LetterNavigation_letterSize, 16);
        maxRadius = a.getDimensionPixelOffset(R.styleable.LetterNavigation_circleRadius, 15);
        unSelectColor = a.getColor(R.styleable.LetterNavigation_unSelectorColor, 0xff333333);
        circleRadius = maxRadius;
        a.recycle();

        init(context);
    }

    private void init(Context context){
        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setColor(context.getResources().getColor(R.color.commonBlue));

        letterPaint = new Paint();
        letterPaint.setColor(Color.WHITE);
        letterPaint.setTextSize(letterSize);
        letterPaint.setAntiAlias(true);
        letterPaint.setTextAlign(Paint.Align.CENTER);

        Paint.FontMetrics metrics = letterPaint.getFontMetrics();
        //metrics.ascent为负数
        dy = -(metrics.descent + metrics.ascent) / 2;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                float pressY = event.getY();
                for (int i = 0;i < letters.length;i++){
                    if (rectFS[i].top < pressY && rectFS[i].bottom + letterMargin > pressY){
                        currentPosition = i;
                        if (onLetterChangeListener != null) {
                            onLetterChangeListener.onLetterChange(letters[i]);
                        }

                        invalidate();
                        return true;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float moveY = event.getY();
                if (currentPosition != 0) {
                    if (rectFS[currentPosition - 1].top < moveY && rectFS[currentPosition - 1].bottom + letterMargin > moveY) {
                        currentPosition = currentPosition - 1;
                        if (onLetterChangeListener != null) {
                            onLetterChangeListener.onLetterChange(letters[currentPosition]);
                        }
                        invalidate();
                        return true;
                    }
                }

                if (currentPosition != letters.length - 1){
                    if (rectFS[currentPosition + 1].top < moveY && rectFS[currentPosition + 1].bottom + letterMargin > moveY) {
                        currentPosition = currentPosition + 1;
                        if (onLetterChangeListener != null) {
                            onLetterChangeListener.onLetterChange(letters[currentPosition]);
                        }
                        invalidate();
                        return true;
                    }
                }
                break;
        }
        return false;
    }

    public void setLetter(String letter){
        if (letter.equals(letters[currentPosition])){
            return;
        }

        for (int i = 0;i < letters.length;i++){
            if (letter.equals(letters[i])){
                currentPosition = i;
                invalidate();
                return;
            }
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        circleRadius = maxRadius;

        if (getMeasuredHeight() / letters.length > (circleRadius * 2)) {
            letterMargin = (getMeasuredHeight() / letters.length) - (circleRadius * 2);
        }else {
            circleRadius = getMeasuredHeight() / letters.length / 2;
            letterMargin = 0;
        }

        rectFS = new RectF[letters.length];

        int x = getMeasuredWidth() - circleRadius * 2;
        for (int i = 0;i < letters.length;i++){
            rectFS[i] = new RectF(x, (circleRadius * 2 + letterMargin) * i,  getMeasuredWidth(), (circleRadius * 2) * (i + 1) + (letterMargin * i));
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0;i < letters.length;i++){
            if (i == currentPosition) {
                canvas.drawCircle(rectFS[i].centerX(), rectFS[i].centerY(), circleRadius, circlePaint);

                letterPaint.setColor(Color.WHITE);
            }else {
                letterPaint.setColor(unSelectColor);
            }

            float baseline = rectFS[i].centerY() + dy;
            canvas.drawText(letters[i], rectFS[i].centerX(), baseline, letterPaint);
        }
    }

    public void setOnLetterChangeListener(OnLetterChangeListener onLetterChangeListener){
        this.onLetterChangeListener = onLetterChangeListener;
    }

    public interface OnLetterChangeListener{
        void onLetterChange(String letter);
    }

}

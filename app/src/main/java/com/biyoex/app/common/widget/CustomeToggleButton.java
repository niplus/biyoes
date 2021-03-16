package com.biyoex.app.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ToggleButton;

import com.biyoex.app.R;


/**
 * 为了使控件放大而图片不放大
 */
public class CustomeToggleButton extends ToggleButton {

    private Paint mPaint;

    private Bitmap checkBitmap;
    private Bitmap unCheckBitmap;

    public CustomeToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomeToggleButton);
        Drawable checkDrawable = typedArray.getDrawable(R.styleable.CustomeToggleButton_checked_drawable);
        Drawable unCheckDrawable = typedArray.getDrawable(R.styleable.CustomeToggleButton_unChecked_drawable);
        typedArray.recycle();

        checkBitmap = drawableToBitmap(checkDrawable);
        unCheckBitmap = drawableToBitmap(unCheckDrawable);
        setTextOn("");
        setTextOff("");
        setBackground(null);
        setButtonDrawable(null);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isChecked()) {
            canvas.drawBitmap(checkBitmap, (getWidth() - checkBitmap.getWidth()) / 2, (getHeight() - checkBitmap.getHeight()) / 2, mPaint);
        } else {
            canvas.drawBitmap(unCheckBitmap, (getWidth() - unCheckBitmap.getWidth()) / 2, (getHeight() - unCheckBitmap.getHeight()) / 2, mPaint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int bitmapHeight =  Math.max(checkBitmap.getHeight(), unCheckBitmap.getHeight());
        if (heightMode == MeasureSpec.AT_MOST
                || (heightMode == MeasureSpec.EXACTLY && height < bitmapHeight)) {
            height = bitmapHeight;
        }

        int bitmapWidth = Math.max(checkBitmap.getWidth(), unCheckBitmap.getWidth());
        if (widthMode == MeasureSpec.AT_MOST
                || (widthMode == MeasureSpec.EXACTLY && width < bitmapWidth)) {
            width = bitmapWidth;
        }
        super.onMeasure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));

    }


    public Bitmap drawableToBitmap(Drawable drawable) {

        int dWidth = drawable.getIntrinsicWidth();
        int dHeight = drawable.getIntrinsicHeight();

        Bitmap mBitmap = Bitmap.createBitmap(dWidth, dHeight, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(mBitmap);

        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);


        return mBitmap;
    }
}

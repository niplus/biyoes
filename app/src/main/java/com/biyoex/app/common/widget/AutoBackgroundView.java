package com.biyoex.app.common.widget;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.biyoex.app.R;

public class AutoBackgroundView extends RelativeLayout {

    private Paint mPaint;

    private Paint shadowPaint;

    public AutoBackgroundView(Context context) {
        super(context);
    }

    public AutoBackgroundView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public AutoBackgroundView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(){
        setWillNotDraw(false);
        setLayerType(View.LAYER_TYPE_SOFTWARE,mPaint);
        mPaint = new Paint();
        mPaint.setColor(getResources().getColor(R.color.colorAccent));
        mPaint.setAntiAlias(true);
        setLayerType(View.LAYER_TYPE_SOFTWARE,null);
//        setLayerType(View.LAYER_TYPE_SOFTWARE,mPaint);

        shadowPaint = new Paint();
        shadowPaint.setColor(0xff000000);
        shadowPaint.setMaskFilter(new BlurMaskFilter(10, BlurMaskFilter.Blur.SOLID));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Path path = new Path();

        //左右各超出100， 否则两边阴影会淡
        path.moveTo(-100, 0.8f * getHeight());
        path.quadTo(getWidth() / 2, getHeight(), getWidth() + 100, 0.8f * getHeight());

        //先画背景色
//        canvas.drawColor(getResources().getColor(R.color.background_gray));
//        mPaint.setMaskFilter(new BlurMaskFilter(5, BlurMaskFilter.Blur.SOLID));

        //画阴影
        canvas.drawPath(path, shadowPaint);

        //因为阴影是黑色，需要盖上背景色
        canvas.drawPath(path, mPaint);
        canvas.drawRect(0, 0, getWidth(), 0.8f * getHeight(), mPaint);


    }
}

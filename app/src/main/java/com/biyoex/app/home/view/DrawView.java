package com.biyoex.app.home.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

import com.biyoex.app.R;

public class DrawView extends View {
    public int XPoint = 60; // 原点的X坐标
    public int YPoint;// 原点的Y坐标260
    public int XScale; // X的刻度长度55
    public int YScale; // Y的刻度长度40
    public int XLength; // X轴的长度380
    public int YLength; // Y轴的长度240
    //  private int scaleLength = 10;//刻度线的长度 TTTTTT
    private int top = 10;//上边缘距离
    private int left = 40;//左边缘距离
    private int right = 10;//右边缘距离
    private int bottom = 40;//下边缘距离
    private String[] YLabel;//y轴的刻度值
    private String[] XLabel;//X轴的刻度值
    public String[] DataStr; // 数据
    public String[] DataStr1; // 数据
    private Paint mPaint;
    private int distanceTop = 0;//折线距离顶部距离
    private int distanceLeft = 0;//折线距离左边刻度距离

    //  private Bitmap mBackGround;
    public DrawView(Context context) {
        super(context);
//      mBackGround  = ((BitmapDrawable) this.getResources().getDrawable(R.drawable.viewbackground)).getBitmap(); //获取背景图片
    }

    public void setDate(String[] YLabel, String[] XLabel, String[] DataStr, String[] DataStr1, int distanceTop, int distanceLeft) {//如果只需要一条折线，最后这个参数给null就行了
        this.YLabel = YLabel;
        this.XLabel = XLabel;
        this.DataStr = DataStr;
        this.DataStr1 = DataStr1;
        this.distanceTop = distanceTop;
        this.distanceLeft = distanceLeft;
        invalidate();
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Log.i("Main", "Width = " + getWidth());//1280
        Log.i("Main", "Height = " + getHeight());//670  测量整个view的高度不包括状态栏

        Log.i("Main", "Width = " + getMeasuredWidth());//一个是测量整个view的高度  一个是测量view里内容的高度
        Log.i("Main", "Height = " + getMeasuredHeight());

        YLength = getHeight() - bottom - top;//整个Y轴的长度
        XLength = getWidth() - right - left;
        YPoint = getHeight() - bottom;
        XScale = (XLength / XLabel.length);//--x轴的刻度平均长度
        YScale = (YLength / YLabel.length);//Y--Y轴的刻度平均长度

        Paint paint = new Paint();
        paint.setStrokeWidth(1);
        paint.setColor(getResources().getColor(R.color.bg_e8));
        paint.setTextSize(18);
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);

        mPaint = new Paint();
        mPaint.setStrokeWidth(5);
        mPaint.setColor(getResources().getColor(R.color.color_F96868));
        mPaint.setTextSize(24);
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);

//      canvas.drawBitmap(mBackGround, 0, 0, paint); //画背景图片
        //画竖线
//        canvas.drawLine(XPoint, YPoint, XPoint, top, paint);
        for (int i = 1; i * YScale <= YLength; i++) {//画横刻度（XPoint + 10是线距离刻度的距离）
            canvas.drawLine(XPoint + distanceLeft, YPoint - i * YScale, XLength, YPoint - i * YScale, paint);
            paint.setColor(getResources().getColor(R.color.bg_44));
            canvas.drawText(YLabel[i - 1], XPoint - 30, YPoint - i * YScale + 7, paint);
            paint.setColor(getResources().getColor(R.color.bg_e8));
        }
        //画横线
//        canvas.drawLine(XPoint, YPoint, XLength, YPoint, paint);
        for (int i = 1; i * XScale <= XLength; i++) {//画竖刻度
//            canvas.drawLine(XPoint + (i - 1) * XScale, YPoint, XPoint + (i - 1) * XScale, top, paint);
            paint.setColor(getResources().getColor(R.color.bg_44));
            canvas.drawText(XLabel[i - 1], XPoint + (i - 1) * XScale - 3, YPoint + 10, paint);
            paint.setColor(getResources().getColor(R.color.bg_e8));
        }
        //画数据图
        for (int i = 0; i < DataStr.length; i++) {
            canvas.drawCircle((float) XPoint + (i + 1) * XScale, (float) calcuLations(DataStr[i]) + distanceTop, 0, mPaint);
            if (i + 1 < DataStr.length) {
                canvas.drawLine((float) XPoint + (i + 1) * XScale, (float) calcuLations(DataStr[i]) + distanceTop
                        , (float) XPoint + (i + 2) * XScale, (float) calcuLations(DataStr[i + 1]) + distanceTop, mPaint);
            }
        }
        if (null != DataStr1) {
            //画数据图第二条线(待封装扩展)
            paint.setColor(Color.GREEN);
            for (int i = 0; i < DataStr1.length; i++) {
                canvas.drawCircle((float) XPoint + (i + 1) * XScale, (float) calcuLations(DataStr1[i]), 5, paint);
                if (i + 1 < DataStr1.length) {
                    canvas.drawLine((float) XPoint + (i + 1) * XScale, (float) calcuLations(DataStr1[i])
                            , (float) XPoint + (i + 2) * XScale, (float) calcuLations(DataStr1[i + 1]), paint);
                }
            }
        }
    }

    private int calcuLations(String y0) //计算y轴坐标
    {
        double y;
        try {
            y = Double.parseDouble(y0);
        } catch (Exception e) {
            return -100;
        }
        return (int) (YPoint - YPoint * (y / Double.parseDouble(YLabel[YLabel.length - 1])));
    }
}
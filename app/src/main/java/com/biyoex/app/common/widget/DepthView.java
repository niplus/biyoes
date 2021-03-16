package com.biyoex.app.common.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.biyoex.app.R;
import com.biyoex.app.common.bean.DepthBean;
import com.biyoex.app.common.utils.MoneyUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 深度图
 * Created by ndl on 2018/9/27.
 */

public class DepthView extends View {

    /**
     * 走势画笔
     */
    private Paint trendPaint;

    /**
     * 坐标轴画笔
     */
    private Paint axisPaint;


    /**
     * 买卖数据
     */
    private List<DepthBean> buyDepthData;
    private List<DepthBean> sellDepthData;

    private double maxVolume;

    /**
     * 用于填充颜色的path和paint
     */
    private Path buyPath;
    private Path sellPath;
    private Paint pathPaint;

    private float dy;

    private int priceDecimal = 2;
    private int amoutDecimal = 2;

    private Context mContext;

    public DepthView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DepthView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        //画笔颜色在画的时候设置
        trendPaint = new Paint();
        trendPaint.setStrokeWidth(3);
        trendPaint.setAntiAlias(true);
        trendPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        axisPaint = new Paint();
        axisPaint.setColor(mContext.getResources().getColor(R.color.text_weak_light));
        axisPaint.setStrokeWidth(3);
        axisPaint.setAntiAlias(true);
        axisPaint.setTextSize(25);

        pathPaint = new Paint();
        pathPaint.setStyle(Paint.Style.FILL);

        buyDepthData = new ArrayList<>();
        sellDepthData = new ArrayList<>();

        buyPath = new Path();
        sellPath = new Path();

        Paint.FontMetrics metrics = axisPaint.getFontMetrics();
        //metrics.ascent为负数
        dy = -(metrics.descent + metrics.ascent) / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawAxis(canvas);
    }

    /**
     * 画坐标轴
     *
     * @param canvas
     */
    private void drawAxis(Canvas canvas) {
        //画坐标轴
        canvas.drawLine(0, getHeight(), 0, 0, axisPaint);
        canvas.drawLine(getWidth(), getHeight(), getWidth(), 0, axisPaint);
        canvas.drawLine(0, getHeight(), getWidth(), getHeight(), axisPaint);
        canvas.drawLine(0, getHeight() - 30, getWidth(), getHeight() - 30, axisPaint);

        drawTrend(canvas);
        drawText(canvas);
    }

    /**
     * 画趋势部分
     * @param canvas
     */
    private void drawTrend(Canvas canvas) {

        if (buyDepthData.size() > 1) {
            trendPaint.setColor(mContext.getResources().getColor(R.color.price_green));
            int startX = getXPosition(buyDepthData.get(0).getPrice());
            int startY = getYPosition(buyDepthData.get(0).getVolume());

            buyPath.reset();
            buyPath.moveTo(startX, startY);
            for (int i = 1; i < buyDepthData.size(); i++) {
                canvas.drawLine(startX, startY, getXPosition(buyDepthData.get(i).getPrice()), getYPosition(buyDepthData.get(i).getVolume()), trendPaint);
                startX = getXPosition(buyDepthData.get(i).getPrice());
                startY = getYPosition(buyDepthData.get(i).getVolume());

                buyPath.lineTo(getXPosition(buyDepthData.get(i).getPrice()), getYPosition(buyDepthData.get(i).getVolume()));
            }
            buyPath.lineTo(startX, getHeight() - 30);
            buyPath.lineTo(0, getHeight() - 30);
            buyPath.close();

            Shader shader = new LinearGradient(0, 0, 0, getHeight() - 30, new int[]{0x1902CA84, 0x1902CA84, 0x1902CA84}, null, Shader.TileMode.CLAMP);
            pathPaint.setShader(shader);
            canvas.drawPath(buyPath, pathPaint);
        }

        if (sellDepthData.size() > 1) {
            trendPaint.setColor(mContext.getResources().getColor(R.color.price_red));
            int startX = getXPosition(sellDepthData.get(0).getPrice());
            int startY = getYPosition(sellDepthData.get(0).getVolume());

            sellPath.reset();
            sellPath.moveTo(startX, getHeight() - 30);
            sellPath.lineTo(startX, startY);
            for (int i = 1; i < sellDepthData.size(); i++) {
                canvas.drawLine(startX, startY, getXPosition(sellDepthData.get(i).getPrice()), getYPosition(sellDepthData.get(i).getVolume()), trendPaint);
                startX = getXPosition(sellDepthData.get(i).getPrice());
                startY = getYPosition(sellDepthData.get(i).getVolume());
                sellPath.lineTo(startX, startY);
            }
            sellPath.lineTo(startX, getHeight() - 30);
            sellPath.lineTo(getWidth(), getHeight() - 30);
            sellPath.close();

            Shader shader = new LinearGradient(0, 0, 0, getHeight() - 30, new int[]{0x19FF2858, 0x19FF2858, 0x19FF2858}, null, Shader.TileMode.CLAMP);
            pathPaint.setShader(shader);
            canvas.drawPath(sellPath, pathPaint);
        }
    }

    /**
     * 添加标签
     * @param canvas
     */
    private void drawText(Canvas canvas){

        axisPaint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("0", 10, getHeight() - 40, axisPaint);
        canvas.drawText(MoneyUtils.decimalByUp(amoutDecimal, new BigDecimal(maxVolume * 0.25f)).toPlainString(), 10, (getHeight() - 30) * 0.75f, axisPaint);
        canvas.drawText(MoneyUtils.decimalByUp(amoutDecimal, new BigDecimal(maxVolume * 0.5f)).toPlainString(), 10, (getHeight() - 30) * 0.5f, axisPaint);
        canvas.drawText(MoneyUtils.decimalByUp(amoutDecimal, new BigDecimal(maxVolume * 0.75f)).toPlainString(), 10, (getHeight() - 30) * 0.25f, axisPaint);


        if (sellDepthData.size() != 0) {
            canvas.drawText(MoneyUtils.decimalByUp(priceDecimal, new BigDecimal(sellDepthData.get(0).getPrice())).toPlainString(), getXPosition(sellDepthData.get(0).getPrice()), getHeight() - 15 + dy, axisPaint);
        }

        if (buyDepthData.size() != 0) {
            axisPaint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText(MoneyUtils.decimalByUp(priceDecimal, new BigDecimal(buyDepthData.get(buyDepthData.size() - 1).getPrice())).toPlainString(), getXPosition(buyDepthData.get(buyDepthData.size() - 1).getPrice()), getHeight() - 15 + dy, axisPaint);
        }
    }

    public void setBuyDepthData(List<DepthBean> buyDepthData) {
        this.buyDepthData.clear();
        double tempVol = 0;

        if (buyDepthData.size() != 0) {

            //价格低的数量需要加上价格高的数量
            for (int i = 0; i < buyDepthData.size(); i++) {
                tempVol += buyDepthData.get(i).getVolume();
                buyDepthData.get(i).setVolume(tempVol);
                this.buyDepthData.add(0, buyDepthData.get(i));
            }

            maxVolume = Math.max(maxVolume, this.buyDepthData.get(0).getVolume());
        }
        invalidate();
    }

    public void setSellDepthData(List<DepthBean> sellDepthData) {
        this.sellDepthData.clear();
        double tempVol = 0;

        if (sellDepthData.size() != 0) {
            tempVol = 0;

            for (int i = 0; i < sellDepthData.size(); i++) {
                tempVol += sellDepthData.get(i).getVolume();
                sellDepthData.get(i).setVolume(tempVol);
                this.sellDepthData.add(sellDepthData.get(i));
            }

            maxVolume = Math.max(maxVolume, this.sellDepthData.get(this.sellDepthData.size() - 1).getVolume());
        }
        invalidate();
    }

    private int getYPosition(double volume) {
        return (int) ((getHeight() - 30) - (getHeight() - 30) * (volume / maxVolume));
    }

    private int getXPosition(double price) {
        double maxPrice = 0;
        double minPrice = 0;

        if (buyDepthData.size() != 0){
            minPrice = buyDepthData.get(0).getPrice();
            maxPrice = buyDepthData.get(buyDepthData.size() - 1).getPrice();
        }

        if (sellDepthData.size() != 0){
            if (buyDepthData.size() == 0){
                minPrice = sellDepthData.get(0).getPrice();
            }

            maxPrice = sellDepthData.get(sellDepthData.size() - 1).getPrice();
        }


        return (int) ((price - minPrice) / (maxPrice - minPrice) * (getWidth()));
    }

    public void setAmoutDecimal(int amoutDecimal) {
        this.amoutDecimal = amoutDecimal;
    }

    public void setPriceDecimal(int priceDecimal) {
        this.priceDecimal = priceDecimal;
    }
}

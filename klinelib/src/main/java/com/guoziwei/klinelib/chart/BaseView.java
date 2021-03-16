package com.guoziwei.klinelib.chart;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.Transformer;
import com.guoziwei.klinelib.R;
import com.guoziwei.klinelib.model.HisData;
import com.guoziwei.klinelib.util.DateUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/14 0014.
 */

class BaseView extends LinearLayout {

    protected String mDateFormat = "yyyy-MM-dd HH:mm";

    protected int mDecreasingColor;
    protected int mIncreasingColor;
    protected int mAxisColor;
    protected int mGridColor;
    protected int mTransparentColor;
    protected int mHighLightColor;


    public int MAX_COUNT = 150;
    public int MIN_COUNT = 10;
    public int INIT_COUNT = 30;

    protected List<HisData> mData = new ArrayList<>(300);

    public BaseView(Context context) {
        this(context, null);
    }

    public BaseView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BaseView);
        mAxisColor = a.getColor(R.styleable.BaseView_axisColor, 0xff333333);
        mGridColor = a.getColor(R.styleable.BaseView_gridColor, 0xff999999);
        mDecreasingColor = a.getColor(R.styleable.BaseView_decreaseColor, ContextCompat.getColor(getContext(), R.color.decreasing_color));
        mIncreasingColor = a.getColor(R.styleable.BaseView_increaseColor, ContextCompat.getColor(getContext(), R.color.increasing_color));
        mHighLightColor = a.getColor(R.styleable.BaseView_highlightColor, ContextCompat.getColor(getContext(), R.color.highlight_color));
        a.recycle();
        mTransparentColor = ContextCompat.getColor(getContext(), android.R.color.transparent);
    }

    protected void initBottomChart(CustomCombinedChart chart) {
        chart.setScaleEnabled(true);
        chart.setDrawBorders(false);
        chart.setBorderWidth(1);
        chart.setDragEnabled(true);
        chart.setScaleYEnabled(false);
        chart.setAutoScaleMinMaxEnabled(true);
        chart.setDragDecelerationEnabled(false);
        chart.setHighlightPerDragEnabled(false);
        Legend lineChartLegend = chart.getLegend();
        lineChartLegend.setEnabled(false);


        XAxis xAxisVolume = chart.getXAxis();
        xAxisVolume.setDrawLabels(true);
        xAxisVolume.setDrawAxisLine(false);
        xAxisVolume.setDrawGridLines(false);
        xAxisVolume.setTextColor(mAxisColor);
        xAxisVolume.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxisVolume.setLabelCount(3, true);
        xAxisVolume.setAvoidFirstLastClipping(true);
        xAxisVolume.setAxisMinimum(-0.5f);

        xAxisVolume.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if (mData.isEmpty()) {
                    return "";
                }
                if (value < 0) {
                    value = 0;
                }
                if (value < mData.size()) {
                    return DateUtils.formatDate(mData.get((int) value).getDate(), mDateFormat);
                }
                return "";
            }
        });

        YAxis axisLeftVolume = chart.getAxisLeft();
        axisLeftVolume.setDrawLabels(true);
        axisLeftVolume.setDrawGridLines(false);
        axisLeftVolume.setLabelCount(3, true);
        axisLeftVolume.setDrawAxisLine(false);
        axisLeftVolume.setTextColor(mAxisColor);
        axisLeftVolume.setSpaceTop(10);
        axisLeftVolume.setSpaceBottom(0);
        axisLeftVolume.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);

        Transformer leftYTransformer = chart.getRendererLeftYAxis().getTransformer();
        ColorContentYAxisRenderer leftColorContentYAxisRenderer = new ColorContentYAxisRenderer(chart.getViewPortHandler(), chart.getAxisLeft(), leftYTransformer);
        leftColorContentYAxisRenderer.setLabelInContent(true);
        leftColorContentYAxisRenderer.setUseDefaultLabelXOffset(false);
        chart.setRendererLeftYAxis(leftColorContentYAxisRenderer);

        //右边y
        YAxis axisRightVolume = chart.getAxisRight();
        axisRightVolume.setDrawLabels(false);
        axisRightVolume.setDrawGridLines(false);
        axisRightVolume.setDrawAxisLine(false);
    }
    protected void moveToLast(CustomCombinedChart chart) {
        if (mData.size() > INIT_COUNT) {
            chart.moveViewToX(mData.size() - INIT_COUNT);
        }
    }

    /**
     * set the count of k chart
     */
    public void setCount(int init, int max, int min) {
        INIT_COUNT = init;
        MAX_COUNT = max;
        MIN_COUNT = min;
    }

    protected void setDescription(Chart chart, String text) {
        Description description = chart.getDescription();
        description.setText(text);
    }

    protected void setDescription(Chart chart, String text, int color) {
        Description description = chart.getDescription();
        description.setTextColor(color);
        description.setText(text);
    }

    public HisData getLastData() {
        if (mData != null && !mData.isEmpty()) {
            return mData.get(mData.size() - 1);
        }
        return null;
    }

    public void setDateFormat(String mDateFormat) {
        this.mDateFormat = mDateFormat;
    }

}

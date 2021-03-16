package com.guoziwei.klinelib.chart;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ICandleDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Transformer;
import com.guoziwei.klinelib.R;
import com.guoziwei.klinelib.model.HisData;
import com.guoziwei.klinelib.util.DataUtils;
import com.guoziwei.klinelib.util.DisplayUtils;
import com.guoziwei.klinelib.util.DoubleUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * kline
 * Created by guoziwei on 2017/10/26.
 */
public class KLineView extends BaseView implements CoupleChartGestureListener.OnAxisChangeListener {


    public static final int NORMAL_LINE = 0;
    /**
     * average line
     */
    public static final int AVE_LINE = 1;
    /**
     * hide line
     */
    public static final int INVISIABLE_LINE = 6;


    public static final int MA5 = 5;
    public static final int MA10 = 10;
    public static final int MA20 = 20;
    public static final int MA30 = 30;

    private boolean isShowMA5 = false;
    private boolean isShowMA10 = false;
    private boolean isShowMA20 = false;
    private boolean isShowMA30 = false;

    public static final int K = 31;
    public static final int D = 32;
    public static final int J = 33;

    public static final int DIF = 34;
    public static final int DEA = 35;


    protected CustomCombinedChart mChartPrice;
    protected CustomCombinedChart mChartVolume;
    protected CustomCombinedChart mChartMacd;
    protected CustomCombinedChart mChartKdj;

    protected ChartInfoView mChartInfoView;
    protected Context mContext;

    /**
     * last price
     */
    private double mLastPrice;

    /**
     * yesterday close price
     */
    private double mLastClose;

    /**
     * the digits of the symbol
     */
    private int mDigits = 2;
    private CoupleChartGestureListener mCoupleChartGestureListener;

    public KLineView(Context context) {
        this(context, null);
    }

    public KLineView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KLineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.view_kline, this);
        mChartPrice = findViewById(R.id.price_chart);
        mChartVolume = findViewById(R.id.vol_chart);
        mChartMacd = findViewById(R.id.macd_chart);
        mChartKdj = findViewById(R.id.kdj_chart);
        mChartInfoView = findViewById(R.id.k_info);
        mChartInfoView.setChart(mChartPrice, mChartVolume, mChartMacd, mChartKdj);

        mChartPrice.setNoDataText("无数据");
        initChartPrice();
        initBottomChart(mChartVolume);
        initBottomChart(mChartMacd);
        initBottomChart(mChartKdj);
        setOffset();
        initChartListener();
    }

    public void showKdj() {
        mChartVolume.setVisibility(GONE);
        mChartMacd.setVisibility(GONE);
        mChartKdj.setVisibility(VISIBLE);
    }

    public void showMacd() {
        mChartVolume.setVisibility(GONE);
        mChartMacd.setVisibility(VISIBLE);
        mChartKdj.setVisibility(GONE);
    }

    public void showVolume() {
        mChartMacd.setVisibility(GONE);
        mChartKdj.setVisibility(GONE);
        mChartVolume.setVisibility(VISIBLE);
    }

    public int getmDigits() {
        return mDigits;
    }

    public void setmDigits(int mDigits) {
        this.mDigits = mDigits;
        YAxis axisLeftPrice = mChartPrice.getAxisLeft();
        axisLeftPrice.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return DoubleUtil.getStringByDigits(value, KLineView.this.mDigits);
            }
        });
    }

    protected void initChartPrice() {
        mChartPrice.setScaleEnabled(true);
        mChartPrice.setDrawBorders(false);
        mChartPrice.setBorderWidth(1);
        mChartPrice.setDragEnabled(true);
        mChartPrice.setScaleYEnabled(false);
        mChartPrice.setAutoScaleMinMaxEnabled(true);
        mChartPrice.setDragDecelerationEnabled(false);
        mChartPrice.setDrawGridBackground(false);
        LineChartXMarkerView mvx = new LineChartXMarkerView(mContext, mData);
        mvx.setChartView(mChartPrice);
        mChartPrice.setXMarker(mvx);
        Legend lineChartLegend = mChartPrice.getLegend();
        lineChartLegend.setEnabled(false);

        XAxis xAxisPrice = mChartPrice.getXAxis();
        xAxisPrice.setDrawLabels(false);
        xAxisPrice.setDrawAxisLine(false);
        xAxisPrice.setDrawGridLines(true);
        xAxisPrice.setGridColor(mGridColor);
        xAxisPrice.setAxisMinimum(-0.5f);

        YAxis rightYAxis = mChartPrice.getAxisRight();
        rightYAxis.setDrawGridLines(true);


        YAxis axisLeftPrice = mChartPrice.getAxisLeft();
        axisLeftPrice.setLabelCount(5, true);
        axisLeftPrice.setDrawLabels(true);
        axisLeftPrice.setDrawGridLines(true);

        axisLeftPrice.setDrawAxisLine(false);
        axisLeftPrice.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        axisLeftPrice.setTextColor(mAxisColor);
        axisLeftPrice.setGridColor(mGridColor);
        axisLeftPrice.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return DoubleUtil.getStringByDigits(value, mDigits);
            }
        });

//        int[] colorArray = {mDecreasingColor, mDecreasingColor, mAxisColor, mIncreasingColor, mIncreasingColor};
        int[] colorArray = {mAxisColor, mAxisColor, mAxisColor, mAxisColor, mAxisColor};
        Transformer leftYTransformer = mChartPrice.getRendererLeftYAxis().getTransformer();
        ColorContentYAxisRenderer leftColorContentYAxisRenderer = new ColorContentYAxisRenderer(mChartPrice.getViewPortHandler(), mChartPrice.getAxisLeft(), leftYTransformer);
        leftColorContentYAxisRenderer.setLabelColor(colorArray);
        leftColorContentYAxisRenderer.setLabelInContent(true);
        leftColorContentYAxisRenderer.setUseDefaultLabelXOffset(false);
        mChartPrice.setRendererLeftYAxis(leftColorContentYAxisRenderer);


        YAxis axisRightPrice = mChartPrice.getAxisRight();
        axisRightPrice.setLabelCount(5, true);
        axisRightPrice.setDrawLabels(true);

        axisRightPrice.setDrawGridLines(false);
        axisRightPrice.setDrawAxisLine(false);
        axisRightPrice.setTextColor(mAxisColor);
        axisRightPrice.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);

        axisRightPrice.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                double rate = (value - mLastClose) / mLastClose * 100;
                if (Double.isNaN(rate) || Double.isInfinite(rate)) {
                    return "";
                }
                String s = String.format(Locale.getDefault(), "%.2f%%",
                        rate);
                if (TextUtils.equals("-0.00%", s)) {
                    return "0.00%";
                }
                return s;
            }
        });

//        设置标签Y渲染器
        Transformer rightYTransformer = mChartPrice.getRendererRightYAxis().getTransformer();
        ColorContentYAxisRenderer rightColorContentYAxisRenderer = new ColorContentYAxisRenderer(mChartPrice.getViewPortHandler(), mChartPrice.getAxisRight(), rightYTransformer);
        rightColorContentYAxisRenderer.setLabelInContent(true);
        rightColorContentYAxisRenderer.setUseDefaultLabelXOffset(false);
        rightColorContentYAxisRenderer.setLabelColor(colorArray);
        mChartPrice.setRendererRightYAxis(rightColorContentYAxisRenderer);

    }


    private void initChartListener() {
        mCoupleChartGestureListener = new CoupleChartGestureListener(this, mChartPrice, mChartVolume, mChartMacd, mChartKdj);
        mChartPrice.setOnChartGestureListener(mCoupleChartGestureListener);
        mChartPrice.setOnChartValueSelectedListener(new InfoViewListener(mContext, mLastClose, mData, mChartInfoView, mChartVolume, mChartMacd, mChartKdj));
        mChartPrice.setOnTouchListener(new ChartInfoViewHandler(mChartPrice));
    }

    public void notifyDataChange() {

        ArrayList<CandleEntry> lineCJEntries = new ArrayList<>(INIT_COUNT);
        ArrayList<Entry> ma5Entries = new ArrayList<>(INIT_COUNT);
        ArrayList<Entry> ma10Entries = new ArrayList<>(INIT_COUNT);
        ArrayList<Entry> ma20Entries = new ArrayList<>(INIT_COUNT);
        ArrayList<Entry> ma30Entries = new ArrayList<>(INIT_COUNT);
        ArrayList<Entry> paddingEntries = new ArrayList<>(INIT_COUNT);

        for (int i = 0; i < mData.size(); i++) {
            HisData hisData = mData.get(i);
            lineCJEntries.add(new CandleEntry(i, (float) hisData.getHigh(), (float) hisData.getLow(), (float) hisData.getOpen(), (float) hisData.getClose()));

            if (!Double.isNaN(hisData.getMa5())) {
                ma5Entries.add(new Entry(i, (float) hisData.getMa5()));
            }

            if (!Double.isNaN(hisData.getMa10())) {
                ma10Entries.add(new Entry(i, (float) hisData.getMa10()));
            }

            if (!Double.isNaN(hisData.getMa20())) {
                ma20Entries.add(new Entry(i, (float) hisData.getMa20()));
            }

            if (!Double.isNaN(hisData.getMa30())) {
                ma30Entries.add(new Entry(i, (float) hisData.getMa30()));
            }
        }

        if (!mData.isEmpty() && mData.size() < MAX_COUNT) {
            for (int i = mData.size(); i < MAX_COUNT; i++) {
                paddingEntries.add(new Entry(i, (float) mData.get(mData.size() - 1).getClose()));
            }
        }

        List<ILineDataSet> lineDataList = new ArrayList<>();
        LineDataSet invisibleLine = setLine(INVISIABLE_LINE, paddingEntries);
        LineDataSet MA5Line = setLine(MA5, ma5Entries);
        LineDataSet MA10Line = setLine(MA10, ma10Entries);
        LineDataSet MA20Line = setLine(MA20, ma20Entries);
        LineDataSet MA30Line = setLine(MA30, ma30Entries);

        lineDataList.add(invisibleLine);
        if (isShowMA5) {
            lineDataList.add(MA5Line);
        }
        if (isShowMA10) {
            lineDataList.add(MA10Line);
        }
        if (isShowMA20) {
            lineDataList.add(MA20Line);
        }
        if (isShowMA30) {
            lineDataList.add(MA30Line);
        }

        LineData lineData = new LineData(lineDataList);
        CombinedData combinedData = mChartPrice.getCombinedData();
        combinedData.setData(lineData);
        mChartPrice.setData(combinedData);

        mChartPrice.notifyDataSetChanged();
        mChartPrice.invalidate();
    }

    public void notifyDataChange(List<HisData> hisDatas, boolean stepChanged) {
        if (hisDatas == null || hisDatas.size() == 0) {
            mChartPrice.clear();
            mChartVolume.clear();
            return;
        }
        mData.clear();

        mData.addAll(DataUtils.calculateHisData(hisDatas));
        mChartPrice.setRealCount(mData.size());

        ArrayList<CandleEntry> lineCJEntries = new ArrayList<>(INIT_COUNT);
        ArrayList<Entry> ma5Entries = new ArrayList<>(INIT_COUNT);
        ArrayList<Entry> ma10Entries = new ArrayList<>(INIT_COUNT);
        ArrayList<Entry> ma20Entries = new ArrayList<>(INIT_COUNT);
        ArrayList<Entry> ma30Entries = new ArrayList<>(INIT_COUNT);
        ArrayList<Entry> paddingEntries = new ArrayList<>(INIT_COUNT);

        for (int i = 0; i < mData.size(); i++) {
            HisData hisData = mData.get(i);
            lineCJEntries.add(new CandleEntry(i, (float) hisData.getHigh(), (float) hisData.getLow(), (float) hisData.getOpen(), (float) hisData.getClose()));

            if (!Double.isNaN(hisData.getMa5())) {
                ma5Entries.add(new Entry(i, (float) hisData.getMa5()));
            }

            if (!Double.isNaN(hisData.getMa10())) {
                ma10Entries.add(new Entry(i, (float) hisData.getMa10()));
            }

            if (!Double.isNaN(hisData.getMa20())) {
                ma20Entries.add(new Entry(i, (float) hisData.getMa20()));
            }

            if (!Double.isNaN(hisData.getMa30())) {
                ma30Entries.add(new Entry(i, (float) hisData.getMa30()));
            }
        }

        if (!mData.isEmpty() && mData.size() < MAX_COUNT) {
            for (int i = mData.size(); i < MAX_COUNT; i++) {
                paddingEntries.add(new Entry(i, (float) mData.get(mData.size() - 1).getClose()));
            }
        }

        List<ILineDataSet> lineDataList = new ArrayList<>();
        LineDataSet invisibleLine = setLine(INVISIABLE_LINE, paddingEntries);
        LineDataSet MA5Line = setLine(MA5, ma5Entries);
        LineDataSet MA10Line = setLine(MA10, ma10Entries);
        LineDataSet MA20Line = setLine(MA20, ma20Entries);
        LineDataSet MA30Line = setLine(MA30, ma30Entries);

        lineDataList.add(invisibleLine);
        if (isShowMA5) {
            lineDataList.add(MA5Line);
        }
        if (isShowMA10) {
            lineDataList.add(MA10Line);
        }
        if (isShowMA20) {
            lineDataList.add(MA20Line);
        }
        if (isShowMA30) {
            lineDataList.add(MA30Line);
        }


        LineData lineData = new LineData(lineDataList);
        CandleData candleData = new CandleData(setKLine(NORMAL_LINE, lineCJEntries));
        CombinedData combinedData = new CombinedData();
        combinedData.setData(lineData);
        combinedData.setData(candleData);
        combinedData.setValueTextColor(mAxisColor);
        mChartPrice.setData(combinedData);
        mChartPrice.notifyDataSetChanged();
        mChartPrice.invalidate();
        mChartPrice.setVisibleXRangeMaximum(MAX_COUNT);
        mChartPrice.setVisibleXRangeMinimum(MIN_COUNT);

        if (stepChanged) {
            mChartPrice.moveViewToX(0);
            moveToLast(mChartPrice);
        }
        initChartVolumeData(stepChanged);

        mChartVolume.notifyDataSetChanged();
        mChartVolume.invalidate();

    }

    public void initData(List<HisData> hisDatas) {
        if (hisDatas == null || hisDatas.size() == 0) {
            mChartPrice.clear();
            mChartVolume.clear();
            return;
        }
        mData.clear();

        mData.addAll(DataUtils.calculateHisData(hisDatas));
        mChartPrice.setRealCount(mData.size());

        ArrayList<CandleEntry> lineCJEntries = new ArrayList<>(INIT_COUNT);
        ArrayList<Entry> ma5Entries = new ArrayList<>(INIT_COUNT);
        ArrayList<Entry> ma10Entries = new ArrayList<>(INIT_COUNT);
        ArrayList<Entry> ma20Entries = new ArrayList<>(INIT_COUNT);
        ArrayList<Entry> ma30Entries = new ArrayList<>(INIT_COUNT);
        ArrayList<Entry> paddingEntries = new ArrayList<>(INIT_COUNT);

        for (int i = 0; i < mData.size(); i++) {
            HisData hisData = mData.get(i);
            lineCJEntries.add(new CandleEntry(i, (float) hisData.getHigh(), (float) hisData.getLow(), (float) hisData.getOpen(), (float) hisData.getClose()));

            if (!Double.isNaN(hisData.getMa5())) {
                ma5Entries.add(new Entry(i, (float) hisData.getMa5()));
            }

            if (!Double.isNaN(hisData.getMa10())) {
                ma10Entries.add(new Entry(i, (float) hisData.getMa10()));
            }

            if (!Double.isNaN(hisData.getMa20())) {
                ma20Entries.add(new Entry(i, (float) hisData.getMa20()));
            }

            if (!Double.isNaN(hisData.getMa30())) {
                ma30Entries.add(new Entry(i, (float) hisData.getMa30()));
            }
        }

        if (!mData.isEmpty() && mData.size() < MAX_COUNT) {
            for (int i = mData.size(); i < MAX_COUNT; i++) {
                paddingEntries.add(new Entry(i, (float) mData.get(mData.size() - 1).getClose()));
            }
        }

        List<ILineDataSet> lineDataList = new ArrayList<>();
        LineDataSet invisibleLine = setLine(INVISIABLE_LINE, paddingEntries);
        LineDataSet MA5Line = setLine(MA5, ma5Entries);
        LineDataSet MA10Line = setLine(MA10, ma10Entries);
        LineDataSet MA20Line = setLine(MA20, ma20Entries);
        LineDataSet MA30Line = setLine(MA30, ma30Entries);

        lineDataList.add(invisibleLine);
        if (isShowMA5) {
            lineDataList.add(MA5Line);
        }
        if (isShowMA10) {
            lineDataList.add(MA10Line);
        }
        if (isShowMA20) {
            lineDataList.add(MA20Line);
        }
        if (isShowMA30) {
            lineDataList.add(MA30Line);
        }

        LineData lineData = new LineData(lineDataList);
        CandleData candleData = new CandleData(setKLine(NORMAL_LINE, lineCJEntries));
        CombinedData combinedData = new CombinedData();
        combinedData.setData(lineData);
        combinedData.setData(candleData);
        combinedData.setValueTextColor(mAxisColor);
        mChartPrice.setData(combinedData);
        mChartPrice.notifyDataSetChanged();
        mChartPrice.setVisibleXRangeMaximum(MAX_COUNT);
        mChartPrice.setVisibleXRangeMinimum(MIN_COUNT);

        mChartPrice.moveViewToX(0);
        moveToLast(mChartPrice);
        initChartVolumeData(true);

        mChartPrice.zoom(3, 0, 0, 0);
        mChartVolume.zoom(3, 0, 0, 0);

        HisData hisData = getLastData();

        //如果没有数据时会崩溃
        if (hisData != null) {
            setDescription(mChartVolume, mContext.getString(R.string.volume) + hisData.getVol(), mAxisColor);
            setDescription(mChartPrice, "", mAxisColor);
        }

    }

    public void setShowMA5(boolean showMA5) {
        isShowMA5 = showMA5;
    }

    public void setShowMA10(boolean showMA10) {
        isShowMA10 = showMA10;
    }

    public void setShowMA20(boolean showMA20) {
        isShowMA20 = showMA20;
    }

    public void setShowMA30(boolean showMA30) {
        isShowMA30 = showMA30;
    }

    private BarDataSet setBar(ArrayList<BarEntry> barEntries, int type) {
        BarDataSet barDataSet = new BarDataSet(barEntries, "vol");
        barDataSet.setHighLightAlpha(120);
        barDataSet.setHighLightColor(mHighLightColor);
        barDataSet.setDrawValues(false);
        barDataSet.setVisible(type != INVISIABLE_LINE);
        barDataSet.setHighlightEnabled(type != INVISIABLE_LINE);
        barDataSet.setColors(mIncreasingColor, mDecreasingColor);
        return barDataSet;
    }


    @androidx.annotation.NonNull
    private LineDataSet setLine(int type, ArrayList<Entry> lineEntries) {
        LineDataSet lineDataSetMa = new LineDataSet(lineEntries, "ma" + type);
        lineDataSetMa.setDrawValues(false);
        if (type == NORMAL_LINE) {
            lineDataSetMa.setColor(getResources().getColor(R.color.normal_line_color));
            lineDataSetMa.setCircleColor(ContextCompat.getColor(mContext, R.color.normal_line_color));
        } else if (type == K) {
            lineDataSetMa.setColor(getResources().getColor(R.color.k));
            lineDataSetMa.setCircleColor(mTransparentColor);
        } else if (type == D) {
            lineDataSetMa.setColor(getResources().getColor(R.color.d));
            lineDataSetMa.setCircleColor(mTransparentColor);
            lineDataSetMa.setHighlightEnabled(false);
        } else if (type == J) {
            lineDataSetMa.setColor(getResources().getColor(R.color.j));
            lineDataSetMa.setCircleColor(mTransparentColor);
            lineDataSetMa.setHighlightEnabled(false);
        } else if (type == DIF) {
            lineDataSetMa.setColor(getResources().getColor(R.color.dif));
            lineDataSetMa.setCircleColor(mTransparentColor);
            lineDataSetMa.setHighlightEnabled(false);
        } else if (type == DEA) {
            lineDataSetMa.setColor(getResources().getColor(R.color.dea));
            lineDataSetMa.setCircleColor(mTransparentColor);
            lineDataSetMa.setHighlightEnabled(false);
        } else if (type == AVE_LINE) {
            lineDataSetMa.setColor(getResources().getColor(R.color.ave_color));
            lineDataSetMa.setCircleColor(mTransparentColor);
            lineDataSetMa.setHighlightEnabled(false);
        } else if (type == MA5) {
            lineDataSetMa.setColor(Color.BLUE);
            lineDataSetMa.setCircleColor(mTransparentColor);
            lineDataSetMa.setHighlightEnabled(false);
        } else if (type == MA10) {
            lineDataSetMa.setColor(0xff29ac4e);
            lineDataSetMa.setCircleColor(mTransparentColor);
            lineDataSetMa.setHighlightEnabled(false);
        } else if (type == MA20) {
            lineDataSetMa.setColor(Color.RED);
            lineDataSetMa.setCircleColor(mTransparentColor);
            lineDataSetMa.setHighlightEnabled(false);
        } else if (type == MA30) {
            lineDataSetMa.setColor(0xfff19149);
            lineDataSetMa.setCircleColor(mTransparentColor);
            lineDataSetMa.setHighlightEnabled(false);
        } else {
            lineDataSetMa.setVisible(false);
            lineDataSetMa.setHighlightEnabled(false);
        }
        lineDataSetMa.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSetMa.setLineWidth(1f);
        lineDataSetMa.setCircleRadius(1f);

        lineDataSetMa.setDrawCircles(false);
        lineDataSetMa.setDrawCircleHole(false);

        return lineDataSetMa;
    }

    @androidx.annotation.NonNull
    public CandleDataSet setKLine(int type, ArrayList<CandleEntry> lineEntries) {
        CandleDataSet set = new CandleDataSet(lineEntries, "KLine" + type);
        set.setDrawIcons(false);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setShadowColor(Color.DKGRAY);
        set.setShadowWidth(0.75f);
        set.setDecreasingColor(mDecreasingColor);
        set.setDecreasingPaintStyle(Paint.Style.FILL);
        set.setShadowColorSameAsCandle(true);
        set.setIncreasingColor(mIncreasingColor);
        set.setNeutralColor(mIncreasingColor);
        set.setIncreasingPaintStyle(Paint.Style.FILL);
        set.setDrawValues(true);
        set.setValueTextSize(10);
        set.setHighlightEnabled(true);
        set.setHighLightColor(mHighLightColor);
        if (type != NORMAL_LINE) {
            set.setVisible(false);
        }
        return set;
    }

    private void initChartVolumeData(boolean isStepChanged) {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        ArrayList<BarEntry> paddingEntries = new ArrayList<>();
        for (int i = 0; i < mData.size(); i++) {
            HisData t = mData.get(i);
            barEntries.add(new BarEntry(i, (float) t.getVol(), t));
        }
        int maxCount = MAX_COUNT;
        if (!mData.isEmpty() && mData.size() < maxCount) {
            for (int i = mData.size(); i < maxCount; i++) {
                paddingEntries.add(new BarEntry(i, 0));
            }
        }

        BarData barData = new BarData(setBar(barEntries, NORMAL_LINE), setBar(paddingEntries, INVISIABLE_LINE));
        barData.setBarWidth(0.75f);
        CombinedData combinedData = new CombinedData();
        combinedData.setData(barData);
        mChartVolume.setData(combinedData);

        mChartVolume.setVisibleXRange(MAX_COUNT, MIN_COUNT);

        mChartVolume.notifyDataSetChanged();
        if (isStepChanged) {
            mChartVolume.moveViewToX(0);
            moveToLast(mChartVolume);
        }

    }

    private void initChartMacdData() {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        ArrayList<BarEntry> paddingEntries = new ArrayList<>();
        ArrayList<Entry> difEntries = new ArrayList<>();
        ArrayList<Entry> deaEntries = new ArrayList<>();
        for (int i = 0; i < mData.size(); i++) {
            HisData t = mData.get(i);
            barEntries.add(new BarEntry(i, (float) t.getMacd()));
            difEntries.add(new Entry(i, (float) t.getDif()));
            deaEntries.add(new Entry(i, (float) t.getDea()));
        }
        int maxCount = MAX_COUNT;
        if (!mData.isEmpty() && mData.size() < maxCount) {
            for (int i = mData.size(); i < maxCount; i++) {
                paddingEntries.add(new BarEntry(i, 0));
            }
        }
        BarData barData = new BarData(setBar(barEntries, NORMAL_LINE), setBar(paddingEntries, INVISIABLE_LINE));
        barData.setBarWidth(0.75f);
        CombinedData combinedData = new CombinedData();
        combinedData.setData(barData);
        LineData lineData = new LineData(setLine(DIF, difEntries), setLine(DEA, deaEntries));
        combinedData.setData(lineData);
        mChartMacd.setData(combinedData);

        mChartMacd.setVisibleXRange(MAX_COUNT, MIN_COUNT);

        mChartMacd.notifyDataSetChanged();
        moveToLast(mChartMacd);
    }

    private void initChartKdjData() {
        ArrayList<Entry> kEntries = new ArrayList<>(INIT_COUNT);
        ArrayList<Entry> dEntries = new ArrayList<>(INIT_COUNT);
        ArrayList<Entry> jEntries = new ArrayList<>(INIT_COUNT);
        ArrayList<Entry> paddingEntries = new ArrayList<>(INIT_COUNT);

        for (int i = 0; i < mData.size(); i++) {
            kEntries.add(new Entry(i, (float) mData.get(i).getK()));
            dEntries.add(new Entry(i, (float) mData.get(i).getD()));
            jEntries.add(new Entry(i, (float) mData.get(i).getJ()));
        }
        if (!mData.isEmpty() && mData.size() < MAX_COUNT) {
            for (int i = mData.size(); i < MAX_COUNT; i++) {
                paddingEntries.add(new Entry(i, (float) mData.get(mData.size() - 1).getK()));
            }
        }
        ArrayList<ILineDataSet> sets = new ArrayList<>();
        sets.add(setLine(K, kEntries));
        sets.add(setLine(D, dEntries));
        sets.add(setLine(J, jEntries));
        sets.add(setLine(INVISIABLE_LINE, paddingEntries));
        LineData lineData = new LineData(sets);

        CombinedData combinedData = new CombinedData();
        combinedData.setData(lineData);
        mChartKdj.setData(combinedData);

        mChartMacd.setVisibleXRange(MAX_COUNT, MIN_COUNT);

        mChartKdj.notifyDataSetChanged();
        moveToLast(mChartKdj);
    }


    /**
     * according to the price to refresh the last data of the chart
     */
    public void refreshData(float price) {
        if (price <= 0 || price == mLastPrice) {
            return;
        }
        mLastPrice = price;
        CombinedData data = mChartPrice.getData();
        if (data == null) return;
        LineData lineData = data.getLineData();
        if (lineData != null) {
            ILineDataSet set = lineData.getDataSetByIndex(0);
            if (set.removeLast()) {
                set.addEntry(new Entry(set.getEntryCount(), price));
            }
        }
        CandleData candleData = data.getCandleData();
        if (candleData != null) {
            ICandleDataSet set = candleData.getDataSetByIndex(0);
            if (set.removeLast()) {
                HisData hisData = mData.get(mData.size() - 1);
                hisData.setClose(price);
                hisData.setHigh(Math.max(hisData.getHigh(), price));
                hisData.setLow(Math.min(hisData.getLow(), price));
                set.addEntry(new CandleEntry(set.getEntryCount(), (float) hisData.getHigh(), (float) hisData.getLow(), (float) hisData.getOpen(), price));

            }
        }
        mChartPrice.notifyDataSetChanged();
        mChartPrice.invalidate();
    }

    public void addDatas(List<HisData> hisDatas) {
        for (HisData hisData : hisDatas) {
            addData(hisData);
        }
    }

    public void addData(HisData hisData) {
        hisData = DataUtils.calculateHisData(hisData, mData);
        CombinedData combinedData = mChartPrice.getData();
        LineData priceData = combinedData.getLineData();
        ILineDataSet padding = priceData.getDataSetByIndex(0);
        ILineDataSet ma5Set = priceData.getDataSetByIndex(1);
        ILineDataSet ma10Set = priceData.getDataSetByIndex(2);
        ILineDataSet ma20Set = priceData.getDataSetByIndex(3);
        ILineDataSet ma30Set = priceData.getDataSetByIndex(4);
        CandleData kData = combinedData.getCandleData();
        ICandleDataSet klineSet = kData.getDataSetByIndex(0);
        IBarDataSet volSet = mChartVolume.getData().getBarData().getDataSetByIndex(0);
        IBarDataSet macdSet = mChartMacd.getData().getBarData().getDataSetByIndex(0);
        ILineDataSet difSet = mChartMacd.getData().getLineData().getDataSetByIndex(0);
        ILineDataSet deaSet = mChartMacd.getData().getLineData().getDataSetByIndex(1);
        LineData kdjData = mChartKdj.getData().getLineData();
        ILineDataSet kSet = kdjData.getDataSetByIndex(0);
        ILineDataSet dSet = kdjData.getDataSetByIndex(1);
        ILineDataSet jSet = kdjData.getDataSetByIndex(2);

        if (mData.contains(hisData)) {
            int index = mData.indexOf(hisData);
            klineSet.removeEntry(index);
            padding.removeFirst();
            // ma比较特殊，entry数量和k线的不一致，移除最后一个
            ma5Set.removeLast();
            ma10Set.removeLast();
            ma20Set.removeLast();
            ma30Set.removeLast();
            volSet.removeEntry(index);
            macdSet.removeEntry(index);
            difSet.removeEntry(index);
            deaSet.removeEntry(index);
            kSet.removeEntry(index);
            dSet.removeEntry(index);
            jSet.removeEntry(index);
            mData.remove(index);
        }
        mData.add(hisData);
        mChartPrice.setRealCount(mData.size());
        int klineCount = klineSet.getEntryCount();
        klineSet.addEntry(new CandleEntry(klineCount, (float) hisData.getHigh(), (float) hisData.getLow(), (float) hisData.getOpen(), (float) hisData.getClose()));
        volSet.addEntry(new BarEntry(volSet.getEntryCount(), hisData.getVol(), hisData));

        macdSet.addEntry(new BarEntry(macdSet.getEntryCount(), (float) hisData.getMacd()));
        difSet.addEntry(new Entry(difSet.getEntryCount(), (float) hisData.getDif()));
        deaSet.addEntry(new Entry(deaSet.getEntryCount(), (float) hisData.getDea()));

        kSet.addEntry(new Entry(kSet.getEntryCount(), (float) hisData.getK()));
        dSet.addEntry(new Entry(dSet.getEntryCount(), (float) hisData.getD()));
        jSet.addEntry(new Entry(jSet.getEntryCount(), (float) hisData.getJ()));

        // 因为ma的数量会少，所以这里用kline的set数量作为x
        if (!Double.isNaN(hisData.getMa5())) {
            ma5Set.addEntry(new Entry(klineCount, (float) hisData.getMa5()));
        }
        if (!Double.isNaN(hisData.getMa10())) {
            ma10Set.addEntry(new Entry(klineCount, (float) hisData.getMa10()));
        }
        if (!Double.isNaN(hisData.getMa20())) {
            ma20Set.addEntry(new Entry(klineCount, (float) hisData.getMa20()));
        }
        if (!Double.isNaN(hisData.getMa30())) {
            ma30Set.addEntry(new Entry(klineCount, (float) hisData.getMa30()));
        }


        mChartPrice.getXAxis().setAxisMaximum(combinedData.getXMax() + 1.5f);
        mChartVolume.getXAxis().setAxisMaximum(mChartVolume.getData().getXMax() + 1.5f);
        mChartMacd.getXAxis().setAxisMaximum(mChartMacd.getData().getXMax() + 1.5f);
        mChartKdj.getXAxis().setAxisMaximum(mChartKdj.getData().getXMax() + 1.5f);


        mChartPrice.setVisibleXRange(MAX_COUNT, MIN_COUNT);
        mChartVolume.setVisibleXRange(MAX_COUNT, MIN_COUNT);
        mChartMacd.setVisibleXRange(MAX_COUNT, MIN_COUNT);
        mChartKdj.setVisibleXRange(MAX_COUNT, MIN_COUNT);

        mChartPrice.notifyDataSetChanged();
        mChartPrice.invalidate();
        mChartVolume.notifyDataSetChanged();
        mChartVolume.invalidate();
        mChartMacd.notifyDataSetChanged();
        mChartMacd.invalidate();
        mChartKdj.notifyDataSetChanged();
        mChartKdj.invalidate();


        setChartDescription(hisData);

    }

    public void addDatasFirst(List<HisData> hisDatas) {
        CombinedData combinedData = mChartPrice.getData();
        LineData priceData = combinedData.getLineData();
        ILineDataSet padding = priceData.getDataSetByIndex(0);
        ILineDataSet ma5Set = priceData.getDataSetByIndex(1);
        ILineDataSet ma10Set = priceData.getDataSetByIndex(2);
        ILineDataSet ma20Set = priceData.getDataSetByIndex(3);
        ILineDataSet ma30Set = priceData.getDataSetByIndex(4);
        CandleData kData = combinedData.getCandleData();
        ICandleDataSet klineSet = kData.getDataSetByIndex(0);
        IBarDataSet volSet = mChartVolume.getData().getBarData().getDataSetByIndex(0);
        IBarDataSet macdSet = mChartMacd.getData().getBarData().getDataSetByIndex(0);
        ILineDataSet difSet = mChartMacd.getData().getLineData().getDataSetByIndex(0);
        ILineDataSet deaSet = mChartMacd.getData().getLineData().getDataSetByIndex(1);
        LineData kdjData = mChartKdj.getData().getLineData();
        ILineDataSet kSet = kdjData.getDataSetByIndex(0);
        ILineDataSet dSet = kdjData.getDataSetByIndex(1);
        ILineDataSet jSet = kdjData.getDataSetByIndex(2);

        mData.addAll(0, hisDatas);
        // 这里需要重新绘制图表，把之前的图表清理掉
        klineSet.clear();
        padding.clear();
        ma5Set.clear();
        ma10Set.clear();
        ma20Set.clear();
        ma30Set.clear();
        volSet.clear();
        macdSet.clear();
        difSet.clear();
        deaSet.clear();
        deaSet.clear();
        kSet.clear();
        dSet.clear();
        jSet.clear();

        // 重新计算各个指标
        DataUtils.calculateHisData(mData);
        mChartPrice.setRealCount(mData.size());
        for (int i = 0; i < mData.size(); i++) {
            HisData hisData = mData.get(i);
            klineSet.addEntry(new CandleEntry(i, (float) hisData.getHigh(), (float) hisData.getLow(), (float) hisData.getOpen(), (float) hisData.getClose()));

            if (!Double.isNaN(hisData.getMa5())) {
                ma5Set.addEntry(new Entry(i, (float) hisData.getMa5()));
            }

            if (!Double.isNaN(hisData.getMa10())) {
                ma10Set.addEntry(new Entry(i, (float) hisData.getMa10()));
            }

            if (!Double.isNaN(hisData.getMa20())) {
                ma20Set.addEntry(new Entry(i, (float) hisData.getMa20()));
            }

            if (!Double.isNaN(hisData.getMa30())) {
                ma30Set.addEntry(new Entry(i, (float) hisData.getMa30()));
            }
            volSet.addEntry(new BarEntry(i, hisData.getVol(), hisData));

            macdSet.addEntry(new BarEntry(i, (float) hisData.getMacd()));
            difSet.addEntry(new Entry(i, (float) hisData.getDif()));
            deaSet.addEntry(new Entry(i, (float) hisData.getDea()));

            kSet.addEntry(new Entry(i, (float) hisData.getK()));
            dSet.addEntry(new Entry(i, (float) hisData.getD()));
            jSet.addEntry(new Entry(i, (float) hisData.getJ()));
        }


        mChartPrice.setVisibleXRange(MAX_COUNT, MIN_COUNT);
        mChartVolume.setVisibleXRange(MAX_COUNT, MIN_COUNT);
        mChartMacd.setVisibleXRange(MAX_COUNT, MIN_COUNT);
        mChartKdj.setVisibleXRange(MAX_COUNT, MIN_COUNT);

        mChartPrice.moveViewToX(hisDatas.size() - 0.5f);
        mChartVolume.moveViewToX(hisDatas.size() - 0.5f);
        mChartMacd.moveViewToX(hisDatas.size() - 0.5f);
        mChartKdj.moveViewToX(hisDatas.size() - 0.5f);

        mChartPrice.notifyDataSetChanged();
        mChartPrice.invalidate();
        mChartVolume.notifyDataSetChanged();
        mChartVolume.invalidate();
        mChartMacd.notifyDataSetChanged();
        mChartMacd.invalidate();
        mChartKdj.notifyDataSetChanged();
        mChartKdj.invalidate();


        HisData hisData = mData.get(0);
        setChartDescription(hisData);

    }

    private void setChartDescription(HisData hisData) {
        if (hisData == null) {
            return;
        }
//        setDescription(mChartPrice, String.format(Locale.getDefault(), "MA5:%.2f  MA10:%.2f  MA20:%.2f  MA30:%.2f",
//                hisData.getMa5(), hisData.getMa10(), hisData.getMa20(), hisData.getMa30()));
        setDescription(mChartVolume, mContext.getString(R.string.volume) + hisData.getVol());
        setDescription(mChartMacd, String.format(Locale.getDefault(), "MACD:%.2f  DEA:%.2f  DIF:%.2f",
                hisData.getMacd(), hisData.getDea(), hisData.getDif()));
        setDescription(mChartKdj, String.format(Locale.getDefault(), "K:%.2f  D:%.2f  J:%.2f",
                hisData.getK(), hisData.getD(), hisData.getJ()));
    }


    /**
     * align two chart
     */
    private void setOffset() {
        int chartHeight = getResources().getDimensionPixelSize(R.dimen.bottom_chart_height);
        mChartPrice.setViewPortOffsets(0, 0, 0, chartHeight);
        int bottom = DisplayUtils.dip2px(mContext, 20);
        mChartVolume.setViewPortOffsets(0, 0, 0, bottom);
        mChartMacd.setViewPortOffsets(0, 0, 0, bottom);
        mChartKdj.setViewPortOffsets(0, 0, 0, bottom);
    }


    /**
     * add limit line to chart
     */
    public void setLimitLine(double lastClose) {
        LimitLine limitLine = new LimitLine((float) lastClose);
        limitLine.enableDashedLine(5, 10, 0);
        limitLine.setLineColor(getResources().getColor(R.color.limit_color));
        mChartPrice.getAxisLeft().addLimitLine(limitLine);
    }

    public void setLimitLine() {
        setLimitLine(mLastClose);
    }

    public void setLastClose(double lastClose) {
        mLastClose = lastClose;
        mChartPrice.setOnChartValueSelectedListener(new InfoViewListener(mContext, mLastClose, mData, mChartInfoView, mChartVolume, mChartMacd, mChartKdj));
        mChartVolume.setOnChartValueSelectedListener(new InfoViewListener(mContext, mLastClose, mData, mChartInfoView, mChartPrice, mChartMacd, mChartKdj));
        mChartMacd.setOnChartValueSelectedListener(new InfoViewListener(mContext, mLastClose, mData, mChartInfoView, mChartPrice, mChartVolume, mChartKdj));
        mChartKdj.setOnChartValueSelectedListener(new InfoViewListener(mContext, mLastClose, mData, mChartInfoView, mChartPrice, mChartVolume, mChartMacd));

    }


    @Override
    public void onAxisChange(BarLineChartBase chart) {
        float lowestVisibleX = chart.getLowestVisibleX();
        if (lowestVisibleX <= chart.getXAxis().getAxisMinimum()) return;
        int maxX = (int) chart.getHighestVisibleX();
        int x = Math.min(maxX, mData.size() - 1);
        HisData hisData = mData.get(x < 0 ? 0 : x);
        setChartDescription(hisData);
    }

    public void setOnLoadMoreListener(OnLoadMoreListener l) {
        if (mCoupleChartGestureListener != null) {
            mCoupleChartGestureListener.setOnLoadMoreListener(l);
        }
    }

    public void loadMoreComplete() {
        if (mCoupleChartGestureListener != null) {
            mCoupleChartGestureListener.loadMoreComplete();
        }
    }

    @Override
    public void setDateFormat(String mDateFormat) {
        super.setDateFormat(mDateFormat);
        ((KLineChartInfoView) mChartInfoView).setDateFormate(mDateFormat);
    }
}

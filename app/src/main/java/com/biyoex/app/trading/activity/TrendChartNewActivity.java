package com.biyoex.app.trading.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;

import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.github.fujianlian.klinechart.DataHelper;
import com.github.fujianlian.klinechart.KLineChartAdapter;
import com.github.fujianlian.klinechart.KLineChartView;
import com.github.fujianlian.klinechart.KLineEntity;
import com.github.fujianlian.klinechart.draw.Status;
import com.github.fujianlian.klinechart.formatter.DateFormatter;
import com.github.fujianlian.klinechart.formatter.ValueFormatter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.biyoex.app.R;
import com.biyoex.app.common.Constants;
import com.biyoex.app.common.base.RxBus;
import com.biyoex.app.common.base.RxBusData;

import com.biyoex.app.common.mvpbase.BaseActivity;
import com.biyoex.app.common.okhttp.SocketUtils;
import com.biyoex.app.common.utils.ActivityManagerUtils;
import com.biyoex.app.common.utils.GsonUtil;
import com.biyoex.app.common.utils.LanguageUtils;
import com.biyoex.app.common.utils.MoneyUtils;
import com.biyoex.app.common.utils.SharedPreferencesUtils;
import com.biyoex.app.common.widget.CoinSelectPopoWindow;
import com.biyoex.app.common.widget.MyViewPager;
import com.biyoex.app.common.widget.ShareDialog;
import com.biyoex.app.home.adapter.HomeVpAdapter;
import com.biyoex.app.home.bean.CoinTradeRankBean;
import com.biyoex.app.market.fragment.NowMarketFragment;
import com.biyoex.app.trading.bean.MarketRealBean;
import com.biyoex.app.trading.bean.RealBean;
import com.biyoex.app.trading.bean.RefreshUserInfoBean;
import com.biyoex.app.trading.bean.TabEntity;
import com.biyoex.app.trading.fragmnet.CoinDetailsFragment;
import com.biyoex.app.trading.fragmnet.KlineEntrustFragment;
import com.biyoex.app.trading.persenter.TrendChartPersenter;
import com.biyoex.app.trading.view.TrendChartView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;

import static com.biyoex.app.VBTApplication.FALL_COLOR;
import static com.biyoex.app.VBTApplication.RISE_COLOR;

/***
 * 行情走势图
 * */

public class TrendChartNewActivity extends BaseActivity<TrendChartPersenter> implements TrendChartView, SocketUtils.OnReceiveMessageListener, CoinSelectPopoWindow.OnPopupWindowItemClickLiseser {
    @BindView(R.id.iv_fall_back)
    ImageView ivFallBack;
    @BindView(R.id.tv_kline_coinname)
    TextView tvTitle;
    @BindView(R.id.st_website_name)
    CommonTabLayout stWebsiteName;
    @BindView(R.id.tv_ma5)
    TextView tvMa5;
    @BindView(R.id.tv_ma10)
    TextView tvMa10;
    @BindView(R.id.tv_ma20)
    TextView tvMa20;
    @BindView(R.id.tv_ma30)
    TextView tvMa30;
    @BindView(R.id.ll_kline)
    LinearLayout llKline;
    @BindView(R.id.klineView)
    KLineChartView kLineView;
    @BindView(R.id.scrollview_trend_chart)
    NestedScrollView scrollView;
    @BindView(R.id.tv_lowest_price)
    TextView tvLowestPrice;
    @BindView(R.id.tv_highest_price)
    TextView tvHighestPrice;
    @BindView(R.id.tv_day_volume)
    TextView tvDayVolume;
    @BindView(R.id.tv_actual_price)
    TextView tvActualPrice;
    @BindView(R.id.iv_up_down)
    ImageView ivUpDown;
    @BindView(R.id.tv_up_down)
    TextView tvUpDown;
    @BindView(R.id.tv_rmb_value)
    TextView tvRmbValue;
    @BindView(R.id.tab_tren_chart)
    TabLayout mTabLayout;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.iv_kline_fons)
    ImageView ivKlineFons;
    @BindView(R.id.iv_kline_share)
    ImageView ivKlineShare;
    @BindView(R.id.kline_vp)
    MyViewPager mViewPager;
    private TextView macdText, kdjText, rsiText, wrText, maText, bollText, mainHide, subHide;
    private String[] mTitles;

    private CoinTradeRankBean.DealDatasBean coin;
    private String intStep = "15m.json";
    private String group;
    private SimpleDateFormat sdfTime;
    private String formate;

    private String lastStep = "";

    private int subIndex = -1;
//    private double rate;

    private boolean isCollection = false;
    private BaseQuickAdapter<List<String>, BaseViewHolder> soldRecordAdapter;
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private ArrayList<String> mTabList = new ArrayList<>();
    private ArrayList<TextView> subTexts;
    private List<List<String>> soldRecordList;
    private List<Fragment> fragments;
    private KLineChartAdapter mKlineAdapter = new KLineChartAdapter();
    private PopupWindow popupWindow;
    private PopupWindow selectTimePopupWindow;
    CoinSelectPopoWindow popuCoinSelect;
    Timer timer = new Timer();
    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            // 需要做的事:发送消息
            Message message = new Message();
            message.what = 0x1649;
            handler.sendMessage(message);
        }
    };
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x1649) {
                mPresent.getCoinData(coin.getFid());
//                requestPeriod();
                mPresent.requestKlineInfo(intStep + "", coin.getFid() + "");
            } else if (msg.what == 0x1305) {
                SocketUtils.getIns().setOnReceiveMessageListener(TrendChartNewActivity.this);
                SocketUtils.getIns().requestWebSocketInfo(coin.getFid() + "", (String) msg.obj);
            }
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_trend_chart_kt;
    }


    /**
     * 判断是否已经收藏
     *
     * @return true
     */
    public boolean isCollectioned() {
        String coins = SharedPreferencesUtils.getInstance().getString("coin_id", "");
        return coins.contains("," + coin.getFid() + ",");
    }

    @SuppressLint({"ClickableViewAccessibility", "SimpleDateFormat"})
    @Override
    public void initData() {
        Intent itSymbol = getIntent();
//       itSymbol.getParcelableExtra()
        popupWindow = new PopupWindow(this);
        coin = (CoinTradeRankBean.DealDatasBean) itSymbol.getSerializableExtra("coin");
        if (coin == null) {
            return;
        }
        subTexts = new ArrayList<>();
        fragments = new ArrayList<>();
        showPopWindows();
        kLineView.setGridColumns(4);
        kLineView.setGridRows(4);
        kLineView.setChildDraw(0);
        kLineView.setMainDrawLine(false);
        kLineView.setAdapter(mKlineAdapter);
        tvTitle.setText(coin.getFname_sn());
        ValueFormatter.mPriceDecimals = coin.getPriceDecimals();
        group = itSymbol.getIntExtra("isnow", 0) == 1 ? "POC+" : coin.getGroup();
        mTitles = new String[]{getString(R.string.minute_time), "15" + getString(R.string.minute), "1" + getString(R.string.hour), getString(R.string.day_line), getString(R.string.text_again_kline)};
        mPresent.requestMarketRefresh(coin.getFid() + "");
        //处理scrollview和K线VIew冲突
        kLineView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                scrollView.requestDisallowInterceptTouchEvent(false);
            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                if (kLineView.isLongPress()) {
                    scrollView.requestDisallowInterceptTouchEvent(true);
                }
            }
            return false;
        });
//        Map<String, RateBean.RateInfo> map = RateLivedata.getIns().getValue();
//        if (map != null && map.containsKey(coin.getGroup())) {
//            if (LanguageUtils.currentLanguage == 1) {
//                rate = map.get(coin.getGroup()).getRmbPrice();
//            } else {
//                rate = map.get(coin.getGroup()).getUsdtPrice();
//            }
//        }

        for (String mTitle : mTitles) {
            mTabEntities.add(new TabEntity(mTitle, 0, 0));
        }
        //设置底部fragment
        stWebsiteName.setTabData(mTabEntities);
        formate = "HH:mm";
        sdfTime = new SimpleDateFormat("HH:mm");
        sdfTime.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
        stWebsiteName.setCurrentTab(1);
        mTabList.add(getString(R.string.current_entrust));
        mTabList.add(getString(R.string.now_deal));
        mTabList.add(getString(R.string.coin_details));
        fragments.add(new KlineEntrustFragment(0, coin, mViewPager));
        fragments.add(new NowMarketFragment(1, mViewPager, coin, 1));
        fragments.add(new CoinDetailsFragment(coin.getFid() + "", mViewPager, 2));
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(new HomeVpAdapter(getSupportFragmentManager(), fragments, mTabList));
        mTabLayout.setupWithViewPager(mViewPager);
//        CommonUtil.setTabWidth(mTabLayout);
        //动态监听viewpager高度。
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                mIndex = position;
                mViewPager.resetHeight(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
//        mTabLayout.setTabData(mTabList);
        initSoldHistory();
        getHttpData();
        timer.schedule(task, 60000, 60000);
        stWebsiteName.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                showLoadingDialog();
                switch (position) {
                    case 0:
                        intStep = "1m.json";
                        break;
                    case 1:
                        intStep = "15m.json";
                        break;
                    case 2:
                        intStep = "1h.json";
                        break;
                    case 3:
                        intStep ="1d.json";
                        break;
                    case 4:
                        if (selectTimePopupWindow.isShowing()) {
                            selectTimePopupWindow.dismiss();
                        } else {
                            selectTimePopupWindow.showAsDropDown(stWebsiteName);
                        }
                        break;
                }
                mTabEntities.set(4, new TabEntity(getString(R.string.text_again_kline), 0, 0));
                stWebsiteName.setTabData(mTabEntities);
                popupWindow.dismiss();
//                requestPeriod();
                mPresent.requestKlineInfo(intStep + "", coin.getFid() + "");
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

    }

    private void getHttpData() {
        showLoadingDialog();
        mPresent.requestKlineInfo(intStep + "", coin.getFid() + "");
        mPresent.getCoinData(coin.getFid());
        mPresent.requestMarketRefresh(coin.getFid() + "");
    }

    private void showPopWindows() {
        View view = View.inflate(this, R.layout.popuview_trendchart, null);
        popupWindow.setWidth(getWindow().getAttributes().width);
        popupWindow.setContentView(view);
//        popupWindow.setClippingEnabled(false);
        popupWindow.setOutsideTouchable(true);
        macdText = view.findViewById(R.id.macdText);
        kdjText = view.findViewById(R.id.kdjText);
        rsiText = view.findViewById(R.id.rsiText);
        wrText = view.findViewById(R.id.wrText);
        maText = view.findViewById(R.id.maText);
        bollText = view.findViewById(R.id.bollText);
        mainHide = view.findViewById(R.id.mainHide);
        subHide = view.findViewById(R.id.subHide);
        subHide.setOnClickListener(v -> {
            if (subIndex != -1) {
                kLineView.hideSelectData();
                subTexts.get(subIndex).setTextColor(Color.WHITE);
                subIndex = -1;
                kLineView.hideChildDraw();
            }
        });
        mainHide.setOnClickListener(v -> {
            bollText.setTextColor(getResources().getColor(R.color.white));
            maText.setTextColor(getResources().getColor(R.color.white));
            kLineView.changeMainDrawType(Status.NONE);
        });
        maText.setOnClickListener(v -> {
            maText.setTextColor(getResources().getColor(R.color.login_yellow));
            bollText.setTextColor(Color.WHITE);
            kLineView.changeMainDrawType(Status.MA);
        });
        bollText.setOnClickListener(v -> {
            bollText.setTextColor(getResources().getColor(R.color.login_yellow));
            maText.setTextColor(Color.WHITE);
            kLineView.changeMainDrawType(Status.BOLL);
        });
        subTexts.clear();
        if (subTexts.isEmpty()) {
            subTexts.add(macdText);
            subTexts.add(kdjText);
            subTexts.add(rsiText);
            subTexts.add(wrText);
        }
        for (int i = 0; i < subTexts.size(); i++) {
            int finalI = i;
            subTexts.get(i).setOnClickListener(v -> {
                if (subIndex != finalI) {
                    kLineView.hideSelectData();
                    if (subIndex != -1) {
                        subTexts.get(subIndex).setTextColor(Color.WHITE);
                    }
                    subIndex = finalI;
                    subTexts.get(finalI).setTextColor(getResources().getColor(R.color.login_yellow));
                    kLineView.setChildDraw(subIndex);
                }
            });
        }
        //K线更多分时数据
        selectTimePopupWindow = new PopupWindow(this);
        View viewTime = View.inflate(this, R.layout.item_popuwindow_time, null);
        selectTimePopupWindow.setWidth(getWindow().getAttributes().width);
        selectTimePopupWindow.setContentView(viewTime);
        selectTimePopupWindow.setOutsideTouchable(true);
        CommonTabLayout tabLayout = viewTime.findViewById(R.id.tablayout_popuwindow);
        ArrayList arrayList = new ArrayList();
        arrayList.add(new TabEntity("1" + getString(R.string.minute), 0, 0));
        arrayList.add(new TabEntity("5" + getString(R.string.minute), 0, 0));
        arrayList.add(new TabEntity("30" + getString(R.string.minute), 0, 0));
        arrayList.add(new TabEntity(getString(R.string.week_line), 0, 0));
        tabLayout.setTabData(arrayList);
        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                showLoadingDialog();
                switch (position) {
                    case 0:
                        intStep = "1m.json";
                        mTitles[4] = "1" + getString(R.string.minute);
                        mTabEntities.set(4, new TabEntity("1" + getString(R.string.minute), 0, 0));
                        break;
                    case 1:
                        intStep = "5m.json";
                        mTabEntities.set(4, new TabEntity("5" + getString(R.string.minute), 0, 0));
                        break;
                    case 2:
                        intStep = "30m.json";
                        mTabEntities.set(4, new TabEntity("30" + getString(R.string.minute), 0, 0));
                        break;
                    case 3:
                        intStep = "1w.json";
                        mTabEntities.set(4, new TabEntity(getString(R.string.week_line), 0, 0));
                        break;
                }
                stWebsiteName.setTabData(mTabEntities);
//                requestPeriod();
                mPresent.requestKlineInfo(intStep + "", coin.getFid() + "");
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
    }

    @Override
    protected TrendChartPersenter createPresent() {
        return new TrendChartPersenter();
    }

    @Override
    public void initComp() {
        isCollection = isCollectioned();
        ivKlineFons.setImageResource(isCollection ? R.mipmap.ico_collect_seclect : R.mipmap.ico_collect_normal);
        popuCoinSelect = new CoinSelectPopoWindow(this, 0);
        popuCoinSelect.setOnPopuwindowListener(this);
    }

    private void initSoldHistory() {
        //
        soldRecordList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        soldRecordAdapter = new BaseQuickAdapter<List<String>, BaseViewHolder>(R.layout.item_sold_record, soldRecordList) {
            @Override
            protected void convert(BaseViewHolder helper, List<String> item) {
                if (item.get(3).equals("2")) {
                    helper.setTextColor(R.id.tv_buy_and_sell, FALL_COLOR);
                    helper.setText(R.id.tv_buy_and_sell, R.string.sell);
                } else {
                    helper.setTextColor(R.id.tv_buy_and_sell, RISE_COLOR);
                    helper.setText(R.id.tv_buy_and_sell, R.string.buy);
                }
                helper.setText(R.id.tv_deal_price, MoneyUtils.decimalByUp(coin.getPriceDecimals(), new BigDecimal(item.get(0))) + "");
                helper.setText(R.id.tv_volume, MoneyUtils.decimalByUp(coin.getAmountDecimals(), new BigDecimal(item.get(1))) + "");
                helper.setText(R.id.tv_timer, item.get(2).split(" ")[1]);
            }
        };

        //防止滑动卡顿
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        linearLayoutManager.setAutoMeasureEnabled(true);
    }

    @OnClick({R.id.iv_fall_back, R.id.btn_roll_in, R.id.btn_roll_out, R.id.tv_kline_type, R.id.iv_kline_fons, R.id.tv_kline_coinname, R.id.iv_kline_share})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_fall_back:
                finish();
                break;
            case R.id.btn_roll_in:
                Constants.coinName = coin.getFShortName();
                Constants.coinGroup = group;
                Constants.isBuy = true;
                ActivityManagerUtils.getInstance().getMainActivity().turnPage(2);
                finish();
                break;
            case R.id.btn_roll_out:
                Constants.isBuy = false;
                Constants.coinName = coin.getFShortName();
                Constants.coinGroup = group;
                ActivityManagerUtils.getInstance().getMainActivity().turnPage(2);
                finish();
                break;

            case R.id.tv_kline_type:
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                } else {
                    popupWindow.showAsDropDown(stWebsiteName);
                }
                break;
            case R.id.iv_kline_fons:
                mPresent.updateFonsCoin(isCollectioned(), coin.getFid());
                break;
            case R.id.tv_kline_coinname:
//                CoinSelectPopoWindow.Companion.getInstance(this).showAsDropDown(ivFallBack);
                popuCoinSelect.showAsDropDown(ivFallBack);
                break;
            case R.id.iv_kline_share:
                new ShareDialog(this).show();
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("TrendChartKline", "onDestroy: ");
        if (timer != null) {
            timer.cancel();
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onMessage(String text) {
        if(isDestroyed()){
            return;
        }
        if (!isFinishing()) {
            JSONArray jsonData = null;
            try {
                jsonData = new JSONArray(text);
                String strName = (String) jsonData.get(0);
                if (strName.equals("entrust-log")) {
                    final String log = jsonData.getString(1);
                    runOnUiThread(() -> entrustLog(log));
                } else if (strName.equals("real")) {
                    final String real = jsonData.getString(1);
                    //Log.e("real", real);
                    runOnUiThread(() -> {
                                RealBean realBean = GsonUtil.GsonToBean(real, RealBean.class);
                                if (realBean.getFupanddown() >= 0) {
                                    tvUpDown.setTextColor(getResources().getColor(R.color.price_green));
                                    tvUpDown.setText("+" + MoneyUtils.decimal2ByUp(new BigDecimal(realBean.getFupanddown() * 100)) + "%");
                                    ivUpDown.setImageResource(R.mipmap.ico_home_rise);
                                    tvActualPrice.setTextColor(getResources().getColor(R.color.price_green));
                                } else {
                                    tvUpDown.setTextColor(getResources().getColor(R.color.price_red));
                                    tvUpDown.setText(MoneyUtils.decimal2ByUp(new BigDecimal(realBean.getFupanddown() * 100)) + "%");
                                    ivUpDown.setImageResource(R.mipmap.ico_home_fall);
                                    tvActualPrice.setTextColor(getResources().getColor(R.color.price_red));
                                }
                                tvActualPrice.setText(MoneyUtils.decimalByUp(coin.getPriceDecimals(), new BigDecimal(realBean.getLast())).toPlainString());
                                tvRmbValue.setText("≈" + MoneyUtils.decimalByUp(LanguageUtils.currentLanguage == 1 ? 4 : 4, new BigDecimal(MoneyUtils.mul(realBean.getLast(), coin.getRate()))) + getString(R.string.money_unit));
                                tvHighestPrice.setText(MoneyUtils.decimalByUp(coin.getPriceDecimals(), new BigDecimal(realBean.getHigh())).toPlainString());
                                tvLowestPrice.setText(MoneyUtils.decimalByUp(coin.getPriceDecimals(), new BigDecimal(realBean.getLow())).toPlainString());
                                tvDayVolume.setText(MoneyUtils.decimalByUp(coin.getAmountDecimals(), new BigDecimal(realBean.getVol())).toPlainString());
                            }
                    );

                    //最新交易买单M
                } else if (strName.equals("entrust-buy")) {
                    final String buy = jsonData.getString(1);
                    mPresent.getBuyDaata(TrendChartNewActivity.this, buy);
                    //
                    //最新交易卖单
                } else if (strName.equals("entrust-sell")) {
                    final String sell = (String) jsonData.get(1);
                    mPresent.getSellData(TrendChartNewActivity.this, sell);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    protected void setStatusBar() {
        super.setStatusBar();
        immersionBar.statusBarDarkFont(false).init();
    }

    /**
     * 刷新历史记录
     *
     * @param response
     * @throws JSONException
     */
    public void entrustLog(final String response) {
        if (soldRecordList.size() > 0) {
            soldRecordList.clear();
        }
        runOnUiThread(() -> {
            List<List<String>> data = new Gson().fromJson(response, new TypeToken<List<List<String>>>() {
            }.getType());
//                soldRecordList.add(null);
            soldRecordList.addAll(data);
            soldRecordAdapter.notifyDataSetChanged();
        });
    }

    //获取币种交易信息
    @Override
    public void getCoinMarketInfoSuccess(@NotNull List<List<String>> sellList) {
        soldRecordList.addAll(sellList);
        soldRecordAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SocketUtils.getIns().removeListener(this);
    }

    //处理交易卖单数据
    @Override
    public void getCoinMarketInfoData(@NotNull List<List<String>> sellList, double totalSell, double maxSell) {
    }

    //处理交易卖单数据
    @Override
    public void getCoinMarketBuyData(@NotNull List<List<String>> buyList, double totalbuy, double maxbuy) {
    }

    //获取K线数据
    @Override
    public void getCoinNewKlineData(@NotNull List<KLineEntity> klineList) {
        DataHelper.calculate(klineList);
        kLineView.setDateTimeFormatter(new DateFormatter());
        if (lastStep == "") {
            mKlineAdapter.addHeaderData(klineList);
            //         kLineView.initData(klineList);
        } else {
            if (lastStep == intStep) {
                mKlineAdapter.addHeaderData(klineList);
//                mKlineAdapter.
            } else {
//                kLineView.notifyDataChange(klineList, true);
                mKlineAdapter.addHeaderData(klineList);
            }
        }
//        kLineView.showVolume();
        kLineView.changeMainDrawType(Status.MA);
        mKlineAdapter.notifyDataSetChanged();
//        kLineView.refreshComplete();
//        kLineView.setons
//        kLineView.
        lastStep = intStep;
        hideLoadingDialog();
    }

    //获取用户信息
    @Override
    public void getUserMarketData(@NotNull RefreshUserInfoBean response) {
        Message message = new Message();
        message.obj = response.getToken();
        message.what = 0x1305;
        handler.sendMessage(message);
        hideLoadingDialog();
    }

    //获取币种信息
    @SuppressLint("SetTextI18n")
    @Override
    public void getCoinData(@NotNull MarketRealBean response) {
        if (isDestroyed()) {
            return;
        }
        if (Double.valueOf(response.getFupanddown()) >= 0) {
            tvUpDown.setTextColor(getResources().getColor(R.color.price_green));
            tvUpDown.setText("+" + MoneyUtils.decimal2ByUp(new BigDecimal(Double.valueOf(response.getFupanddown()))) + "%");
            ivUpDown.setImageResource(R.mipmap.ico_home_rise);
            tvActualPrice.setTextColor(getResources().getColor(R.color.price_green));
        } else {
            tvUpDown.setTextColor(getResources().getColor(R.color.price_red));
            tvUpDown.setText(MoneyUtils.decimal2ByUp(new BigDecimal(Double.valueOf(response.getFupanddown()))) + "%");
            ivUpDown.setImageResource(R.mipmap.ico_home_fall);
            tvActualPrice.setTextColor(getResources().getColor(R.color.price_red));
        }
        Log.e("", response.getLast() + "   ");
        tvActualPrice.setText(MoneyUtils.decimalByUp(coin.getPriceDecimals(), new BigDecimal(response.getLast())).toPlainString());
//        if (rate != 0) {
        tvRmbValue.setText("≈" + MoneyUtils.decimalByUp(LanguageUtils.currentLanguage == 1 ? 4 : 4, new BigDecimal(MoneyUtils.mul(Double.valueOf(response.getLast()), coin.getRate()))) + getString(R.string.money_unit));
//        }
        tvHighestPrice.setText(MoneyUtils.decimalByUp(coin.getPriceDecimals(), new BigDecimal(response.getHigh())).toPlainString());
        tvLowestPrice.setText(MoneyUtils.decimalByUp(coin.getPriceDecimals(), new BigDecimal(response.getLow())).toPlainString());
        tvDayVolume.setText(MoneyUtils.decimalByUp(coin.getAmountDecimals(), new BigDecimal(response.getVol())).toPlainString());
    }

    @Override
    public void retrunFons(boolean isFons) {
        ivKlineFons.setImageResource(isFons ? R.mipmap.ico_collect_seclect : R.mipmap.ico_collect_normal);
    }

    @Override
    public void OnItemListener(@NotNull CoinTradeRankBean.DealDatasBean mCoinTradeRankBean) {
        coin = mCoinTradeRankBean;
        Constants.coinName = coin.getFShortName();
        tvTitle.setText(coin.getFname_sn());
        RxBusData rxBusData = new RxBusData();
        rxBusData.setMsgData(coin);
        rxBusData.setMsgName("KlineData");
        RxBus.get().post(rxBusData);
        getHttpData();
    }

//    @Override
//    public void getCoinKlineData(@NotNull List<HisData> klineList) {
//
//    }

}

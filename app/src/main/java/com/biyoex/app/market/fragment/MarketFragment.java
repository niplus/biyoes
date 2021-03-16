package com.biyoex.app.market.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.biyoex.app.R;
import com.biyoex.app.VBTApplication;
import com.biyoex.app.common.Constants;
import com.biyoex.app.common.activity.LoginActivity;
import com.biyoex.app.common.base.RxBus;
import com.biyoex.app.common.base.RxBusData;
import com.biyoex.app.common.bean.RequestResult;
import com.biyoex.app.common.data.CoinLiveData;
import com.biyoex.app.common.http.RetrofitHelper;
import com.biyoex.app.common.mvpbase.BaseFragment;
import com.biyoex.app.common.mvpbase.BaseObserver;
import com.biyoex.app.common.okhttp.SocketUtils;
import com.biyoex.app.common.utils.CashierInputFilterUtils;
import com.biyoex.app.common.utils.CommonUtil;
import com.biyoex.app.common.utils.DateUtil;
import com.biyoex.app.common.utils.GsonUtil;
import com.biyoex.app.common.utils.LanguageUtils;
import com.biyoex.app.common.utils.MoneyUtils;
import com.biyoex.app.common.utils.SharedPreferencesUtils;
import com.biyoex.app.common.utils.log.Log;
import com.biyoex.app.common.widget.BURNMessageDialog;
import com.biyoex.app.common.widget.CoinSelectPopoWindow;
import com.biyoex.app.common.widget.IconMessageDialog;
import com.biyoex.app.common.widget.MyViewPager;
import com.biyoex.app.common.widget.PasswordDialog;
import com.biyoex.app.common.widget.RatioLinearLayout;
import com.biyoex.app.home.adapter.HomeVpAdapter;
import com.biyoex.app.home.bean.CoinTradeRankBean;
import com.biyoex.app.market.presenter.MarketPresenter;
import com.biyoex.app.market.view.MarketView;
import com.biyoex.app.my.activity.NewsEntrustedManageActivity;
import com.biyoex.app.my.activity.RevisePayPasswordActivity;
import com.biyoex.app.trading.activity.TrendChartNewActivity;
import com.biyoex.app.trading.bean.CoinMarketLiveData;
import com.biyoex.app.trading.bean.EntrustUpdate;
import com.biyoex.app.trading.bean.MarketRealBean;
import com.biyoex.app.trading.bean.RealBean;
import com.biyoex.app.trading.bean.RefreshUserInfoBean;
import com.biyoex.app.trading.bean.ResonateBean;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MarketFragment extends BaseFragment<MarketPresenter> implements SocketUtils.OnReceiveMessageListener, View.OnClickListener, MarketView, OnSeekChangeListener, CoinSelectPopoWindow.OnPopupWindowItemClickLiseser {
    //    @BindView(R.id.recyclerView)
//    RecyclerView recyclerView;
    @BindView(R.id.st_website_name)
    TabLayout stWebsiteName;
    @BindView(R.id.rv_sell)
    RecyclerView rvSell;
    @BindView(R.id.rv_buy)
    RecyclerView rvBuy;
    @BindView(R.id.tv_current_price)
    TextView tvCurrentPrice;
    @BindView(R.id.tv_fupanddown)
    TextView tvFupanddown;
    @BindView(R.id.tv_price_value)
    TextView tvPriceValue;
    @BindView(R.id.btn_buy)
    TextView btn_buy;
    @BindView(R.id.btn_sell)
    TextView btn_sell;
    @BindView(R.id.ll_buy_and_sell)
    LinearLayout llBuyAndSell;
    @BindView(R.id.ed_price)
    EditText edPrice;
    @BindView(R.id.edt_number)
    EditText edtNumber;
    @BindView(R.id.tv_useful)
    TextView tvUseful;
    @BindView(R.id.btn_trade)
    Button btnTrade;
    @BindView(R.id.tv_trade_volume)
    TextView tvTradeVolume;
    @BindView(R.id.tv_sub)
    TextView tvSub;
    @BindView(R.id.tv_add)
    TextView tvAdd;
    @BindView(R.id.tv_activity)
    TextView tvActivity;
    @BindView(R.id.tv_market_coin_name)
    TextView tvtitleNmae;
    @BindView(R.id.iv_market_rank)
    ImageView ivMarketRank;
    @BindView(R.id.iv_market_fons)
    ImageView ivMarketFons;
    @BindView(R.id.seekbar_market)
    IndicatorSeekBar seekBar;
    @BindView(R.id.user_account_total)
    TextView tvUserFreese;
    @BindView(R.id.buy_price_value)
    TextView tvBuyPriceValue;
    @BindView(R.id.smartrefresh)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.market_bottom_vp)
    MyViewPager mBottomViewPager;
    @BindView(R.id.tv_isburn)
    TextView tvIsBurn;
    private Timer resonateTimer;
    private String TAG = "marketFragment";
    private CoinTradeRankBean.DealDatasBean coin;
    //卖出
    private BaseQuickAdapter mSellOutAdapter;
    //买入
    private BaseQuickAdapter mBuyAdapter;
    //侧面popupwindow
    private CoinSelectPopoWindow coinSelectPopoWindow;
    private double totalSell;
    private double totalBuy;

    /**
     * 买卖挂单
     */
    private List<List<String>> mSellOutList = new ArrayList<>();
    private List<List<String>> mBuyList = new ArrayList<>();
    private List<Fragment> mBottomList = new ArrayList<>();
    private List<String> mTabList = new ArrayList<>();

//    private double rate;

    /**
     * 是否需要交易密码
     */
    private boolean needPassword;

    /**
     * 可买数量
     */
    private String strActualNumber = "0.0000";

    /**
     * 可用支付币
     */
    private String strActualRmb = "0.000";
    /**
     * 冻结币
     */
    private String freeseRmb = "0";
    /**
     * 可用币种数量
     */
    private String strActualCurrency = "0.0000";
    /**
     * 冻结币种数量
     */
    private String strFreeseCoinNum = "0.000";
    /**
     * 判断是否登录 true为登录，故反之false
     */
    private boolean isLogin;

    /**
     * 推荐买入价格
     */
    private String strRecommendedPrice = "0.0000";

    /**
     * 推荐买卖出价格
     */
    private String strSellOutPrice = "0.0000";


    private long restTime;
    /**
     * 0 没有启动， 1 倒计时， 2 15s刷新
     */
    private int timerStatus;
    /**
     * 活动到第几期
     */
    private int num;
    /**
     * 总数据
     */
    private CoinTradeRankBean data;
    /**
     * 是否自选
     */
    private boolean isCollection = false;
    /**
     * market懒加载  判断是否加载过视图
     */
    private boolean mHaveLoadData = false;

    /**
     * 查看区下标
     * 0 自选 1 普通 2创新
     */
    private double progress = 0;
    private int isInitViewPager = 0;
    private int isBuyAndSell = 0; //判断卖单买单是否成功，成功之后不展示推荐价格

    private Disposable rxBusDisposable;
    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x1650) {
                if (tvActivity != null) {
                    tvActivity.setText(String.format("距离第%d期开始还有%s", num, DateUtil.formatRestTime(restTime)));
                }
            } else if (msg.what == 0x1305) {
                SocketUtils.getIns().removeSocketClose();
                SocketUtils.getIns().removeListener(MarketFragment.this);
                SocketUtils.getIns().setOnReceiveMessageListener(MarketFragment.this);
                SocketUtils.getIns().requestWebSocketInfo(coin.getFid() + "", (String) msg.obj);
                MarketFragment.this.sendMessage();
            } else if (msg.what == 0x1) {
                if (tvActivity != null) {
                    tvActivity.setText(String.format("第%d期正在进行中...", num));
                }
            }

        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.fragment_market;
    }

    @Override
    public void initData() {
        data = CoinMarketLiveData.getIns().getValue();
        transactionRecord();
        seekBar.setOnSeekChangeListener(this);
        mBottomViewPager.setOffscreenPageLimit(2);
        //数量变化监听
        edtNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                double value = 0d;
                if (!TextUtils.isEmpty(s)) {
                    value = Double.valueOf(s.toString());
                }

                if (!TextUtils.isEmpty(edPrice.getText())) {
                    if (coin != null) {
                        tvTradeVolume.setText("" + MoneyUtils.decimalByUp(coin.getPriceDecimals(), new BigDecimal(MoneyUtils.mul(value, Double.valueOf(edPrice.getText().toString())))).toPlainString() + coin.getGroup());
                    }
                }
            }
        }
        );
        //价钱的变化监听
        edPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (coin != null) {
                    if (!edPrice.getText().toString().isEmpty()) {
                        progress = 0;
                        seekBar.setProgress(0);
                        double value = 0d;
                        if (!TextUtils.isEmpty(s)) {
                            value = Double.valueOf(s.toString());
                        }
                        if (Constants.isBuy) {
                            if (Double.valueOf(edPrice.getText().toString()) > 0) {
                                edtNumber.setText(MoneyUtils.decimalByUp(coin.getAmountDecimals(), new BigDecimal(MoneyUtils.mul(Double.valueOf(strActualRmb), progress)).divide(new BigDecimal(edPrice.getText().toString()), 4, BigDecimal.ROUND_HALF_DOWN)).toPlainString());
                            }
                        } else {
                            edtNumber.setText(MoneyUtils.decimalByUp(coin.getAmountDecimals(), new BigDecimal(MoneyUtils.mul(Double.valueOf(strActualCurrency), progress))).toPlainString());
                        }
                        if (!TextUtils.isEmpty(edtNumber.getText())) {
                            tvTradeVolume.setText("" + MoneyUtils.decimalByUp(coin.getPriceDecimals(), new BigDecimal(MoneyUtils.mul(value, Double.valueOf(edtNumber.getText().toString())))).toPlainString() + coin.getGroup());
                        }
                        tvBuyPriceValue.setText("≈" + MoneyUtils.decimalByUp(LanguageUtils.currentLanguage == 1 ? 4 : 4, new BigDecimal(MoneyUtils.mul(Double.valueOf(edPrice.getText().toString()), Double.valueOf(coin.getRate())))) + getString(R.string.money_unit));
                    } else {
                        edPrice.setText("0");
                    }
                }
            }
        }
        );

        smartRefreshLayout.setOnRefreshListener(refreshlayout -> mPresent.getRefreshUserInfo(coin.getFid()));
        //为了动态变化一个底部viewpager的高度
        mBottomViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                mIndex = position;
                mBottomViewPager.resetHeight(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //事件总线，目前只有刷新数据
        rxBusDisposable = RxBus.get().toFlowable().map(o -> (RxBusData) o).subscribe(rxBusData -> {
            if (rxBusData.getMsgName().equals("MarketFragment") && rxBusData.getMsgData().equals("refreshData")) {
                getMarketData("");
            } else if (rxBusData.getMsgName().equals("refreshUserInfo")) {
                mPresent.getRefreshUserInfo(coin.getFid());
            }
        });
        //添加tab选中标签字体加粗
        stWebsiteName.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                updateTabView(tab,true);
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                updateTabView(tab,false);
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
//        currentEntrust();
    }
    //切换币种逻辑
    private void getMarketData(String s){
        isBuyAndSell = 0;
        if (data != null) {
            Map<String, List<CoinTradeRankBean.DealDatasBean>> dataMap = data.getDataMap();
            Set<Map.Entry<String, List<CoinTradeRankBean.DealDatasBean>>> entries = dataMap.entrySet();
            Iterator<Map.Entry<String, List<CoinTradeRankBean.DealDatasBean>>> iterator = entries.iterator();
            //全局筛选是否有这个币种
            while (iterator.hasNext()) {
                Map.Entry<String, List<CoinTradeRankBean.DealDatasBean>> next = iterator.next();
                for (int i = 0; i < next.getValue().size(); i++) {
                    if (Constants.coinName.equals(next.getValue().get(i).getfShortName())) {
                        coin = next.getValue().get(i);
                        break;
                    }
                }
            }
            //如果没有这个币种 默认显示USDT第一个币种
            if(coin==null&&dataMap.get("USDT")!=null){
                coin = dataMap.get("USDT").get(0);
            }
//                        tvCoinName.setText(coin.getFname());
//            Map<String, RateBean.RateInfo> map = RateLivedata.getIns().getValue();
//            if (map != null && map.containsKey("USDT")) {
//                rate = map.get("USDT").getRmbPrice();
//            }
            if(coin!=null){
                CoinLiveData.Companion.getIns(getContext()).setCoinValue(coin);
                tvtitleNmae.setText(coin.getFname_sn());
                updateBuyStatus(Constants.isBuy ? 0 : 1);
                seekBar.setProgress(0);
                mSellOutList = new ArrayList<>();
                mBuyList = new ArrayList<>();
                edtNumber.setText("");
                isCollection = isCollectioned();
                ivMarketFons.setImageResource(isCollection ? R.mipmap.ico_collect_seclect : R.mipmap.ico_collect_normal);
                textControl();
                mPresent.getCoinData(coin.getFid());//获取币种信息
                mPresent.requestMarketRefresh(coin.getFid() + "");
                mPresent.getRefreshUserInfo(coin.getFid());
                mBuyAdapter.setNewData(mBuyList);
                mSellOutAdapter.setNewData(mSellOutList);
                //加载底部fragment
                initBottomViewPager();
                tvIsBurn.setVisibility(coin.isBurn()?View.VISIBLE:View.GONE);
//                sendMessage();
                if(coin.isBurn()){
                }
                if (coin.getFid() == 38) {
                    getResonateStatus();
                    btn_sell.setClickable(false);
                    edPrice.setEnabled(false);
                    tvAdd.setClickable(false);
                    tvSub.setClickable(false);
                } else {
                    tvActivity.setVisibility(View.GONE);
                    btn_sell.setClickable(true);
                    edPrice.setEnabled(true);
                    tvAdd.setClickable(true);
                    tvSub.setClickable(true);
                }
            }
        }
    }


    private void initBottomViewPager() {
        if (mBottomViewPager.getAdapter() == null) {
            isInitViewPager++;
            mBottomList.clear();
            mTabList.clear();
            mBottomList.add(new MineEntrustOrderFragment(0, mBottomViewPager, coin));
            mBottomList.add(new NowMarketFragment(1, mBottomViewPager, coin, 0));
//            mBottomList.add(new BurnFragment(coin));
//            mBottomList.add(new MineEntrustOrderFragment(2, mBottomViewPager, coin));
            mTabList.add(getString(R.string.current_entrust));
            mTabList.add(getString(R.string.now_entrust));
            if(coin.isBurn()){
                mBottomList.add(new BurnFragment(2,mBottomViewPager,coin));
                mTabList.add(getString(R.string.burn_destory));
            }
            mBottomViewPager.setAdapter(new HomeVpAdapter(getChildFragmentManager(), mBottomList, mTabList));
            stWebsiteName.setupWithViewPager(mBottomViewPager);
        }
        else {
            if(mBottomList.size()<3&&coin.isBurn()){
                mBottomList.add(new BurnFragment(2,mBottomViewPager,coin));
                mTabList.add(getString(R.string.burn_destory));
            }
            else if(mBottomList.size()==3&&!coin.isBurn()){
                mBottomList.remove(2);
                mTabList.remove(2);
            }
            mBottomViewPager.getAdapter().notifyDataSetChanged();
            mBottomViewPager.setCurrentItem(0);
        }
        for (int i = 0; i < mTabList.size(); i++) {
            View tab = LayoutInflater.from(getActivity()).inflate(R.layout.item_market_tab, null);
            TextView textView = tab.findViewById(R.id.tv_tab_name);
            textView.setText(mTabList.get(i));
            if(i==0){
                textView.setTextSize(14);
                textView.setTypeface(Typeface.DEFAULT_BOLD);
            }
            stWebsiteName.getTabAt(i).setCustomView(tab);
        }
    }
    private void  openBRDIalog(){
        new BURNMessageDialog(getContext()).show();
    }


    /**
     * 判断是否已经收藏
     *
     * @return
     */
    public boolean isCollectioned() {
        String coins = SharedPreferencesUtils.getInstance().getString("coin_id", "");
        return coins.contains(coin.getFid() + ",");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (data != null) {
            getMarketData("");
        }
    }

    @Override
    protected MarketPresenter createPresent() {
        return new MarketPresenter(getContext());
    }

    @Override
    public void initComp() {
        coinSelectPopoWindow = new CoinSelectPopoWindow(getActivity(), 1);
        coinSelectPopoWindow.setOnPopuwindowListener(this);
    }
    /**
     *  用来改变tabLayout选中后的字体大小及颜色
     * @param tab
     * @param isSelect
     */
    private void updateTabView(TabLayout.Tab tab, boolean isSelect) {
        //找到自定义视图的控件ID
     if(tab.getCustomView()!=null){
         TextView tv_tab = tab.getCustomView().findViewById(R.id.tv_tab_name);
         if(isSelect) {
             //设置标签选中
             tv_tab.setSelected(true);
             tv_tab.setTextSize(14);
             //选中后字体变大
             tv_tab.setTypeface(Typeface.DEFAULT_BOLD);

         }else{
             //设置标签取消选中
             tv_tab.setSelected(false);
             tv_tab.setTextSize(13);

             //恢复为默认字体大小
             tv_tab.setTypeface(Typeface.DEFAULT);
         }
     }
    }

    /**
     * 卖出文本更改
     */
    public void textControl() {
        //卖出价格
        CashierInputFilterUtils cashierInputFilterUtils = new CashierInputFilterUtils();
        cashierInputFilterUtils.POINTER_LENGTH = coin.getPriceDecimals();
        final InputFilter[] isPrice = {cashierInputFilterUtils};
        edPrice.setFilters(isPrice);
        CashierInputFilterUtils cashierInputFilterUtils1 = new CashierInputFilterUtils();
        cashierInputFilterUtils1.POINTER_LENGTH = coin.getAmountDecimals();
        InputFilter[] isTradingQuantity = {cashierInputFilterUtils1};
        edtNumber.setFilters(isTradingQuantity);
    }

    /**
     * 挂单买入卖出记录
     */
    public void transactionRecord() {
        final LinearLayoutManager sellOutLayout = new LinearLayoutManager(getContext());
        sellOutLayout.setOrientation(LinearLayoutManager.VERTICAL);
        mSellOutAdapter = new BaseQuickAdapter<List<String>, BaseViewHolder>(R.layout.item_market_list, mSellOutList) {
            @Override
            protected void convert(BaseViewHolder holder, List<String> item) {
                RatioLinearLayout ratioLinearLayout = holder.getView(R.id.ll_layout_trade_info);
                ratioLinearLayout.setPaintColor((0x00ffffff & getResources().getColor(R.color.marekt_bg_alpha_red) | (0x26 << 24)));
                //币种单价
                holder.setText(R.id.tv_price, MoneyUtils.decimalByUp(coin.getPriceDecimals(), new BigDecimal(item.get(0))) + "");
                holder.setTextColor(R.id.tv_price, getResources().getColor(R.color.price_red));
                //购买币的数量
                double amount = Double.valueOf(item.get(1));
                holder.setText(R.id.tv_amount, new BigDecimal(amount).setScale(coin.getAmountDecimals(), BigDecimal.ROUND_DOWN).toString());
                double beforePrice =  0;
                for (int i = mData.size()-1; i >=holder.getPosition(); i--) {
                    beforePrice+=Double.valueOf(mData.get(i).get(1));
                }
                ratioLinearLayout.setRatio(new BigDecimal(beforePrice).divide(new BigDecimal(totalSell), 4, BigDecimal.ROUND_DOWN).doubleValue());
            }
        };
        mSellOutAdapter.setOnItemClickListener((adapter, view, position) -> {
                    List<String> data = mSellOutList.get(position);
                    edPrice.setText(MoneyUtils.decimalByUp(coin.getPriceDecimals(), new BigDecimal(data.get(0))).toPlainString());
                    tvBuyPriceValue.setText("≈" + MoneyUtils.decimalByUp(LanguageUtils.currentLanguage == 1 ? 4 : 4, new BigDecimal(MoneyUtils.mul(Double.valueOf(data.get(0)), Double.valueOf(coin.getRate())))) + getString(R.string.money_unit));
                    double selectNumberSum = 0;
                    for (int i = position; i < mSellOutList.size(); i++) {
                        selectNumberSum += Double.valueOf(mSellOutList.get(i).get(1));
                    }
                    edtNumber.setText(new BigDecimal(selectNumberSum).setScale(coin.getAmountDecimals(), BigDecimal.ROUND_DOWN).toString());
                    jumpAnimation(edPrice);
                    updateLayout();
                });

        rvSell.setLayoutManager(sellOutLayout);
//        ListSpaceItemDecoration listSpaceItemDecoration1 = new ListSpaceItemDecoration(5);
//        listSpaceItemDecoration1.setType(3);
//        rvSell.addItemDecoration(listSpaceItemDecoration1);
        rvSell.setAdapter(mSellOutAdapter);
        rvSell.setFocusable(false);
        LinearLayoutManager purchaseLayout = new LinearLayoutManager(getContext());
        purchaseLayout.setOrientation(LinearLayoutManager.VERTICAL);
        mBuyAdapter = new BaseQuickAdapter<List<String>, BaseViewHolder>(R.layout.item_market_list, mBuyList) {
            @Override
            protected void convert(BaseViewHolder holder, List<String> item) {
                RatioLinearLayout ratioLinearLayout = holder.getView(R.id.ll_layout_trade_info);
                ratioLinearLayout.setPaintColor((0x00ffffff & getResources().getColor(R.color.market_bg_alpha_green) | (0x26 << 24)));
                //币种单价
                holder.setText(R.id.tv_price, MoneyUtils.decimalByUp(coin.getPriceDecimals(), new BigDecimal(item.get(0))).toPlainString());
                holder.setTextColor(R.id.tv_price, getResources().getColor(R.color.price_green));
                //购买币的数量
                double amount = Double.valueOf(item.get(1));
                holder.setText(R.id.tv_amount, new BigDecimal(amount).setScale(coin.getAmountDecimals(), BigDecimal.ROUND_DOWN).toString());
                double beforePrice =  0;
                for (int i = 0; i < mData.size(); i++) {
                    if(i<=holder.getPosition()){
                        beforePrice+=Double.valueOf(mData.get(i).get(1));
                    }
                }
                ratioLinearLayout.setRatio(new BigDecimal(beforePrice).divide(new BigDecimal(totalBuy), 4, BigDecimal.ROUND_DOWN).doubleValue());
            }
        };
        mBuyAdapter.setOnItemClickListener((adapter, view, position) -> {
            List<String> data = mBuyList.get(position);
            edPrice.setText(MoneyUtils.decimalByUp(coin.getPriceDecimals(), new BigDecimal(data.get(0))).toPlainString());
            tvBuyPriceValue.setText("≈" + MoneyUtils.decimalByUp(LanguageUtils.currentLanguage == 1 ? 4 : 4, new BigDecimal(MoneyUtils.mul(Double.valueOf(data.get(0)), Double.valueOf(coin.getRate())))) + getString(R.string.money_unit));
            double selectNumberSum = 0;
            for (int i = 0; i <= position; i++) {
                selectNumberSum += Double.valueOf(mBuyList.get(i).get(1));
            }
            edtNumber.setText(new BigDecimal(selectNumberSum).setScale(coin.getAmountDecimals(), BigDecimal.ROUND_DOWN).toString());
            jumpAnimation(edPrice);
            updateLayout();
        }
        );
        rvBuy.setFocusable(false);
        rvBuy.setLayoutManager(purchaseLayout);
//        ListSpaceItemDecoration listSpaceItemDecoration = new ListSpaceItemDecoration(5);
//        listSpaceItemDecoration.setType(1);
//        rvBuy.addItemDecoration(listSpaceItemDecoration);
        rvBuy.setAdapter(mBuyAdapter);
    }

    //改变seekbar布局
    private void updateLayout() {
        double mul;
        if (Constants.isBuy) {
            String sedtNumberString = edtNumber.getText().toString();
            mul = MoneyUtils.mul(Double.valueOf(sedtNumberString.isEmpty() ? "0" : sedtNumberString), Double.valueOf(edPrice.getText().toString().isEmpty() ? "0" : edPrice.getText().toString()));
        } else {
            mul = Double.valueOf(edtNumber.getText().toString());
        }
        if (Constants.isBuy ? Double.valueOf(strActualRmb) > 0 : Double.valueOf(strActualCurrency) > 0) {
            BigDecimal bigDecimal = MoneyUtils.divide2ByUp(new BigDecimal(mul), new BigDecimal(Constants.isBuy ? strActualRmb : strActualCurrency)).multiply(new BigDecimal(100));
            seekBar.setProgress(bigDecimal.floatValue());
        }
    }

    private void jumpAnimation(TextView textView) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(01f, 1.2f, 1f, 1.2f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(100);
        textView.startAnimation(scaleAnimation);
    }

    /**
     * 修改买入卖出状态显示
     */
    public void updateBuyStatus(int status) {
        if(coin==null)
            return;
        {
            seekBar.setProgress(0);
            edtNumber.setText("");
            if (status == 1) {
                Constants.isBuy = false;
                edPrice.setText(strSellOutPrice);
                edPrice.setHint(getString(R.string.sell_price));
                edtNumber.setHint(getString(R.string.sell_volume));
                btn_sell.setTextColor(getResources().getColor(R.color.white));
                btn_buy.setTextColor(getResources().getColor(R.color.gray_to_black));
                llBuyAndSell.setBackground(getResources().getDrawable(R.mipmap.icon_buy_normal));
                tvUseful.setText(String.format("%s%s", strActualCurrency, coin.getFShortName()));
                tvUserFreese.setText(String.format("%s%s", freeseRmb, coin.getFShortName()));
                android.util.Log.i(TAG, "updateBuyStatus: " + coin.getFShortName());
//            // TODO: 2019/4/29 切换到卖
//            if (isLogin) {
//                btnTrade.setText(getString(R.string.sell));
//            }
                btnTrade.setBackground(getResources().getDrawable(R.drawable.btn_color_click_red));
            }
            else {
            Constants.isBuy = true;
            edPrice.setText(strRecommendedPrice);
            edPrice.setHint(getString(R.string.buy_price));
            edtNumber.setHint(getString(R.string.buy_number));
            btn_buy.setTextColor(getResources().getColor(R.color.white));
            btn_sell.setTextColor(getResources().getColor(R.color.gray_to_black));
            tvUseful.setText(String.format("%s%s", strActualRmb, coin.getGroup()));
            tvUserFreese.setText(String.format("%s%s", strFreeseCoinNum, coin.getGroup()));
            llBuyAndSell.setBackground(getResources().getDrawable(R.mipmap.icon_sell_normal));
            btnTrade.setBackgroundColor(getResources().getColor(R.color.price_green));
                btnTrade.setBackground(getResources().getDrawable(R.drawable.btn_color_click_green));
        }
            btnTrade.setText(isLogin ? Constants.isBuy ? getString(R.string.buy) : getString(R.string.sell) : getString(R.string.login));

        }
    }

    @OnClick({R.id.tv_isburn,R.id.tv_market_coin_name, R.id.btn_trade, R.id.tv_sub, R.id.tv_add, R.id.btn_sell, R.id.btn_buy, R.id.iv_market_rank, R.id.iv_market_fons, R.id.tv_number_add, R.id.tv_number_sub, R.id.tv_allorder_cancel, R.id.iv_history_entrust})
    public void onClick(View view) {
        if(coin==null) return;
        switch (view.getId()) {
            case R.id.tv_isburn:
                openBRDIalog();
                break;
            case R.id.tv_market_coin_name:
//                drawerLayout.openDrawer(leftLayout);
                coinSelectPopoWindow.showAsDropDown(tvtitleNmae);
                break;
            case R.id.btn_trade:
                if (Constants.isBuy) {
                    buyClick(0);
                } else {
                    buyClick(1);
                }
                break;
            case R.id.tv_sub:
                edPrice.setText(new BigDecimal(CommonUtil.getEditADDandSub(0, edPrice.getText().toString(), coin.getPriceDecimals())).setScale(coin.getPriceDecimals()).toPlainString());
                tvBuyPriceValue.setText("≈" + MoneyUtils.decimalByUp(LanguageUtils.currentLanguage == 1 ? 4 : 4, new BigDecimal(MoneyUtils.mul(Double.valueOf(edPrice.getText().toString()), coin.getRate()))) + getString(R.string.money_unit));
                break;
            case R.id.tv_add:
                edPrice.setText(new BigDecimal(CommonUtil.getEditADDandSub(1, edPrice.getText().toString(), coin.getPriceDecimals())).setScale(coin.getPriceDecimals()).toPlainString());
                tvBuyPriceValue.setText("≈" + MoneyUtils.decimalByUp(LanguageUtils.currentLanguage == 1 ? 4 : 4, new BigDecimal(MoneyUtils.mul(Double.valueOf(edPrice.getText().toString()), coin.getRate()))) + getString(R.string.money_unit));
                break;
            case R.id.tv_number_add:
                edtNumber.setText(new BigDecimal(CommonUtil.getEditADDandSub(1, edtNumber.getText().toString(), coin.getAmountDecimals())).setScale(coin.getAmountDecimals()).toPlainString());
                break;
            case R.id.tv_number_sub:
                edtNumber.setText(MoneyUtils.decimalByUp(coin.getAmountDecimals(), new BigDecimal(CommonUtil.getEditADDandSub(0, edtNumber.getText().toString(), coin.getAmountDecimals()))).toPlainString());
                break;
            case R.id.iv_market_fons:   //是否收藏按钮
                if (isCollection) {
                    isCollection = false;
                    ivMarketFons.setImageResource(R.mipmap.ico_collect_normal);
                    mPresent.requestCancelCollection(coin.getFid());
                    showToast(getString(R.string.cancel_collect_success));
                }
                else {
                    isCollection = true;
                    ivMarketFons.setImageResource(R.mipmap.ico_collect_seclect);
                    mPresent.requestAddCollection(coin.getFid());
//                    requestAddCollection();
                    showToast(getString(R.string.add_collection_success));
                }
                break;
            case R.id.iv_market_rank:   //查看当前币种klin走势
                Intent itTrendChart = new Intent(VBTApplication.getContext(), TrendChartNewActivity.class);
                itTrendChart.putExtra("coin", coin);
                getContext().startActivity(itTrendChart);
                break;
            case R.id.btn_buy:  //买入
                updateBuyStatus(0);
                break;
            case R.id.btn_sell: //卖出
                updateBuyStatus(1);
                break;
            case R.id.tv_allorder_cancel: //是否撤单
                new IconMessageDialog.Builder(getContext())
                        .setTitle(R.string.hint_message)
                        .setContent(R.string.confirm_revoke)
                        .setNegativeMessage(R.string.cancel)
                        .setPositiveMessage(R.string.confirm)
                        .setPositiveButtonListener((dialog, which) -> {
                            if(isLogin){
                                mPresent.cancelAllOrder(coin.getFid());
                                dialog.dismiss();
                            }
                            else {showToast(R.string.login_first);}
                        }).setNegativeButtonListener((dialog, which) -> dialog.dismiss()).build().show();
                break;
            case R.id.iv_history_entrust://历史委托
                Intent intent = new Intent(getContext(), NewsEntrustedManageActivity.class);
                intent.putExtra("id", coin.getFid()+"");
                startActivity(intent);
                break;
        }
    }

    public void buyClick(int type) {
        if (!isLogin) {
            Intent itLogin = new Intent(getContext(), LoginActivity.class);
            itLogin.putExtra("type", "info");
            startActivity(itLogin);
        } else if (TextUtils.isEmpty(edPrice.getText())) {
//            ToastUtils.showToast(getString(R.string.please_input_buy_price));
            showToast(type == 0 ? getString(R.string.please_input_buy_price) : getString(R.string.please_input_sell_price));
        } else if (Double.parseDouble(edPrice.getText().toString()) <= 0) {

//            ToastUtils.showToast(getString(R.string.price_can_not_be_zero));
            showToast(type == 0 ? getString(R.string.price_can_not_be_zero) : getString(R.string.sell_price_can_not_be_zero));
        } else if (TextUtils.isEmpty(edtNumber.getText())) {
//            ToastUtils.showToast(getString(R.string.please_input_buy_volume));
            showToast(type == 0 ? getString(R.string.please_input_buy_volume) : getString(R.string.please_input_sell_volume));
        } else if (Double.parseDouble(edtNumber.getText().toString()) <= 0) {
//            ToastUtils.showToast(getString(R.string.buy_volume_can_not_be_zero));
            showToast(type == 0 ? getString(R.string.buy_volume_can_not_be_zero) : getString(R.string.sell_volume_can_not_be_zero));
        } else if (needPassword) {
            showPasswordDialog(type == 0);
        } else {
//            requestBuyBtcSubmit("");
            new Handler().postDelayed(() -> btnTrade.setText(type==0?"买入":"卖出"), 3000);
            if (type == 0&&btnTrade.getText().equals("确认买入")) {
                mPresent.postUserBuySubmit(coin.getFid(), edtNumber.getText().toString(), edPrice.getText().toString(), "");
            } else if(type==1&&btnTrade.getText().equals("确认卖出")){
                mPresent.postUserSellSubmit(coin.getFid(), edtNumber.getText().toString(), edPrice.getText().toString(), "");
            }
            else {
              btnTrade.setText(type==0?"确认买入":"确认卖出");
            }
        }
    }
    //交易密码dialog
    private void showPasswordDialog(final boolean isBuy) {
        final PasswordDialog passwordDialog = new PasswordDialog(getContext(), getString(R.string.trade_password), getString(R.string.password_hint));
        passwordDialog.setOnConfirmListener(pwd -> {
            if (TextUtils.isEmpty(pwd)) {
//                    ToastUtils.showToast(R.string.password_hint);
                showToast(getString(R.string.password_hint));
                return;
            }
            if (isBuy) {
//                    requestBuyBtcSubmit(pwd);
                mPresent.postUserBuySubmit(coin.getFid(), edtNumber.getText().toString(), edPrice.getText().toString(), pwd);
            } else {
                mPresent.postUserSellSubmit(coin.getFid(), edtNumber.getText().toString(), edPrice.getText().toString(), pwd);
            }
            hideKeyBoard();
            passwordDialog.dismiss();
        });
        passwordDialog.show();
    }

    //隐藏输入法
    public void hideKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    //长链接接收当前币种信息
    @Override
    public void onMessage(String text) {
        if (isDetached()) {
            return;
        }
        try {
            JSONArray jsonData = new JSONArray(text);
            String strName = (String) jsonData.get(0);

            if (strName.equals("ok")) {

            } else if (strName.equals("lease")) {
                //行情信息
            } else if (strName.equals("real")) {
                final String real = jsonData.getString(1);
                getActivity().runOnUiThread(() -> {
                    RealBean realBean = GsonUtil.GsonToBean(real, RealBean.class);
                    if (realBean == null || isDetached()) {
                        return;
                    }
                    if (realBean.getFupanddown() >= 0) {
                        tvCurrentPrice.setTextColor(getResources().getColor(R.color.price_green));
                        tvFupanddown.setTextColor(getResources().getColor(R.color.price_green));
                        tvFupanddown.setBackgroundColor(getResources().getColor(R.color.home_alape_green));
                    } else {
                        tvCurrentPrice.setTextColor(getResources().getColor(R.color.price_red));
                        tvFupanddown.setTextColor(getResources().getColor(R.color.price_red));
                        tvFupanddown.setBackgroundColor(getResources().getColor(R.color.home_alape_red));

                    }
                    tvFupanddown.setText(new BigDecimal(realBean.getFupanddown()).multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_DOWN) + "%");
                    tvCurrentPrice.setText(MoneyUtils.decimalByUp(coin.getPriceDecimals(), new BigDecimal(realBean.getLast())).toPlainString());
                    tvPriceValue.setText("≈" + MoneyUtils.decimalByUp(LanguageUtils.currentLanguage == 1 ? 4 : 4, new BigDecimal(MoneyUtils.mul(realBean.getLast(), coin.getRate()))) + getString(R.string.money_unit));
                }
                );
                //最新交易买单
            } else if (strName.equals("entrust-buy")) {
                final String buy = jsonData.getString(1);
//                buyDepthRefresh(buy);
                mPresent.getBuyDaata(getActivity(), buy);
                //
                //最新交易卖单
            }
            else if (strName.equals("entrust-sell")) {
                final String sell = (String) jsonData.get(1);
//                sellOutRefresh(sell);
                mPresent.getSellData(getActivity(), sell);
            } else if (strName.equals("entrust-log")) {
//                final String log = jsonData.getString(1);
//                getActivity().runOnUiThread(() -> entrustLog(log));
                //我的交易
            } else if (strName.equals("entrust-update")) {
                final String update = jsonData.getString(1);
                EntrustUpdate entrustUpdate = GsonUtil.GsonToBean(update, EntrustUpdate.class);
                //未登录
                if (entrustUpdate == null || entrustUpdate.getIsLogin() == 0 || isDetached()) {
                    return;
                }
//                EntrustUpdate(entrustUpdate);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            Log.e(e, "onMessage");
        }
    }


    public void getResonateStatus() {
        RetrofitHelper
                .getIns()
                .getZgtopApi()
                .getResonateStatus()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<RequestResult<ResonateBean>>(this) {
                    @Override
                    protected void success(RequestResult<ResonateBean> result) {
                        if (tvActivity.getVisibility() == View.GONE) {
                            tvActivity.setVisibility(View.VISIBLE);
                        }
                        ResonateBean resonateBean = result.getData();
                        num = resonateBean.getNum();
                        switch (resonateBean.getStatus()) {
                            case 2:
                                if (timerStatus != 2) {
                                    startResonateTimer(2);
                                }
                                break;
                            case 1:
                                restTime = (resonateBean.getStartTime() - resonateBean.getTimestamp()) / 1000;
                                if (timerStatus != 1 && restTime > 0) {
                                    startResonateTimer(1);
                                }
                                break;
                        }
                    }
                });
    }

    /**
     * @param type 1 倒计时， 2 15秒刷新
     */
    private void startResonateTimer(int type) {
        timerStatus = type;
        if (resonateTimer != null) {
            resonateTimer.cancel();
            resonateTimer = new Timer("Resonate");
            switch (type) {
                case 1:
                    TimerTask downCount = new TimerTask() {
                        @Override
                        public void run() {
                            restTime--;
                            if (restTime == 0) {
                                startResonateTimer(2);
                                return;
                            }
                            handler.sendEmptyMessage(0x1650);
                        }
                    };

                    resonateTimer.schedule(downCount, 1000, 1000);
                    break;
                case 2:
                    TimerTask refresh = new TimerTask() {
                        @Override
                        public void run() {
                            getResonateStatus();
                        }
                    };
                    handler.sendEmptyMessage(0x1);

                    resonateTimer.schedule(refresh, 15 * 1000, 15 * 1000);
                    break;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);
        SocketUtils.getIns().removeListener(this);
        rxBusDisposable.dispose();
        if (resonateTimer != null) {
            resonateTimer.cancel();
            resonateTimer = null;
        }
//        super.onDestroy();
    }
    //进度条改变监听
    @Override
    public void onSeeking(SeekParams seekParams) {
        if(coin==null) return;
        progress = new BigDecimal(seekBar.getProgress()).divide(new BigDecimal(100)).doubleValue();
        if (Constants.isBuy) {
            edtNumber.setText(Double.valueOf(edPrice.getText().toString()) > 0 ? MoneyUtils.decimalByUp(coin.getAmountDecimals(), new BigDecimal(MoneyUtils.mul(Double.valueOf(strActualRmb), progress)).divide(new BigDecimal(edPrice.getText().toString()), 4, BigDecimal.ROUND_DOWN)).toPlainString() : "0.00");
        } else {
            edtNumber.setText(MoneyUtils.decimalByUp(coin.getAmountDecimals(), new BigDecimal(MoneyUtils.mul(Double.valueOf(strActualCurrency), progress))).toPlainString());
        }
    }

    @Override
    public void onStartTrackingTouch(IndicatorSeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(IndicatorSeekBar seekBar) {

    }

    //
    //获取用户信息成功
    @Override
    public void getMarketUserInfoSuccess(RefreshUserInfoBean response) {
        smartRefreshLayout.finishRefresh();
        Message message = new Message();
        message.obj = response.getToken();
        message.what = 0x1305;
        handler.sendMessage(message);
        sendMessage();
        hideLoadingDialog();
        if (isDetached()) {
            return;
        }
        if (response.getIsLogin().equals("1")) {
            isLogin = true;
            //可用币种数量
            strActualCurrency = response.getVirtotal() > 0 ? MoneyUtils.decimalByUp(coin.getAmountDecimals(), new BigDecimal(response.getVirtotal())) + "" : "0.0000";//可用币种的数量
            freeseRmb = response.getVirfrozen() > 0 ? MoneyUtils.decimalByUp(coin.getAmountDecimals(), new BigDecimal(response.getVirfrozen())) + "" : "0.0000";//被冻结的币种数量
            strFreeseCoinNum = response.getRmbfrozen() > 0 ? MoneyUtils.decimalByUp(coin.getAmountDecimals(), new BigDecimal(response.getRmbfrozen())).toPlainString() : "0.000";//被冻结的USDT
            if (response.getRmbtotal() > 0) {
                //可用的USDT
                strActualRmb = MoneyUtils.decimalByUp(coin.getAmountDecimals(), new BigDecimal(response.getRmbtotal())).toPlainString();
                BigDecimal dividedAmount = new BigDecimal(response.getRecommendPrizebuy());
                if (dividedAmount.doubleValue() <= 0) {
                    strActualNumber = "0.0000";
                } else {
                    //strActualNumber = MoneyUtils.divide4ByUp(divideAmount, dividedAmount).toString();
                    strActualNumber = (response.getRmbtotal() / response.getRecommendPrizebuy()) + "";
                    DecimalFormat formater = new DecimalFormat();
                    formater.setMaximumFractionDigits(4);
                    formater.setGroupingSize(0);
                    formater.setRoundingMode(RoundingMode.FLOOR);
                    strActualNumber = MoneyUtils.accuracy4Position(formater.format(Double.parseDouble(strActualNumber)));
                }
            } else {
                strActualNumber = "0.0000";
                strActualRmb = "0.0000";
            }
            needPassword = response.isNeedTradePasswd();
            btnTrade.setText(Constants.isBuy ? getString(R.string.buy) : getString(R.string.sell));
            tvUseful.setText(Constants.isBuy ? String.format("%s%s", strActualRmb, coin.getGroup()) : String.format("%s%s", strActualCurrency, coin.getFShortName()));
            tvUserFreese.setText(Constants.isBuy ? String.format("%s%s", strFreeseCoinNum, coin.getGroup()) : String.format("%s%s", freeseRmb, coin.getFShortName()));
        } else {
            btnTrade.setText(R.string.login);
            isLogin = false;
            strActualNumber = "0.0000";
            strActualRmb = "0.0000";
            strActualCurrency = "0.0000";
        }
        strRecommendedPrice = MoneyUtils.decimalByUp(coin.getPriceDecimals(), new BigDecimal(response.getRecommendPrizebuy())).toPlainString();
        strSellOutPrice = MoneyUtils.decimalByUp(coin.getPriceDecimals(), new BigDecimal(response.getRecommendPrizesell())).toPlainString();
        if (isBuyAndSell == 0) {
            edPrice.setText(Constants.isBuy ? strRecommendedPrice : strSellOutPrice);
        }
    }


    //提交买单,买单 成功
    @Override
    public void postUserBuyOrderSuccess() {
        isBuyAndSell = 1;
        edtNumber.setText("");
        sendMessage();
        mPresent.getRefreshUserInfo(coin.getFid());
    }

    private void sendMessage() {
        RxBusData rxBusData = new RxBusData();
        rxBusData.setMsgName("CoinData");
        rxBusData.setMsgData(coin);
        RxBus.get().post(rxBusData);
    }

    //错误
    @Override
    public void marketError() {
    }

    @Override
    public void marketUserNoPassword() {
        showToast(getString(R.string.need_set_trade_password));
        Intent itPersonalInfo = new Intent(getContext(), RevisePayPasswordActivity.class);
        itPersonalInfo.putExtra("revise", getString(R.string.setting));
        startActivity(itPersonalInfo);
    }


    //获取币种卖单
    @Override
    public void getCoinMarketInfoData(@NotNull List<List<String>> sellList,
                                      double totalSell, double maxSell, int type) {
        this.totalSell = totalSell;
        mSellOutList = sellList;
        mSellOutAdapter.setNewData(sellList);
        if (type == 0) {
            rvSell.scrollToPosition(mSellOutAdapter.getItemCount() - 1);
        }
    }

    //获取币种买单
    @Override
    public void getCoinMarketBuyData(@NotNull List<List<String>> buyList, double totalbuy,
                                     double maxbuy) {
        this.totalBuy = totalbuy;
        mBuyList.clear();
        mBuyList.addAll(buyList);
        mBuyAdapter.setNewData(buyList);
    }

    //获取实时成交数据
    @Override
    public void getCoinMarketInfoSuccess(@NotNull List<List<String>> sellList) {
    }

    //获取币种信息
    @Override
    public void getCoinData(@NotNull MarketRealBean response) {
        try {
            tvCurrentPrice.setTextColor(getResources().getColor(R.color.price_green));
            tvCurrentPrice.setTextColor(getResources().getColor(R.color.price_red));
            tvCurrentPrice.setText(MoneyUtils.decimalByUp(coin.getPriceDecimals(), new BigDecimal(response.getLast())).toPlainString());
            tvPriceValue.setText("≈" + MoneyUtils.decimalByUp(LanguageUtils.currentLanguage == 1 ? 4 : 4, new BigDecimal(MoneyUtils.mul(Double.valueOf(response.getLast()), coin.getRate()))) + getString(R.string.money_unit));
            tvBuyPriceValue.setText("≈" + MoneyUtils.decimalByUp(LanguageUtils.currentLanguage == 1 ? 4 : 4, new BigDecimal(MoneyUtils.mul(Double.valueOf(response.getLast()), coin.getRate()))) + getString(R.string.money_unit));
        } catch (Exception e) {

        }
    }

    @Override
    public void marketCancelOrderSuccess() {
        showToast(getString(R.string.cancel_success));
        sendMessage();
    }

    // 侧面选择币种
    @Override
    public void OnItemListener(@NotNull CoinTradeRankBean.DealDatasBean mCoinTradeRankBean) {
        coinSelectPopoWindow.dismiss();
        Constants.coinName = mCoinTradeRankBean.getFShortName();
        Constants.coinGroup = mCoinTradeRankBean.getIsNew() == 1 ? "POC+" : mCoinTradeRankBean.getGroup();
        getMarketData("");

    }
}
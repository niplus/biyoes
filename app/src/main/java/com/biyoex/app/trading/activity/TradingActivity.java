package com.biyoex.app.trading.activity;


import androidx.lifecycle.Lifecycle;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;
import com.biyoex.app.R;
import com.biyoex.app.common.Constants;
import com.biyoex.app.common.activity.LoginActivity;
import com.biyoex.app.common.bean.DepthBean;
import com.biyoex.app.common.bean.RateBean;
import com.biyoex.app.common.bean.RequestResult;
import com.biyoex.app.common.data.RateLivedata;
import com.biyoex.app.common.data.SessionLiveData;
import com.biyoex.app.common.http.RetrofitHelper;
import com.biyoex.app.common.mvpbase.BaseActivity;
import com.biyoex.app.common.mvpbase.BaseObserver;
import com.biyoex.app.common.mvpbase.BasePresent;
import com.biyoex.app.common.okhttp.OkHttpUtils;
import com.biyoex.app.common.okhttp.SocketUtils;
import com.biyoex.app.common.okhttp.callback.ResponseCallBack;
import com.biyoex.app.common.okhttp.callback.ResultModelCallback;
import com.biyoex.app.common.okhttp.callback.StringCallback;
import com.biyoex.app.common.utils.CashierInputFilterUtils;
import com.biyoex.app.common.utils.DateUtil;
import com.biyoex.app.common.utils.GsonUtil;
import com.biyoex.app.common.utils.LanguageUtils;
import com.biyoex.app.common.utils.ListSpaceItemDecoration;
import com.biyoex.app.common.utils.MoneyUtils;
import com.biyoex.app.common.utils.ToastUtils;
import com.biyoex.app.common.utils.log.Log;
import com.biyoex.app.common.widget.PasswordDialog;
import com.biyoex.app.common.widget.RatioLinearLayout;
import com.biyoex.app.home.bean.CoinTradeRankBean;
import com.biyoex.app.my.activity.RevisePayPasswordActivity;
import com.biyoex.app.trading.bean.EntrustUpdate;
import com.biyoex.app.trading.bean.MarketRealBean;
import com.biyoex.app.trading.bean.MarketRefreshBean;
import com.biyoex.app.trading.bean.RealBean;
import com.biyoex.app.trading.bean.RefreshUserInfoBean;
import com.biyoex.app.trading.bean.ResonateBean;
import com.biyoex.app.trading.bean.TabEntity;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;

import static com.uber.autodispose.AutoDispose.autoDisposable;
import static com.biyoex.app.VBTApplication.FALL_COLOR;
import static com.biyoex.app.VBTApplication.RISE_COLOR;

public class TradingActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener, SocketUtils.OnReceiveMessageListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.st_website_name)
    CommonTabLayout stWebsiteName;
    @BindView(R.id.rv_sell)
    RecyclerView rvSell;
    @BindView(R.id.rv_buy)
    RecyclerView rvBuy;
    @BindView(R.id.tv_current_price)
    TextView tvCurrentPrice;
    @BindView(R.id.tv_price_value)
    TextView tvPriceValue;
    @BindView(R.id.rv_current_entrust)
    RecyclerView rvCurrentEntrust;
    @BindView(R.id.rv_completed)
    RecyclerView rvCompleted;
    @BindView(R.id.sold_record_layout)
    View soldRecordLayout;
    @BindView(R.id.my_transaction_layout)
    View myTransactionLayout;
    @BindView(R.id.market_layout)
    View marketLayout;
    @BindView(R.id.tb_trade_type)
    ToggleButton tbTradeType;
    @BindView(R.id.ed_price)
    EditText edPrice;
    @BindView(R.id.edt_number)
    EditText edtNumber;
    @BindView(R.id.tv_useful)
    TextView tvUseful;
    @BindView(R.id.rg_percent)
    RadioGroup rgPercent;
    @BindView(R.id.btn_trade)
    Button btnTrade;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_trade_volume)
    TextView tvTradeVolume;
    @BindView(R.id.tv_sub)
    TextView tvSub;
    @BindView(R.id.tv_add)
    TextView tvAdd;
    @BindView(R.id.tv_activity)
    TextView tvActivity;
    @BindView(R.id.tv_coin_name)
    TextView tvCoinName;

    private Timer resonateTimer;

    private List<List<String>> mUnfinishedList;
    private List<List<String>> mCompletedList;

    //??????????????????
    private BaseQuickAdapter<List<String>, BaseViewHolder> mCurrentEntrustAdapter;
    private BaseQuickAdapter<List<String>, BaseViewHolder> mCompletedAdapter;

    /**
     * ????????????
     */
    private BaseQuickAdapter<List<String>, BaseViewHolder> soldRecordAdapter;
    private List<List<String>> soldRecordList;

    private CoinTradeRankBean.DealDatasBean coin;

    private String[] mTitles;
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    //??????
    private CommonAdapter mSellOutAdapter;
    //??????
    private CommonAdapter mBuyAdapter;

    private double totalSell;
    private double totalBuy;
    private double maxSell;
    private double maxBuy;

    /**
     * ????????????
     */
    private List<List<String>> mSellOutList;
    private List<List<String>> mBuyList;

    private double rate;

    /**
     * ????????????????????????
     */
    private boolean needPassword;

    /**
     * ????????????
     */
    private String strActualNumber = "0.0000";

    /**
     * ???????????????
     */
    private String strActualRmb = "0";

    /**
     * ??????????????????
     */
    private String strActualCurrency = "0.0000";

    /**
     * ?????????????????? true?????????????????????false
     */
    private boolean isLogin;

    /**
     * ?????????????????????
     */
    private boolean isBuy = true;

    /**
     * ??????????????????
     */
    private String strRecommendedPrice = "0.0000";

    /**
     * ?????????????????????
     */
    private String strSellOutPrice = "0.0000";

    private long restTime;
    /**
     * 0 ??????????????? 1 ???????????? 2 15s??????
     */
    private int timerStatus;
    /**
     * ??????????????????
     */
    private int num;

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x1650) {
                if (tvActivity != null) {
                    tvActivity.setText(String.format("?????????%d???????????????%s", num, DateUtil.formatRestTime(restTime)));
                }
            } else if (msg.what == 0x1305) {
//                SocketUtils.getIns().setOnReceiveMessageListener(TradingActivity.this);
//                SocketUtils.getIns().requestWebSocketInfo(coin.getFid() + "", (String) msg.obj);
            } else if (msg.what == 0x1) {
                if (tvActivity != null) {
                    tvActivity.setText(String.format("???%d??????????????????...", num));
                }
            }
        }
    };

    @Override
    public void initData() {
        Intent itSymbol = getIntent();
        coin = itSymbol.getParcelableExtra("coin");
        boolean isBuytype = itSymbol.getBooleanExtra("isBuy", true);
        this.isBuy = isBuytype;

        if (!isBuy) {
            edPrice.setHint("????????????");
            edtNumber.setHint("????????????");
            edPrice.setText(strSellOutPrice);
            tvUseful.setText(String.format("??????%s%s", strActualCurrency, coin.getFShortName()));
            if (isLogin) {
                btnTrade.setText("??????");
            }
            btnTrade.setBackgroundColor(getResources().getColor(R.color.price_red));
        }

        mSellOutList = new ArrayList<>();
        mBuyList = new ArrayList<>();

        Map<String, RateBean.RateInfo> map = RateLivedata.getIns().getValue();
        if (map != null && map.containsKey(coin.getGroup())) {
            rate = map.get(coin.getGroup()).getRmbPrice();
        }

        SessionLiveData.getIns().observe(this, userinfoBean -> {
            if (userinfoBean != null) {
                isLogin = true;

                btnTrade.setText(isBuy ? R.string.buy : R.string.sell);
                requestRefreshUserInfo();

            } else {
                isLogin = false;
                btnTrade.setText(R.string.login);
            }
        });
    }

    @Override
    protected BasePresent createPresent() {
        return null;
    }

    @OnClick(R.id.rl_fall_back)
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void initComp() {
        tvTitle.setText(coin.getFname_sn());
        tvCoinName.setText(coin.getFShortName());

        initSoldHistory();

        mTitles = new String[]{"????????????", getString(R.string.current_entrust), getString(R.string.entrust_history)};
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], 0, 0));
        }

        stWebsiteName.setTabData(mTabEntities);
        stWebsiteName.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                switch (position) {
                    case 0:
                        marketLayout.setVisibility(View.VISIBLE);
                        soldRecordLayout.setVisibility(View.GONE);
                        myTransactionLayout.setVisibility(View.GONE);
                        break;
                    case 1:
                        marketLayout.setVisibility(View.GONE);
                        soldRecordLayout.setVisibility(View.VISIBLE);
                        myTransactionLayout.setVisibility(View.GONE);
                        break;
                    case 2:
                        marketLayout.setVisibility(View.GONE);
                        soldRecordLayout.setVisibility(View.GONE);
                        myTransactionLayout.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

        tbTradeType.setOnCheckedChangeListener(this);
        tbTradeType.setChecked(!isBuy);

        textControl();
        initPercentView();

        requestReal();
        requestMarketRefresh();
        transactionRecord();
        myTransactionRecord();
        currentEntrust();
        requestRefreshUserInfo();

        //???????????????????????????
        if (coin.getFid() == 38) {
            getResonateStatus();
            tbTradeType.setClickable(false);
            edPrice.setEnabled(false);
            tvAdd.setClickable(false);
            tvSub.setClickable(false);
        }
    }

    private void initPercentView() {
        rgPercent.setOnCheckedChangeListener((group, checkedId) -> {
            double percent = 0;
            switch (checkedId) {
                case R.id.rb_buy25percent:
                    if (!((RadioButton) group.getChildAt(0)).isChecked()) {
                        return;
                    }
                    percent = 0.25;
                    break;
                case R.id.rb_buy50percent:
                    if (!((RadioButton) group.getChildAt(1)).isChecked()) {
                        return;
                    }
                    percent = 0.5;
                    break;
                case R.id.rb_buy75percent:
                    if (!((RadioButton) group.getChildAt(2)).isChecked()) {
                        return;
                    }
                    percent = 0.75;
                    break;
                case R.id.rb_buy100percent:
                    if (!((RadioButton) group.getChildAt(3)).isChecked()) {
                        return;
                    }
                    percent = 1;
                    break;
                default:
                    return;
            }

            if (isBuy) {
                edtNumber.setText(MoneyUtils.decimalByUp(coin.getAmountDecimals(), new BigDecimal(MoneyUtils.mul(Double.valueOf(strActualNumber), percent))).toPlainString());
            } else {
                edtNumber.setText(MoneyUtils.decimalByUp(coin.getAmountDecimals(), new BigDecimal(MoneyUtils.mul(Double.valueOf(strActualCurrency), percent))).toPlainString());
            }
        });
    }

    /**
     * ??????????????????
     */
    public void textControl() {
        //????????????
        CashierInputFilterUtils cashierInputFilterUtils = new CashierInputFilterUtils();
        cashierInputFilterUtils.POINTER_LENGTH = coin.getPriceDecimals();
        final InputFilter[] isPrice = {cashierInputFilterUtils};
        edPrice.setFilters(isPrice);

        edPrice.addTextChangedListener(new TextWatcher() {
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

                if (coin.getFid() != 38) {
                    if (value == 0) {
                        tvSub.setTextColor(getResources().getColor(R.color.colorText4));
                        tvSub.setClickable(false);
                    } else {
                        tvSub.setTextColor(0xFFFFEDC8);
                        tvSub.setClickable(true);
                    }
                }

                if (!TextUtils.isEmpty(edtNumber.getText())) {
                    tvTradeVolume.setText(MoneyUtils.decimalByUp(coin.getPriceDecimals(), new BigDecimal(MoneyUtils.mul(value, Double.valueOf(edtNumber.getText().toString())))).toPlainString() + coin.getGroup());
                }
            }
        });

        CashierInputFilterUtils cashierInputFilterUtils1 = new CashierInputFilterUtils();
        cashierInputFilterUtils1.POINTER_LENGTH = coin.getAmountDecimals();
        InputFilter[] isTradingQuantity = {cashierInputFilterUtils1};
        edtNumber.setFilters(isTradingQuantity);
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
                    tvTradeVolume.setText(MoneyUtils.decimalByUp(coin.getPriceDecimals(), new BigDecimal(MoneyUtils.mul(value, Double.valueOf(edPrice.getText().toString())))).toPlainString() + coin.getGroup());
                }
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_trading_main;
    }

    @Override
    protected void setStatusBar() {
        super.setStatusBar();
        immersionBar.statusBarColor(R.color.tradingBar);
    }

    private void initSoldHistory() {
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

        //??????????????????
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        linearLayoutManager.setAutoMeasureEnabled(true);
        ListSpaceItemDecoration listSpaceItemDecoration = new ListSpaceItemDecoration(20);
        listSpaceItemDecoration.setType(1);
        recyclerView.addItemDecoration(listSpaceItemDecoration);
        recyclerView.setLayoutManager(linearLayoutManager);
//        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(soldRecordAdapter);
    }


    /**
     * ????????????????????????
     */
    public void transactionRecord() {
        final LinearLayoutManager sellOutLayout = new LinearLayoutManager(this);
        sellOutLayout.setOrientation(LinearLayoutManager.VERTICAL);
        mSellOutAdapter = new CommonAdapter<List<String>>(this, R.layout.item_market_list, mSellOutList) {
            @Override
            protected void convert(ViewHolder holder, List<String> list, int position) {
                RatioLinearLayout ratioLinearLayout = holder.getView(R.id.ll_layout_trade_info);
//                ratioLinearLayout.setPaintColor(Color.parseColor("#26f96a66"));
                ratioLinearLayout.setPaintColor((0x00ffffff & getResources().getColor(R.color.price_red) | (0x26 << 24)));

                //????????????
                holder.setText(R.id.tv_price, MoneyUtils.decimalByUp(coin.getPriceDecimals(), new BigDecimal(list.get(0))) + "");
                holder.setTextColor(R.id.tv_price, getResources().getColor(R.color.price_red));
                //??????????????????
                double amount = Double.valueOf(list.get(1));
                String showAmount = "";
                if (amount / 1000000 > 1) {
                    showAmount = MoneyUtils.decimalByUp(coin.getAmountDecimals(), new BigDecimal(amount / 1000000)).toPlainString() + "M";
                } else if (amount / 1000 > 1) {
                    showAmount = MoneyUtils.decimalByUp(coin.getAmountDecimals(), new BigDecimal(amount / 1000)).toPlainString() + "K";
                } else {
                    showAmount = MoneyUtils.decimalByUp(coin.getAmountDecimals(), new BigDecimal(amount)).toPlainString();
                }
                holder.setText(R.id.tv_amount, showAmount);
                holder.setTextColor(R.id.tv_amount, getResources().getColor(R.color.price_red));
//                holder.setText(R.id.tv_num, (mSellOutList.size() - position) + "");

                ratioLinearLayout.setValues(amount, maxSell, totalSell);
            }
        };

        mSellOutAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                List<String> data = mSellOutList.get(position);
                double price = Double.valueOf(data.get(0));
                edPrice.setText(MoneyUtils.decimalByUp(coin.getPriceDecimals(), new BigDecimal(price)).toPlainString());
                jumpAnimation(edPrice);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        rvSell.setLayoutManager(sellOutLayout);
        ListSpaceItemDecoration listSpaceItemDecoration1 = new ListSpaceItemDecoration(5);
        listSpaceItemDecoration1.setType(3);
        rvSell.addItemDecoration(listSpaceItemDecoration1);
        rvSell.setAdapter(mSellOutAdapter);

        LinearLayoutManager purchaseLayout = new LinearLayoutManager(this);
        purchaseLayout.setOrientation(LinearLayoutManager.VERTICAL);
        mBuyAdapter = new CommonAdapter<List<String>>(this, R.layout.item_market_list, mBuyList) {
            @Override
            protected void convert(ViewHolder holder, List<String> list, int position) {

                RatioLinearLayout ratioLinearLayout = holder.getView(R.id.ll_layout_trade_info);
                ratioLinearLayout.setPaintColor((0x00ffffff & getResources().getColor(R.color.price_green) | (0x26 << 24)));

                //????????????
                holder.setText(R.id.tv_price, MoneyUtils.decimalByUp(coin.getPriceDecimals(), new BigDecimal(list.get(0))).toPlainString());
                holder.setTextColor(R.id.tv_price, getResources().getColor(R.color.price_green));
                //??????????????????
                double amount = Double.valueOf(list.get(1));
                String showAmount = "";
                if (amount / 1000000 > 1) {
                    showAmount = MoneyUtils.decimalByUp(coin.getAmountDecimals(), new BigDecimal(amount / 1000000)).toPlainString() + "M";
                } else if (amount / 1000 > 1) {
                    showAmount = MoneyUtils.decimalByUp(coin.getAmountDecimals(), new BigDecimal(amount / 1000)).toPlainString() + "K";
                } else {
                    showAmount = MoneyUtils.decimalByUp(coin.getAmountDecimals(), new BigDecimal(amount)).toPlainString();
                }
                holder.setText(R.id.tv_amount, showAmount);
                holder.setTextColor(R.id.tv_amount, getResources().getColor(R.color.price_green));
//                holder.setText(R.id.tv_num, (1 + position) + "");

//                    ratioLinearLayout.setRatio(amount / totalBuy);
                ratioLinearLayout.setValues(amount, maxBuy, totalBuy);

            }
        };
        mBuyAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                List<String> data = mBuyList.get(position);
                double price = Double.valueOf(data.get(0));
                edPrice.setText(MoneyUtils.decimalByUp(coin.getPriceDecimals(), new BigDecimal(price)).toPlainString());
                jumpAnimation(edPrice);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        rvBuy.setLayoutManager(purchaseLayout);
        ListSpaceItemDecoration listSpaceItemDecoration = new ListSpaceItemDecoration(5);
        listSpaceItemDecoration.setType(1);
        rvBuy.addItemDecoration(listSpaceItemDecoration);
        rvBuy.setAdapter(mBuyAdapter);
    }

    private void jumpAnimation(TextView textView) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(01f, 1.2f, 1f, 1.2f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(100);
        textView.startAnimation(scaleAnimation);
    }

    public void myTransactionRecord() {
        LinearLayoutManager completedLayout = new LinearLayoutManager(TradingActivity.this);
        completedLayout.setOrientation(LinearLayoutManager.VERTICAL);
        mCompletedList = new ArrayList<>();
        mCompletedAdapter = new BaseQuickAdapter<List<String>, BaseViewHolder>(R.layout.item_trade_entrust, mCompletedList) {
            @Override
            protected void convert(BaseViewHolder helper, List<String> item) {
//                helper.setText(R.id.tv_currency_abbreviated, coin.getFname_sn().replaceAll(" ", ""));
                //0:?????????1:???????????????2:???????????????3:??????ID???4:?????????1????????? ???2 ???????????????3???????????????4????????? ??????5:????????????6:???????????????7:??????????????????8:????????????

                if (item.get(0).equals("1")) {
                    helper.setTextColor(R.id.tv_entrust_type, FALL_COLOR);
                    helper.setText(R.id.tv_entrust_type, R.string.sell);
                } else {
                    helper.setText(R.id.tv_entrust_type, R.string.buy);
                    helper.setTextColor(R.id.tv_entrust_type, RISE_COLOR);
                }

                String status = item.get(4);
//                helper.setVisible(R.id.tv_cancel, false);
//                helper.setVisible(R.id.tv_status, true);

//                switch (status) {
//                    case "3":
//                        helper.setText(R.id.tv_status, R.string.already_deal);
//                        helper.setTextColor(R.id.tv_status, getResources().getColor(R.color.price_green));
//                        break;
//                    case "4":
//                        helper.setText(R.id.tv_status, R.string.already_revoke);
//                        helper.setTextColor(R.id.tv_status, getResources().getColor(R.color.colorText4));
//                        break;
//                }

//                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm:ss");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

                helper.setText(R.id.tv_entrust_price, MoneyUtils.decimalByUp(coin.getPriceDecimals(), new BigDecimal(item.get(1))).toPlainString());
                helper.setText(R.id.tv_number, MoneyUtils.decimalByUp(coin.getAmountDecimals(), new BigDecimal(item.get(7))).toPlainString());
                helper.setText(R.id.tv_time, simpleDateFormat.format(Long.valueOf(item.get(6))));

//                //????????????
//                double avgPrice = 0;
//                if (!TextUtils.isEmpty(item.get(8))){
//                    avgPrice = Double.valueOf(item.get(8));
//                }
//                //?????????
//                double turnVolume = MoneyUtils.mul(Double.valueOf(item.get(8)), Double.valueOf(item.get(2)));
//                if (avgPrice == 0){
//                    helper.setText(R.id.tv_close_price, "--");
//                }else {
//                    helper.setText(R.id.tv_close_price, MoneyUtils.decimalByUp(coin.getPriceDecimals(), new BigDecimal(avgPrice)).toPlainString());
//                }
                helper.setText(R.id.tv_success_number, MoneyUtils.decimalByUp(coin.getAmountDecimals(), new BigDecimal(item.get(2))).toPlainString());

//                helper.setText(R.id.tv_close_volume, MoneyUtils.decimalByUp(coin.getPriceDecimals(), new BigDecimal(turnVolume)).toPlainString());

            }
        };
        mCompletedAdapter.setEmptyView(R.layout.layout_no_data, (ViewGroup) rvCompleted.getParent());
        ListSpaceItemDecoration listSpaceItemDecoration = new ListSpaceItemDecoration(20);
        listSpaceItemDecoration.setType(1);
        rvCompleted.addItemDecoration(listSpaceItemDecoration);
        //??????????????????
        completedLayout.setSmoothScrollbarEnabled(true);
        completedLayout.setAutoMeasureEnabled(true);
        rvCompleted.setNestedScrollingEnabled(false);
        rvCompleted.setLayoutManager(completedLayout);
        rvCompleted.setAdapter(mCompletedAdapter);
    }

    /**
     * ????????????
     */
    public void currentEntrust() {
        LinearLayoutManager sellOutLayout = new LinearLayoutManager(this);
        sellOutLayout.setOrientation(LinearLayoutManager.VERTICAL);
        mUnfinishedList = new ArrayList<>();
        mCurrentEntrustAdapter = new BaseQuickAdapter<List<String>, BaseViewHolder>(R.layout.item_trade_entrust, mUnfinishedList) {
            @Override
            protected void convert(BaseViewHolder helper, final List<String> item) {
//                helper.setText(R.id.tv_currency_abbreviated, coin.getFname_sn().replaceAll(" ", ""));
                //0:?????????1:???????????????2:??????????????????3:??????ID???4:???????????????5:?????????1????????? ???2 ???????????????3???????????????4???????????????6:???????????????7:????????????

                if (item.get(0).equals("1")) {
                    helper.setTextColor(R.id.tv_entrust_type, FALL_COLOR);
                    helper.setText(R.id.tv_entrust_type, R.string.sell);
                } else {
                    helper.setText(R.id.tv_entrust_type, R.string.buy);
                    helper.setTextColor(R.id.tv_entrust_type, RISE_COLOR);
                }

//                    int status = dataJson.getInt(5);
//                helper.setVisible(R.id.tv_cancel, true);
//                helper.setVisible(R.id.tv_status, false);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

                helper.setText(R.id.tv_entrust_price, MoneyUtils.decimalByUp(coin.getPriceDecimals(), new BigDecimal(item.get(1))).toPlainString());
                helper.setText(R.id.tv_number, MoneyUtils.decimalByUp(coin.getAmountDecimals(), new BigDecimal(item.get(6))).toPlainString());
                helper.setText(R.id.tv_time, simpleDateFormat.format(Long.valueOf(item.get(4))));

                helper.setVisible(R.id.tv_cancle, true);


//                //????????????
//                double avgPrice = 0;
//                if (!TextUtils.isEmpty(item.get(7))){
//                    avgPrice = Double.valueOf(item.get(7));
//                }
//                //?????????
//                double turnVolume = MoneyUtils.mul(MoneyUtils.sub(item.get(6), item.get(2)).doubleValue(), avgPrice);
//
//                if (avgPrice != 0) {
//
//                    helper.setText(R.id.tv_close_price, MoneyUtils.decimalByUp(coin.getPriceDecimals(), new BigDecimal(avgPrice)).toPlainString());
//                } else {
//                    helper.setText(R.id.tv_success_number, "0.00");
//                    helper.setText(R.id.tv_close_price, "--");
//                }
                helper.setText(R.id.tv_success_number, MoneyUtils.decimalByUp(coin.getAmountDecimals(), MoneyUtils.sub(item.get(6), item.get(2))).toPlainString());
//                helper.setText(R.id.tv_close_volume, MoneyUtils.decimalByUp(coin.getPriceDecimals(), new BigDecimal(turnVolume)).toPlainString());

//                final String id = item.get(3);
                helper.setOnClickListener(R.id.tv_cancle, v -> new AlertDialog.Builder(TradingActivity.this)
                        .setTitle(R.string.hint_message)
                        .setMessage(R.string.confirm_revoke)
                        .setNegativeButton(R.string.confirm, (dialog, which) -> {
                            showLoadingDialog();
                            requestCancelEntrust(item.get(3));

                        })
                        .setPositiveButton(R.string.cancel, (dialog, which) -> {

                        })
                        .show());

            }
        };
        mCurrentEntrustAdapter.setEmptyView(R.layout.layout_no_data, (ViewGroup) rvCurrentEntrust.getParent());

        rvCurrentEntrust.setLayoutManager(sellOutLayout);

        ListSpaceItemDecoration listSpaceItemDecoration = new ListSpaceItemDecoration(20);
        listSpaceItemDecoration.setType(1);
        //??????????????????
        sellOutLayout.setSmoothScrollbarEnabled(true);
        sellOutLayout.setAutoMeasureEnabled(true);
        rvCurrentEntrust.setLayoutManager(sellOutLayout);
        rvCurrentEntrust.setNestedScrollingEnabled(false);
        rvCurrentEntrust.addItemDecoration(listSpaceItemDecoration);
        rvCurrentEntrust.setAdapter(mCurrentEntrustAdapter);
    }

    /**
     * ????????????
     */
    public void requestReal() {
        OkHttpUtils
                .get()
                .url(Constants.BASE_URL + "v2/market/real")
                .addHeader("X-Requested-With", "XMLHttpReques")
                .addParams("symbol", coin.getFid() + "")
                .addParams("t", System.currentTimeMillis() + "")
                .build()
                .execute(new ResultModelCallback(this, new ResponseCallBack<MarketRealBean>() {
                    @Override
                    public void onError(String e) {

                    }

                    @Override
                    public void onResponse(MarketRealBean response) {
                        if (isDestroyed()) {
                            return;
                        }
//                        if (Double.valueOf(response.getFupanddown()) >= 0) {
//                            tvUpDown.setTextColor(getResources().getColor(R.color.price_green));
//                            tvUpDown.setText("+" + response.getFupanddown() + "%");
//                            ivUpDown.setImageResource(R.mipmap.ico_home_rise);
//                            tvActualPrice.setTextColor(getResources().getColor(R.color.price_green));
                        tvCurrentPrice.setTextColor(getResources().getColor(R.color.price_green));
//                        } else {
//                            tvUpDown.setTextColor(getResources().getColor(R.color.price_red));
//                            tvUpDown.setText(response.getFupanddown() + "%");
//                            ivUpDown.setImageResource(R.mipmap.ico_home_fall);
//                            tvActualPrice.setTextColor(getResources().getColor(R.color.price_red));
                        tvCurrentPrice.setTextColor(getResources().getColor(R.color.price_red));
//                        }
//
//                        tvActualPrice.setText(MoneyUtils.decimalByUp(coin.getPriceDecimals(), new BigDecimal(response.getLast())).toPlainString());
                        tvCurrentPrice.setText(MoneyUtils.decimalByUp(coin.getPriceDecimals(), new BigDecimal(response.getLast())).toPlainString());
//
//
//                        tvRmbValue.setText("???" + MoneyUtils.decimalByUp(LanguageUtils.currentLanguage == 1 ? 2 : 4, new BigDecimal(MoneyUtils.mul(Double.valueOf(response.getLast()), rate))) + getString(R.string.money_unit));
                        tvPriceValue.setText("???" + MoneyUtils.decimalByUp(LanguageUtils.currentLanguage == 1 ? 2 : 4, new BigDecimal(MoneyUtils.mul(Double.valueOf(response.getLast()), rate))) + getString(R.string.money_unit));
////
//                        tvHighestPrice.setText(MoneyUtils.decimalByUp(coin.getPriceDecimals(), new BigDecimal(response.getHigh())).toPlainString());
//                        tvLowestPrice.setText(MoneyUtils.decimalByUp(coin.getPriceDecimals(), new BigDecimal(response.getLow())).toPlainString());
//                        tvDayVolume.setText(MoneyUtils.decimalByUp(coin.getAmountDecimals(), new BigDecimal(response.getVol())).toPlainString());
                    }

                }));
    }

    /**
     * ??????????????????
     */
    public void requestMarketRefresh() {
        OkHttpUtils
                .get()
                .addHeader("X-Requested-With", "XMLHttpReques")
                .url(Constants.BASE_URL + "v2/market/marketRefresh.html")
                .addParams("symbol", coin.getFid() + "")
                .addParams("t", System.currentTimeMillis() + "")
                .addParams("deep", "4")
                .build()
                .execute(new ResultModelCallback(this, new ResponseCallBack<MarketRefreshBean>() {
                    @Override
                    public void onError(String e) {
                        //ToastUtils.showToast(e);
                        //hideLoadingDialog();
                    }

                    @Override
                    public void onResponse(MarketRefreshBean response) throws JSONException {

                        if (isDestroyed()) {
                            return;
                        }

                        List<DepthBean> buydata = new ArrayList<>();
                        List<DepthBean> sellData = new ArrayList<>();
                        //????????????????????????????????????
                        if (mSellOutList.size() > 0) {
                            mSellOutList.clear();
                        }

                        totalSell = 0;
                        maxSell = 0;

                        //??????????????????????????????????????????list?????????
                        for (int i = 0; i < response.getSellDepthList().size(); i++) {
                            if (i >= 30) {
                                break;
                            }
                            String data = response.getSellDepthList().get(i) + "";
                            JSONArray jsonObject = new JSONArray(data);

                            double volume = jsonObject.getDouble(1);
                            sellData.add(new DepthBean(jsonObject.getDouble(0), volume));

                            //?????????????????????????????????
//                            if (i == 0 && Double.valueOf(strRecommendedPrice) == 0) {
//                                strRecommendedPrice = MoneyUtils.decimalByUp(coin.getPriceDecimals(), new BigDecimal(jsonObject.getDouble(0))).toString();
//                                if (isBuy){
//                                    edPrice.setText(strRecommendedPrice);
//                                }
//                            }

                            mSellOutList.add(0, response.getSellDepthList().get(i));
                            totalSell += volume;
                            if (volume > maxSell) {
                                maxSell = volume;
                            }
                        }


                        if (mBuyList.size() > 0) {
                            mBuyList.clear();
                        }

                        totalBuy = 0;
                        maxBuy = 0;
                        //??????????????????????????????????????????list????????????
                        for (int i = 0; i < response.getBuyDepthList().size(); i++) {
                            if (i >= 30) {
                                break;
                            }
                            String data = response.getBuyDepthList().get(i) + "";
                            JSONArray jsonObject = new JSONArray(data);

                            double volume = jsonObject.getDouble(1);
                            buydata.add(new DepthBean(jsonObject.getDouble(0), volume));

//                            ?????????????????????????????????
//                            if (i == 0 && Double.valueOf(strSellOutPrice) == 0) {
//                                strSellOutPrice = MoneyUtils.decimalByUp(coin.getPriceDecimals(), new BigDecimal(jsonObject.getDouble(0))).toString();
//                                if (!isBuy){
//                                    edPrice.setText(strSellOutPrice);
//                                }
//                            }

                            mBuyList.add(response.getBuyDepthList().get(i));
                            totalBuy += volume;
                            if (volume > maxBuy) {
                                maxBuy = volume;
                            }
                        }

                        soldRecordList.addAll(response.getRecentDealList());
                        soldRecordAdapter.notifyDataSetChanged();
                        mBuyAdapter.notifyDataSetChanged();
                        mSellOutAdapter.notifyDataSetChanged();
                        rvSell.scrollToPosition(mSellOutAdapter.getItemCount() - 1);
                    }

                }));
    }

    /**
     * ??????????????????
     */
    public void requestRefreshUserInfo() {
        OkHttpUtils
                .get()
                .url(Constants.BASE_URL + "v2/market/refreshUserInfo")
                .addHeader("X-Requested-With", "XMLHttpReques")
                .addParams("symbol", coin.getFid() + "")
                .addParams("t", System.currentTimeMillis() + "")
                .build()
                .execute(new ResultModelCallback(this, new ResponseCallBack<RefreshUserInfoBean>() {
                    @Override
                    public void onError(String e) {
                        //ToastUtils.showToast(e);
                        hideLoadingDialog();
                    }

                    @Override
                    public void onResponse(final RefreshUserInfoBean response) throws JSONException {

                        if (isDestroyed()) {
                            return;
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Message message = new Message();
                                message.obj = response.getToken();
                                message.what = 0x1305;
                                handler.sendMessage(message);

                            }
                        });

                        if (response.getIsLogin().equals("1")) {
                            isLogin = true;

                            if (response.getVirtotal() > 0) {
                                strActualCurrency = MoneyUtils.decimalByUp(coin.getAmountDecimals(), new BigDecimal(response.getVirtotal())) + "";
                            } else {
                                strActualCurrency = "0.0000";
                            }

                            if (response.getVirfrozen() > 0) {
                                //??????????????????
//                                tvFrozen.setText(MoneyUtils.decimalByUp(coin.getAmountDecimals(), new BigDecimal(response.getVirfrozen())) + "");
                            } else {
                            }

                            if (response.getRmbtotal() > 0) {
                                //?????????????????????
                                strActualRmb = MoneyUtils.decimalByUp(4, new BigDecimal(response.getRmbtotal())).toPlainString();

                                BigDecimal dividedAmount = new BigDecimal(response.getRecommendPrizebuy());
                                if (dividedAmount.doubleValue() <= 0) {
                                    strActualNumber = "0.0000";
                                } else {
                                    strActualNumber = (response.getRmbtotal() / response.getRecommendPrizebuy()) + "";

                                    DecimalFormat formater = new DecimalFormat();
                                    formater.setMaximumFractionDigits(4);
                                    formater.setGroupingSize(0);
                                    formater.setRoundingMode(RoundingMode.FLOOR);
                                    strActualNumber = MoneyUtils.decimalByUp(coin.getAmountDecimals(), new BigDecimal(formater.format(Double.parseDouble(strActualNumber)))) + "";
                                }

                            } else {
                                strActualNumber = "0.0000";
                                strActualRmb = "0.0000";
                            }

                            if (response.getVirtotal() > 0) {
                                //????????????
//                                edSelloutNumber.setHint(MoneyUtils.decimalByUp(coin.getAmountDecimals(), new BigDecimal(response.getVirtotal())) + "");
                            } else {

                            }

                            //????????????????????????????????????
                            needPassword = response.isNeedTradePasswd();

                            //??????????????????
                            if (mCompletedList.size() > 0) {
                                mCompletedList.clear();
                            }

                            mCompletedList.addAll(response.getEntrustListLog());
                            mCompletedAdapter.notifyDataSetChanged();

                            if (mUnfinishedList.size() > 0) {
                                mUnfinishedList.clear();
                            }
                            mUnfinishedList.addAll(response.getEntrustList());
                            mCurrentEntrustAdapter.notifyDataSetChanged();


                        } else {
                            isLogin = false;
                            strActualNumber = "0.0000";
                            strActualRmb = "0.0000";

                            strActualCurrency = "0.0000";
//                            tvFrozen.setText("0.0000");
                        }

                        if (!isLogin) {
                            btnTrade.setText(R.string.login);
                        } else if (isBuy) {
                            btnTrade.setText("??????");
                            edPrice.setText(strRecommendedPrice);
                            tvUseful.setText(String.format("??????%s%s", strActualRmb, coin.getGroup()));
                        } else {
                            btnTrade.setText("??????");
                            edPrice.setText(strRecommendedPrice);
                            tvUseful.setText(String.format("??????%s%s", strActualCurrency, coin.getFShortName()));
                        }

                        strRecommendedPrice = MoneyUtils.decimalByUp(coin.getPriceDecimals(), new BigDecimal(response.getRecommendPrizebuy())).toPlainString();
                        strSellOutPrice = MoneyUtils.decimalByUp(coin.getPriceDecimals(), new BigDecimal(response.getRecommendPrizesell())).toPlainString();
                        edPrice.setText(isBuy ? strRecommendedPrice : strSellOutPrice);
                        // TODO: 2019/4/29 ?????????????????????
//                        tvValueMoney.setText(getString(R.string.useful) + strActualRmb + coin.getGroup());
//                        tvValueCurrency.setText(getString(R.string.useful) + strActualCurrency + coin.getFShortName());


                        hideLoadingDialog();
                    }

                }));

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        isBuy = !isChecked;
        edtNumber.setText("");

        if (isChecked) {
            edPrice.setHint("????????????");
            edtNumber.setHint("????????????");
            edPrice.setText(strSellOutPrice);
            tvUseful.setText(String.format("??????%s%s", strActualCurrency, coin.getFShortName()));
            // TODO: 2019/4/29 ????????????
            if (isLogin) {
                btnTrade.setText("??????");
            }
            btnTrade.setBackgroundColor(getResources().getColor(R.color.price_red));

        } else {
            edPrice.setHint("????????????");
            edtNumber.setHint("????????????");
            edPrice.setText(strRecommendedPrice);
            tvUseful.setText(String.format("??????%s%s", strActualRmb, coin.getGroup()));

            if (isLogin) {
                btnTrade.setText("??????");
            }
            btnTrade.setBackgroundColor(getResources().getColor(R.color.price_green));
        }
    }

    /**
     * ????????????
     *
     * @param id
     */
    public void requestCancelEntrust(String id) {
        Log.i("request cancelEntrust");
        OkHttpUtils
                .post()
                .url(Constants.BASE_URL + "v2/market/cancelEntrust.html")
                .addHeader("X-Requested-With", "XMLHttpReques")
                .addParams("id", id)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showToast(getString(R.string.revoke_failed));
                        hideLoadingDialog();
                    }

                    @Override
                    public void onResponse(String response, int id) throws JSONException {
                        ToastUtils.showToast(getString(R.string.already_revoke));
                        requestAgainRefreshUserInfo();
                        hideLoadingDialog();
                    }
                });
    }

    /**
     * ??????????????????
     */
    public void requestAgainRefreshUserInfo() {

        OkHttpUtils
                .get()
                .url(Constants.BASE_URL + "v2/market/refreshUserInfo")
                .addHeader("X-Requested-With", "XMLHttpReques")
                .addParams("symbol", coin.getFid() + "")
                .addParams("t", System.currentTimeMillis() + "")
                .build()
                .execute(new ResultModelCallback(this, new ResponseCallBack<RefreshUserInfoBean>() {
                    @Override
                    public void onError(String e) {
                        //ToastUtils.showToast(e);
                        hideLoadingDialog();
                    }

                    @Override
                    public void onResponse(final RefreshUserInfoBean response) throws JSONException {

                        if (isDestroyed()) {
                            return;
                        }

                        if (response.getIsLogin().equals("1")) {
                            isLogin = true;

                            if (response.getVirtotal() > 0) {
                                strActualCurrency = MoneyUtils.accuracy4Position(MoneyUtils.decimal4ByUp(new BigDecimal(response.getVirtotal())) + "");
                                //??????????????????
                            } else {
                                strActualCurrency = "0.0000";
                            }

                            if (response.getVirfrozen() > 0) {
                                //??????????????????
//                                tvFrozen.setText(MoneyUtils.accuracy4Position(MoneyUtils.decimal4ByUp(new BigDecimal(response.getVirfrozen())) + ""));
                            } else {
                            }

                            if (response.getRmbtotal() > 0) {
                                //?????????????????????
                                strActualRmb = MoneyUtils.accuracy4Position(MoneyUtils.decimal4ByUp(new BigDecimal(response.getRmbtotal())).toString());

                                BigDecimal divideAmount = new BigDecimal(response.getRmbtotal());
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

                            if (response.getVirtotal() > 0) {
                                //????????????
//                                edSelloutNumber.setHint(MoneyUtils.accuracy4Position(MoneyUtils.decimal4ByUp(new BigDecimal(response.getVirtotal())) + ""));
                            } else {
                            }

                            needPassword = response.isNeedTradePasswd();

                            //??????????????????
                            if (mCompletedList.size() > 0) {
                                mCompletedList.clear();
                            }

                            mCompletedList.addAll(response.getEntrustListLog());

                            mCompletedAdapter.notifyDataSetChanged();

                            if (mUnfinishedList.size() > 0) {
                                mUnfinishedList.clear();
                            }

                            mUnfinishedList.addAll(response.getEntrustList());
                            mCurrentEntrustAdapter.notifyDataSetChanged();


                        } else {
                            isLogin = false;
                            strActualNumber = "0.0000";
                            strActualRmb = "0.0000";

                            strActualCurrency = "0.0000";
                        }

                        if (!isLogin) {
                            btnTrade.setText(R.string.login);
                        } else if (isBuy) {
                            btnTrade.setText("??????");
                            edPrice.setText(strRecommendedPrice);
                            tvUseful.setText(String.format("??????%s%s", strActualRmb, coin.getGroup()));
                        } else {
                            btnTrade.setText("??????");
                            edPrice.setText(strRecommendedPrice);
                            tvUseful.setText(String.format("??????%s%s", strActualCurrency, coin.getFShortName()));
                        }

                        strRecommendedPrice = MoneyUtils.decimalByUp(coin.getPriceDecimals(), new BigDecimal(response.getRecommendPrizebuy())).toPlainString();
                        strSellOutPrice = MoneyUtils.decimalByUp(coin.getPriceDecimals(), new BigDecimal(response.getRecommendPrizesell())).toPlainString();
                        edPrice.setText(isBuy ? strRecommendedPrice : strSellOutPrice);
                    }

                }));

    }

    @OnClick({R.id.btn_trade, R.id.tv_sub, R.id.tv_add})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_trade:
                if (isBuy) {
                    buyClick();
                } else {
                    sellClick();
                }
                break;
            case R.id.tv_sub:
                double base1 = getDoubleByDecimals(coin.getPriceDecimals());
                if (!TextUtils.isEmpty(edPrice.getText())) {
                    double currentPrice = Double.valueOf(edPrice.getText().toString());
                    double result = MoneyUtils.sub(currentPrice, base1);
                    if (result < 0) {
                        result = 0;
                    }
                    edPrice.setText(MoneyUtils.decimalByUp(coin.getPriceDecimals(), new BigDecimal(result)).toPlainString());
                }
                break;
            case R.id.tv_add:
                double base = getDoubleByDecimals(coin.getPriceDecimals());
                if (!TextUtils.isEmpty(edPrice.getText())) {
                    double currentPrice = Double.valueOf(edPrice.getText().toString());
                    double result = MoneyUtils.add(currentPrice, base);
                    edPrice.setText(MoneyUtils.decimalByUp(coin.getPriceDecimals(), new BigDecimal(result)).toPlainString());
                } else {
                    edPrice.setText(MoneyUtils.decimalByUp(coin.getPriceDecimals(), new BigDecimal(base)).toPlainString());
                }
                break;
        }
    }

    private double getDoubleByDecimals(int decimals) {
        if (decimals == 0) {
            return 1;
        }

        StringBuilder stringBuilder = new StringBuilder("0.");
        for (int i = 0; i < decimals - 1; i++) {
            stringBuilder.append("0");
        }
        stringBuilder.append("1");

        return Double.valueOf(stringBuilder.toString());
    }

    public void buyClick() {
        if (!isLogin) {
            Intent itLogin = new Intent(this, LoginActivity.class);
            itLogin.putExtra("type", "info");
            startActivity(itLogin);
        } else if (TextUtils.isEmpty(edPrice.getText())) {
            ToastUtils.showToast(getString(R.string.please_input_buy_price));
        } else if (Double.parseDouble(edPrice.getText().toString()) <= 0) {
            ToastUtils.showToast(getString(R.string.price_can_not_be_zero));
        } else if (TextUtils.isEmpty(edtNumber.getText())) {
            ToastUtils.showToast(getString(R.string.please_input_buy_volume));
        } else if (Double.parseDouble(edtNumber.getText().toString()) <= 0) {
            ToastUtils.showToast(getString(R.string.buy_volume_can_not_be_zero));
        } else if (needPassword) {
            showPasswordDialog(true);
        } else {
            requestBuyBtcSubmit("");
        }
    }

    public void sellClick() {
        if (!isLogin) {
            Intent itLogin = new Intent(this, LoginActivity.class);
            itLogin.putExtra("type", "info");
            startActivity(itLogin);
        } else if (TextUtils.isEmpty(edPrice.getText())) {
            ToastUtils.showToast(getString(R.string.please_input_sell_price));
        } else if (Double.parseDouble(edPrice.getText().toString()) <= 0) {
            ToastUtils.showToast(getString(R.string.sell_price_can_not_be_zero));
        } else if (TextUtils.isEmpty(edtNumber.getText())) {
            ToastUtils.showToast(getString(R.string.please_input_sell_volume));
        } else if (Double.parseDouble(edtNumber.getText().toString()) <= 0) {
            ToastUtils.showToast(getString(R.string.sell_volume_can_not_be_zero));
        } else if (needPassword) {
            showPasswordDialog(false);
        } else {
            requestSellBtcSubmit("");
//            showPasswordDialog(true);
        }
    }

    private void showPasswordDialog(final boolean isBuy) {

        final PasswordDialog passwordDialog = new PasswordDialog(this, getString(R.string.trade_password), getString(R.string.password_hint));
        passwordDialog.setOnConfirmListener(new PasswordDialog.OnConfirmListener() {
            @Override
            public void onConfirm(String pwd) {
                if (TextUtils.isEmpty(pwd)) {
                    ToastUtils.showToast(R.string.password_hint);
                    return;
                }

                if (isBuy) {
                    requestBuyBtcSubmit(pwd);
                } else {
                    requestSellBtcSubmit(pwd);
                }

                hideKeyBoard();
                passwordDialog.dismiss();

            }
        });

        passwordDialog.show();
    }

    public void hideKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    /**
     * ????????????
     */
    public void requestBuyBtcSubmit(String password) {
        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("symbol", coin.getFid() + "");
        //??????
        mapParams.put("tradeAmount", edtNumber.getText().toString());
        //??????
        mapParams.put("tradeCnyPrice", edPrice.getText().toString());
        //????????????
        mapParams.put("tradePwd", password);
        OkHttpUtils
                .post()
                .url(Constants.BASE_URL + "v2/market/buyBtcSubmit.html")
                .addHeader("X-Requested-With", "XMLHttpReques")
                .params(mapParams)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(e, "requestBuyBtcSubmit");
                        hideLoadingDialog();
                        ToastUtils.showToast(getString(R.string.make_order_failed));
                    }

                    @Override
                    public void onResponse(String response, int id) throws JSONException {
                        JSONObject dataJson = new JSONObject(response);
                        String resultCode = dataJson.getString("resultCode");

                        Log.i("requestBuyBtcSubmit result :" + resultCode);
                        hideLoadingDialog();
                        if (resultCode.equals("0")) {
                            ToastUtils.showToast(getString(R.string.make_order_success));
                            requestAgainRefreshUserInfo();
                        } else if (resultCode.equals("-5")) {
                            ToastUtils.showToast(getString(R.string.need_set_trade_password));
                            Intent itPersonalInfo = new Intent(TradingActivity.this, RevisePayPasswordActivity.class);
                            itPersonalInfo.putExtra("revise", getString(R.string.setting));
                            startActivity(itPersonalInfo);
                            //?????????????????????
                        } else if (resultCode.equals("-50")) {
                            ToastUtils.showToast(getString(R.string.input_trade_password));
                        } else if (resultCode.equals("-1") || resultCode.equals("-3") || resultCode.equals("-35")) {
                            String value = dataJson.getString("value");
                            ToastUtils.showToast(transactionMessage(resultCode, value));
                        } else if (resultCode.equals("-999")) {
                            String data = dataJson.getString("data");
                            ToastUtils.showToast(data);
                        } else {
                            ToastUtils.showToast(transactionMessage(resultCode, ""));
                        }
                    }
                });
    }

    /**
     * ????????????
     */
    public void requestSellBtcSubmit(String password) {
        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("symbol", coin.getFid() + "");
        //??????
        mapParams.put("tradeAmount", edtNumber.getText().toString());
        //??????
        mapParams.put("tradeCnyPrice", edPrice.getText().toString());
        //????????????
        mapParams.put("tradePwd", password);

        OkHttpUtils
                .post()
                .url(Constants.BASE_URL + "v2/market/sellBtcSubmit.html")
                .addHeader("X-Requested-With", "XMLHttpReques")
                .params(mapParams)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(e, "requestSellBtcSubmit");
                        hideLoadingDialog();
                        ToastUtils.showToast(getString(R.string.make_order_failed));
                    }

                    @Override
                    public void onResponse(String response, int id) throws JSONException {
                        JSONObject dataJson = new JSONObject(response);
                        String resultCode = dataJson.getString("resultCode");
                        Log.i("requestSellBtcSubmit result : " + resultCode);
                        hideLoadingDialog();
                        if (resultCode.equals("0")) {
                            ToastUtils.showToast(getString(R.string.make_order_success));
                            //edPassword.setText("");
                            //requestMarketRefresh();
                            requestAgainRefreshUserInfo();
                        } else if (resultCode.equals("-5")) {
                            ToastUtils.showToast(getString(R.string.need_set_trade_password));
                            Intent itPersonalInfo = new Intent(TradingActivity.this, RevisePayPasswordActivity.class);
                            itPersonalInfo.putExtra("revise", getString(R.string.setting));
                            startActivity(itPersonalInfo);
                            //?????????????????????
                        } else if (resultCode.equals("-50")) {
                            ToastUtils.showToast(getString(R.string.input_trade_password));
                        } else if (resultCode.equals("-1") || resultCode.equals("-3") || resultCode.equals("-35")) {
                            String value = dataJson.getString("value");
                            ToastUtils.showToast(transactionMessage(resultCode, value));
                        } else if (resultCode.equals("-999")) {
                            String data = dataJson.getString("data");
                            ToastUtils.showToast(data);
                        } else {
                            ToastUtils.showToast(transactionMessage(resultCode, ""));
                        }
                    }
                });

    }

    /**
     * ???????????????
     *
     * @param resultCode
     * @return
     */
    public String transactionMessage(String resultCode, String value) {
        String messag = "";
        switch (resultCode) {
            case "-1":
                messag = getString(R.string.message_min_volume) + value;
                break;
            case "-3":
                messag = getString(R.string.message_min_amount) + value;
                break;
            case "-35":
                messag = getString(R.string.message_max_amount) + value;
                break;
            case "-100":
            case "-101":
                messag = getString(R.string.message_not_open_trade);
                break;
            case "-400":
                messag = getString(R.string.message_not_trade_time);
                break;
            case "-4":
                messag = getString(R.string.message_not_enough_balance);
                break;
            case "-50":
                messag = getString(R.string.message_trade_pass_empty);
                break;
            case "-2":
                messag = getString(R.string.message_pass_error);
                break;
            case "-200":
                messag = getString(R.string.message_wallet_exception);
                break;
            case "-900":
                messag = getString(R.string.message_trade_price_error);
                break;
        }
        if (messag.equals("")) {
            messag = getString(R.string.net_error);
        }
        return messag;
    }

    @Override
    public void onMessage(String text) {
        if (isDestroyed()) {
            return;
        }
        try {
            JSONArray jsonData = new JSONArray(text);
            String strName = (String) jsonData.get(0);

            if (strName.equals("ok")) {

            } else if (strName.equals("lease")) {

                //????????????
            } else if (strName.equals("real")) {
                final String real = jsonData.getString(1);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        RealBean realBean = GsonUtil.GsonToBean(real, RealBean.class);

                        if (realBean == null || isDestroyed()) {
                            return;
                        }

                        if (realBean.getFupanddown() >= 0) {
                            tvCurrentPrice.setTextColor(getResources().getColor(R.color.price_green));
                        } else {
                            tvCurrentPrice.setTextColor(getResources().getColor(R.color.price_red));
                        }

                        tvCurrentPrice.setText(MoneyUtils.decimalByUp(coin.getPriceDecimals(), new BigDecimal(realBean.getLast())).toPlainString());

                        tvPriceValue.setText("???" + MoneyUtils.decimalByUp(LanguageUtils.currentLanguage == 1 ? 2 : 4, new BigDecimal(MoneyUtils.mul(realBean.getLast(), rate))) + getString(R.string.money_unit));
                    }
                });


                //??????????????????
            } else if (strName.equals("entrust-buy")) {
                final String buy = jsonData.getString(1);
                buyDepthRefresh(buy);
                //
                //??????????????????
            } else if (strName.equals("entrust-sell")) {
                final String sell = (String) jsonData.get(1);
                sellOutRefresh(sell);

            } else if (strName.equals("entrust-log")) {
                final String log = jsonData.getString(1);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        entrustLog(log);
                    }
                });


                //????????????
            } else if (strName.equals("entrust-update")) {
                final String update = jsonData.getString(1);
                EntrustUpdate entrustUpdate = GsonUtil.GsonToBean(update, EntrustUpdate.class);
                //?????????
                if (entrustUpdate == null || entrustUpdate.getIsLogin() == 0 || isDestroyed()) {
                    return;
                }
                EntrustUpdate(entrustUpdate);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            Log.e(e, "onMessage");
        }
    }

    /**
     * ??????????????????
     *
     * @param
     */
    public void EntrustUpdate(final EntrustUpdate entrustUpdate) {
        //??????????????????
        if (mCompletedList.size() > 0) {
            mCompletedList.clear();
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (isDestroyed()) {
                    return;
                }

                for (int i = 0; i < entrustUpdate.getEntrustListLog().size(); i++) {
                    mCompletedList.add(entrustUpdate.getEntrustListLog().get(i));
                }

                mCompletedAdapter.notifyDataSetChanged();

                if (mUnfinishedList.size() > 0) {
                    mUnfinishedList.clear();
                }

                for (int i = 0; i < entrustUpdate.getEntrustList().size(); i++) {
                    mUnfinishedList.add(entrustUpdate.getEntrustList().get(i));
                }
                mCurrentEntrustAdapter.notifyDataSetChanged();
            }
        });

    }

    /**
     * ??????????????????
     *
     * @param response
     * @throws JSONException
     */
    public void buyDepthRefresh(final String response) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mBuyList.size() > 0) {
                    mBuyList.clear();
                }

                List<DepthBean> buydata = new ArrayList<>();

                List<List<String>> data = new Gson().fromJson(response, new TypeToken<List<List<String>>>() {
                }.getType());

                totalBuy = 0;
                maxBuy = 0;
                for (int i = 0; i < data.size(); i++) {
                    if (i >= 30) {
                        break;
                    }
                    mBuyList.add(data.get(i));
                    double volume = Double.valueOf(data.get(i).get(1));
                    buydata.add(new DepthBean(Double.valueOf(data.get(i).get(0)), volume));
                    totalBuy += volume;
                    if (volume > maxBuy) {
                        maxBuy = volume;
                    }
                }

                if (isDestroyed()) {
                    return;
                }
                mBuyAdapter.notifyDataSetChanged();
            }
        });


    }

    /**
     * ??????????????????
     *
     * @param response
     * @throws JSONException
     */
    public void sellOutRefresh(final String response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mSellOutList.size() > 0) {
                    mSellOutList.clear();
                }
                List<DepthBean> selldata = new ArrayList<>();

                List<List<String>> data = new Gson().fromJson(response, new TypeToken<List<List<String>>>() {
                }.getType());

                totalSell = 0;
                maxSell = 0;

                for (int i = 0; i < data.size(); i++) {
                    if (i >= 30) {
                        break;
                    }
                    mSellOutList.add(0, data.get(i));
                    double volume = Double.valueOf(data.get(i).get(1));
                    selldata.add(new DepthBean(Double.valueOf(data.get(i).get(0)), volume));
                    totalSell += volume;

                    if (volume > maxSell) {
                        maxSell = volume;
                    }
                }

                if (isDestroyed()) {
                    return;
                }
                mSellOutAdapter.notifyDataSetChanged();
            }
        });

    }

    /**
     * ??????????????????
     *
     * @param response
     * @throws JSONException
     */
    public void entrustLog(final String response) {

        if (soldRecordList.size() > 0) {
            soldRecordList.clear();
        }

        List<List<String>> data = new Gson().fromJson(response, new TypeToken<List<List<String>>>() {
        }.getType());
//        soldRecordList.add(null);
        soldRecordList.addAll(data);
//        for (int i = 0; i < data.size(); i++) {
//            soldRecordList.add(data.get(i));
//        }

        if (isDestroyed()) {
            return;
        }
        soldRecordAdapter.notifyDataSetChanged();
    }

    public void getResonateStatus() {
        Log.i("getResonateStatus");
        RetrofitHelper
                .getIns()
                .getZgtopApi()
                .getResonateStatus()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(autoDisposable(AndroidLifecycleScopeProvider.from(this, Lifecycle.Event.ON_DESTROY)))
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
     * @param type 1 ???????????? 2 15?????????
     */
    private void startResonateTimer(int type) {
        timerStatus = type;
        if (resonateTimer != null) {
            resonateTimer.cancel();
        }

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

    @Override
    protected void onDestroy() {
//        SocketUtils.getIns().removeListener(this);
        if (resonateTimer != null) {
            resonateTimer.cancel();
            resonateTimer = null;
        }
        super.onDestroy();
    }
}

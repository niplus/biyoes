package com.biyoex.app.trading.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import androidx.core.widget.NestedScrollView;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.biyoex.app.R;
import com.biyoex.app.VBTApplication;
import com.biyoex.app.common.Constants;
import com.biyoex.app.common.activity.LoginActivity;
import com.biyoex.app.common.base.BaseAppCompatActivity;
import com.biyoex.app.common.bean.DepthBean;
import com.biyoex.app.common.bean.RateBean;
import com.biyoex.app.common.data.RateLivedata;
import com.biyoex.app.common.data.SessionLiveData;
import com.biyoex.app.common.okhttp.OkHttpUtils;
import com.biyoex.app.common.okhttp.SocketUtils;
import com.biyoex.app.common.okhttp.callback.ResponseCallBack;
import com.biyoex.app.common.okhttp.callback.ResultModelCallback;
import com.biyoex.app.common.okhttp.callback.StringCallback;
import com.biyoex.app.common.utils.CashierInputFilterUtils;
import com.biyoex.app.common.utils.GsonUtil;
import com.biyoex.app.common.utils.LanguageUtils;
import com.biyoex.app.common.utils.ListSpaceItemDecoration;
import com.biyoex.app.common.utils.MoneyUtils;
import com.biyoex.app.common.utils.SharedPreferencesUtils;
import com.biyoex.app.common.utils.ToastUtils;
import com.biyoex.app.common.utils.log.Log;
import com.biyoex.app.common.widget.DepthView;
import com.biyoex.app.common.widget.PasswordDialog;
import com.biyoex.app.common.widget.RatioLinearLayout;
import com.biyoex.app.home.bean.CoinTradeRankBean;
import com.biyoex.app.my.activity.RevisePayPasswordActivity;
import com.biyoex.app.trading.bean.EntrustUpdate;
import com.biyoex.app.trading.bean.MarketRealBean;
import com.biyoex.app.trading.bean.MarketRefreshBean;
import com.biyoex.app.trading.bean.RealBean;
import com.biyoex.app.trading.bean.RefreshUserInfoBean;
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

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

import static com.biyoex.app.VBTApplication.FALL_COLOR;
import static com.biyoex.app.VBTApplication.RISE_COLOR;

/**
 * 因为前面交易详情不刷新、导致我把4个页面页面集合在一起去了。
 * Created by LG on 2017/6/28.
 */

public class TradeDetailsActivity extends BaseAppCompatActivity implements SocketUtils.OnReceiveMessageListener, View.OnFocusChangeListener {

    @BindView(R.id.iv_fall_back)
    ImageView ivFallBack;
    @BindView(R.id.rl_fall_back)
    RelativeLayout rlFallBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_menu)
    TextView tvMenu;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.rl_menu)
    RelativeLayout rlMenu;
    @BindView(R.id.st_website_name)
    CommonTabLayout stWebsiteName;
    @BindView(R.id.ll_layout_currency)
    LinearLayout llLayoutCurrency;
    @BindView(R.id.sold_record_layout)
    View soldRecordLayout;
    @BindView(R.id.my_transaction_layout)
    View myTransactionLayout;
    @BindView(R.id.rv_current_entrust)
    RecyclerView rvCurrentEntrust;
    @BindView(R.id.rv_completed)
    RecyclerView rvCompleted;
    @BindView(R.id.ns_scroll)
    NestedScrollView nsScroll;
    @BindView(R.id.tv_lowest_price)
    TextView tvLowestPrice;
    @BindView(R.id.tv_highest_price)
    TextView tvHighestPrice;
    @BindView(R.id.tv_day_volume)
    TextView tvDayVolume;
    @BindView(R.id.tv_actual_price)
    TextView tvActualPrice;
    @BindView(R.id.tv_current_price)
    TextView tvCurrentPrice;
    @BindView(R.id.tv_price_value)
    TextView tvPriceValue;
    @BindView(R.id.iv_up_down)
    ImageView ivUpDown;
    @BindView(R.id.tv_up_down)
    TextView tvUpDown;
    @BindView(R.id.tv_rmb_value)
    TextView tvRmbValue;

    @BindView(R.id.layout_buy)
    View layoutBuy;
    @BindView(R.id.layout_sell)
    View layoutSell;
    @BindView(R.id.depthView)
    DepthView depthView;
    @BindView(R.id.rv_sell)
    RecyclerView rvSell;
    @BindView(R.id.rv_buy)
    RecyclerView rvBuy;
    @BindView(R.id.trading_layout)
    View tradingLayout;
    @BindView(R.id.ed_buy_price)
    EditText edBuyPrice;
    @BindView(R.id.tv_buy_price_rmb)
    TextView tvBuyPriceRmb;
    @BindView(R.id.edt_buy_number)
    EditText edtBuyNumber;
    @BindView(R.id.tv_buy_total_value)
    TextView tvBuyTotalValue;
    @BindView(R.id.tv_buy_currency)
    TextView tvBuyCurrency;
    @BindView(R.id.tv_buy_group)
    TextView tvBuyGroup;
    @BindView(R.id.tv_value_money)
    TextView tvValueMoney;

    @BindView(R.id.ed_sell_price)
    EditText edSellPrice;
    @BindView(R.id.tv_sell_price_rmb)
    TextView tvSellPriceRmb;
    @BindView(R.id.edt_sell_number)
    EditText edtSellNumber;
    @BindView(R.id.tv_sell_total_value)
    TextView tvSellTotalValue;
    @BindView(R.id.tv_sell_currency)
    TextView tvSellCurrency;
    @BindView(R.id.tv_sell_group)
    TextView tvSellGroup;
    @BindView(R.id.tv_value_currency)
    TextView tvValueCurrency;
    @BindView(R.id.iv_second_menu)
    ImageView ivSecondMenu;

    @BindView(R.id.rg_buy)
    RadioGroup rgBuy;
    @BindView(R.id.rg_sell)
    RadioGroup rgSell;
    @BindView(R.id.tv_buy_sub)
    TextView tvBuySub;
    @BindView(R.id.tv_buy_add)
    TextView tvBuyAdd;
    @BindView(R.id.tv_sell_sub)
    TextView tvSellSub;
    @BindView(R.id.tv_sell_add)
    TextView tvSellAdd;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.btn_sell)
    Button btnSellOut;
    @BindView(R.id.btn_buy)
    Button btnBuy;
    @BindView(R.id.ll_sell_price)
    View llSellPrice;
    @BindView(R.id.ll_buy_price)
    View llBuyPrice;
    @BindView(R.id.rl_sell_number)
    View rlSellNumber;
    @BindView(R.id.rl_buy_number)
    View rlBuyNumber;
    @BindView(R.id.tv_price_label)
    TextView tvPriceLabel;
    @BindView(R.id.tv_volume_label)
    TextView tvVolumeLable;

    private String[] mTitles;
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    private List<List<String>> mSellOutList;
    private List<List<String>> mBuyList;

    private List<List<String>> mUnfinishedList;

    private List<List<String>> mCompletedList;
    //卖出
    private CommonAdapter mSellOutAdapter;
    //买入
    private CommonAdapter mBuyAdapter;
    //交易历史记录
    private BaseQuickAdapter<List<String>, BaseViewHolder> mCurrentEntrustAdapter;
    private BaseQuickAdapter<List<String>, BaseViewHolder> mCompletedAdapter;
    /**
     * 推荐买入价格
     */
    private String strRecommendedPrice = "0.0000";

    /**
     * 可买数量
     */
    private String strActualNumber = "0.0000";
    /**
     * 可用币
     */
    private String strActualRmb = "0";
    /**
     * 判断是否登录 true为登录，故反之false
     */
    private boolean isLogin;
    /**
     * 总钱数
     */
    private double mTotalMoney = 0;

    private boolean needPassword;

    /**
     * 推荐买卖出价格
     */
    private String strSellOutPrice = "0.0000";

    /**
     * 可用币种数量
     */
    private String strActualCurrency = "0.0000";

    private CoinTradeRankBean.DealDatasBean coin;

    private boolean isCollection = false;

    private boolean isRgClick = false;

    private double totalSell;
    private double totalBuy;
    private double maxSell;
    private double maxBuy;

    private BaseQuickAdapter<List<String>, BaseViewHolder> soldRecordAdapter;
    private List<List<String>> soldRecordList;

    private double rate;


    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x1650) {

            } else if (msg.what == 0x1305) {
                SocketUtils.getIns().setOnReceiveMessageListener(TradeDetailsActivity.this);
                SocketUtils.getIns().requestWebSocketInfo(coin.getFid() + "", (String) msg.obj);
            }
        }
    };


    @Override
    protected void initView() {
        ivMenu.setImageResource(R.mipmap.zhexian);
        ivMenu.setVisibility(View.VISIBLE);
        ivSecondMenu.setImageResource(R.mipmap.ico_collect_normal);
        ivSecondMenu.setVisibility(View.VISIBLE);

        isCollection = isCollectioned();
        ivSecondMenu.setImageResource(isCollection ? R.mipmap.ico_collect_seclect : R.mipmap.ico_collect_normal);
        ivSecondMenu.setVisibility(View.VISIBLE);

        tvPriceLabel.setText(getString(R.string.rate) + "(" + coin.getGroup() + ")");
        tvVolumeLable.setText(getString(R.string.number) + "(" + coin.getFShortName() + ")");
//        GlideUtils.getInstance().displayCurrencyImage(this, VBTApplication.appPictureUrl + coin.getFurl(), ivCoin);

        buyTextControl();
        selloutTextControl();

        edtSellNumber.setOnFocusChangeListener(this);
        edBuyPrice.setOnFocusChangeListener(this);
        edSellPrice.setOnFocusChangeListener(this);
        edtBuyNumber.setOnFocusChangeListener(this);
        tvTitle.setFocusable(true);
        tvTitle.setFocusableInTouchMode(true);

        initSoldHistory();

        rgBuy.setOnCheckedChangeListener((group, checkedId) -> {
            double percent = 0;
            isRgClick = true;
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
                    isRgClick = false;
                    return;
            }

            edtBuyNumber.setText(MoneyUtils.decimalByUp(coin.getAmountDecimals(), new BigDecimal(MoneyUtils.mul(Double.valueOf(strActualNumber), percent))).toPlainString());
        });

        rgSell.setOnCheckedChangeListener((group, checkedId) -> {
            double percent = 0;
            isRgClick = true;

            switch (checkedId) {
                case R.id.rb_sell25percent:
                    if (!((RadioButton) group.getChildAt(0)).isChecked()) {
                        return;
                    }
                    percent = 0.25;
                    break;
                case R.id.rb_sell50percent:
                    if (!((RadioButton) group.getChildAt(1)).isChecked()) {
                        return;
                    }
                    percent = 0.5;
                    break;
                case R.id.rb_sell75percent:
                    if (!((RadioButton) group.getChildAt(2)).isChecked()) {
                        return;
                    }
                    percent = 0.75;
                    break;
                case R.id.rb_sell100percent:
                    if (!((RadioButton) group.getChildAt(3)).isChecked()) {
                        return;
                    }
                    percent = 1;
                    break;
                default:
                    isRgClick = false;
                    return;
            }

            edtSellNumber.setText(MoneyUtils.decimalByUp(coin.getAmountDecimals(), new BigDecimal(MoneyUtils.mul(Double.valueOf(strActualCurrency), percent))).toPlainString());
        });
    }

    @Override
    protected void initData() {
        Intent itSymbol = getIntent();
        coin = (CoinTradeRankBean.DealDatasBean) itSymbol.getSerializableExtra("coin");
        Log.i(coin.getFname_sn() + " trade page");


        mTitles = new String[]{getString(R.string.buy), getString(R.string.sell), getString(R.string.current_entrust), getString(R.string.entrust_history)};
        tvTitle.setText(coin.getFShortName() + "/" + coin.getGroup());

        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], 0, 0));
        }

        stWebsiteName.setTabData(mTabEntities);
        mSellOutList = new ArrayList<>();

        mBuyList = new ArrayList<>();

        mCompletedList = new ArrayList<>();
        depthView.setAmoutDecimal(coin.getAmountDecimals());
        depthView.setPriceDecimal(coin.getPriceDecimals());

        SessionLiveData.getIns().observe(this, userinfoBean -> {
            if (userinfoBean != null) {
                isLogin = true;

                btnSellOut.setText(R.string.sell);
                btnBuy.setText(R.string.buy);
                requestRefreshUserInfo();

            } else {
                isLogin = false;
                btnSellOut.setText(R.string.login);
                btnBuy.setText(R.string.login);
            }
        });

        Map<String, RateBean.RateInfo> map = RateLivedata.getIns().getValue();
        if (map != null && map.containsKey(coin.getGroup())) {
            if (LanguageUtils.currentLanguage == 1) {
                rate = map.get(coin.getGroup()).getRmbPrice();
            } else {
                rate = map.get(coin.getGroup()).getUsdtPrice();
            }
        }


        mUnfinishedList = new ArrayList<>();

        stWebsiteName.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                llLayoutCurrency.setFocusable(true);
                llLayoutCurrency.setFocusableInTouchMode(true);

                switch (position) {
                    case 0:
                        myTransactionLayout.setVisibility(View.GONE);
                        tradingLayout.setVisibility(View.VISIBLE);
                        soldRecordLayout.setVisibility(View.GONE);
                        layoutBuy.setVisibility(View.VISIBLE);
                        layoutSell.setVisibility(View.GONE);
                        break;
                    case 1:
                        myTransactionLayout.setVisibility(View.GONE);
                        tradingLayout.setVisibility(View.VISIBLE);
                        soldRecordLayout.setVisibility(View.GONE);
                        layoutBuy.setVisibility(View.GONE);
                        layoutSell.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        soldRecordLayout.setVisibility(View.VISIBLE);
                        myTransactionLayout.setVisibility(View.GONE);
                        tradingLayout.setVisibility(View.GONE);
                        break;
                    case 3:
                        hideSoftKeyboard();
                        tradingLayout.setVisibility(View.GONE);
                        soldRecordLayout.setVisibility(View.GONE);
                        myTransactionLayout.setVisibility(View.VISIBLE);
                        break;
                }

            }
            @Override
            public void onTabReselect(int position) {

            }
        });

        transactionRecord();
        currentEntrust();
        myTransactionRecord();

        showLoadingDialog();
        requestRefreshUserInfo();
        requestReal();
        requestMarketRefresh();
    }

    public void myTransactionRecord() {
        LinearLayoutManager completedLayout = new LinearLayoutManager(TradeDetailsActivity.this);
        completedLayout.setOrientation(LinearLayoutManager.VERTICAL);
        mCompletedAdapter = new BaseQuickAdapter<List<String>, BaseViewHolder>(R.layout.item_entrust_info, mCompletedList) {
            @Override
            protected void convert(BaseViewHolder helper, List<String> item) {
                helper.setText(R.id.tv_currency_abbreviated, coin.getFname_sn().replaceAll(" ", ""));
//                0:买卖，1:委托价格，2:交易数量，3:主键ID，4:状态（1未成交，2部分成交），5:成交额，6:创建时间，7:未成交数量，8:成交金额，9:成交均价
                //0:买卖，1:委托价格，2:成交数量，3:主键ID，4:状态（1未成交 ，2 部分成交，3完全成交，4已取消 ），5:成交额，6:创建时间，7:挂单总数量，8:成交均价

                if (item.get(0).equals("1")) {
                    helper.setTextColor(R.id.tv_entrust_type, FALL_COLOR);
                    helper.setText(R.id.tv_entrust_type, R.string.sell);
                } else {
                    helper.setText(R.id.tv_entrust_type, R.string.buy);
                    helper.setTextColor(R.id.tv_entrust_type, RISE_COLOR);
                }

                String status = item.get(4);
                helper.setVisible(R.id.tv_cancel, false);
                helper.setVisible(R.id.tv_status, true);

                switch (status) {
                    case "3":
                        helper.setText(R.id.tv_status, R.string.already_deal);
                        helper.setTextColor(R.id.tv_status, getResources().getColor(R.color.price_green));
                        break;
                    case "4":
                        helper.setText(R.id.tv_status, R.string.already_revoke);
                        helper.setTextColor(R.id.tv_status, getResources().getColor(R.color.colorText4));
                        break;
                }

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm:ss");

                helper.setText(R.id.tv_entrust_price, MoneyUtils.decimalByUp(coin.getPriceDecimals(), new BigDecimal(item.get(1))).toPlainString());
                helper.setText(R.id.tv_number, MoneyUtils.decimalByUp(coin.getAmountDecimals(), new BigDecimal(item.get(7))).toPlainString());
                helper.setText(R.id.tv_time, simpleDateFormat.format(Long.valueOf(item.get(6))));

                //成交均价
                double avgPrice = 0;
                if (!TextUtils.isEmpty(item.get(8))) {
                    avgPrice = Double.valueOf(item.get(78));
                }
                //成交额
                double turnVolume = MoneyUtils.mul(Double.valueOf(item.get(8)), Double.valueOf(item.get(2)));
                if (avgPrice == 0) {
                    helper.setText(R.id.tv_close_price, "--");
                } else {
                    helper.setText(R.id.tv_close_price, MoneyUtils.decimalByUp(coin.getPriceDecimals(), new BigDecimal(avgPrice)).toPlainString());
                }
                helper.setText(R.id.tv_success_number, MoneyUtils.decimalByUp(coin.getAmountDecimals(), new BigDecimal(item.get(2))).toPlainString());

                helper.setText(R.id.tv_close_volume, MoneyUtils.decimalByUp(coin.getPriceDecimals(), new BigDecimal(turnVolume)).toPlainString());

            }
        };
        mCompletedAdapter.setEmptyView(R.layout.layout_no_data, (ViewGroup) rvCompleted.getParent());
        ListSpaceItemDecoration listSpaceItemDecoration = new ListSpaceItemDecoration(20);
        listSpaceItemDecoration.setType(3);
        rvCompleted.addItemDecoration(listSpaceItemDecoration);
        //防止滑动卡顿
        completedLayout.setSmoothScrollbarEnabled(true);
        completedLayout.setAutoMeasureEnabled(true);
        rvCompleted.setNestedScrollingEnabled(false);
        rvCompleted.setLayoutManager(completedLayout);
        rvCompleted.setAdapter(mCompletedAdapter);
    }

    /**
     * 当前委托
     */
    public void currentEntrust() {
        LinearLayoutManager sellOutLayout = new LinearLayoutManager(this);
        sellOutLayout.setOrientation(LinearLayoutManager.VERTICAL);
        mCurrentEntrustAdapter = new BaseQuickAdapter<List<String>, BaseViewHolder>(R.layout.item_entrust_info, mUnfinishedList) {
            @Override
            protected void convert(BaseViewHolder helper, List<String> item) {
                helper.setText(R.id.tv_currency_abbreviated, coin.getFname_sn().replaceAll(" ", ""));

                //0:买卖，1:委托价格，2:委托数量，3:主键ID，4:创建时间，5:状态（1未成交，2部分成交），6:未成交数量，7:成交金额，8:成交均价
                //0:买卖，1:委托价格，2:未成交数量，3:主键ID，4:创建时间，5:状态（1未成交 ，2 部分成交，3完全成交，4已取消），6:委托数量，7:成交均价

                if (item.get(0).equals("1")) {
                    helper.setTextColor(R.id.tv_entrust_type, FALL_COLOR);
                    helper.setText(R.id.tv_entrust_type, R.string.sell);
                } else {
                    helper.setText(R.id.tv_entrust_type, R.string.buy);
                    helper.setTextColor(R.id.tv_entrust_type, RISE_COLOR);
                }

//                    int status = dataJson.getInt(5);
                helper.setVisible(R.id.tv_cancel, true);
                helper.setVisible(R.id.tv_status, false);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm:ss");

                helper.setText(R.id.tv_entrust_price, MoneyUtils.decimalByUp(coin.getPriceDecimals(), new BigDecimal(item.get(1))).toPlainString());
                helper.setText(R.id.tv_number, MoneyUtils.decimalByUp(coin.getAmountDecimals(), new BigDecimal(item.get(6))).toPlainString());
                helper.setText(R.id.tv_time, simpleDateFormat.format(Long.valueOf(item.get(4))));


                //成交均价
                double avgPrice = 0;
                if (!TextUtils.isEmpty(item.get(7))) {
                    avgPrice = Double.valueOf(item.get(7));
                }
                //成交额
                double turnVolume = MoneyUtils.mul(MoneyUtils.sub(item.get(6), item.get(2)).doubleValue(), avgPrice);

                if (avgPrice != 0) {
                    helper.setText(R.id.tv_success_number, MoneyUtils.decimalByUp(coin.getAmountDecimals(), MoneyUtils.sub(item.get(6), item.get(2))).toPlainString());
                    helper.setText(R.id.tv_close_price, MoneyUtils.decimalByUp(coin.getPriceDecimals(), new BigDecimal(avgPrice)).toPlainString());
                } else {
                    helper.setText(R.id.tv_success_number, "0.00");
                    helper.setText(R.id.tv_close_price, "--");
                }
                helper.setText(R.id.tv_close_volume, MoneyUtils.decimalByUp(coin.getPriceDecimals(), new BigDecimal(turnVolume)).toPlainString());

                final String id = item.get(3);
                helper.setOnClickListener(R.id.tv_cancel, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(TradeDetailsActivity.this)
                                .setTitle(R.string.hint_message)
                                .setMessage(R.string.confirm_revoke)
                                .setNegativeButton(R.string.confirm, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        showLoadingDialog();
                                        requestCancelEntrust(id);

                                    }
                                })
                                .setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .show();
                    }
                });

            }
        };
        mCurrentEntrustAdapter.setEmptyView(R.layout.layout_no_data, (ViewGroup) rvCurrentEntrust.getParent());

        rvCurrentEntrust.setLayoutManager(sellOutLayout);

        ListSpaceItemDecoration listSpaceItemDecoration = new ListSpaceItemDecoration(20);
        listSpaceItemDecoration.setType(3);
        //防止滑动卡顿
        sellOutLayout.setSmoothScrollbarEnabled(true);
        sellOutLayout.setAutoMeasureEnabled(true);
        rvCurrentEntrust.setLayoutManager(sellOutLayout);
//        recyclerView.setHasFixedSize(true);
        rvCurrentEntrust.setNestedScrollingEnabled(false);
        rvCurrentEntrust.addItemDecoration(listSpaceItemDecoration);
        rvCurrentEntrust.setAdapter(mCurrentEntrustAdapter);
    }


    /**
     * 挂单买入卖出记录
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

                //币种单价
                holder.setText(R.id.tv_price, MoneyUtils.decimalByUp(coin.getPriceDecimals(), new BigDecimal(list.get(0))) + "");
                holder.setTextColor(R.id.tv_price, getResources().getColor(R.color.price_red));
                //购买币的数量
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
//                holder.setText(R.id.tv_num, (mSellOutList.size() - position) + "");

                ratioLinearLayout.setValues(amount, maxSell, totalSell);
            }
        };

        mSellOutAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                List<String> data = mSellOutList.get(position);
                double price = Double.valueOf(data.get(0));
                if (layoutBuy.getVisibility() == View.VISIBLE) {
                    edBuyPrice.setText(MoneyUtils.decimalByUp(coin.getPriceDecimals(), new BigDecimal(price)).toPlainString());
                    jumpAnimation(edBuyPrice);
                } else {
                    edSellPrice.setText(MoneyUtils.decimalByUp(coin.getPriceDecimals(), new BigDecimal(price)).toPlainString());
                    jumpAnimation(edSellPrice);
                }
                jumpAnimation(edBuyPrice);
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

                //币种单价
                holder.setText(R.id.tv_price, MoneyUtils.decimalByUp(coin.getPriceDecimals(), new BigDecimal(list.get(0))).toPlainString());
                holder.setTextColor(R.id.tv_price, getResources().getColor(R.color.price_green));
                //购买币的数量
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
                if (layoutBuy.getVisibility() == View.VISIBLE) {
                    edBuyPrice.setText(MoneyUtils.decimalByUp(coin.getPriceDecimals(), new BigDecimal(price)).toPlainString());
                    jumpAnimation(edBuyPrice);
                } else {
                    edSellPrice.setText(MoneyUtils.decimalByUp(coin.getPriceDecimals(), new BigDecimal(price)).toPlainString());
                    jumpAnimation(edSellPrice);
                }


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

    /**
     * 卖出文本更改
     */
    public void selloutTextControl() {
        tvSellCurrency.setText(coin.getFShortName());
        tvSellGroup.setText(coin.getGroup());

        //卖出价格
        CashierInputFilterUtils cashierInputFilterUtils = new CashierInputFilterUtils();
        cashierInputFilterUtils.POINTER_LENGTH = coin.getPriceDecimals();
        final InputFilter[] isPrice = {cashierInputFilterUtils};
        edSellPrice.setFilters(isPrice);
        edSellPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                rgSell.clearCheck();
            }

            @Override
            public void afterTextChanged(Editable s) {
                edSellPrice.setSelection(edSellPrice.getText().length());
                double sellPrice;
                if (TextUtils.isEmpty(s)) {
                    sellPrice = 0;
                    tvSellPriceRmb.setText("≈0.00" + getString(R.string.money_unit));
                    tvSellTotalValue.setText("0.0000");
                } else {
                    sellPrice = Double.valueOf(s.toString());

                    tvSellPriceRmb.setText("≈" + MoneyUtils.decimalByUp(LanguageUtils.currentLanguage == 1 ? 2 : 4, new BigDecimal(Double.valueOf(sellPrice) * rate)) + getString(R.string.money_unit));

                    if (!TextUtils.isEmpty(edtSellNumber.getText().toString())) {
                        BigDecimal big = new BigDecimal(MoneyUtils.mul(Double.parseDouble(edtSellNumber.getText().toString()), sellPrice));
                        tvSellTotalValue.setText(MoneyUtils.decimalByUp(4, big).toString());
                    } else {
                        tvSellTotalValue.setText("0.0000");
                    }


                }

                if (sellPrice == 0) {
                    tvSellSub.setTextColor(getResources().getColor(R.color.colorText4));
                    tvSellSub.setClickable(false);
                } else {
                    tvSellSub.setTextColor(getResources().getColor(R.color.commonBlue));
                    tvSellSub.setClickable(true);
                }
            }
        });

        CashierInputFilterUtils cashierInputFilterUtils1 = new CashierInputFilterUtils();
        cashierInputFilterUtils1.POINTER_LENGTH = coin.getAmountDecimals();
        InputFilter[] isTradingQuantity = {cashierInputFilterUtils1};
        edtSellNumber.setFilters(isTradingQuantity);

        edtSellNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!isRgClick) {
                    rgSell.clearCheck();
                }
                isRgClick = false;

                if (TextUtils.isEmpty(s)) {
                    tvSellTotalValue.setText("0.0000");
                } else {
                    if (!TextUtils.isEmpty(edSellPrice.getText().toString())) {
                        BigDecimal big = new BigDecimal(MoneyUtils.mul(Double.parseDouble(edSellPrice.getText().toString()), Double.parseDouble(edtSellNumber.getText().toString())) + "");
                        tvSellTotalValue.setText(MoneyUtils.decimalByUp(coin.getPriceDecimals(), big).toString());
                    } else {
                        tvSellTotalValue.setText("0.0000");
                    }

                }

            }
        });

    }


    /**
     * 买入文本控制
     */
    public void buyTextControl() {

        tvBuyCurrency.setText(coin.getFShortName());
        tvBuyGroup.setText(coin.getGroup());

        //买入价格
        CashierInputFilterUtils cashierInputFilterUtils = new CashierInputFilterUtils();
        cashierInputFilterUtils.POINTER_LENGTH = coin.getPriceDecimals();
        final InputFilter[] isPrice = {cashierInputFilterUtils};
        edBuyPrice.setFilters(isPrice);
        edBuyPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                rgBuy.clearCheck();
            }

            @Override
            public void afterTextChanged(Editable s) {
                edBuyPrice.setSelection(edBuyPrice.getText().length());
                String buyingPrice;
                if (TextUtils.isEmpty(s)) {
                    buyingPrice = "0.0000";
                    tvBuyPriceRmb.setText("≈0.00" + getString(R.string.money_unit));
                    tvBuyTotalValue.setText("0.0000");

                } else {

                    buyingPrice = s.toString();
                    if (rate != 0) {
                        tvBuyPriceRmb.setText("≈" + MoneyUtils.decimalByUp(LanguageUtils.currentLanguage == 1 ? 2 : 4, new BigDecimal(Double.valueOf(buyingPrice) * rate)) + getString(R.string.money_unit));
                    }

                    if (!TextUtils.isEmpty(edtBuyNumber.getText().toString())) {
                        BigDecimal big = new BigDecimal(MoneyUtils.mul(Double.parseDouble(edtBuyNumber.getText().toString()), Double.parseDouble(buyingPrice)));
                        tvBuyTotalValue.setText(MoneyUtils.decimalByUp(4, big).toString());
                    } else {
                        tvBuyTotalValue.setText("0.0000");
                    }
                }


                if (Double.parseDouble(buyingPrice) > 0) {
                    strActualNumber = (mTotalMoney / Double.parseDouble(buyingPrice)) + "";

                    DecimalFormat formater = new DecimalFormat();
                    formater.setMaximumFractionDigits(4);
                    formater.setGroupingSize(0);
                    formater.setRoundingMode(RoundingMode.FLOOR);
                    strActualNumber = MoneyUtils.decimalByUp(coin.getAmountDecimals(), new BigDecimal(formater.format(Double.parseDouble(strActualNumber)))).toPlainString();

                    tvBuySub.setTextColor(getResources().getColor(R.color.commonBlue));
                    tvBuySub.setClickable(true);
                } else {
                    tvBuySub.setTextColor(getResources().getColor(R.color.colorText4));
                    tvBuySub.setClickable(false);
                    strActualNumber = "0.0000";
                }

            }
        });

        CashierInputFilterUtils cashierInputFilterUtils1 = new CashierInputFilterUtils();
        cashierInputFilterUtils1.POINTER_LENGTH = coin.getAmountDecimals();
        InputFilter[] isTradingQuantity = {cashierInputFilterUtils1};
        edtBuyNumber.setFilters(isTradingQuantity);

        edtBuyNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!isRgClick) {
                    rgBuy.clearCheck();
                }

                isRgClick = false;

                double buyingNumber;

                if (TextUtils.isEmpty(s)) {

                    tvBuyTotalValue.setText("0.0000");

                } else {
                    buyingNumber = Double.parseDouble(s.toString());

                    if (TextUtils.isEmpty(edBuyPrice.getText().toString())) {
                        tvBuyTotalValue.setText("0.0000");
                    } else {
                        BigDecimal big = new BigDecimal(MoneyUtils.mul(Double.parseDouble(edBuyPrice.getText().toString()), buyingNumber) + "");
                        tvBuyTotalValue.setText(MoneyUtils.decimalByUp(coin.getPriceDecimals(), big).toString());
                    }

                }


            }
        });
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_trade_details;
    }

    @OnClick({R.id.tv_buy_add, R.id.tv_buy_sub, R.id.tv_sell_add, R.id.tv_sell_sub})
    public void adjustPrice(View view) {
        double base = getDoubleByDecimals(coin.getPriceDecimals());
        switch (view.getId()) {
            case R.id.tv_buy_add:
                if (!TextUtils.isEmpty(edBuyPrice.getText())) {
                    double currentPrice = Double.valueOf(edBuyPrice.getText().toString());
                    double result = MoneyUtils.add(currentPrice, base);
                    edBuyPrice.setText(MoneyUtils.decimalByUp(coin.getPriceDecimals(), new BigDecimal(result)).toPlainString());
                } else {
                    edBuyPrice.setText(MoneyUtils.decimalByUp(coin.getPriceDecimals(), new BigDecimal(base)).toPlainString());
                }
                break;
            case R.id.tv_buy_sub:
                if (!TextUtils.isEmpty(edBuyPrice.getText())) {
                    double currentPrice = Double.valueOf(edBuyPrice.getText().toString());
                    double result = MoneyUtils.sub(currentPrice, base);
                    if (result < 0) {
                        result = 0;
                    }
                    edBuyPrice.setText(MoneyUtils.decimalByUp(coin.getPriceDecimals(), new BigDecimal(result)).toPlainString());
                }
                break;
            case R.id.tv_sell_add:
                if (!TextUtils.isEmpty(edSellPrice.getText())) {
                    double currentPrice = Double.valueOf(edSellPrice.getText().toString());
                    double result = MoneyUtils.add(currentPrice, base);
                    edSellPrice.setText(MoneyUtils.decimalByUp(coin.getPriceDecimals(), new BigDecimal(result)).toPlainString());
                } else {
                    edSellPrice.setText(MoneyUtils.decimalByUp(coin.getPriceDecimals(), new BigDecimal(base)).toPlainString());
                }
                break;
            case R.id.tv_sell_sub:
                if (!TextUtils.isEmpty(edSellPrice.getText())) {
                    double currentPrice = Double.valueOf(edSellPrice.getText().toString());
                    double result = MoneyUtils.sub(currentPrice, base);
                    if (result < 0) {
                        result = 0;
                    }
                    edSellPrice.setText(MoneyUtils.decimalByUp(coin.getPriceDecimals(), new BigDecimal(result)).toPlainString());
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x1) {
            if (resultCode == 1) {
                stWebsiteName.setCurrentTab(0);
                myTransactionLayout.setVisibility(View.GONE);
                tradingLayout.setVisibility(View.VISIBLE);
                soldRecordLayout.setVisibility(View.GONE);
                layoutBuy.setVisibility(View.VISIBLE);
                layoutSell.setVisibility(View.GONE);
            } else if (resultCode == 2) {
                stWebsiteName.setCurrentTab(1);
                myTransactionLayout.setVisibility(View.GONE);
                tradingLayout.setVisibility(View.VISIBLE);
                soldRecordLayout.setVisibility(View.GONE);
                layoutBuy.setVisibility(View.GONE);
                layoutSell.setVisibility(View.VISIBLE);
            }
        }
    }

    @OnClick({R.id.rl_fall_back, R.id.iv_menu, R.id.btn_buy, R.id.iv_second_menu, R.id.btn_sell})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_fall_back:
                finish();
                break;
            //进入K线走势
            case R.id.iv_menu:
                Intent itTrendChart = new Intent(VBTApplication.getContext(), TrendChartNewActivity.class);
                itTrendChart.putExtra("coin", coin);
                this.startActivityForResult(itTrendChart, 0x1);
                break;
            case R.id.iv_second_menu:
                if (isCollection) {
                    requestCancelCollection();
                } else {
                    requestAddCollection();
                }
                break;
            //买入按钮
            case R.id.btn_buy:
                if (!isLogin) {
                    Intent itLogin = new Intent(this, LoginActivity.class);
                    itLogin.putExtra("type", "info");
                    startActivity(itLogin);
                } else if (TextUtils.isEmpty(edBuyPrice.getText())) {
                    ToastUtils.showToast(getString(R.string.please_input_buy_price));
                } else if (Double.parseDouble(edBuyPrice.getText().toString()) <= 0) {
                    ToastUtils.showToast(getString(R.string.price_can_not_be_zero));
                } else if (TextUtils.isEmpty(edtBuyNumber.getText())) {
                    ToastUtils.showToast(getString(R.string.please_input_buy_volume));
                } else if (Double.parseDouble(edtBuyNumber.getText().toString()) <= 0) {
                    ToastUtils.showToast(getString(R.string.buy_volume_can_not_be_zero));
                } else if (MoneyUtils.isGreaterThan(new BigDecimal(tvBuyTotalValue.getText().toString()), new BigDecimal(strActualRmb))) {
                    ToastUtils.showToast(coin.getGroup() + getString(R.string.not_enough));
                } else if (needPassword) {
                    showPasswordDialog(true);
                } else {
                    requestBuyBtcSubmit("");
                }
                break;
            //卖出按钮
            case R.id.btn_sell:
                if (!isLogin) {
                    Intent itLogin = new Intent(this, LoginActivity.class);
                    itLogin.putExtra("type", "info");
                    startActivity(itLogin);
                } else if (TextUtils.isEmpty(edSellPrice.getText())) {
                    ToastUtils.showToast(getString(R.string.please_input_sell_price));
                } else if (Double.parseDouble(edSellPrice.getText().toString()) <= 0) {
                    ToastUtils.showToast(getString(R.string.sell_price_can_not_be_zero));
                } else if (TextUtils.isEmpty(edtSellNumber.getText())) {
                    ToastUtils.showToast(getString(R.string.please_input_sell_volume));
                } else if (Double.parseDouble(edtSellNumber.getText().toString()) <= 0) {
                    ToastUtils.showToast(getString(R.string.sell_volume_can_not_be_zero));
                } else if (MoneyUtils.isGreaterThan(new BigDecimal(edtSellNumber.getText().toString()), new BigDecimal(strActualCurrency))) {
                    ToastUtils.showToast(getString(R.string.not_enough_coin));
                } else if (needPassword) {
                    showPasswordDialog(false);
                } else {
                    requestSellBtcSubmit("");
                }

                break;
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
                passwordDialog.dismiss();

            }
        });

        passwordDialog.show();
    }


    /**
     * 币种信息
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
                        if (Double.valueOf(response.getFupanddown()) >= 0) {
                            tvUpDown.setTextColor(getResources().getColor(R.color.price_green));
                            tvUpDown.setText("+" + response.getFupanddown() + "%");
                            ivUpDown.setImageResource(R.mipmap.ico_home_rise);
                            tvActualPrice.setTextColor(getResources().getColor(R.color.price_green));
                            tvCurrentPrice.setTextColor(getResources().getColor(R.color.price_green));
                        } else {
                            tvUpDown.setTextColor(getResources().getColor(R.color.price_red));
                            tvUpDown.setText(response.getFupanddown() + "%");
                            ivUpDown.setImageResource(R.mipmap.ico_home_fall);
                            tvActualPrice.setTextColor(getResources().getColor(R.color.price_red));
                            tvCurrentPrice.setTextColor(getResources().getColor(R.color.price_red));
                        }

                        tvActualPrice.setText(MoneyUtils.decimalByUp(coin.getPriceDecimals(), new BigDecimal(response.getLast())).toPlainString());
                        tvCurrentPrice.setText(MoneyUtils.decimalByUp(coin.getPriceDecimals(), new BigDecimal(response.getLast())).toPlainString());


                        tvRmbValue.setText("≈" + MoneyUtils.decimalByUp(LanguageUtils.currentLanguage == 1 ? 2 : 4, new BigDecimal(MoneyUtils.mul(Double.valueOf(response.getLast()), rate))) + getString(R.string.money_unit));
                        tvPriceValue.setText("≈" + MoneyUtils.decimalByUp(LanguageUtils.currentLanguage == 1 ? 2 : 4, new BigDecimal(MoneyUtils.mul(Double.valueOf(response.getLast()), rate))) + getString(R.string.money_unit));
//
                        tvHighestPrice.setText(MoneyUtils.decimalByUp(coin.getPriceDecimals(), new BigDecimal(response.getHigh())).toPlainString());
                        tvLowestPrice.setText(MoneyUtils.decimalByUp(coin.getPriceDecimals(), new BigDecimal(response.getLow())).toPlainString());
                        tvDayVolume.setText(MoneyUtils.decimalByUp(coin.getAmountDecimals(), new BigDecimal(response.getVol())).toPlainString());
                    }

                }));
    }


    /**
     * 币种交易信息
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

                        List<DepthBean> buydata = new ArrayList<>();
                        List<DepthBean> sellData = new ArrayList<>();
                        //把卖出的交易数据变成倒序
                        if (mSellOutList.size() > 0) {
                            mSellOutList.clear();
                        }

                        totalSell = 0;
                        maxSell = 0;

                        //得到卖出的交易信息并且添加到list集合中
                        for (int i = 0; i < response.getSellDepthList().size(); i++) {
                            if (i >= 30) {
                                break;
                            }
                            String data = response.getSellDepthList().get(i) + "";
                            JSONArray jsonObject = new JSONArray(data);

                            double volume = jsonObject.getDouble(1);
                            sellData.add(new DepthBean(jsonObject.getDouble(0), volume));

                            //卖一价作为推荐买入价格
                            if (i == 0
                                    && Double.valueOf(strRecommendedPrice) == 0) {
                                strRecommendedPrice = MoneyUtils.decimalByUp(coin.getPriceDecimals(), new BigDecimal(jsonObject.getDouble(0))).toString();
                                edBuyPrice.setText(strRecommendedPrice);
                            }

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
                        //得到买入信息并且用循环加入到list集合当中
                        for (int i = 0; i < response.getBuyDepthList().size(); i++) {
                            if (i >= 30) {
                                break;
                            }
                            String data = response.getBuyDepthList().get(i) + "";
                            JSONArray jsonObject = new JSONArray(data);

                            double volume = jsonObject.getDouble(1);
                            buydata.add(new DepthBean(jsonObject.getDouble(0), volume));

                            //买一价作为推荐卖出价格
                            if (i == 0 && Double.valueOf(strSellOutPrice) == 0) {
                                strSellOutPrice = MoneyUtils.decimalByUp(coin.getPriceDecimals(), new BigDecimal(jsonObject.getDouble(0))).toString();
                                edSellPrice.setText(strSellOutPrice);
                            }

                            mBuyList.add(response.getBuyDepthList().get(i));
                            totalBuy += volume;
                            if (volume > maxBuy) {
                                maxBuy = volume;
                            }
                        }

                        depthView.setSellDepthData(sellData);
                        depthView.setBuyDepthData(buydata);


//                        soldRecordList.add(null);
                        soldRecordList.addAll(response.getRecentDealList());
//                        for (int i = 0; i < response.getRecentDealList().size(); i++) {
//                            soldRecordList.add(response.getRecentDealList().get(i));
//                        }
                        soldRecordAdapter.notifyDataSetChanged();
                        mBuyAdapter.notifyDataSetChanged();
                        mSellOutAdapter.notifyDataSetChanged();
                        rvSell.scrollToPosition(mSellOutAdapter.getItemCount() - 1);
                    }

                }));
    }

    /**
     * 提交买入
     */
    public void requestBuyBtcSubmit(String password) {
        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("symbol", coin.getFid() + "");
        //数量
        mapParams.put("tradeAmount", edtBuyNumber.getText().toString());
        //单价
        mapParams.put("tradeCnyPrice", edBuyPrice.getText().toString());
        //交易密码
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
                            Intent itPersonalInfo = new Intent(TradeDetailsActivity.this, RevisePayPasswordActivity.class);
                            itPersonalInfo.putExtra("revise", getString(R.string.setting));
                            startActivity(itPersonalInfo);
                            //未设置支付密码
                        } else if (resultCode.equals("-50")) {

                            ToastUtils.showToast(getString(R.string.input_trade_password));
                        } else if (resultCode.equals("-1") || resultCode.equals("-3") || resultCode.equals("-35")) {
                            String value = dataJson.getString("value");
                            ToastUtils.showToast(transactionMessage(resultCode, value));
                        } else {
                            ToastUtils.showToast(transactionMessage(resultCode, ""));
                        }
                    }
                });

    }

    /**
     * 刷新用户信息
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
                                //冻结的币数量
//                                tvFrozen.setText(MoneyUtils.decimalByUp(coin.getAmountDecimals(), new BigDecimal(response.getVirfrozen())) + "");
                            } else {
                            }

                            if (response.getRmbtotal() > 0) {
                                //可用人民币金额
                                strActualRmb = MoneyUtils.decimalByUp(4, new BigDecimal(response.getRmbtotal())).toString();
                                //总的人名币
                                mTotalMoney = response.getRmbtotal();

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
                                //卖出数量
//                                edSelloutNumber.setHint(MoneyUtils.decimalByUp(coin.getAmountDecimals(), new BigDecimal(response.getVirtotal())) + "");
                            } else {

                            }

                            //判断隐藏交易密码布局控件
                            needPassword = response.isNeedTradePasswd();

                            //我的交易记录
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

                        if (isLogin) {
                            btnSellOut.setText(R.string.sell);
                            btnBuy.setText(R.string.buy);
                        } else {
                            btnSellOut.setText(R.string.login);
                            btnBuy.setText(R.string.login);
                        }

                        tvValueMoney.setText(getString(R.string.useful) + strActualRmb + coin.getGroup());
                        tvValueCurrency.setText(getString(R.string.useful) + strActualCurrency + coin.getFShortName());


                        hideLoadingDialog();
                    }

                }));

    }

    /**
     * 刷新用户信息
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

                        if (response.getIsLogin().equals("1")) {
                            isLogin = true;

                            if (response.getVirtotal() > 0) {
                                strActualCurrency = MoneyUtils.accuracy4Position(MoneyUtils.decimal4ByUp(new BigDecimal(response.getVirtotal())) + "");
                                //可用的币数量
                            } else {
                                strActualCurrency = "0.0000";
                            }

                            if (response.getVirfrozen() > 0) {
                                //冻结的币数量
//                                tvFrozen.setText(MoneyUtils.accuracy4Position(MoneyUtils.decimal4ByUp(new BigDecimal(response.getVirfrozen())) + ""));
                            } else {
                            }

                            if (response.getRmbtotal() > 0) {
                                //可用人民币金额
                                strActualRmb = MoneyUtils.accuracy4Position(MoneyUtils.decimal4ByUp(new BigDecimal(response.getRmbtotal())).toString());

                                //总的人名币
                                mTotalMoney = response.getRmbtotal();

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
                                //卖出数量
//                                edSelloutNumber.setHint(MoneyUtils.accuracy4Position(MoneyUtils.decimal4ByUp(new BigDecimal(response.getVirtotal())) + ""));
                            } else {
                            }

                            needPassword = response.isNeedTradePasswd();

                            //我的交易记录
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

                        if (isLogin) {
                            btnSellOut.setText(R.string.sell);
                            btnBuy.setText(R.string.buy);
                        } else {
                            btnSellOut.setText(R.string.login);
                            btnBuy.setText(R.string.login);
                        }

//                        strRecommendedPrice = MoneyUtils.decimalByUp(coin.getPriceDecimals(), new BigDecimal(response.getRecommendPrizebuy())).toPlainString();
                        tvValueMoney.setText(getString(R.string.useful) + strActualRmb + coin.getGroup());

//                        strSellOutPrice = MoneyUtils.decimalByUp(coin.getPriceDecimals(), new BigDecimal(response.getRecommendPrizesell())).toString();
                        tvValueCurrency.setText(getString(R.string.useful) + strActualCurrency + coin.getFShortName());
                    }

                }));

    }

    /**
     * 提交卖出
     */
    public void requestSellBtcSubmit(String password) {
        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("symbol", coin.getFid() + "");
        //数量
        mapParams.put("tradeAmount", edtSellNumber.getText().toString());
        //单价
        mapParams.put("tradeCnyPrice", edSellPrice.getText().toString());
        //交易密码
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
                            Intent itPersonalInfo = new Intent(TradeDetailsActivity.this, RevisePayPasswordActivity.class);
                            itPersonalInfo.putExtra("revise", getString(R.string.setting));
                            startActivity(itPersonalInfo);
                            //未设置支付密码
                        } else if (resultCode.equals("-50")) {
                            ToastUtils.showToast(getString(R.string.input_trade_password));
                        } else if (resultCode.equals("-1") || resultCode.equals("-3") || resultCode.equals("-35")) {
                            String value = dataJson.getString("value");
                            ToastUtils.showToast(transactionMessage(resultCode, value));
                        } else {
                            ToastUtils.showToast(transactionMessage(resultCode, ""));
                        }
                    }
                });

    }

    /**
     * 撤销挂单
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
     * 刷新卖出记录
     *
     * @param response
     * @throws JSONException
     */
    public void sellOutRefresh(final String response) {
        if (mSellOutList.size() > 0) {
            mSellOutList.clear();
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
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

                depthView.setSellDepthData(selldata);
                mSellOutAdapter.notifyDataSetChanged();
            }
        });

    }

    /**
     * 属性买入价格
     *
     * @param response
     * @throws JSONException
     */
    public void buyDepthRefresh(final String response) {
        if (mBuyList.size() > 0) {
            mBuyList.clear();
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
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

                depthView.setBuyDepthData(buydata);
                mBuyAdapter.notifyDataSetChanged();
            }
        });


    }


    /**
     * 我的交易记录
     *
     * @param
     */
    public void EntrustUpdate(final EntrustUpdate entrustUpdate) {
        //我的交易记录
        if (mCompletedList.size() > 0) {
            mCompletedList.clear();
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

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


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        SocketUtils.getIns().removeListener(this);
        Log.d("trade detail onDestroy");
    }

    @Override
    public void onMessage(String text) {

//        Log.d("socket : " + text);
        Log.json(text);
        try {
            JSONArray jsonData = new JSONArray(text);
            String strName = (String) jsonData.get(0);

            if (strName.equals("ok")) {

            } else if (strName.equals("lease")) {

                //行情信息
            } else if (strName.equals("real")) {
                final String real = jsonData.getString(1);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        RealBean realBean = GsonUtil.GsonToBean(real, RealBean.class);

                        if (realBean == null) {
                            return;
                        }
                        if (realBean.getFupanddown() >= 0) {
                            tvUpDown.setTextColor(getResources().getColor(R.color.price_green));
                            tvUpDown.setText("+" + MoneyUtils.decimal2ByUp(new BigDecimal(realBean.getFupanddown() * 100)) + "%");
                            ivUpDown.setImageResource(R.mipmap.ico_home_rise);
                            tvActualPrice.setTextColor(getResources().getColor(R.color.price_green));
                            tvCurrentPrice.setTextColor(getResources().getColor(R.color.price_green));
                        } else {
                            tvUpDown.setTextColor(getResources().getColor(R.color.price_red));
                            tvUpDown.setText(MoneyUtils.decimal2ByUp(new BigDecimal(realBean.getFupanddown() * 100)) + "%");
                            ivUpDown.setImageResource(R.mipmap.ico_home_fall);
                            tvActualPrice.setTextColor(getResources().getColor(R.color.price_red));
                            tvCurrentPrice.setTextColor(getResources().getColor(R.color.price_red));
                        }

                        tvActualPrice.setText(MoneyUtils.decimalByUp(coin.getPriceDecimals(), new BigDecimal(realBean.getLast())).toPlainString());
                        tvCurrentPrice.setText(MoneyUtils.decimalByUp(coin.getPriceDecimals(), new BigDecimal(realBean.getLast())).toPlainString());


                        tvRmbValue.setText("≈" + MoneyUtils.decimalByUp(LanguageUtils.currentLanguage == 1 ? 2 : 4, new BigDecimal(MoneyUtils.mul(realBean.getLast(), rate))) + getString(R.string.money_unit));
                        tvPriceValue.setText("≈" + MoneyUtils.decimalByUp(LanguageUtils.currentLanguage == 1 ? 2 : 4, new BigDecimal(MoneyUtils.mul(realBean.getLast(), rate))) + getString(R.string.money_unit));
//
                        tvHighestPrice.setText(MoneyUtils.decimalByUp(coin.getPriceDecimals(), new BigDecimal(realBean.getHigh())).toPlainString());
                        tvLowestPrice.setText(MoneyUtils.decimalByUp(coin.getPriceDecimals(), new BigDecimal(realBean.getLow())).toPlainString());
                        tvDayVolume.setText(MoneyUtils.decimalByUp(coin.getAmountDecimals(), new BigDecimal(realBean.getVol())).toPlainString());

                    }
                });


                //最新交易买单
            } else if (strName.equals("entrust-buy")) {
                final String buy = jsonData.getString(1);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        buyDepthRefresh(buy);
                    }
                });
                //
                //最新交易卖单
            } else if (strName.equals("entrust-sell")) {
                final String sell = (String) jsonData.get(1);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        sellOutRefresh(sell);
                    }
                });


            } else if (strName.equals("entrust-log")) {
                final String log = jsonData.getString(1);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        entrustLog(log);
                    }
                });


                //我的交易
            } else if (strName.equals("entrust-update")) {
                final String update = jsonData.getString(1);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        EntrustUpdate entrustUpdate = GsonUtil.GsonToBean(update, EntrustUpdate.class);

                        //未登录
                        if (entrustUpdate == null || entrustUpdate.getIsLogin() == 0) {
                            return;
                        }
                        EntrustUpdate(entrustUpdate);
                    }
                });

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加收藏
     */
    public void requestAddCollection() {
        String coin = SharedPreferencesUtils.getInstance().getString("coin_id", "");
        if (coin.equals("")) {
            SharedPreferencesUtils.getInstance().saveString("coin_id", "," + this.coin.getFid() + ",");
        } else {
            SharedPreferencesUtils.getInstance().saveString("coin_id", coin + this.coin.getFid() + ",");
        }
        isCollection = true;
        ivSecondMenu.setImageResource(R.mipmap.ico_collect_seclect);
        ToastUtils.showToast(getString(R.string.add_collection_success));
    }

    /**
     * 判断是否已经收藏
     *
     * @return
     */
    public boolean isCollectioned() {
        String coins = SharedPreferencesUtils.getInstance().getString("coin_id", "");
        return coins.contains("," + coin.getFid() + ",");
    }

    /**
     * 取消收藏
     */
    public void requestCancelCollection() {
        String coins = SharedPreferencesUtils.getInstance().getString("coin_id", "");
        if (coins.contains("," + coin.getFid() + ",")) {
            coins = coins.replace("," + coin.getFid() + ",", ",");
        }

        SharedPreferencesUtils.getInstance().saveString("coin_id", coins);
        isCollection = false;
        ivSecondMenu.setImageResource(R.mipmap.ico_collect_normal);
        ToastUtils.showToast(getString(R.string.cancel_collect_success));
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

        //防止滑动卡顿
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        linearLayoutManager.setAutoMeasureEnabled(true);
        recyclerView.setLayoutManager(linearLayoutManager);
//        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(soldRecordAdapter);
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

        List<List<String>> data = new Gson().fromJson(response, new TypeToken<List<List<String>>>() {
        }.getType());
//        soldRecordList.add(null);
        soldRecordList.addAll(data);
//        for (int i = 0; i < data.size(); i++) {
//            soldRecordList.add(data.get(i));
//        }
        soldRecordAdapter.notifyDataSetChanged();


    }

    private void jumpAnimation(TextView textView) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(01f, 1.2f, 1f, 1.2f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(100);
        textView.startAnimation(scaleAnimation);
//        scaleAnimation.setRepeatMode(Mode);
    }

    //    @Override
    public void onFocusChange(View v, boolean hasFocus) {
//        View changeView = null;
//        switch (v.getId()) {
//            case R.id.edt_buy_number:
//                changeView = rlBuyNumber;
//                break;
//            case R.id.ed_buy_price:
//                changeView = llBuyPrice;
//                break;
//            case R.id.ed_sell_price:
//                changeView = llSellPrice;
//                break;
//            case R.id.edt_sell_number:
//                changeView = rlSellNumber;
//                break;
//        }
//        if (hasFocus) {
////            changeView.setBackgroundResource(R.drawable.layout_blue_square_line_frame);
//
//        } else {
////            changeView.setBackgroundResource(R.drawable.layout_square_line_frame);
//            hideInput(v);
//        }
    }

    public void hideInput(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    /**
     * 交易提示语
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
}

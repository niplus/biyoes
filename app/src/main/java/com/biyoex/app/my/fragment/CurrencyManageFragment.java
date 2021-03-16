package com.biyoex.app.my.fragment;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.biyoex.app.R;
import com.biyoex.app.common.Constants;
import com.biyoex.app.common.mvpbase.BaseFragment;
import com.biyoex.app.common.mvpbase.BasePresent;
import com.biyoex.app.common.okhttp.OkHttpUtils;
import com.biyoex.app.common.okhttp.callback.ResponseCallBack;
import com.biyoex.app.common.okhttp.callback.ResultModelCallback;
import com.biyoex.app.common.okhttp.callback.StringCallback;
import com.biyoex.app.common.utils.A2bigA;
import com.biyoex.app.common.utils.GsonUtil;
import com.biyoex.app.common.utils.ListSpaceItemDecoration;
import com.biyoex.app.my.adapter.CurrencyCoinListAdapter;
import com.biyoex.app.my.adapter.EntrustSuperviseAdapter;
import com.biyoex.app.my.bean.CoinsBean;
import com.biyoex.app.my.bean.TradeRecordBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import kotlin.collections.CollectionsKt;
import okhttp3.Call;

/**
 * 币种管理
 * Created by LG on 2017/8/22.
 */

public class CurrencyManageFragment extends BaseFragment implements BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener, EntrustSuperviseAdapter.DialogClickListener, View.OnClickListener, OnRefreshLoadmoreListener {
    //    @BindView(R.id.sp_currency)
//    Spinner spCurrency;
//    @BindView(R.id.sp_type)
//    Spinner spType;
//    @BindView(R.id.sp_state)
//    Spinner spState;
    @BindView(R.id.base_recyclerview)
    RecyclerView rvEntrustList;
    @BindView(R.id.smartrefresh)
    SmartRefreshLayout srSwipeRefreshLayout;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.iv_right)
    ImageView ivright;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.iv_menu)
    ImageView iv_menu;
    @BindView(R.id.right_layout)
    RelativeLayout right_layout;
    @BindView(R.id.sure)
    Button btn_sure;
    @BindView(R.id.edit_coin)
    EditText edit_coin;
    @BindView(R.id.rv_drawlayout)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_all)
    TextView tv_All;
    @BindView(R.id.tv_sell)
    TextView tv_sell;
    @BindView(R.id.tv_buy)
    TextView tv_buy;
    private List<TradeRecordBean.Record> mEntrustSuperviseList;
    private int drawlayout_position;

    /**
     * 币种名称
     */
    private List<String> mCurrencyNameList;
    /**
     * 用币种名称作为key来保存币种id
     */
    private Map<String, String> mCurrencyNameMap;
    /**
     * 委托类型
     */
    private List<String> mTypeList;
    /**
     * 委托状态
     */
    private List<String> mStateList;

    //侧拉框的数据
    private List<String> mDatas_Draw;

    private ArrayAdapter mCurrencyNameAdapter;
    /**
     * 买入 卖出状态
     */
    private String strType = "";
    /**
     * 状态 1 - 未成交 2 - 部分成交 3 - 完全成交 4 - 撤销
     */
    private String strStatus = "";
    /**
     * 总页数
     */
    private int intPages = 1;

    private EntrustSuperviseAdapter mEntrustSuperviseAdapter;

    private boolean isQuerySuccess = false;

    //用来筛选0  全部  1买入  2卖出
    private int mIndex = 0;
    /**
     * 总页数
     */
    private int pageCount;

    private View mRefreshDataView;

    private boolean isLoadRefresh = false;
    private CurrencyCoinListAdapter mAdapter;

    /**
     * 是否显示单一币种
     * */
    private String firstID = "";
    @Override
    public void initComp() {
        TextView tvMessage = mRefreshDataView.findViewById(R.id.tv_message);
        tvMessage.setText(R.string.no_entrust_record);
        ivright.setVisibility(View.VISIBLE);
        mRefreshDataView.setOnClickListener(v -> {
            intPages = 1;
            isLoadRefresh = true;
        });
        drawerLayout.addDrawerListener(mSimpleDrawerListener);
    }

    @Override
    public void initData() {
        tv_title.setText(getString(R.string.entrust_manager));
        mEntrustSuperviseList = new ArrayList<>();
        mCurrencyNameList = new ArrayList<>();
        mDatas_Draw = new ArrayList<>();
        mDatas_Draw.add(getString(R.string.all));
        mCurrencyNameList.add("BTC/USDT");
        mCurrencyNameMap = new HashMap<>();
        mTypeList = new ArrayList<>();
        mTypeList.add(getString(R.string.all));
        mTypeList.add(getString(R.string.buy));
        mTypeList.add(getString(R.string.sell));
        mStateList = new ArrayList<>();
        mStateList.add(getString(R.string.all));
        mStateList.add(getString(R.string.no_deal));
        mStateList.add(getString(R.string.partial_transation));
        mStateList.add(getString(R.string.complete_transaction));
        mStateList.add(getString(R.string.already_revoke));

        firstID = getArguments().getString("id");
        LinearLayoutManager entrustSuperviseLayout = new LinearLayoutManager(getActivity());
        entrustSuperviseLayout.setOrientation(LinearLayoutManager.VERTICAL);
        mEntrustSuperviseAdapter = new EntrustSuperviseAdapter(getActivity(), R.layout.item_market_entrust, mEntrustSuperviseList);
        //加入底部刷新
//        mEntrustSuperviseAdapter.setOnLoadMoreListener(CurrencyManageFragment.this, rvEntrustList);
        mEntrustSuperviseAdapter.setOnDialogClickListener(this);

        mRefreshDataView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_no_data, (ViewGroup) rvEntrustList.getParent(), false);
        mEntrustSuperviseAdapter.setEmptyView(mRefreshDataView);

        ListSpaceItemDecoration itemDecoration = new ListSpaceItemDecoration(20);
        itemDecoration.setType(1);

        rvEntrustList.addItemDecoration(itemDecoration);
        rvEntrustList.setLayoutManager(entrustSuperviseLayout);
        rvEntrustList.setAdapter(mEntrustSuperviseAdapter);
        srSwipeRefreshLayout.setOnRefreshLoadmoreListener(this);
//        srSwipeRefreshLayout.setOnRefreshListener(this);
//        srSwipeRefreshLayout.setColorSchemeColors(Color.rgb(44, 183, 227));
        mCurrencyNameAdapter = new ArrayAdapter(getActivity(), R.layout.item_simple_list_2, mCurrencyNameList);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new CurrencyCoinListAdapter(R.layout.item_currency_coin, getContext());
        mRecyclerView.setAdapter(mAdapter);
        edit_coin.setTransformationMethod(new A2bigA());
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            if(position==0){
                firstID=null;
            }
            mAdapter.setSelectPosition(position);
            drawlayout_position = position;
            mAdapter.notifyDataSetChanged();
        });

        iv_menu.setOnClickListener(this);
        btn_sure.setOnClickListener(this);
        ivright.setOnClickListener(this);
        ivright.setImageResource(R.mipmap.icon_select);
        edit_coin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!edit_coin.getText().toString().equals("")) {
                    mDatas_Draw.clear();
                    drawlayout_position = 0;
                    String s1 = edit_coin.getText().toString();
                    Log.i("输入字母", "afterTextChanged: " + s);
                    mDatas_Draw.add(getString(R.string.all));
                    for (int i = 0; i < mCurrencyNameList.size(); i++) {
                        if (mCurrencyNameList.get(i).contains(s1.toUpperCase())) {
                            mDatas_Draw.add(mCurrencyNameList.get(i));
                        }
                    }
                    mAdapter.setNewData(mDatas_Draw);
                } else {
                    mDatas_Draw.clear();
                    mDatas_Draw.add(getString(R.string.all));
                    mDatas_Draw.addAll(mCurrencyNameList);
                    mAdapter.setNewData(mDatas_Draw);
                }
            }
        });
        showLoadingDialog();
        requestCoins();
    }

    @Override
    protected BasePresent createPresent() {
        return null;
    }

    private DrawerLayout.SimpleDrawerListener mSimpleDrawerListener = new DrawerLayout.SimpleDrawerListener() {
        @Override
        public void onDrawerOpened(View drawerView) {
            drawerView.setClickable(true);
        }

        @Override
        public void onDrawerClosed(View drawerView) {
            super.onDrawerClosed(drawerView);
        }
    };

    public void refreshRollTop() {
        if (rvEntrustList != null) {
            rvEntrustList.scrollToPosition(0);
            showLoadingDialog();
            intPages = 1;
            isLoadRefresh = true;
//            requestEntrustList();
            requestTradeOpening();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_currency_manage;
    }

    /**
     * 得到币种信息
     */
    public void requestCoins() {
        OkHttpUtils
                .get()
                .addHeader("X-Requested-With", "XMLHttpReques")
                .url(Constants.BASE_URL + "v1/coins")
                .build()
                .execute(new ResultModelCallback(getActivity(), new ResponseCallBack<CoinsBean>() {
                    @Override
                    public void onError(String e) {
                        if (e.equals("8989")) {
                            requestCoins();
                        } else {
                            hideLoadingDialog();
                        }
                    }

                    @Override
                    public void onResponse(CoinsBean response) throws JSONException {
                        int position = 0;
                        mCurrencyNameList.clear();
                        mDatas_Draw.clear();
                        mDatas_Draw.add(getString(R.string.all));
                        for (int i = 0; i < response.getData().size(); i++) {
                            mCurrencyNameList.add(response.getData().get(i).getName() + "/" + response.getData().get(i).getKey());
                            mCurrencyNameMap.put(response.getData().get(i).getName() + "/" + response.getData().get(i).getKey(), response.getData().get(i).getId() + "");
                            mDatas_Draw.add(response.getData().get(i).getName() + "/" + response.getData().get(i).getKey());
                        }
                        mCurrencyNameAdapter.notifyDataSetChanged();
                        mAdapter.setNewData(mDatas_Draw);
//                        spCurrency.setSelection(position);
                        isLoadRefresh = true;
                        requestTradeOpening();
                    }
                }));
    }

    public void requestTradeOpening() {
        Map<String, String> mapParams = new HashMap<>();
        if (drawlayout_position != 0 && !mCurrencyNameMap.get(mDatas_Draw.get(drawlayout_position)).equals("")) {
            mapParams.put("id", mCurrencyNameMap.get(mDatas_Draw.get(drawlayout_position)));
        }
        else {
            if(firstID!=null){
                mapParams.put("id",firstID);
            }
        }
        if (!strStatus.equals("")) {
            //传空字符串表示查询全部  1 - 未成交 2 - 部分成交 3 - 完全成交 4 - 撤销
            mapParams.put("status", strStatus);
        }
//        mapParams.put("id", "");
        mapParams.put("page", intPages + "");
        mapParams.put("pageSize", "20");
//        mapParams.put("id","");
        Log.i("请求数据", "requestTradeOpening: " + mapParams.toString());
        OkHttpUtils
                .post()
                .addHeader("X-Requested-With", "XMLHttpReques")
                .url(Constants.BASE_URL + "v1/account/history")
                .params(mapParams)
                .tag(this)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if (getActivity() == null || getActivity().isDestroyed()) {
                            return;
                        }
                        hideLoadingDialog();
                        srSwipeRefreshLayout.finishLoadmore();
                        srSwipeRefreshLayout.finishRefresh();
                    }

                    @Override
                    public void onResponse(String response, int id) throws JSONException {
                        if (getActivity() == null || getActivity().isDestroyed()) {
                            return;
                        }
                        hideLoadingDialog();
                        srSwipeRefreshLayout.finishLoadmore();
                        srSwipeRefreshLayout.finishRefresh();
                        int code = new JSONObject(response).getInt("code");
                        if (code != 200) {
                            return;
                        }
                        isQuerySuccess = true;
                        TradeRecordBean tradeRecord = GsonUtil.GsonToBean(response, TradeRecordBean.class);
                        pageCount = tradeRecord.getTotalCount();
                        if (intPages == 1) {
                            if (tradeRecord.getData().size() > 0) {
                                if (mEntrustSuperviseList.size() > 0) {
                                    mEntrustSuperviseList.clear();
//                                    ToastUtils.showToast(getString(R.string.data_refresh_success));
                                    showToast(getString(R.string.data_refresh_success));
                                }
                                mEntrustSuperviseList.addAll(tradeRecord.getData());
//                                intPages++;
                            } else {
                                if (mEntrustSuperviseList.size() > 0) {
                                    mEntrustSuperviseList.clear();
                                }
                                //ToastUtils.showToast("暂无数据");
                            }
                            mEntrustSuperviseAdapter.notifyDataSetChanged();
                            //关闭刷新控件

//                            //把底部刷新控件打开
//                            mEntrustSuperviseAdapter.setEnableLoadMore(true);
                        } else {
                            mEntrustSuperviseList.addAll(tradeRecord.getData());
                            mEntrustSuperviseAdapter.setNewData(mEntrustSuperviseList);
//                            intPages += 1;
                            //数据刷新成功
//                            mEntrustSuperviseAdapter.loadMoreComplete();
//                            srSwipeRefreshLayout.setEnabled(true);
                            //设置为数据已经全部加载完成
//                            if (pageCount >= 2) {
//                                if (intPages > pageCount) {
//                                    mEntrustSuperviseAdapter.loadMoreEnd(false);
//                                }
//                            }
                        }
//                        if (pageCount < 2) {
                        mEntrustSuperviseAdapter.disableLoadMoreIfNotFullPage(rvEntrustList);
//                        }
                        updateListData();
                    }

                });
    }

    @Override
    public void onLoadMoreRequested() {

    }

    @Override
    public void onDestroy() {
        OkHttpUtils.getInstance().cancelTag(this);
        super.onDestroy();


    }

    @Override
    public void onRefresh() {
        //把底部刷新设为不可用
        mEntrustSuperviseAdapter.setEnableLoadMore(false);
        intPages = 1;
        isLoadRefresh = true;
//        requestEntrustList();
        requestTradeOpening();
    }


    public void requestCancelEntrust(String id, final int position) {
        OkHttpUtils
                .post()
                .url(Constants.BASE_URL + "v2/market/cancelEntrust.html")
                .addHeader("X-Requested-With", "XMLHttpReques")
                .addParams("id", id)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        //ToastUtils.showToast(e.getMessage());
                        hideLoadingDialog();
                    }

                    @Override
                    public void onResponse(String response, int id) throws JSONException {
//                        ToastUtils.showToast(getString(R.string.revoke_success));
                        showToast(getString(R.string.revoke_success));
                        mEntrustSuperviseList.get(position).setStatus(4);
                        mEntrustSuperviseAdapter.setNewData(mEntrustSuperviseList);
                        hideLoadingDialog();
                    }
                });
    }

    @Override
    public void OnDialogClickListener(String id, int position) {
        showLoadingDialog();
        requestCancelEntrust(id, position);
    }

    @OnClick({R.id.iv_right, R.id.sure, R.id.iv_menu, R.id.tv_buy, R.id.tv_sell, R.id.tv_all})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_right:
                drawerLayout.openDrawer(right_layout);
                break;
            case R.id.sure:
                updateListData();
                drawerLayout.closeDrawers();
                intPages = 1;
                mEntrustSuperviseList.clear();
                requestTradeOpening();
                mIndex = 0;
                tv_buy.setTextColor(getContext().getResources().getColor(R.color.login_yellow));
                tv_All.setTextColor(getContext().getResources().getColor(R.color.gray_to_black));
                tv_sell.setTextColor(getContext().getResources().getColor(R.color.gray_to_black));
                break;
            case R.id.iv_menu:
                getActivity().finish();
                break;
            case R.id.tv_buy:
                mIndex = 1;
                updateListData();
                break;
            case R.id.tv_sell:
                mIndex = 2;
                updateListData();
                break;
            case R.id.tv_all:
                mIndex = 0;
                updateListData();
                break;
        }
    }

    private void updateListData() {
        List<TradeRecordBean.Record> mUpdateList = mEntrustSuperviseList;
        switch (mIndex) {
            case 0:
                tv_All.setTextColor(getContext().getResources().getColor(R.color.login_yellow));
                tv_buy.setTextColor(getContext().getResources().getColor(R.color.gray_to_black));
                tv_sell.setTextColor(getContext().getResources().getColor(R.color.gray_to_black));
                break;
            case 1:
                tv_buy.setTextColor(getContext().getResources().getColor(R.color.login_yellow));
                tv_All.setTextColor(getContext().getResources().getColor(R.color.gray_to_black));
                tv_sell.setTextColor(getContext().getResources().getColor(R.color.gray_to_black));
                mUpdateList = CollectionsKt.filter(mUpdateList, t1 -> t1.getType() == 0);
                break;
            case 2:
                tv_sell.setTextColor(getContext().getResources().getColor(R.color.login_yellow));
                tv_All.setTextColor(getContext().getResources().getColor(R.color.gray_to_black));
                tv_buy.setTextColor(getContext().getResources().getColor(R.color.gray_to_black));
                mUpdateList = CollectionsKt.filter(mUpdateList, t1 -> t1.getType() == 1);
                break;
        }
        mEntrustSuperviseAdapter.setNewData(mUpdateList);
    }


    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        intPages++;
//    }
//        //把顶部刷新设置为不可用
//        if (pageCount >= 2) {
//            srSwipeRefreshLayout.setEnabled(false);
//            isLoadRefresh = false;
//            requestEntrustList();
        requestTradeOpening();
//        } else {
//            if (mEntrustSuperviseList.size() >= 10) {
//                mEntrustSuperviseAdapter.loadMoreEnd(false);
//            } else {
//                mEntrustSuperviseAdapter.loadMoreEnd(true);
//                //去掉第一次进入页面进行底部刷新
//                mEntrustSuperviseAdapter.disableLoadMoreIfNotFullPage(rvEntrustList);
//            }
//
//        }
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        intPages = 1;
        isLoadRefresh = true;
//        requestEntrustList();
        requestTradeOpening();
    }
}

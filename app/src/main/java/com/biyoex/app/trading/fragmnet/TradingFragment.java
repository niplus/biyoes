package com.biyoex.app.trading.fragmnet;


import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.biyoex.app.R;
import com.biyoex.app.common.mvpbase.BaseLazyFragment;
import com.biyoex.app.common.mvpbase.BasePresent;
import com.biyoex.app.common.utils.LanguageUtils;
import com.biyoex.app.home.adapter.HomeVpAdapter;
import com.biyoex.app.home.bean.CoinTradeRankBean;
import com.biyoex.app.trading.activity.SearchCoinActivity;
import com.biyoex.app.trading.bean.CoinMarketLiveData;
import com.biyoex.app.trading.bean.SearchAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 交易模块
 * Created by LG on 2017/5/17.
 */

public class TradingFragment extends BaseLazyFragment {
    @BindView(R.id.vp_trade)
    ViewPager vpTrade;
    @BindView(R.id.trade_tablayout)
    TabLayout tabLayout;
//    @BindView(R.id.rv_drawlayout)
//    RecyclerView mRecyclerView;

    /**
     * 页面集合
     */
    private List<Fragment> mFragmentList;
    private HomeVpAdapter mLayoutAdapter;
    private List<String> pageTitle;
    ArrayList<CoinTradeRankBean.DealDatasBean> searchData;

    private boolean isFirstRefresh = true;
    private List<CoinTradeRankBean.DealDatasBean> coinData = new ArrayList<>();
    private SearchAdapter searchAdapter;

    private Map<String, List<CoinTradeRankBean.DealDatasBean>> map;
    private int rightIndex = 0;//当前侧面选中下标 默认第一位

    @Override
    public int getLayoutId() {
        return R.layout.fragment_tarding;
    }

    @Override
    public void initComp() {

    }

    private void matchWord(String word) {
        coinData.clear();
        word = word.toLowerCase();
        if (map != null) {
            Set<String> keySet = map.keySet();
            List<CoinTradeRankBean.DealDatasBean> dealDataList = null;
            String tradeSn;
            coinData.clear();
            for (String key : keySet) {
                dealDataList = map.get(key);
                if (TextUtils.isEmpty(word)) {
                    coinData.addAll(dealDataList);
                    continue;
                }
                for (CoinTradeRankBean.DealDatasBean dealData : dealDataList) {
                    tradeSn = dealData.getFname_sn().toLowerCase();
                    if (tradeSn.contains(word)) {
                        CoinTradeRankBean.DealDatasBean dealDataClone = null;
                        try {
                            dealDataClone = dealData.clone();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        coinData.add(dealDataClone);
                    }
                }
            }
            searchData.clear();
            searchData.addAll(coinData);
            searchAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void initData() {
        mFragmentList = new ArrayList<>();
        pageTitle = new ArrayList<>();
        mLayoutAdapter = new HomeVpAdapter(getChildFragmentManager(), mFragmentList, pageTitle);
        vpTrade.setAdapter(mLayoutAdapter);
        tabLayout.setupWithViewPager(vpTrade);
        vpTrade.setOffscreenPageLimit(3);
        CoinMarketLiveData.getIns().observe(this, coinTradeRankBean -> {

            if (coinTradeRankBean == null) {
                return;
            }

            Set<String> titles = coinTradeRankBean.getDataMap().keySet();

            //如果新的到的标签和原来不一致，则需要刷新标签页
            if (!pageTitle.containsAll(titles)) {
                mFragmentList.clear();
                pageTitle.clear();
            } else {
                return;
            }
            pageTitle.add(getString(R.string.self_selected_area));
            pageTitle.addAll(titles);
            MainEditionFragment mMainEditionFragment = null;
            Bundle bundle = null;

            for (String key : pageTitle) {
                mMainEditionFragment = new MainEditionFragment();
                bundle = new Bundle();
                bundle.putString("type", key);
                mMainEditionFragment.setArguments(bundle);
                mFragmentList.add(mMainEditionFragment);
            }

            mLayoutAdapter.notifyDataSetChanged();
            for (int i = 0; i < pageTitle.size(); i++) {
                if (i == 0) {
                    View tab = LayoutInflater.from(getActivity()).inflate(R.layout.item_tab_mine_fons, null);
//                    ((TextView) tab.findViewById(R.id.tv_tab_name)).setCompoundDrawables(getResources().getDrawable(R.mipmap.icon_mine_fons), null, null, null);
                    tabLayout.getTabAt(i).setCustomView(tab);
                } else {

                    View tab = LayoutInflater.from(getActivity()).inflate(R.layout.item_trading_tab, null);
                  if(LanguageUtils.currentLanguage == 1&&pageTitle.get(i).equals("BURN")){
                        ((TextView) tab.findViewById(R.id.tv_tab_name)).setText("燃烧版");
                    }
                    else if(LanguageUtils.currentLanguage == 1&&pageTitle.get(i).equals("POC+")){
                        ((TextView) tab.findViewById(R.id.tv_tab_name)).setText("矿币版");
                    }
                  else{
                      ((TextView) tab.findViewById(R.id.tv_tab_name)).setText(pageTitle.get(i));
                  }
                    tabLayout.getTabAt(i).setCustomView(tab);
                }
            }
            //第一次进来跳转到第一个交易区，不要收藏
            if (isFirstRefresh && mFragmentList.size() >= 2) {
                vpTrade.setCurrentItem(0);
                isFirstRefresh = false;
            }
        });
    }

    @Override
    protected BasePresent createPresent() {
        return null;
    }

    @OnClick({R.id.iv_menu})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_menu:
//                initDrawData();
//                drawerLayout.openDrawer(right_layout);
                startActivity(new Intent(getContext(), SearchCoinActivity.class));
                break;
//            case R.id.sure:
//                if (!searchData.isEmpty()) {
//                    Intent itTrendChart = new Intent(VBTApplication.getContext(), TrendChartNewActivity.class);
//                    itTrendChart.putExtra("coin", searchData.get(rightIndex));
//                    getContext().startActivity(itTrendChart);
//                } else {
//                    showToast("没有该交易对");
//                }
//                break;
        }
    }

//    private void initDrawData() {
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        searchData = new ArrayList();
//        searchData.addAll(coinData);
//        searchAdapter = new SearchAdapter(R.layout.item_search_result, coinData, getContext());
//        LinearLayoutManager mainEditionLayout = new LinearLayoutManager(getContext());
//        mRecyclerView.setLayoutManager(mainEditionLayout);
//        mRecyclerView.setAdapter(searchAdapter);
//        edit_coin.setTransformationMethod(new A2bigA());
//        searchAdapter.setOnItemClickListener((adapter, view, position) -> {
//            searchAdapter.setSelectIndex(position);
//            rightIndex = position;
//        });
//        CoinTradeRankBean coinTradeRankBean = CoinMarketLiveData.getIns().getValue();
//        if (coinTradeRankBean != null) {
//            map = coinTradeRankBean.getDataMap();
//        }
//        matchWord("");
//        edit_coin.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (!edit_coin.getText().toString().equals("")) {
//                    searchData.clear();
//                    rightIndex = 0;
//                    String s1 = edit_coin.getText().toString();
//                    Log.i("输入字母", "afterTextChanged: " + s);
//                    for (int i = 0; i < coinData.size(); i++) {
//                        if (coinData.get(i).getFShortName().toUpperCase().contains(s1.toUpperCase())) {
//                            searchData.add(coinData.get(i));
//                        }
//                    }
//                    searchAdapter.setNewData(searchData);
//                } else {
//                    searchData.clear();
//                    searchData.addAll(coinData);
//                    searchAdapter.setNewData(searchData);
//                }
//            }
//        });
//    }

    @Override
    public void onLazyLoad() {

    }

    @Override
    public void reLazyLoad() {

    }
}

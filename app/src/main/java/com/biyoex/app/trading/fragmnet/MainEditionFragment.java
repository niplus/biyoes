package com.biyoex.app.trading.fragmnet;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.biyoex.app.R;
import com.biyoex.app.common.mvpbase.BaseLazyFragment;
import com.biyoex.app.common.mvpbase.BasePresent;
import com.biyoex.app.common.utils.SharedPreferencesUtils;
import com.biyoex.app.home.bean.CoinTradeRankBean;
import com.biyoex.app.trading.adapter.MainEditionAdapter;
import com.biyoex.app.trading.bean.CoinMarketLiveData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 币主版
 * Created by LG on 2017/5/19.
 */

public class MainEditionFragment extends BaseLazyFragment {
    @BindView(R.id.rv_main_info_list)
    public RecyclerView rvMainInfoList;
    @BindView(R.id.ll_deal_prize)
    LinearLayout llDealPrize;
    @BindView(R.id.ll_fentrust_value)
    LinearLayout llFentrustValue;
    @BindView(R.id.ll_upanddown)
    LinearLayout llUpandDown;
    private List<CoinTradeRankBean.DealDatasBean> mMainInfoList;
    private List<CoinTradeRankBean.DealDatasBean> unSortList;

    public MainEditionAdapter mMainEditionAdapter;
    /**
     * 那种排序类型
     */
    public int strSort;
    String type;

    @Override
    public void initComp() {
        mMainEditionAdapter = new MainEditionAdapter(getActivity(), R.layout.item_home_trend, mMainInfoList, getArguments().getString("type"));
        LinearLayoutManager mainEditionLayout = new LinearLayoutManager(getActivity());
        mainEditionLayout.setOrientation(LinearLayoutManager.VERTICAL);
        //动画，只执行一次
        mMainEditionAdapter.isFirstOnly(true);
        mMainEditionAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        rvMainInfoList.setLayoutManager(mainEditionLayout);
        rvMainInfoList.setAdapter(mMainEditionAdapter);
        getData();
    }

    @Override
    public void initData() {
        type = getArguments().getString("type");
        mMainInfoList = new ArrayList<>();
        unSortList = new ArrayList<>();
    }

    private void getData() {

        strSort = 0;
        CoinMarketLiveData.getIns().observe(this, coinTradeRankBean -> {
            if (coinTradeRankBean != null) {
                Map<String, List<CoinTradeRankBean.DealDatasBean>> stringListMap = coinTradeRankBean.getDataMap();
                List<CoinTradeRankBean.DealDatasBean> coinData;
                if (type.equals(getString(R.string.self_selected_area))) {
                    List<CoinTradeRankBean.DealDatasBean> dealDataList = null;
                    Set<String> keySet = stringListMap.keySet();
                    String coins = SharedPreferencesUtils.getInstance().getString("coin_id", "");
                    coinData = new ArrayList<>();
                    for (String key : keySet) {
                        dealDataList = stringListMap.get(key);
                        for (CoinTradeRankBean.DealDatasBean dealData : dealDataList) {
                            if (key.equals("POC+")) {
                                dealData.setIsNew(1);
                            }
                            if (coins.contains("," + dealData.getFid() + ",")) {
                                coinData.add(dealData);
                            }
                        }
                    }
                } else {
                    coinData = stringListMap.get(type);
                }

                if (coinData == null || coinData.size() == 0) {
                    View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_no_data, null, false);
                    mMainEditionAdapter.setEmptyView(view);
                }
                if (mMainInfoList.size() > 0) {
                    mMainInfoList.clear();
                    unSortList.clear();
                }

                if (coinData != null) {
                    unSortList.addAll(coinData);
                    mMainInfoList.addAll(unSortList);
                    sortList();
                }
                mMainEditionAdapter.notifyDataSetChanged();
            }
        });
    }


    @Override
    protected BasePresent createPresent() {
        return null;
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_main_edition;
    }

    @OnClick({R.id.ll_upanddown, R.id.ll_deal_prize, R.id.ll_fentrust_value,R.id.layout_price,R.id.layout_priceup})
    public void onClick(View view) {
//        LinearLayout clickView = (LinearLayout) view;
////        ImageView imageView = (ImageView) clickView.getChildAt(1);
//        llDealPrize.getChildAt(1).setVisibility(View.INVISIBLE);
//        llFentrustValue.getChildAt(1).setVisibility(View.INVISIBLE);
//        llUpandDown.getChildAt(1).setVisibility(View.INVISIBLE);
        switch (view.getId()) {
            case R.id.layout_priceup:
                if (strSort == 7) {
                    strSort = 8;
                } else if (strSort == 8) {
                    strSort = 0;
                } else {
                    strSort = 7;
                }
                break;
            case R.id.layout_price:
                if (strSort == 1) {
                    strSort = 2;
                } else if (strSort == 2) {
                    strSort = 0;
                } else {
                    strSort = 1;
                }
                break;
            case R.id.ll_fentrust_value:
                if (strSort == 3) {
                    strSort = 4;
                } else if (strSort == 4) {
                    strSort = 0;
                } else {
                    strSort = 3;
                }
                break;
        }

//        if (strSort % 2 == 1) {
//            imageView.setImageResource(R.drawable.sort_down);
//            imageView.setVisibility(View.VISIBLE);
//        } else if (strSort != 0) {
//            imageView.setImageResource(R.drawable.sort_up);
//            imageView.setVisibility(View.VISIBLE);
//        }

        mMainInfoList.clear();
        mMainInfoList.addAll(unSortList);
        sortList();
        mMainEditionAdapter.notifyDataSetChanged();
    }


    private void sortList() {
        if (strSort == 0) {
            return;
        }
        Collections.sort(mMainInfoList, (o1, o2) -> {
            double sortData1 = 0;
            double sortData2 = 0;
            switch (strSort) {
                //最新价
                case 1:
                case 2:
                    sortData1 = o1.getLastDealPrize();
                    sortData2 = o2.getLastDealPrize();
                    break;
                //成交量
                case 3:
                case 4:
                    sortData1 = o1.getVolumn();
                    sortData2 = o2.getVolumn();
                    break;
                //成交额
                case 5:
                case 6:
                    sortData1 = o1.getFentrustValue();
                    sortData2 = o2.getFentrustValue();
                    break;
                //日涨跌
                case 7:
                case 8:
                    sortData1 = o1.getFupanddown();
                    sortData2 = o2.getFupanddown();
                    break;
            }

            if (strSort % 2 == 0) {
                if (sortData1 - sortData2 > 0) {
                    return 1;
                } else if (sortData1 - sortData2 < 0) {
                    return -1;
                } else {
                    return 0;
                }
            } else {
                if (sortData1 - sortData2 > 0) {
                    return -1;
                } else if (sortData1 - sortData2 < 0) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onLazyLoad() {
        initData();
        initComp();
        getData();
    }

    @Override
    public void reLazyLoad() {
        getData();
    }
}

package com.biyoex.app.my.activity;

import androidx.lifecycle.Lifecycle;
import android.content.Intent;
import android.graphics.Color;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;
import com.biyoex.app.R;
import com.biyoex.app.common.http.RetrofitHelper;
import com.biyoex.app.common.mvpbase.BaseActivity;
import com.biyoex.app.common.mvpbase.BaseObserver;
import com.biyoex.app.common.mvpbase.BasePresent;
import com.biyoex.app.my.adapter.WithdrawAndRechargeCurrencyRecoordAdapter;
import com.biyoex.app.my.bean.WithdrawCoinrecordBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.uber.autodispose.AutoDispose.autoDisposable;

/**
 * Created by LG on 2017/6/8.
 */
//充值提現历史记录
public class WithdrawAndRechargeCurrencyRecordActivity extends BaseActivity implements BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.rl_menu)
    RelativeLayout rlMenu;
    @BindView(R.id.p_recyclerview)
    RecyclerView pRecyclerview;
    @BindView(R.id.sr_SwipeRefreshLayout)
    SwipeRefreshLayout srSwipeRefreshLayout;

    private List<WithdrawCoinrecordBean.DataBean> mCurrencyRecordList;

    private String strRecordType;
    private String strUrl;

    private String strSymbol;

    private WithdrawAndRechargeCurrencyRecoordAdapter mWithdrawCurrencyRecoordAdapter;

    /**
     * 当前页数
     */
    private int intCurrentPage = 1;
    private boolean isLoadRefresh = true;

    private String imgUrl;
    private String strCurrencyName;


    @Override
    public void initData() {
//        EventBus.getDefault().register(this);
        Intent itRecord = getIntent();
        strRecordType = itRecord.getStringExtra("type");
        strSymbol = itRecord.getStringExtra("symbol");
        imgUrl = itRecord.getStringExtra("img_url");
        strCurrencyName = itRecord.getStringExtra("currency_name");

        mCurrencyRecordList = new ArrayList<>();
        if (strRecordType.equals(getString(R.string.recharge))) {
            tvTitle.setText(getString(R.string.recharge_record));
            strUrl = "coinIn";
        } else {
            tvTitle.setText(getString(R.string.withdraw_record));
            strUrl = "coinOut";
        }
        LinearLayoutManager currencyRecordLayout = new LinearLayoutManager(this);
        currencyRecordLayout.setOrientation(LinearLayoutManager.VERTICAL);
        mWithdrawCurrencyRecoordAdapter = new WithdrawAndRechargeCurrencyRecoordAdapter(this, R.layout.item_withdraw_currency, mCurrencyRecordList, strRecordType);
        mWithdrawCurrencyRecoordAdapter.setOnLoadMoreListener(this, pRecyclerview);
        mWithdrawCurrencyRecoordAdapter.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent(WithdrawAndRechargeCurrencyRecordActivity.this, WithDrawRecordDetailActivity.class);
            intent.putExtra("type", strRecordType);
            intent.putExtra("recordDetail", mCurrencyRecordList.get(position));
            intent.putExtra("img_url", imgUrl);
            intent.putExtra("currency_name", strCurrencyName);
            startActivity(intent);
        });
        pRecyclerview.setLayoutManager(currencyRecordLayout);
        pRecyclerview.setAdapter(mWithdrawCurrencyRecoordAdapter);
        srSwipeRefreshLayout.setOnRefreshListener(this);
        srSwipeRefreshLayout.setColorSchemeColors(Color.rgb(44, 183, 227));
        showLoadingDialog();
        isLoadRefresh = true;

    }

    @Override
    protected BasePresent createPresent() {
        return null;
    }

    @Override
    public void initComp() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCancelWithdraw() {
        //在提现详情页撤销需要重新刷新
        isLoadRefresh = true;
        intCurrentPage = 1;
        requestRechargeCoinrecord();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.public_title_bar_refresh_recyclerview;
    }


    @OnClick(R.id.iv_menu)
    public void onClick() {
        finish();
    }

    public void requestRechargeCoinrecord() {
        RetrofitHelper
                .getIns()
                .getZgtopApi()
                .withdrawAndRechargRecord(strUrl, strSymbol, intCurrentPage, 20)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(autoDisposable(AndroidLifecycleScopeProvider.from(this, Lifecycle.Event.ON_DESTROY)))
                .subscribe(new BaseObserver<WithdrawCoinrecordBean>(this) {
                    @Override
                    protected void success(WithdrawCoinrecordBean withdrawCoinrecordBean) {
                        if (withdrawCoinrecordBean.getData() == null) {
                            return;
                        }

                        if (isLoadRefresh) {
                            if (withdrawCoinrecordBean.getData().size() > 0) {
                                if (mCurrencyRecordList.size() > 0) {
                                    mCurrencyRecordList.clear();
//                                    ToastUtils.showToast(getString(R.string.data_refresh_success));
                                    showToast(getString(R.string.data_refresh_success));
                                }
                                mCurrencyRecordList.addAll(withdrawCoinrecordBean.getData());
                            }

                            if (withdrawCoinrecordBean.getData().size() >= 20) {
                                mWithdrawCurrencyRecoordAdapter.setEnableLoadMore(true);
                            }
                        } else {
                            mCurrencyRecordList.addAll(withdrawCoinrecordBean.getData());
                        }

                        if (withdrawCoinrecordBean.getData().size() < 20) {
                            mWithdrawCurrencyRecoordAdapter.setEnableLoadMore(false);
                        }

                        mWithdrawCurrencyRecoordAdapter.notifyDataSetChanged();
                        if (strRecordType.equals(getString(R.string.recharge))) {
                            if (mCurrencyRecordList.size() == 0) {
                                mWithdrawCurrencyRecoordAdapter.setEmptyView(R.layout.layout_no_data, (ViewGroup) pRecyclerview.getParent());
                                ((TextView) mWithdrawCurrencyRecoordAdapter.getEmptyView().findViewById(R.id.tv_message)).setText(R.string.no_recharge_record);
                            }
                        } else {
                            if (mCurrencyRecordList.size() == 0) {
                                mWithdrawCurrencyRecoordAdapter.setEmptyView(R.layout.layout_no_data, (ViewGroup) pRecyclerview.getParent());
                                ((TextView) mWithdrawCurrencyRecoordAdapter.getEmptyView().findViewById(R.id.tv_message)).setText(R.string.no_withdraw_record);
                            }
                        }
                    }

                    @Override
                    protected void endOperate() {
                        hideLoadingDialog();
                        if (isLoadRefresh) {
                            //关闭刷新控件
                            srSwipeRefreshLayout.setRefreshing(false);
                            //把底部刷新控件打开
                        } else {
                            //设置为刷新失败
                            mWithdrawCurrencyRecoordAdapter.loadMoreComplete();
                            srSwipeRefreshLayout.setEnabled(true);
                        }
                        if (mCurrencyRecordList.size() == 0) {
                            mWithdrawCurrencyRecoordAdapter.setEmptyView(getEmptyView(1));
                        }
                    }
                });

    }


    @Override
    public void onRefresh() {
        //把底部刷新设为不可用
        mWithdrawCurrencyRecoordAdapter.setEnableLoadMore(false);
        intCurrentPage = 1;
        isLoadRefresh = true;
        requestRechargeCoinrecord();
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestRechargeCoinrecord();
    }

    @Override
    public void onLoadMoreRequested() {
        intCurrentPage += 1;
        //把顶部刷新设置为不可用
        srSwipeRefreshLayout.setEnabled(false);
        isLoadRefresh = false;
        requestRechargeCoinrecord();
    }
}

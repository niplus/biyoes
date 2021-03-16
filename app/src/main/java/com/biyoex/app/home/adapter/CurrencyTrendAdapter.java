package com.biyoex.app.home.adapter;

import android.content.Intent;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.biyoex.app.R;
import com.biyoex.app.VBTApplication;
import com.biyoex.app.common.activity.LoginActivity;

import com.biyoex.app.common.bean.RequestResult;

import com.biyoex.app.common.http.RetrofitHelper;
import com.biyoex.app.common.mvpbase.BaseObserver;
import com.biyoex.app.common.utils.GlideUtils;
import com.biyoex.app.common.utils.LanguageUtils;
import com.biyoex.app.common.utils.MoneyUtils;
import com.biyoex.app.common.utils.SharedPreferencesUtils;
import com.biyoex.app.common.utils.ToastUtils;
import com.biyoex.app.home.bean.CoinTradeRankBean;
import com.biyoex.app.trading.activity.TrendChartNewActivity;

import java.math.BigDecimal;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by LG on 2017/5/18.
 */

public class CurrencyTrendAdapter extends BaseQuickAdapter<CoinTradeRankBean.DealDatasBean, BaseViewHolder> {
    private int mFupanddownState = 0;

    public CurrencyTrendAdapter(@LayoutRes int layoutResId, @Nullable List<CoinTradeRankBean.DealDatasBean> data) {
        super(layoutResId, data);
    }

    public void setFupanddownState(int fupanddownState) {
        mFupanddownState = fupanddownState;
        notifyDataSetChanged();
    }

    @Override
    protected View getItemView(int layoutResId, ViewGroup parent) {
        return super.getItemView(layoutResId, parent);
    }

    @Override
    protected void convert(BaseViewHolder helper, final CoinTradeRankBean.DealDatasBean item) {
        String coins = SharedPreferencesUtils.getInstance().getString("coin_id", "");
//        helper.setImageDrawable(R.id.is_fons, mContext.getResources().getDrawable(coins.contains("" + item.getFid()) ? R.mipmap.icon_home_select : R.mipmap.icon_home_normal));
        TextView textView = helper.getView(R.id.tv_percentage);
        if (mFupanddownState != 0) {
            BigDecimal ratio = MoneyUtils.decimal2ByUp(new BigDecimal(item.getFupanddown()));
            textView.setBackground(mContext.getResources().getDrawable(item.getFupanddown() >= 0 ? R.drawable.bg_item_homecoin_green : R.drawable.bg_item_homecoin_red));
            textView.setText((item.getFupanddown() >= 0 ? "+" : "") + ratio + "%");
            textView.setTextColor( mContext.getResources().getColor(item.getFupanddown() >= 0 ? R.color.price_green : R.color.price_red));
            helper.setTextColor(R.id.tv_coin_price, mContext.getResources().getColor(item.getFupanddown() >= 0 ? R.color.price_green : R.color.price_red));
        } else {
            BigDecimal ratio = MoneyUtils.decimal2ByUp(new BigDecimal(item.getFupanddownweek()));
            textView.setBackground(mContext.getResources().getDrawable(item.getFupanddownweek() >= 0 ? R.drawable.bg_item_homecoin_green : R.drawable.bg_item_homecoin_red));
            textView.setText((item.getFupanddownweek() >= 0 ? "+" : "") + ratio + "%");
            textView.setTextColor( mContext.getResources().getColor(item.getFupanddownweek() >= 0 ? R.color.price_green : R.color.price_red));
            helper.setTextColor(R.id.tv_coin_price, mContext.getResources().getColor(item.getFupanddownweek() >= 0 ? R.color.price_green : R.color.price_red));
        }

        ImageView ivCurrencyIcon = helper.getView(R.id.iv_coin_icon);
        GlideUtils.getInstance().displayCurrencyImage(mContext, VBTApplication.appPictureUrl + item.getFurl(), ivCurrencyIcon);

        helper.setText(R.id.tv_coin_name, item.getfShortName());
//                .setVisible(R.id.iv_hot,item.isBurn());
        helper.setText(R.id.tv_trade_area, "/" + item.getFname_sn().split("/")[1].trim());
        helper.setText(R.id.tv_coin_price, "" + MoneyUtils.decimalByUp(item.getPriceDecimals(), new BigDecimal(item.getLastDealPrize())).toPlainString());
//        helper.getView(R.id.is_fons).setOnClickListener(v -> {
//            addSelectCoin(item.getFid(), coins.contains("" + item.getFid()));
//        });
        if (item.getVolumn() > 100000000) {
            helper.setText(R.id.tv_turnover, "成交额 "+MoneyUtils.decimalByUp(item.getAmountDecimals(), new BigDecimal(item.getVolumn() / 100000000)) + mContext.getString(R.string.million));
        } else if (item.getVolumn() > 10000) {
            helper.setText(R.id.tv_turnover, "成交额 "+MoneyUtils.decimalByUp(item.getAmountDecimals(), new BigDecimal(item.getVolumn() / 10000)) + mContext.getString(R.string.ten_thousand));
        } else {
            helper.setText(R.id.tv_turnover, "成交额 " + MoneyUtils.decimalByUp(item.getAmountDecimals(), new BigDecimal(item.getVolumn())));
        }
//        Map<String, RateBean.RateInfo> map = RateLivedata.getIns().getValue();
//        if (map != null && map.containsKey(item.getGroup())) {
        if (LanguageUtils.currentLanguage == 1) {
            helper.setText(R.id.tv_usdt_price, "￥" + MoneyUtils.decimal2ByUp(new BigDecimal(MoneyUtils.mul(Double.valueOf(item.getLastDealPrize()), Double.valueOf(item.getRate())))).toPlainString());
        } else {
            helper.setText(R.id.tv_usdt_price, "$" + MoneyUtils.decimal4ByUp(new BigDecimal(MoneyUtils.mul(Double.valueOf(item.getLastDealPrize()), Double.valueOf(item.getRate())))).toPlainString());
        }
//        }
        helper.getView(R.id.ll_coin_info).setOnClickListener(v -> {
            Intent itTrendChart = new Intent(VBTApplication.getContext(), TrendChartNewActivity.class);
            itTrendChart.putExtra("coin", item);
            itTrendChart.putExtra("isnow", item.getIsNew());
            mContext.startActivity(itTrendChart);
        });
    }

    private void addSelectCoin(int fid, boolean type) {
        String coinId = SharedPreferencesUtils.getInstance().getString("coin_id", "");
        if (type) {
            coinId = coinId.replace("," + fid + ",", ",");
            SharedPreferencesUtils.getInstance().saveString("coin_id", coinId);
        } else {
            if (coinId.equals("")) {
                SharedPreferencesUtils.getInstance().saveString("coin_id", "," + fid + ",");
            } else {
                SharedPreferencesUtils.getInstance().saveString("coin_id", coinId + fid + ",");
            }
        }
        String SelectCoinId = SharedPreferencesUtils.getInstance().getString("coin_id", "");
        RetrofitHelper.getIns().getZgtopApi().updateUserSelfToken(SelectCoinId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<RequestResult>() {
                    @Override
                    protected void success(RequestResult requestResult) {
//                        String coins2 = SharedPreferencesUtils.getInstance().getString("coin_id", "");
                        if (type) {
                            ToastUtils.showToast(mContext.getResources().getString(R.string.cancel_collect_success));
                        } else {
                            ToastUtils.showToast(mContext.getResources().getString(R.string.add_collection_success));
                        }
                        notifyDataSetChanged();
                    }

                    @Override
                    protected void failed(int code, String data, String msg) {
                        super.failed(code, data, msg);
                        String coinId = SharedPreferencesUtils.getInstance().getString("coin_id", "");
                        if (type) {
                            if (coinId.equals("")) {
                                SharedPreferencesUtils.getInstance().saveString("coin_id", "," + fid + ",");
                            } else {
                                SharedPreferencesUtils.getInstance().saveString("coin_id", coinId + fid + ",");
                            }
                        } else {
                            coinId = coinId.replace("," + fid + ",", ",");
                            SharedPreferencesUtils.getInstance().saveString("coin_id", coinId);

                        }
                        if (code == 401) {
                            mContext.startActivity(new Intent(mContext, LoginActivity.class));
                        }
                    }
                });
    }
}

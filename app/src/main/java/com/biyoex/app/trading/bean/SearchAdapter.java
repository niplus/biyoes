package com.biyoex.app.trading.bean;

import android.content.Context;
import androidx.annotation.Nullable;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.biyoex.app.R;

import com.biyoex.app.common.utils.MoneyUtils;
import com.biyoex.app.common.utils.SharedPreferencesUtils;
import com.biyoex.app.common.utils.ToastUtils;
import com.biyoex.app.home.bean.CoinTradeRankBean;


import java.math.BigDecimal;
import java.util.List;

/**
 * Created by xxx on 2018/7/17.
 */

public class SearchAdapter extends BaseQuickAdapter<CoinTradeRankBean.DealDatasBean, BaseViewHolder> {

    private Context mContext;
    private int indexSelect;

    public SearchAdapter(int layoutResId, @Nullable List<CoinTradeRankBean.DealDatasBean> data, Context context) {
        super(layoutResId, data);
        mContext = context;
    }

    public void setSelectIndex(int position) {
        this.indexSelect = position;
        notifyDataSetChanged();

    }

    @Override
    protected void convert(final BaseViewHolder helper, final CoinTradeRankBean.DealDatasBean item) {
        helper.setText(R.id.tv_search_coin, item.getFname_sn());

        helper.setImageResource(R.id.iv_collection_status, isCollectioned(item.getFid()) ? R.mipmap.ico_collect_seclect : R.mipmap.ico_collect_normal);
        if (indexSelect == helper.getAdapterPosition()) {
            helper.setBackgroundColor(R.id.tv_search_coin, mContext.getResources().getColor(R.color.color_purple));
        } else {
            helper.setBackgroundColor(R.id.tv_search_coin, mContext.getResources().getColor(R.color.white));
        }
        helper.setText(R.id.tv_price, MoneyUtils.decimalByUp(item.getPriceDecimals(), new BigDecimal(item.getLastDealPrize())).toPlainString());
        BigDecimal ratio = MoneyUtils.decimal2ByUp(new BigDecimal(item.getFupanddown()));
        if (item.getFupanddown() >= 0) {
            helper.setTextColor(R.id.tv_trend, mContext.getResources().getColor(R.color.price_green));
            helper.setText(R.id.tv_trend, "+" + ratio + "%");
        } else if (item.getFupanddown() < 0) {
            helper.setTextColor(R.id.tv_trend, mContext.getResources().getColor(R.color.price_red));
            helper.setText(R.id.tv_trend, ratio + "%");
        }

//        helper.getView(R.id.click_view).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isCollectioned(item.getFid())){
//                    requestCancelCollection(item.getFid());
//                    helper.setImageResource(R.id.iv_collection_status, R.mipmap.ico_collect_normal);
//                }else {
//                    requestAddCollection(item.getFid());
//                    helper.setImageResource(R.id.iv_collection_status, R.mipmap.ico_collect_seclect);
//                }
//            }
//        });

    }

    /**
     * 添加收藏
     */
    public void requestAddCollection(int coinId) {
        String coin = SharedPreferencesUtils.getInstance().getString("coin_id", "");
        if (coin.equals("")) {
            SharedPreferencesUtils.getInstance().saveString("coin_id", "," + coinId + ",");
        } else {
            SharedPreferencesUtils.getInstance().saveString("coin_id", coin + coinId + ",");
        }
        ToastUtils.showToast(mContext.getString(R.string.add_collection_success));
    }

    /**
     * 判断是否已经收藏
     *
     * @return
     */
    public boolean isCollectioned(int coinId) {
        String coins = SharedPreferencesUtils.getInstance().getString("coin_id", "");
        return coins.contains("," + coinId + ",");
    }

    /**
     * 取消收藏
     */
    public void requestCancelCollection(int coinId) {
        String coins = SharedPreferencesUtils.getInstance().getString("coin_id", "");
        if (coins.contains("," + coinId + ",")) {
            coins = coins.replace("," + coinId + ",", ",");
        }

        SharedPreferencesUtils.getInstance().saveString("coin_id", coins);
        ToastUtils.showToast(mContext.getString(R.string.cancel_collect_success));
    }
}

package com.biyoex.app.market;

import android.content.Context;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.biyoex.app.R;

import com.biyoex.app.common.utils.MoneyUtils;
import com.biyoex.app.home.bean.CoinTradeRankBean;


import java.math.BigDecimal;
import java.util.List;

import static com.biyoex.app.VBTApplication.FALL_COLOR;
import static com.biyoex.app.VBTApplication.RISE_COLOR;

public class MarketRightAdapter extends BaseQuickAdapter<CoinTradeRankBean.DealDatasBean, BaseViewHolder> {

    private Context mContext;

    public MarketRightAdapter(Context context, @LayoutRes int layoutResId, @Nullable List<CoinTradeRankBean.DealDatasBean> data) {
        super(layoutResId, data);
        this.mContext = context;

    }

    @Override
    protected void convert(BaseViewHolder helper, final CoinTradeRankBean.DealDatasBean item) {
        BigDecimal ratio = MoneyUtils.decimal2ByUp(new BigDecimal(item.getFupanddown()));
        if (item.getFupanddown() >= 0) {
//            helper.setTextColor(R.id.tv_coin_price,RISE_COLOR);
            helper.setTextColor(R.id.item_market_right_money, RISE_COLOR);
            helper.setText(R.id.item_market_right_money, "+" + ratio + "%");
        } else if (item.getFupanddown() < 0) {
//            helper.setTextColor(R.id.tv_coin_price,FALL_COLOR);
            helper.setTextColor(R.id.item_market_right_money, FALL_COLOR);
            helper.setText(R.id.item_market_right_money, ratio + "%");
        }
        helper.setText(R.id.item_market_right_tv, item.getFShortName());

//        helper.getView(R.id.ll_coin_info).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent itTrendChart = new Intent(VBTApplication.getContext(), TrendChartActivity.class);
//                itTrendChart.putExtra("coin", item);
//                mContext.startActivity(itTrendChart);
//            }
//        });
    }
}

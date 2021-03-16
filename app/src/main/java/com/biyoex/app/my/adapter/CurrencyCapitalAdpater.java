package com.biyoex.app.my.adapter;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.biyoex.app.R;
import com.biyoex.app.common.utils.MoneyUtils;
import com.biyoex.app.my.bean.RechargeCoinBean;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by LG on 2017/6/9.
 */

public class CurrencyCapitalAdpater extends BaseQuickAdapter<RechargeCoinBean,BaseViewHolder>{

    private double rate;

    public CurrencyCapitalAdpater(@LayoutRes int layoutResId, @Nullable List<RechargeCoinBean> data) {
        super(layoutResId, data);

//        rate = RateLivedata.getIns().getValue().get("USDT").getRmbPrice();
    }

    @Override
    protected void convert(BaseViewHolder helper, final RechargeCoinBean item) {

        helper.setText(R.id.tv_shortName, item.getName());

        helper.setText(R.id.tv_total_number, MoneyUtils.decimal4ByUp(new BigDecimal(item.getTotal())) + "");

        helper.setText(R.id.tv_frozen_number, MoneyUtils.decimal4ByUp(new BigDecimal(item.getFrozen())) + "");

        helper.setText(R.id.tv_value, MoneyUtils.decimal2ByUp(new BigDecimal(MoneyUtils.mul(item.getValue(), rate))).toPlainString());

//        ImageView coinIcon = helper.getView(R.id.coin_icon);
//        GlideUtils.getInstance().displayCurrencyImage(mContext, VBTApplication.appPictureUrl + item.getUrl(), coinIcon);

    }


}

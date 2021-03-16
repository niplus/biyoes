package com.biyoex.app.my.adapter;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.biyoex.app.R;
import com.biyoex.app.VBTApplication;
import com.biyoex.app.common.utils.MoneyUtils;
import com.biyoex.app.my.bean.WithdrawCoinrecordBean;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by LG on 2017/6/8.
 */

public class WithdrawAndRechargeCurrencyRecoordAdapter extends BaseQuickAdapter<WithdrawCoinrecordBean.DataBean,BaseViewHolder>{

    private Context mContext;
    private String strRecordType;
    public WithdrawAndRechargeCurrencyRecoordAdapter(Context context, @LayoutRes int layoutResId, @Nullable List<WithdrawCoinrecordBean.DataBean> data, String recordType) {
        super(layoutResId, data);
        this.mContext=context;
        this.strRecordType=recordType;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final WithdrawCoinrecordBean.DataBean item) {
        helper.setText(R.id.tv_coin_name, item.getName());
        if(!strRecordType.equals(mContext.getString(R.string.recharge))){
            SimpleDateFormat sp=new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
            helper.setText(R.id.tv_timer,sp.format(item.getTime()));

            helper.setText(R.id.tv_money, "-" + MoneyUtils.decimal4ByUp(new BigDecimal(item.getAmount())));

            switch (item.getStatus()){
                    case 4:
                        helper.setTextColor(R.id.tv_state,mContext.getResources().getColor(R.color.price_red));
                        helper.setText(R.id.tv_state, R.string.user_revocation);
                        break;
                    case 3:
                        helper.setTextColor(R.id.tv_state, VBTApplication.RISE_COLOR);
                        helper.setText(R.id.tv_state, R.string.withdraw_success);
                        break;
                    case 2:
                        helper.setTextColor(R.id.tv_state,mContext.getResources().getColor(R.color.commonBlue));
                        helper.setText(R.id.tv_state, R.string.lock_dealing);
                        break;
                    case 1:
                        helper.setTextColor(R.id.tv_state, Color.parseColor("#f5bb1e"));
                        helper.setText(R.id.tv_state, R.string.wait_deal);
                        break;
            }

        }else{
            SimpleDateFormat sp=new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");

            helper.setText(R.id.tv_timer,sp.format(item.getTime()));

            helper.setText(R.id.tv_money, "+" + MoneyUtils.decimal4ByUp(new BigDecimal(item.getAmount())));

            switch (item.getStatus()){
                case 0:
                    helper.setTextColor(R.id.tv_state,mContext.getResources().getColor(R.color.commonBlue));
                    helper.setText(R.id.tv_state,R.string.wait_deal);
                    break;
                case 3:
                    helper.setTextColor(R.id.tv_state,VBTApplication.RISE_COLOR);
                    helper.setText(R.id.tv_state, R.string.recharge_success);
                    break;
                case 2:
                    helper.setTextColor(R.id.tv_state,mContext.getResources().getColor(R.color.commonBlue));
                    helper.setText(R.id.tv_state,R.string.lock_dealing);
                    break;
                case 1:
                    helper.setTextColor(R.id.tv_state,mContext.getResources().getColor(R.color.commonBlue));
                    helper.setText(R.id.tv_state,R.string.lock_dealing);
                    break;
            }
        }

    }

}

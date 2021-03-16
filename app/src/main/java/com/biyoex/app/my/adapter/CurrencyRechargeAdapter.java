package com.biyoex.app.my.adapter;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;

import android.widget.RelativeLayout;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.biyoex.app.R;
import com.biyoex.app.my.activity.CurrencyReChargeActivity;
import com.biyoex.app.my.activity.WithdrawCurrencyActivity;
import com.biyoex.app.my.bean.RechargeCoinBean;


import java.util.List;

/**
 * Created by LG on 2017/6/7.
 */

public class CurrencyRechargeAdapter extends BaseQuickAdapter<RechargeCoinBean, BaseViewHolder> {

    private Context mContext;
    private String strType;

    public CurrencyRechargeAdapter(Context context, @LayoutRes int layoutResId, @Nullable List<RechargeCoinBean> data, String type) {
        super(layoutResId, data);
        this.mContext = context;
        this.strType = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, final RechargeCoinBean item) {

        helper.setText(R.id.tv_currency_name, item.getName());
        if (strType.equals(mContext.getString(R.string.recharge))) {
            helper.setText(R.id.tv_currency_state, item.isRecharge() ? "" : mContext.getString(R.string.stop_recharge));
        } else {
            helper.setText(R.id.tv_currency_state, item.isWithDraw() ? "" : mContext.getString(R.string.stop_withdraw));
        }
        RelativeLayout rlLayoutCurrency = helper.getView(R.id.rl_layout_currency);
        rlLayoutCurrency.setOnClickListener(v -> {
            if (strType.equals(mContext.getString(R.string.recharge))) {
                if (item.isRecharge()) {
                    Intent itCurrencyReCharge = new Intent(mContext, CurrencyReChargeActivity.class);
                    itCurrencyReCharge.putExtra("currency_name", item.getAllName());
                    itCurrencyReCharge.putExtra("symbol", item.getId());
                    itCurrencyReCharge.putExtra("shortName", item.getName());
                    itCurrencyReCharge.putExtra("img_url", item.getUrl());
                    mContext.startActivity(itCurrencyReCharge);
                } else {
//                        ToastUtils.showToast(mContext.getString(R.string.stop_recharge));
                    Toast.makeText(mContext, mContext.getString(R.string.stop_recharge), Toast.LENGTH_SHORT).show();
                }
            } else if (strType.equals(mContext.getString(R.string.withdraw))) {
                if (item.isWithDraw()) {
                    Intent itWithDrawCurrency = new Intent(mContext, WithdrawCurrencyActivity.class);
                    itWithDrawCurrency.putExtra("currency_name", item.getAllName());
                    itWithDrawCurrency.putExtra("symbol", item.getId());
                    itWithDrawCurrency.putExtra("shortName", item.getName());
                    itWithDrawCurrency.putExtra("frozen", item.getFrozen());
                    itWithDrawCurrency.putExtra("total", item.getTotal());
                    itWithDrawCurrency.putExtra("img_url", item.getUrl());
                    mContext.startActivity(itWithDrawCurrency);
//                    if (SessionLiveData.getIns().getValue() != null && SessionLiveData.getIns().getValue().isGoogleBind()) {
//                        Intent itWithDrawCurrency = new Intent(mContext, WithdrawCurrencyActivity.class);
//                        itWithDrawCurrency.putExtra("currency_name", item.getAllName());
//                        itWithDrawCurrency.putExtra("symbol", item.getId());
//                        itWithDrawCurrency.putExtra("shortName", item.getName());
//                        itWithDrawCurrency.putExtra("frozen", item.getFrozen());
//                        itWithDrawCurrency.putExtra("total", item.getTotal());
//                        itWithDrawCurrency.putExtra("img_url", item.getUrl());
//                        mContext.startActivity(itWithDrawCurrency);
//                    } else {
//                        new AlertDialog.Builder(mContext)
//                                .setTitle(R.string.hint_message)
//                                .setMessage(R.string.please_bind_google).setPositiveButton(R.string.go_bind, (dialog, which) -> {
//                            mContext.startActivity(new Intent(mContext, BindGoogleActivity.class));
//                            dialog.dismiss();
//                        }).setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss()).show();
//                    }
                } else {
//                        ToastUtils.showToast(mContext.getString(R.string.stop_withdraw));
                    Toast.makeText(mContext, mContext.getString(R.string.stop_withdraw), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

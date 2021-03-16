package com.biyoex.app.my.adapter;

import android.content.Context;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.biyoex.app.R;
import com.biyoex.app.common.Constants;
import com.biyoex.app.common.utils.MoneyUtils;
import com.biyoex.app.common.widget.RatioLinearLayout;
import com.biyoex.app.my.bean.TradeRecordBean;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;

import static com.biyoex.app.VBTApplication.FALL_COLOR;
import static com.biyoex.app.VBTApplication.RISE_COLOR;

/**
 * Created by LG on 2017/6/10.
 */

public class EntrustSuperviseAdapter extends BaseQuickAdapter<TradeRecordBean.Record, BaseViewHolder> {

    private Context mContext;

    private DialogClickListener mDialogClickListener;

    public EntrustSuperviseAdapter(Context context, @LayoutRes int layoutResId, @Nullable List<TradeRecordBean.Record> data) {
        super(layoutResId, data);
        this.mContext = context;
    }


    @Override
    protected void convert(final BaseViewHolder helper, final TradeRecordBean.Record item) {
        ///立即撤单
//        TextView btnCancel = helper.getView(R.id.tv_cancel);
        RatioLinearLayout ratioRightLayout = helper.itemView.findViewById(R.id.item_market_entrust_progressbar);
        switch (item.getStatus()) {
            //未成交
            case 1:
//                btnCancel.setVisibility(View.VISIBLE);
            case 2:
                helper.setText(R.id.item_market_entrust_cancel, R.string.revoke);
                helper.setBackgroundColor(R.id.item_market_entrust_cancel, mContext.getResources().getColor(R.color.commonBlue));
                helper.setTextColor(R.id.item_market_entrust_cancel, mContext.getResources().getColor(R.color.white));
                break;
            //撤销
            case 4:
                helper.setText(R.id.item_market_entrust_cancel, R.string.already_revoke);
                helper.setVisible(R.id.item_market_entrust_cancel, true);
                helper.setTextColor(R.id.item_market_entrust_cancel, mContext.getResources().getColor(R.color.colorText4));
                helper.setBackgroundColor(R.id.item_market_entrust_cancel, mContext.getResources().getColor(R.color.white));
                break;
            //完全成交
            case 3:
                helper.setText(R.id.item_market_entrust_cancel, R.string.complete_transaction);
                helper.setVisible(R.id.item_market_entrust_cancel, true);
                helper.setTextColor(R.id.item_market_entrust_cancel, mContext.getResources().getColor(R.color.price_green));
                helper.setBackgroundColor(R.id.item_market_entrust_cancel, mContext.getResources().getColor(R.color.white));
                break;
        }

        if (item.getType() == 0) {
            helper.setText(R.id.item_market_entrust_isbuy, R.string.buy)
                    .setBackgroundColor(R.id.item_market_entrust_isbuy, mContext.getResources().getColor(R.color.price_bg_green))
                    .setImageDrawable(R.id.item_market_entrust_buyandsell, mContext.getResources().getDrawable(R.mipmap.icon_small_buy));
            ratioRightLayout.setPaintColor(RISE_COLOR);
        } else {
            helper.setTextColor(R.id.item_market_entrust_isbuy, FALL_COLOR)
                    .setText(R.id.item_market_entrust_isbuy, R.string.sell)
                    .setBackgroundColor(R.id.item_market_entrust_isbuy, mContext.getResources().getColor(R.color.price_bg_red))
                    .setImageDrawable(R.id.item_market_entrust_buyandsell, mContext.getResources().getDrawable(R.mipmap.icon_small_sell));
            ratioRightLayout.setPaintColor(FALL_COLOR);
        }
        ratioRightLayout.setStartRight(false);
        //委托数量
        helper.setText(R.id.item_market_entrust_number, MoneyUtils.decimal4ByUp(new BigDecimal(item.getCount())) + "")
                .setText(R.id.item_market_entrust_number_name,mContext.getString(R.string.deal_number)+"("+item.getSellName()+")");
        //成交单价
        helper.setText(R.id.item_market_entrust_success_price, MoneyUtils.decimal4ByUp(new BigDecimal(item.getAvgPrice())).toPlainString())
                .setText(R.id.item_deal_single_price,mContext.getString(R.string.deal_success_single_price)+"("+item.getBuyName()+")");
        //成交数量
        helper.setText(R.id.item_market_entrust_success_number, MoneyUtils.decimal4ByUp(new BigDecimal(item.getRightCount())).toString() )
                .setText(R.id.item_entrust_success_sumber_name,mContext.getString(R.string.success_number)+"("+item.getSellName()+")");
        helper.setText(R.id.item_market_entrust_coinname, item.getSellName() + "/" + item.getBuyName());
        //委托单价
        helper.setText(R.id.item_market_entrust_user_price, MoneyUtils.decimal4ByUp(new BigDecimal(item.getPrice())).toPlainString())
                .setText(R.id.item_user_price_name,mContext.getString(R.string.deal_price)+"("+item.getBuyName()+")");
        //成交总量
        String success_total = new BigDecimal(item.getRightCount()).multiply(new BigDecimal(item.getAvgPrice())).setScale(4, BigDecimal.ROUND_DOWN).toString();
        helper.setText(R.id.item_market_entrust_success_total, Constants.numberFormat(Double.valueOf(success_total),4))
                .setText(R.id.item_market_entrust_success_total_name,mContext.getString(R.string.deal_success_sum)+"("+item.getBuyName()+")");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        helper.setText(R.id.item_market_entrust_time, simpleDateFormat.format(item.getTime()));
        ratioRightLayout.setRatio(new BigDecimal(item.getRightCount()).setScale(4, BigDecimal.ROUND_HALF_UP).divide(new BigDecimal(item.getCount()).setScale(4, BigDecimal.ROUND_HALF_UP), 5, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
//        helper.setText(R.id.tv_time, new BigDecimal(item.getRightCount()).divide(new BigDecimal(item.getCount()), BigDecimal.ROUND_HALF_UP).setScale(1, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)) + "%");
//        helper.setText(R.id.tv_close_volume, MoneyUtils.decimal4ByUp(new BigDecimal(item.getSuccessAmount())).toPlainString());
        TextView textView = helper.getView(R.id.item_market_entrust_cancel);
        textView.setOnClickListener(v -> {
            if (textView.getText().toString().equals(mContext.getResources().getString(R.string.revoke))) {
                new AlertDialog.Builder(mContext)
                        .setTitle(R.string.hint_message)
                        .setMessage(R.string.confirm_cancel_entrust).setPositiveButton(R.string.confirm, (dialog, which) -> {
                    mDialogClickListener.OnDialogClickListener(item.getId() + "", helper.getLayoutPosition());
                    dialog.dismiss();
                }).setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss()).show();
            }
        });
    }

    public interface DialogClickListener {
        void OnDialogClickListener(String id, int position);
    }

    public void setOnDialogClickListener(DialogClickListener dialogClickListener) {
        this.mDialogClickListener = dialogClickListener;
    }

}

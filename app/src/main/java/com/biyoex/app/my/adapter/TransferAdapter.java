package com.biyoex.app.my.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.biyoex.app.R;
import com.biyoex.app.common.utils.MoneyUtils;
import com.biyoex.app.my.activity.OtcTransDetailActivity;
import com.biyoex.app.my.bean.TransferRecordBean;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;

public class TransferAdapter extends BaseQuickAdapter<TransferRecordBean, BaseViewHolder> {

    private Context mContext;
    public TransferAdapter(Context context, @Nullable List<TransferRecordBean> data) {
        super(R.layout.item_withdraw_currency, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, final TransferRecordBean item) {
        helper.setText(R.id.tv_coin_name, item.getCoinName());
        SimpleDateFormat sp=new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        helper.setText(R.id.tv_timer,sp.format(item.getCreateTime()));
        //转入
        if (item.getType() == 1){
            helper.setText(R.id.tv_money, "+" + MoneyUtils.decimal4ByUp(new BigDecimal(item.getCount())));
            switch (item.getStatus()){
                case 1:
                    helper.setTextColor(R.id.tv_state,mContext.getResources().getColor(R.color.commonBlue));
                    helper.setText(R.id.tv_state,"待审核");
                    break;
                case 2:
                    helper.setTextColor(R.id.tv_state, mContext.getResources().getColor(R.color.price_green));
                    helper.setText(R.id.tv_state,"转入成功");
                    break;
                case 3:
                    helper.setTextColor(R.id.tv_state,mContext.getResources().getColor(R.color.price_red));
                    helper.setText(R.id.tv_state,"转入失败");
                    break;
            }
        } else {
            helper.setText(R.id.tv_money, "-" + MoneyUtils.decimal4ByUp(new BigDecimal(item.getCount())));
            switch (item.getStatus()){
                case 1:
                    helper.setTextColor(R.id.tv_state,mContext.getResources().getColor(R.color.commonBlue));
                    helper.setText(R.id.tv_state,"待审核");
                    break;
                case 2:
                    helper.setTextColor(R.id.tv_state, mContext.getResources().getColor(R.color.price_green));
                    helper.setText(R.id.tv_state,"转出成功");
                    break;
                case 3:
                    helper.setTextColor(R.id.tv_state,mContext.getResources().getColor(R.color.price_red));
                    helper.setText(R.id.tv_state,"转出失败");
                    break;
            }
        }

        View rlItemParent = helper.getView(R.id.rl_item_parent);
        rlItemParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, OtcTransDetailActivity.class);
                intent.putExtra("record", item);
                mContext.startActivity(intent);
            }
        });
    }
}

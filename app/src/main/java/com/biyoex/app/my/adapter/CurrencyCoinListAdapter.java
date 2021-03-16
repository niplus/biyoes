package com.biyoex.app.my.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.biyoex.app.R;

public class CurrencyCoinListAdapter extends BaseQuickAdapter<String,BaseViewHolder> {
    private int mPosition;
    private Context context;
    public CurrencyCoinListAdapter(int layoutResId, Context context) {
        super(layoutResId);
        this.context = context;
    }
    public void setSelectPosition(int position){
            mPosition = position;

    }
    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_item_currency_coin,item);
        if(helper.getAdapterPosition()==mPosition){
            helper.setBackgroundColor(R.id.tv_item_currency_coin,context.getResources().getColor(R.color.color_purple));
            helper.setTextColor(R.id.tv_item_currency_coin,context.getResources().getColor(R.color.white));
        }
        else {
            helper.setBackgroundColor(R.id.tv_item_currency_coin,context.getResources().getColor(R.color.white));
            helper.setTextColor(R.id.tv_item_currency_coin,context.getResources().getColor(R.color.textBlack));
        }
    }
}

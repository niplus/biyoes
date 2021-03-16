package com.biyoex.app.my.adapter;

import android.content.Context;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.biyoex.app.R;
import com.biyoex.app.my.bean.ChainNameBean;

import java.util.List;

/**
 * Created by LG on 2020/9/2.
 */

public class ChainNameAdapter extends BaseQuickAdapter<ChainNameBean,BaseViewHolder>{

    private Context mContext;
    public ChainNameAdapter(Context context, @LayoutRes int layoutResId, @Nullable List<ChainNameBean> data) {
        super(layoutResId, data);
        this.mContext=context;
    }

    @Override
    protected void convert(BaseViewHolder helper, ChainNameBean item) {
        helper.setText(R.id.tv_transfer_account_name, "USDT-"+item.getType());
    }
}

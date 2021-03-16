package com.biyoex.app.my.adapter;

import android.content.Context;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.biyoex.app.R;
import com.biyoex.app.my.bean.MessagesBean;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by LG on 2017/6/5.
 */

public class MessageNoticeAdapter extends BaseQuickAdapter<MessagesBean.DataBean,BaseViewHolder>{

    private Context mContext;
    private List<MessagesBean.DataBean> mMessagesList;
    private String params="";
    public MessageNoticeAdapter(Context context, @LayoutRes int layoutResId, @Nullable List<MessagesBean.DataBean> data) {
        super(layoutResId, data);
        this.mContext=context;
        this.mMessagesList=data;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final MessagesBean.DataBean item) {

//        ImageView ivSystemLogo = helper.getView(R.id.iv_system_logo);

        SimpleDateFormat spDate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        helper.setText(R.id.tv_date,spDate.format(item.getTime()));

        helper.setText(R.id.tv_message_content,item.getTitle());

//        ImageView ivDelete = helper.getView(R.id.iv_delete);
//
//        if (item.getStatus() == 2){
//            ivSystemLogo.setVisibility(View.GONE);
//        }else {
//            ivSystemLogo.setVisibility(View.VISIBLE);
//        }

//
//        ivDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
    }






}

package com.biyoex.app.common.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.biyoex.app.R;

public class NoticeMF extends MarqueeFactory<TextView, String> {
    private LayoutInflater inflater;

    public NoticeMF(Context mContext) {
        super(mContext);
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public TextView generateMarqueeItemView(String data) {
        TextView mView = (TextView) inflater.inflate(R.layout.home_notice_textview, null);
        mView.setText(data);
        return mView;
    }
}
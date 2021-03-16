package com.biyoex.app.trading.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.biyoex.app.R;

import java.util.List;

/**
 * Created by LG on 2017/5/23.
 */

public class SoldRecordAdapter extends RecyclerView.Adapter<SoldRecordAdapter.SoldRecordViewHolder>{

    private Context mContext;
    private List<String> mSoldRecordList;

    public SoldRecordAdapter(Context mContext, List<String> mSoldRecordList) {
        this.mContext = mContext;
        this.mSoldRecordList = mSoldRecordList;
    }

    @Override
    public SoldRecordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_sold_record,parent,false);
        return new SoldRecordViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(SoldRecordViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mSoldRecordList.size();
    }

    public static class SoldRecordViewHolder extends RecyclerView.ViewHolder{


        public SoldRecordViewHolder(View itemView) {
            super(itemView);
        }
    }
}

package com.biyoex.app.common.utils;

import android.graphics.Rect;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

/**
 * Created by LG on 2017/3/9.
 */

public class SpacesItemDecoration extends RecyclerView.ItemDecoration{
    //行数
    private int mRowNumber;
    //间距
    private int mSpacing;
    private int mType;
    public SpacesItemDecoration(int mRowNumber, int mSpacing, int mType) {
        this.mRowNumber = mRowNumber;
        this.mSpacing = mSpacing;
        this.mType=mType;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        switch (mType){
            case 0x0309:
                if(parent.getChildAdapterPosition(view)==0||parent.getChildAdapterPosition(view)==1){
                    outRect.top=mSpacing;
                }
                if(parent.getChildAdapterPosition(view)%2==0){
                    outRect.left=mSpacing;
                    outRect.right=mSpacing/mRowNumber;
                }else{
                    outRect.left=mSpacing/mRowNumber;
                    outRect.right=mSpacing;
                }
                outRect.bottom=mSpacing;
                break;
            case 0x0308:
                if(parent.getChildAdapterPosition(view)==0){
                    outRect.top=mSpacing;
                }
                outRect.right=mSpacing;
                outRect.left=mSpacing;
                outRect.bottom=mSpacing;
                break;
            case 0x1642:
                if (parent.getChildAdapterPosition(view) >= 3){
                    outRect.top=mSpacing;
                }
                outRect.right=mSpacing;
                break;
            case 0x1049:
                outRect.right=mSpacing;
                break;
            case 0x1555:
                if(parent.getChildAdapterPosition(view)==0) {
                    outRect.top = mSpacing;
                }
                break;
        }


        //super.getItemOffsets(outRect, view, parent, state);
    }
}

package com.biyoex.app.common.utils;

import android.graphics.Rect;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

/**
 * Created by xxx on 2018/6/4.
 */

public class ListSpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int space;
    private int type;

    public ListSpaceItemDecoration(int space)
    {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (type == 1){
            outRect.top = space;
        } else if (type == 2){
            outRect.top = space;
            outRect.left = space;
            outRect.bottom = space;
            if (parent.getChildLayoutPosition(view) == parent.getAdapter().getItemCount() - 1){
                outRect.right = space;
            }
        } else if(type == 3){
            if (parent.getChildLayoutPosition(view) != 0) {
                outRect.top = space;
            }
        }else if (type == 4){
            outRect.top = space;
            outRect.bottom = space;
        }else {
            outRect.top = space;
            outRect.left = space;
            outRect.right = space;
        }
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}

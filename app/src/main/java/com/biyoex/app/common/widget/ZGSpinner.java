package com.biyoex.app.common.widget;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by xxx on 2018/5/31.
 */

public class ZGSpinner extends androidx.appcompat.widget.AppCompatSpinner {
    public ZGSpinner(Context context) {
        super(context);
    }

    public ZGSpinner(Context context, int mode) {
        super(context, mode);
    }

    public ZGSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setSelection(int position, boolean animate) {
        boolean sameSelected = position == getSelectedItemPosition();
        super.setSelection(position, animate);
        if (sameSelected) {
            getOnItemSelectedListener().onItemSelected(this, getSelectedView(),
                    position, getSelectedItemId());
        }
    }

    @Override
    public void setSelection(int position) {
        boolean sameSelected = position == getSelectedItemPosition();
        super.setSelection(position);
        if (sameSelected) {
            getOnItemSelectedListener().onItemSelected(this, getSelectedView(),
                    position, getSelectedItemId());
        }
    }
}

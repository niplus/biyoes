package com.biyoex.app.common.widget;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewTreeObserver;

/**
 * Created by xxx on 2018/9/4.
 */

public class AutoTextSizeTextView extends androidx.appcompat.widget.AppCompatTextView implements ViewTreeObserver.OnGlobalLayoutListener{

    public AutoTextSizeTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoTextSizeTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }


    @Override
    public void onGlobalLayout() {
        //当外部调用setText(String text)方法时回调
        int lineCount = getLineCount();//获取当前行数
        if (lineCount > 1) {//如果当前行数大于1行
            float textSize = getTextSize();//获得的是px单位
            textSize--;//将size-1；
            setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);//重新设置大小,该方法会立即触发onGlobalLayout()方法。这里相当于递归调用，直至文本行数小于1行为止。
        }
    }
}

package com.biyoex.app.common.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

public class AutoButton extends androidx.appcompat.widget.AppCompatButton {

    /**
     * 用来计数，当计数到达需要数量时，才能enableButton
     */
    private int mCount;

    private int total;

    public AutoButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEnabled(false);
    }

    public void setEditTexts(EditText...editTexts){
        total = editTexts.length;
        for (final EditText editText : editTexts){
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    Object tag =  editText.getTag();
                    if (TextUtils.isEmpty(s)){
                        if (tag != null && (boolean)tag){
                            mCount--;
                            editText.setTag(false);
                            setEnabled(mCount == total);
                        }
                    }else {
                        if (tag == null || !(boolean)tag){
                            mCount++;
                            editText.setTag(true);
                            setEnabled(mCount == total);
                        }
                    }
                }
            });
        }
    }
}

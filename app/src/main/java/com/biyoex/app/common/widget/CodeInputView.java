package com.biyoex.app.common.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.biyoex.app.R;

public class CodeInputView extends RelativeLayout {

    private EditText editText;

    private LinearLayout llShow;
    private LinearLayout llLine;

    private OnCodeCompleteListener onCodeCompleteListener;

    public CodeInputView(Context context) {
        this(context, null);
    }

    public CodeInputView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CodeInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        View.inflate(context, R.layout.layout_code_input, this);
        editText = findViewById(R.id.edt_input);
        llShow = findViewById(R.id.ll_show);
        llLine = findViewById(R.id.ll_line);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                int lenth = editable.toString().length();
                for (int i = 0;i < 6;i++){
                    if (i >= lenth){
                        ((TextView)llShow.getChildAt(i)).setText("");
                    }else {
                        ((TextView)llShow.getChildAt(i)).setText(editable.toString().charAt(i)+"");
                    }

//                    llLine.getChildAt(i).setBackgroundColor(Color.parseColor("#f5f5f5"));
                }

                if (lenth != 6){
//                    llLine.getChildAt(lenth).setBackgroundColor(Color.parseColor("#358dfa"));
                }else {
//                    llLine.getChildAt(5).setBackgroundColor(Color.parseColor("#358dfa"));

                    if (onCodeCompleteListener != null){
                        onCodeCompleteListener.onCodeComplete(editable.toString());
                    }
                }
            }
        });
    }


    public void setOnCodeCompleteListener(OnCodeCompleteListener onCodeCompleteListener){
        this.onCodeCompleteListener = onCodeCompleteListener;
    }

    public interface OnCodeCompleteListener{
        void onCodeComplete(String code);
    }
}

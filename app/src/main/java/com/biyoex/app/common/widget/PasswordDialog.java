package com.biyoex.app.common.widget;

import android.content.Context;
import androidx.annotation.NonNull;
import android.text.InputType;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.biyoex.app.R;

public class PasswordDialog extends BottomDialog {
    private OnConfirmListener onConfirmListener;

    public PasswordDialog(@NonNull Context context, String title, String editHint) {
        super(context,  R.layout.dialog_input_password);
        init(title, editHint);
    }


    private void init(String title, String editHint){
        final EditText edtPassword = findViewById(R.id.edt_password);
        edtPassword.setHint(editHint);

        TextView tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText(title);

        setCanceledOnTouchOutside(false);
        findViewById(R.id.img_exit).setOnClickListener(v -> dismiss());

        ToggleButton tbHidePwd = findViewById(R.id.ctb_hide_pwd);
        tbHidePwd.setChecked(true);
        tbHidePwd.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                //密文
                edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                edtPassword.setSelection(edtPassword.getText().toString().length());
            } else {
                edtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                edtPassword.setSelection(edtPassword.getText().toString().length());
            }
        });

        findViewById(R.id.btn_confirm).setOnClickListener(v -> {
           if (onConfirmListener != null){
               onConfirmListener.onConfirm(edtPassword.getText().toString());
           }
        });
    }

    public void setOnConfirmListener(OnConfirmListener onConfirmListener) {
        this.onConfirmListener = onConfirmListener;
    }

    public interface OnConfirmListener{
        void onConfirm(String pwd);
    }

}

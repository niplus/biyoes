package com.biyoex.app.common.widget;

import android.app.Dialog;
import android.content.Context;
import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.biyoex.app.R;
import com.biyoex.app.common.utils.androidutilcode.ScreenUtils;

public class MessageDialog extends Dialog {

    public MessageDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialog_message);
        Window window = getWindow();
        // 获取Window的LayoutParams
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = ScreenUtils.getScreenWidth() - 100;
        attributes.height = WindowManager.LayoutParams.WRAP_CONTENT;
        attributes.gravity = Gravity.CENTER;
        // 一定要重新设置, 才能生效
        window.setAttributes(attributes);
        setCanceledOnTouchOutside(false);
    }

    public static class Builder{
        private MessageDialogParames parames;

        public Builder(Context context){
            parames = new MessageDialogParames();
            parames.context = context;
        }

        public Builder setTitle(String title){
            parames.title = title;
            return this;
        }

        public Builder setContent(String content){
            parames.content = content;
            return this;
        }

        public Builder setNegativeButtonListener(OnClickListener negativeButtonListener){
            parames.negativeButtonListener = negativeButtonListener;
            return this;
        }

        public Builder setPositiveButtonListener(OnClickListener positiveButtonListener){
            parames.positiveButtonListener = positiveButtonListener;
            return this;
        }

        public Builder setNegativeMessage(String negativeMessage){
            parames.negativeMessage = negativeMessage;
            return this;
        }

        public Builder setPositiveMessage(String positiveMessage){
            parames.positiveMessage = positiveMessage;
            return this;
        }

        public BURNMessageDialog build(){
            final BURNMessageDialog messageDialog = new BURNMessageDialog(parames.context);

            TextView tvTitle = messageDialog.findViewById(R.id.tv_title);
            if (TextUtils.isEmpty(parames.title)){
                tvTitle.setVisibility(View.GONE);
            }else {
                tvTitle.setText(parames.title);
            }

            TextView tvContent = messageDialog.findViewById(R.id.tv_content);
            if (TextUtils.isEmpty(parames.content)){
                tvContent.setVisibility(View.GONE);
            }else {
                tvContent.setText(parames.content);
            }

            TextView tvCancel = messageDialog.findViewById(R.id.tv_cancel);
            if (TextUtils.isEmpty(parames.negativeMessage)){
                tvCancel.setVisibility(View.GONE);
            }else {
                tvCancel.setText(parames.negativeMessage);
            }
            if (parames.negativeButtonListener != null){
                tvCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        parames.negativeButtonListener.onClick(messageDialog, 0);
                    }
                });
            }

            TextView tvConfirm = messageDialog.findViewById(R.id.tv_confirm);
            if (TextUtils.isEmpty(parames.positiveMessage)){
                tvConfirm.setVisibility(View.GONE);
            }else {
                tvConfirm.setText(parames.positiveMessage);
            }
            if (parames.positiveButtonListener != null){
                tvConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        parames.positiveButtonListener.onClick(messageDialog, 1);
                    }
                });
            }

            return messageDialog;
        }
    }

    static class MessageDialogParames{
        Context context;
        String title;
        String content;
        String negativeMessage;
        String positiveMessage;
        OnClickListener negativeButtonListener;
        OnClickListener positiveButtonListener;
    }
}

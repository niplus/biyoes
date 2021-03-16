package com.biyoex.app.common.widget;

import android.app.Dialog;
import android.content.Context;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.biyoex.app.R;
import com.biyoex.app.common.utils.androidutilcode.ScreenUtils;

public class IconMessageDialog extends Dialog {

    public IconMessageDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialog_icon_message);
        Window window = getWindow();

        window.setBackgroundDrawableResource(android.R.color.transparent);
        // 获取Window的LayoutParams
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = ScreenUtils.getScreenWidth() - ScreenUtils.dp2px(80);
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

        public Builder setTitle(@StringRes int title){
            return setTitle(parames.context.getString(title));
        }

        public Builder setContent(String content){
            parames.content = content;
            return this;
        }

        public Builder setIcon(@DrawableRes int icon){
            parames.iconRes = icon;
            return this;
        }

        public Builder setContent(@StringRes int content){
            return setContent(parames.context.getString(content));
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

        public Builder setNegativeMessage(@StringRes int negativeMessage){
            return setNegativeMessage(parames.context.getString(negativeMessage));
        }

        public Builder setPositiveMessage(String positiveMessage){
            parames.positiveMessage = positiveMessage;
            return this;
        }

        public Builder setPositiveMessage(@StringRes int positiveMessage){
            return setPositiveMessage(parames.context.getString(positiveMessage));
        }

        public Builder isPositiveHightLight(boolean isHightLight){
            parames.isPositiveButtonHightLight = isHightLight;
            return this;
        }

        public Builder isNegativeHightLight(boolean isHightLight){
            parames.isNegativeButtonHightLight = isHightLight;
            return this;
        }

        public IconMessageDialog build(){
            final IconMessageDialog messageDialog = new IconMessageDialog(parames.context);

            //设置标题
            TextView tvTitle = messageDialog.findViewById(R.id.tv_title);
            if (TextUtils.isEmpty(parames.title)){
                tvTitle.setVisibility(View.GONE);
            }else {
                tvTitle.setText(parames.title);
            }

            //设置图标
            if (parames.iconRes != 0){
                ImageView ivIcon = messageDialog.findViewById(R.id.iv_icon);
                ivIcon.setImageResource(parames.iconRes);
                ivIcon.setVisibility(View.VISIBLE);
            }

            //设置内容
            TextView tvContent = messageDialog.findViewById(R.id.tv_content);
            if (TextUtils.isEmpty(parames.content)){
                tvContent.setVisibility(View.GONE);
            }else {
                tvContent.setText(parames.content);
            }

            //设置取消按钮
            TextView tvCancel = messageDialog.findViewById(R.id.tv_cancel);
            if (TextUtils.isEmpty(parames.negativeMessage)){
                tvCancel.setVisibility(View.GONE);
            }else {
                tvCancel.setText(parames.negativeMessage);
            }

            if (parames.isNegativeButtonHightLight) {
                tvCancel.setBackgroundColor(0xff358dfa);
                tvCancel.setTextColor(0xffffffff);
            }
            if (parames.negativeButtonListener != null){
                tvCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        parames.negativeButtonListener.onClick(messageDialog, 0);
                    }
                });
            }

            //设置确定按钮
            TextView tvConfirm = messageDialog.findViewById(R.id.tv_confirm);
            if (TextUtils.isEmpty(parames.positiveMessage)){
                tvConfirm.setVisibility(View.GONE);
            }else {
                tvConfirm.setText(parames.positiveMessage);
            }

            if (parames.isPositiveButtonHightLight){
                tvConfirm.setBackgroundColor(0xff358dfa);
                tvConfirm.setTextColor(0xffffffff);
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
        @DrawableRes int iconRes;
        String content;
        String negativeMessage;
        String positiveMessage;
        boolean isNegativeButtonHightLight;
        boolean isPositiveButtonHightLight;
        OnClickListener negativeButtonListener;
        OnClickListener positiveButtonListener;
    }
}

package com.biyoex.app.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.biyoex.app.R;

/**
 * 点赞的布局
 * Created by xxx on 2018/7/16.
 */

public class LikeLayout extends LinearLayout {

    private boolean isChecked;

    /**
     * 当前数量
     */
    private int currentNum;

    /**
     * 按钮名字
     */
    private String button;

    /**
     * 按钮图片
     */
    private Drawable checkedImg;
    private Drawable unCheckedImg;

    private Drawable checkedBackground;
    private Drawable uncheckedBackground;

    /**
     * 按钮颜色
     */
    private int buttonColor;

    private OnCheckListener onCheckListener;
    private OnselfClickListener onselfClickListener;

    public LikeLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public LikeLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.LikeLayout, defStyleAttr, 0);

        buttonColor = a.getColor(R.styleable.LikeLayout_button_color, context.getResources().getColor(R.color.price_red));
        checkedImg = a.getDrawable(R.styleable.LikeLayout_checked_img);
        unCheckedImg = a.getDrawable(R.styleable.LikeLayout_uncheck_img);
        button = a.getString(R.styleable.LikeLayout_button_title);
        isChecked = a.getBoolean(R.styleable.LikeLayout_checked, false);
        checkedBackground = a.getDrawable(R.styleable.LikeLayout_checked_background);
        uncheckedBackground = a.getDrawable(R.styleable.LikeLayout_unchecked_background);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                isChecked = !isChecked;
                checkChanged();
                initLayout();

                if (onselfClickListener!=null){
                    onselfClickListener.onClick(isChecked);
                }
            }
        });

        a.recycle();
        initLayout();
    }


    public void setChecked(boolean isChecked, boolean onlyChageStatus){
        if (this.isChecked != isChecked){
            this.isChecked = isChecked;

            //是否仅改变状态
            if (!onlyChageStatus) {
                checkChanged();
            }
        }
        initLayout();
    }

    public boolean isChecked(){
        return isChecked;
    }

    public void setNum(int num){
        currentNum = num;
        initLayout();
    }

    private void checkChanged(){
        if (isChecked){
            currentNum++;
        }else {
            currentNum--;
        }

        if (onCheckListener != null){
            onCheckListener.onChecked(this, isChecked);
        }
    }

    private void initLayout(){
        TextView textView = (TextView) getChildAt(1);
        ImageView imageView = (ImageView) getChildAt(0);

        if (textView == null || imageView == null){
            return;
        }
        if (isChecked){
            if (checkedBackground != null) {
                setBackground(checkedBackground);
            }
            imageView.setImageDrawable(checkedImg);
            textView.setText(button + " " + currentNum);
            textView.setTextColor(buttonColor);
        }else {
            if (uncheckedBackground != null) {
                setBackground(uncheckedBackground);
            }
            imageView.setImageDrawable(unCheckedImg);
            textView.setText(button + " " + currentNum);
            textView.setTextColor(getResources().getColor(R.color.text_middle_light));
        }
    }

    public int getCurrentNum(){
        return currentNum;
    }


    public void setOnCheckListener(OnCheckListener onCheckListener){
        this.onCheckListener = onCheckListener;
    }

    public interface OnCheckListener{
        void onChecked(View view, boolean isChekced);
    }

    public void setOnSelfClickListener(OnselfClickListener onSelfClickListener){
        this.onselfClickListener = onSelfClickListener;
    }

    public interface OnselfClickListener{
        void onClick(boolean isCheck);
    }

}

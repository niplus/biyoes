package com.biyoex.app.common.utils;

import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 输入框金钱过滤类
 * Created by LG on 2017/5/3.
 */

public class CashierInputFilterUtils implements InputFilter {
    Pattern mPattern;
    //小数点后的位数
    public int POINTER_LENGTH = 2;
    private static final String POINTER = ".";
    public CashierInputFilterUtils() {
        mPattern = Pattern.compile("([0-9]|\\.)*");
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        String sourceText = source.toString();
        String destText = dest.toString();
        //验证删除等按键
        if (TextUtils.isEmpty(sourceText)) {
            if (dstart == 0 && destText.indexOf(POINTER) == 1) {//保证小数点不在第一个位置
                return "0";
            }
            return "";
        }
        Matcher matcher = mPattern.matcher(source);
        //已经输入小数点的情况下，只能输入数字
        if (destText.contains(POINTER)) {
            if (!matcher.matches()) {
                return "";
            } else {
                if (POINTER.equals(source)) { //只能输入一个小数点
                    return "";
                }
            }
            //验证小数点精度，保证小数点后只能输入两位
            int index = destText.indexOf(POINTER);
            int length = destText.trim().length() - index;
            if (length > POINTER_LENGTH && dstart > index) {
                return "";
            }
        } else {
            //没有输入小数点的情况下，只能输入小数点和数字，但首位不能输入小数点和0
            if (!matcher.matches()) {
                return "";
            } else {
                if ((POINTER.equals(source)) && dstart == 0) {//第一个位置输入小数点的情况
                    return "0.";
                }
            }
        }

        int length = 0;

        if (destText.contains(".")) {
            length = destText.indexOf(".");
        } else {
            length = destText.length();
        }

        //如果小数点前大于8位则不让他输入
        if (length >= 8) {
            if (!sourceText.equals(".")
                    && (!destText.contains(".")
                    || dstart <= destText.indexOf(".")))
                return "";
        }

        return dest.subSequence(dstart, dend) + sourceText;
    }
}

package com.github.fujianlian.klinechart.formatter;

import com.github.fujianlian.klinechart.base.IValueFormatter;

/**
 * Value格式化类
 * Created by tifezh on 2016/6/21.
 */

public class ValueFormatter implements IValueFormatter {
    public static int mPriceDecimals = 2;
    @Override
    public String format(float value) {
        return String.format("%."+mPriceDecimals+"f", value);
    }
}

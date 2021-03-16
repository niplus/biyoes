package com.biyoex.app.common.utils;

import java.math.BigDecimal;

/**
 * Created by LG on 2017/8/23.
 */

public class DateUtil {

    public static String formatRestTime(long secTime){
        String formatedTime = "%d时%s分%s秒";
        int s = (int) (secTime % 60);
        int m = (int) (secTime / 60 % 60);
        int h = (int) (secTime / 60 / 60);


        return String.format(formatedTime, h, m, s);
    }

    public static BigDecimal getBigDecimal(Double account){
        BigDecimal bd = new BigDecimal(account);
        bd = bd.setScale(4, BigDecimal.ROUND_HALF_UP);
        return bd;
    }
}

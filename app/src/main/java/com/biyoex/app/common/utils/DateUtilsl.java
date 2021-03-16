package com.biyoex.app.common.utils;

import android.text.TextUtils;
import android.util.Log;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by pengfei on 2018/8/27.
 */
public class DateUtilsl {
    /**
     * 掉此方法输入所要转换的时间输入例如（"2014年06月14日16时09分00秒"）返回时间戳
     *
     * @param time
     * @return
     */

    public static String data(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.CHINA);
        Date date;
        String times = null;
        try {
            date = sdr.parse(time);
            long l = date.getTime();
            String stf = String.valueOf(l);
            times = stf.substring(0, 10);
            Log.d("--444444---", times);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return times;
    }

    public static BigDecimal getBigDecimal(Double account) {
        BigDecimal bd = new BigDecimal(account);
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        return bd;
    }

    public static String datatime(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("HH:mm:",
                Locale.CHINA);
        Date date;
        String times = null;

        try {
            date = sdr.parse(time);
            long l = date.getTime();
            String stf = String.valueOf(l);
            times = stf.substring(0, 10);
            Log.d("--444444---", times);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return times;
    }

    /**
     * 掉此方法输入所要转换的时间输入例如（"2014-06-14-16-09-00"）返回时间戳
     *
     * @param
     * @return
     */

    //根据日期取得星期几
    public static String getWeek(String time) throws ParseException {
        String time1 = DateUtilsl.time(time);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");// 日期格式
        String week;
        Date date = format.parse(time1);
        SimpleDateFormat sdf = new SimpleDateFormat("E");
        week = sdf.format(date);
        String replace = week.replace("星期", "周");
        return replace;
    }


    public static String dataOne(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss",
                Locale.CHINA);
        Date date;
        String times = null;
        try {
            date = sdr.parse(time);
            long l = date.getTime();
            String stf = String.valueOf(l);
            times = stf.substring(0, 10);
            Log.d("--444444---", times);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return times;
    }

    /**
     * 调用此方法输入所要转换的时间戳输入例如（1402733340）输出（"2014年06月14日16时09分00秒"）
     *
     * @param time
     * @return
     */
    public static String times(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy/MM/dd/HH:mm");
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
        String times = sdr.format(new Date(lcc));
        return times;
    }

    /**
     * 掉此方法输入所要转换的时间输入例如（"06-14 16:09:00"）返回时间戳
     *
     * @param time
     * @return
     */
    public static String time_state(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
//        long i = Integer.parseInt(time);
//        LogUtils.d(i);

//        LogUtils.d(i+"-------------");
        String times = sdr.format(new Date(lcc));
        return times;
    }

    public static String time_hour(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("HH:mm");
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
//        long i = Integer.parseInt(time);
//        LogUtils.d(i);

//        LogUtils.d(i+"-------------");
        String times = sdr.format(new Date(lcc));
        return times;
    }

    public static String getHotEndTime(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("MM-dd HH:mm");
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
//        long i = Integer.parseInt(time);
//        LogUtils.d(i);

//        LogUtils.d(i+"-------------");
        String times = sdr.format(new Date(lcc));
        return times;
    }

    public static String gettime(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("HH");
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
//        long i = Integer.parseInt(time);
//        LogUtils.d(i);

//        LogUtils.d(i+"-------------");
        String times = sdr.format(new Date(lcc));
        return times;
    }

    /**
     * 调用此方法输入所要转换的时间戳输入例如（1402733340）输出（"2014年06月14日16时09分"）
     *
     * @param time
     * @return
     */
    public static String timet(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy年MM月dd日HH时mm分");
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
        int i = Integer.parseInt(time);
        String times = sdr.format(new Date(i));
        return times;

    }

    /**
     * 时间戳输出---2018-1-21
     *
     * @param time
     * @return
     */
    public static String time(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd");
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
//        int i = Integer.parseInt(time);
        String times = sdr.format(lcc);
        return times;
    }

    /**
     * 时间戳输出---2018-1-21
     *
     * @param time
     * @return
     */
    public static String getKlineDate(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("MM/dd HH:mm");
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
//        int i = Integer.parseInt(time);
        String times = sdr.format(lcc);
        return times;
    }

    /**
     * 时间戳输出---2018-1-21
     *
     * @param time
     * @return
     */
    public static String timeNoYear(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("MM-dd HH:mm");
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
//        int i = Integer.parseInt(time);
        String times = sdr.format(lcc);
        return times;
    }

    /**
     * 时间戳输出---2018-1-21 14:21
     *
     * @param time
     * @return
     */
    public static String time_meddle(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
//        int i = Integer.parseInt(time);
        String times = sdr.format(lcc);
        return times;
    }

    /**
     * 显示--月
     *
     * @param time
     * @return
     */
    public static String time_month(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("mm");
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
//        int i = Integer.parseInt(time);
        String times = sdr.format(lcc);
        return times;
    }

    /**
     * 显示--月
     *
     * @param time
     * @return
     */
    public static String time_monthday(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat(" HH:mm MM/dd");
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
//        int i = Integer.parseInt(time);
        String times = sdr.format(new Date(lcc));
        return times;
    }

    /**
     * 调用此方法输入所要转换的时间戳输入例如（1402733340）输出（"2014年06月14日16时09分"）
     * 订制格式
     *
     * @param time
     * @return
     */

    public static String time_type(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat(" HH:mm MM/dd");
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
        int i = Integer.parseInt(time);
        String times = sdr.format(new Date(i * 1000L));
        return times;
    }

    /**
     * 调用此方法输入所要转换的时间戳输入例如（1402733340）输出（"2014年06月14日"）
     * 订制格式
     *
     * @param time
     * @return
     */
    public static String time_today(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy/MM/dd");
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
//        int i = Integer.parseInt(time);
        String times = sdr.format(lcc);
        return times;
    }

    /**
     * 当前时间
     *
     * @param time
     * @return
     */
    public static String todaytime(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("");
        return time;
    }

    // 将时间戳转为字符串
    public static String getStrTime(String time, String formatStr) {
        return new SimpleDateFormat(TextUtils.isEmpty(formatStr) ? "yyyy/MM/dd" : formatStr).format(new Date(Long.valueOf(time) * 1000L));
    }

}

package com.biyoex.app.common.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;

import com.biyoex.app.VBTApplication;

import java.util.Locale;

public class LanguageUtils {

    /**
     * 当前使用语言 1 中文 2韩文, 3英文,4日文
     */
    public static int currentLanguage = 1;

    public static Context changeLanguage(String language, Context context) {
        //设置语言类型
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        switch (language) {
            case "zh":
                configuration.locale =  Locale.SIMPLIFIED_CHINESE;
//                setLocal(configuration, Locale.SIMPLIFIED_CHINESE);
                currentLanguage = 1;
                break;
            case "kr":
                configuration.locale = Locale.KOREA;
//                setLocal(configuration, Locale.KOREA);
                currentLanguage = 2;
                break;
            case "us":
                configuration.locale = Locale.ENGLISH;
                currentLanguage = 3;
                break;
            case "jp":
                configuration.locale = Locale.JAPAN;
                currentLanguage = 4;
                break;
            case "vn":
                Locale lo = new Locale("vi");
                configuration.locale = lo;
                currentLanguage = 5;
                break;
        default:
        configuration.locale = Locale.getDefault();
        break;
    }

        resources.updateConfiguration(configuration, displayMetrics);

        SharedPreferencesUtils.getInstance().saveString("language", language);
        return  context;
    }

    private static void setLocal(Configuration configuration , Locale lo){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(lo);
        }else {
            configuration.locale = lo;
        }
    }

        public static void setCurrentLanguage(String language){
            switch (language) {
                case "zh":
                    currentLanguage = 1;
                    break;
                case "kr":
    //                setLocal(configuration, Locale.KOREA);
                    currentLanguage = 2;
                    break;
                case "us":
                    currentLanguage = 3;
                    break;
                case "jp":
                    currentLanguage = 4;
                    break;
                case "vn":
                    currentLanguage = 5;
                    break;
            }
        }

    public static String getLanguage(){
        Resources resources = VBTApplication.getContext().getResources();
        Configuration configuration = resources.getConfiguration();
        return configuration.locale.getCountry();
    }

    public static Locale getLocale(){
        String string = SharedPreferencesUtils.getInstance().getString("language", "zh");
        Locale locale = Locale.ENGLISH;
        switch (string){
            case "zh":
                locale = Locale.SIMPLIFIED_CHINESE;
                currentLanguage = 1;
                break;
            case "kr":
                locale = Locale.KOREA;
                currentLanguage = 2;
                break;
            case "us":
                locale = Locale.ENGLISH;
                currentLanguage = 3;
                break;
            case "jp":
                locale  = Locale.JAPAN;
                currentLanguage = 4;
                break;
            case "vn":
                locale = new Locale("vi");
                currentLanguage = 5;
                break;
        }
        return locale;
    }
}

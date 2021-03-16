package com.biyoex.app.common.utils;

import android.content.Context;

import com.biyoex.app.R;

/** 响应码处理工具类
 * Created by LG on 2017/6/13.
 */

public class MessagDealUtils {


    /**
     * 处理响应码，把响应码变成中文并且返回出去给ui界面去提示
     */
    public static String sendAuthCodeMessage(String hint, String resultCode, Context context){
        String messag="";
        switch (resultCode){
            case "101":
                messag=context.getString(R.string.please_input)+hint;
                break;
            case "102":
                messag=context.getString(R.string.not_vip)+hint;
                break;
            case "103":
                messag=context.getString(R.string.not_bind)+ hint;
                break;
            case "4":
                messag= hint + context.getString(R.string.already_regist);
                break;
            case "105":
                messag=hint+context.getString(R.string.opera_too_much);
                break;
            case "3":
                messag=context.getString(R.string.pic_code_error);
                break;
            case "2":
                messag=context.getString(R.string.wrong_phone_or_email);
                break;
            case "5":
                messag=context.getString(R.string.account_not_exit);
                break;
            case "6":
                messag=context.getString(R.string.contact_admin);
                break;
            case "1":
                messag=context.getString(R.string.illegal_opera);
                break;
        }
        if(messag.equals("")){
            messag=context.getString(R.string.send_code_error);
        }
        return messag;
     }

    /**
     * 二次验证错误码判断
     * @param resultCode
     * @return
     */
    public static String loginValidateMessage(String resultCode, Context context){
        String messag="";
        switch (resultCode){
            case "1":
                messag = context.getString(R.string.account_not_exit);
                break;
            case "100":
                messag = context.getString(R.string.code_not_send);
                break;
            case "101":
                messag = context.getString(R.string.opera_too_much);
                break;
            case "102":
                messag = context.getString(R.string.code_error);
                break;
                //之前遗留
            case "-15":
                messag=context.getString(R.string.code_empty);
                break;
            case "-20":
                messag=context.getString(R.string.net_error);
                break;
            case "110":
                messag=context.getString(R.string.send_code_too_much);
                break;
            case "111":
                messag=context.getString(R.string.opera_too_much);
                break;
        }
        if(messag.equals("")){
            messag=context.getString(R.string.net_error);
        }
        return messag;
    }







}

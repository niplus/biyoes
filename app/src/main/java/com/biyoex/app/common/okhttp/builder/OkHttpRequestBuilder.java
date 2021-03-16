package com.biyoex.app.common.okhttp.builder;



import com.biyoex.app.common.okhttp.request.RequestCall;

import java.util.Map;

/**
 * Created by zhy on 15/12/14.
 */
public abstract class OkHttpRequestBuilder<T extends OkHttpRequestBuilder>
{
    protected String url;
    protected Object tag;
    protected Map<String, String> headers;
    protected Map<String, String> params;
    protected int id;

    public T id(int id)
    {
        this.id = id;
        return (T) this;
    }

    public T url(String url)
    {
        this.url = url;
        return (T) this;
    }


    public T tag(Object tag)
    {
        this.tag = tag;
        return (T) this;
    }

    public T headers(Map<String, String> headers)
    {
        this.headers = headers;
        return (T) this;
    }

    //今后改用retrofit，暂时弃用
    public T addHeader(String key, String val)
    {
//        if (this.headers == null)
//        {
//            headers = new LinkedHashMap<>();
//        }

        //headers.put("Cookie", "JSESSIONID=AB65AB244E682D1CFB2F713D01910C8A-n1; route=f1ef9256057b6b7d0f0dee6d99e6aa77;login_device=4bfb584a-032a-4dc3-a9f1-f074879c7bb0");

//        String jsessionid = SharedPreferencesUtils.getInstance().getString("JSESSIONID", "");
//        String route = SharedPreferencesUtils.getInstance().getString("route", "");
//        String login_device = SharedPreferencesUtils.getInstance().getString("login_device", "");
//        String uid = SharedPreferencesUtils.getInstance().getString("uid", "");

        String cookie="";

//        if(!jsessionid.equals("")){
//            cookie+="JSESSIONID="+jsessionid+";";
//        }
//
//        if(!route.equals("")){
//            cookie+="route="+route+";";
//        }
//
//        if(!login_device.equals("")){
//            cookie+="login_device="+login_device+";";
//            //Log.e("sssss",cookie);
//        }
//
//        if (!uid.equals(""))
//        {
//            cookie+="uid="+uid+";";
//        }

//        if(!cookie.equals("")){
//            headers.put("Cookie", cookie);
//        }
//        headers.put("User-Agent", "zhgtrade_android");
        //由于代码中其他地方rjax请求头写错了，在这里全局替换。
//        headers.put("X-Requested-With", "XMLHttpRequest");

//        headers.put(key, val);
        return (T) this;
    }

    public abstract RequestCall build();
}

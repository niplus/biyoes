package com.biyoex.app.common.http;

import android.text.TextUtils;

import com.biyoex.app.VBTApplication;
import com.biyoex.app.common.Constants;
import com.biyoex.app.common.utils.SharedPreferencesUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Created by xxx on 2018/8/8.
 */

public class RetrofitHelper {


    private static RetrofitHelper instance;

    private Retrofit retrofit;
    private OkHttpClient okHttpClient;
    private ZgtopApi zgtopApi;
    public static RetrofitHelper getIns() {
        if (instance == null) {
            synchronized (RetrofitHelper.class) {
                if (instance == null) {
                    instance = new RetrofitHelper();
                }
            }
        }
        return instance;
    }

    private RetrofitHelper() {
        init();
    }

    public ZgtopApi getZgtopApi() {
        return zgtopApi;
    }

    private void init() {
        /**
         * 持久化Cookie
         */
        Interceptor receiveCookieInterceptor = chain -> {
            Response originalResponse = chain.proceed(chain.request());
            if (!originalResponse.headers("Set-Cookie").isEmpty()) {

                for (String header : originalResponse.headers("Set-Cookie")) {
                    String name = header.substring(0, header.indexOf("="));

                    //截取value是为了兼容以前的版本
                    String value = header.substring(header.indexOf("=") + 1, header.indexOf(";"));
                    SharedPreferencesUtils.getInstance().saveString(name, value);
                }
            }
            if (!TextUtils.isEmpty(originalResponse.header("Authorization"))) {
                SharedPreferencesUtils.getInstance().saveString("Authorization", originalResponse.header("Authorization"));
            }
            return originalResponse;
        };

        /**
         *將cookie放到請求頭中
         */
        Interceptor addCookieInterceptor = chain -> {
            Request.Builder builder = chain.request().newBuilder();
//                HashSet<String> preferences = (HashSet<String>) SharedPreferencesUtils.getInstance().getSet("cookie", null);
            //上个开发是这么放的,为了兼容老版本
            String jsessionid = SharedPreferencesUtils.getInstance().getString("JSESSIONID", "");
            builder.addHeader("Cookie", "JSESSIONID=" + jsessionid + ";");
            String route = SharedPreferencesUtils.getInstance().getString("route", "");
            builder.addHeader("Cookie", "route=" + route + ";");
            String login_device = SharedPreferencesUtils.getInstance().getString("login_device", "");
            builder.addHeader("Cookie", "login_device=" + login_device + ";");
            String uid = SharedPreferencesUtils.getInstance().getString("uid", "");
            builder.addHeader("Cookie", "uid=" + uid + ";");
            builder.addHeader("User-Agent", "zhgtrade_android");
            builder.addHeader("X-Requested-With", "XMLHttpRequest");
            return chain.proceed(builder.build());
        };
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder()
                .addInterceptor(receiveCookieInterceptor)
                .addInterceptor(addCookieInterceptor)
                .connectTimeout(Constants.TIME_OUT, TimeUnit.MILLISECONDS)
                .readTimeout(Constants.TIME_OUT, TimeUnit.MILLISECONDS)
                .writeTimeout(Constants.TIME_OUT, TimeUnit.MILLISECONDS);
        if (VBTApplication.ISDEBUG) {
            builder.addInterceptor(new HttpLogInterceptor());
        }
        okHttpClient = builder.build();
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(CustomeConverterFacory.create())
                .client(okHttpClient)
                .build();
        zgtopApi = retrofit.create(ZgtopApi.class);
    }

    public void setOkHttpClient() {
       retrofit  = new Retrofit.Builder()
               .baseUrl(Constants.BASE_URL)
               .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
               .addConverterFactory(CustomeConverterFacory.create())
               .client(okHttpClient)
               .build();
       zgtopApi = retrofit.create(ZgtopApi.class);
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }
}

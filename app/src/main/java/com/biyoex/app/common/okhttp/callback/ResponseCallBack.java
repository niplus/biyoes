package com.biyoex.app.common.okhttp.callback;

import org.json.JSONException;

/** 处理完服务器指定异常回调接口
 * Created by LG on 2017/3/26.
 */

public interface ResponseCallBack<T> {

    //请求错误时调用
    public abstract void onError(String e);
    //成功时调用
    public abstract void onResponse(T response) throws JSONException;

}

package com.biyoex.app.common.okhttp.callback;

import android.content.Context;
import android.util.Log;

import com.biyoex.app.common.data.SessionLiveData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by zhy on 15/12/14.
 */
public abstract class StringCallback extends Callback<String>
{
    private  Context context;
    public StringCallback() {
    }

    @Override
    public String parseNetworkResponse(Response response, int id) throws IOException
    {
        String body = response.body().string();
        try {
            Log.i("body", "parseNetworkResponse: "+body);
            JSONObject jsonObject = new JSONObject(body);
            if (jsonObject.has("code")){
                int code = jsonObject.getInt("code");
                if (code == 401){
                    SessionLiveData.getIns().postValue(null);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }finally {
            return body;
        }
    }

    @Override
    public void onError(Call call, Exception e, int id) {

    }
}

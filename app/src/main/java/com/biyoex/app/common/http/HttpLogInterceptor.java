package com.biyoex.app.common.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.biyoex.app.common.utils.log.Log;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class HttpLogInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();

        StringBuilder builder = new StringBuilder();
        builder.append(request.method() + "\n");
        builder.append(request.url() + "\n");
        builder.append(request.headers() + "\n");
        RequestBody body = request.body();

        if (body != null){
            builder.append("contentLength() : " + body.contentLength() + "\n");

            if (body instanceof  FormBody) {
                FormBody formBody = ((FormBody) body);
                for (int i = 0; i < formBody.size(); i++) {
                    builder.append(formBody.name(i) + ":" + formBody.value(i) + "\n");
                }
            }

            MediaType requestType = body.contentType();
            if (requestType != null) {
                builder.append("mediaType : " + body.contentType());
            }
        }

        Log.d(builder.toString());

        Response response = chain.proceed(request);

        StringBuilder responseBuilder = new StringBuilder();
        responseBuilder.append(response.protocol() + "\n");
        responseBuilder.append(response.headers() + "\n");
        responseBuilder.append("code=" + response.code() + ", message="+ response.message() + ", url="+response.request().url()+"\n");


        //处理response
        ResponseBody responseBody = response.body();
        if (responseBody != null){

            MediaType mediaType = responseBody.contentType();
            if (mediaType != null){
                String content = responseBody.string();
                if (content.startsWith("{")){
                    try {
                        JsonParser jsonParser = new JsonParser();
                        JsonObject jsonObject = jsonParser.parse(content).getAsJsonObject();
                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
                        String json = gson.toJson(jsonObject);
                        responseBuilder.append(json);
                    }
                    catch (Exception e){
                        android.util.Log.i("測試一下", "intercept:"+content);
                    }
                }else if ( content.startsWith("[")){
                    JsonParser jsonParser = new JsonParser();
                    JsonArray jsonObject = jsonParser.parse(content).getAsJsonArray();
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();

                    String json = gson.toJson(jsonObject);
                    responseBuilder.append(json);
                }else {
                    responseBuilder.append(content + "\n");
                }
                Log.d(responseBuilder.toString());

                ResponseBody buildBody = ResponseBody.create(mediaType, content);
                return response.newBuilder().body(buildBody).build();

            }
        }

        Log.d(responseBuilder.toString());
        return response;
    }

}

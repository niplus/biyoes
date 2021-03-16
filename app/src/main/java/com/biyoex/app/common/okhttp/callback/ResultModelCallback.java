package com.biyoex.app.common.okhttp.callback;

import android.content.Context;

import com.google.gson.Gson;
import com.biyoex.app.R;
import com.biyoex.app.VBTApplication;

import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by LG on 2017/3/26.
 */

public class ResultModelCallback<T> extends StringCallback{

    private ResponseCallBack callBack;
    private Context mContext;
    private String strCode;
    private boolean isLoadRefresh=true;
    public ResultModelCallback(Context context, ResponseCallBack callBack) {
        this.mContext=context;
        this.callBack = callBack;
    }

    @Override
    public void onError(Call call, Exception e, int id) {
        e.printStackTrace();
        call.cancel();
        callBack.onError("");
        /*if(id==404){
         EventBus.getDefault().
        }else{*/
            //callBack.onError(e.getMessage());
        //}



    }

    @Override
    public void onResponse(String response, int id) {
        try{
        if(response!=null){

            JSONObject dataJson=new JSONObject(response);
            String code=null;
            String message=null;
            //Log.e("测试数据",response);
            if(dataJson.has("code")){
                code=dataJson.getString("code");
            }

            if(dataJson.has("message")){
                message=dataJson.getString("message");
            }
            //读取服务器的状态码，由于接口返回实体类不一样，只能伪装成code成功的样子。
            if(dataJson.has("ret")){
                code=dataJson.getString("ret");
            }

           if(code==null){
               Type finalNeedType;
               //获取到泛型的对象类型
               final Type[] types = callBack.getClass().getGenericInterfaces();
               if (MethodHandler(types) == null || MethodHandler(types).size() == 0) {
                   finalNeedType=null;
               }

               finalNeedType = MethodHandler(types).get(0);

               if (new Gson().fromJson(response, finalNeedType) == null) {
                   throw new NullPointerException();
               }

               T  modelT= new Gson().fromJson(response, finalNeedType);
               callBack.onResponse(modelT);

           }else{
               if(code.equals("200")){
                   Type finalNeedType;
                   //获取到泛型的对象类型
                   final Type[] types = callBack.getClass().getGenericInterfaces();
                   if (MethodHandler(types) == null || MethodHandler(types).size() == 0) {
                       finalNeedType=null;
                   }

                   finalNeedType = MethodHandler(types).get(0);

                   if (new Gson().fromJson(response, finalNeedType) == null) {
                       throw new NullPointerException();
                   }

                   T  modelT= new Gson().fromJson(response, finalNeedType);

                   callBack.onResponse(modelT);

               }/*else if(code.equals("401")){
                   *//*if(isLoadRefresh&&(boolean)SharedPreferencesUtils.getInstance().getData("islogin", true)){
                       requestUserLogin();
                       isLoadRefresh=false;
                   }*//*
               }*/else{
                   callBack.onError(message);

               }
           }

        }else{
            //callBack.onError("");
        }

        }catch (Exception e){
            e.printStackTrace();
            callBack.onError(VBTApplication.getContext().getString(R.string.net_error));
        }
    }


    /** 得到泛型类型
     * MethodHandler
     */
    private static List<Type> MethodHandler(Type[] types) {

        List<Type> needtypes = new ArrayList<>();
        Type needParentType = null;
        for (Type paramType : types) {
            System.out.println("  " + paramType);

            if (paramType instanceof ParameterizedType) {
                Type[] parentypes = ((ParameterizedType) paramType).getActualTypeArguments();
                for (Type childtype : parentypes) {
                    needtypes.add(childtype);
                    if (childtype instanceof ParameterizedType) {
                        Type[] childtypes = ((ParameterizedType) childtype).getActualTypeArguments();
                        for (Type type : childtypes) {
                            needtypes.add(type);
                        }
                    }
                }
            }
        }
        return needtypes;
    }

}

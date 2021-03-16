package com.biyoex.app.common.data;

import androidx.lifecycle.LiveData;

import com.biyoex.app.common.Constants;
import com.biyoex.app.common.bean.RequestResult;
import com.biyoex.app.common.http.RetrofitHelper;
import com.biyoex.app.common.mvpbase.BaseObserver;
import com.biyoex.app.common.utils.log.Log;
import com.biyoex.app.my.bean.ZGSessionInfoBean;

import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SessionLiveData extends LiveData<ZGSessionInfoBean> {

    private static SessionLiveData ins;

    private Timer timer;
    private TimerTask timerTask;

    private OnRequestSessionSuccessListener onRequestSessionSuccessListener;

    public static SessionLiveData getIns() {
        if (ins == null) {
            ins = new SessionLiveData();
        }
        return ins;
    }

    private SessionLiveData() {
        timer = new Timer();
    }

    //每2分钟请求session
    public void startTimer(OnRequestSessionSuccessListener onRequestSessionSuccessListener) {
        this.onRequestSessionSuccessListener = onRequestSessionSuccessListener;
        Log.i("session timer start");
        stopTimer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                getSeesionInfo();
            }
        };
        timer.schedule(timerTask, 0, 60000);
    }

    public void stopTimer() {
        if (timerTask != null) {
            Log.i("session timer stop");
            timerTask.cancel();
            timerTask = null;
        }
    }

    public void postValue(ZGSessionInfoBean value) {
        if (value == null) {
            stopTimer();
        }
        super.postValue(value);
    }

    @Override
    public void setValue(ZGSessionInfoBean value) {
        //说明请求不到session或者401都有可能为null
        if (value == null) {
            stopTimer();
        }
        super.setValue(value);
    }

    public void getSeesionInfo() {
//        Log.i("get session");
        RetrofitHelper.getIns()
                .getZgtopApi()
                .getSession()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<RequestResult<ZGSessionInfoBean>>() {
                    @Override
                    public void success(RequestResult<ZGSessionInfoBean> zgSessionInfoBeanRequestResult) {
//                        getFrontSeesion(zgSessionInfoBeanRequestResult.getData());
                        setValue(zgSessionInfoBeanRequestResult.getData());
                        Constants.shareId = zgSessionInfoBeanRequestResult.getData().getId() + "";
//                        AppData.setRealName(zgSessionInfoBeanRequestResult.getData().getRealName());//保存账号名
                        if (onRequestSessionSuccessListener != null) {
                            onRequestSessionSuccessListener.onRequestSessionSuccess();
                        }
                    }

                    @Override
                    public void failed(int code, String data, String msg) {
                        setValue(null);
                        if (onRequestSessionSuccessListener != null) {
                            onRequestSessionSuccessListener.onRequestSessionFailed();
                        }
                    }
                });
    }

//    public void getFrontSeesion(final ZGSessionInfoBean sessionInfoBean){
//        RetrofitHelper.getIns()
//                .getZgtopApi()
//                .getFrontSession()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new BaseObserver<RequestResult<FrontSessionBean>>() {
//                    @Override
//                    public void success(RequestResult<FrontSessionBean> frontSessionBeanRequestResult) {
//                        UserinfoBean userInfoBean = new UserinfoBean();
//                        userInfoBean.setSessionInfoBean(sessionInfoBean);
//                        userInfoBean.setFrontSessionBean(frontSessionBeanRequestResult.getData());
////                        setValue(userInfoBean);
//
//                        if (onRequestSessionSuccessListener != null){
//                            onRequestSessionSuccessListener.onRequestSessionSuccess();
//                        }
//                    }
//
//                    @Override
//                    public void failed(int code, String data, String msg) {
//                        setValue(null);
//                        if (onRequestSessionSuccessListener != null){
//                            onRequestSessionSuccessListener.onRequestSessionFailed();
//                        }
//                    }
//                });
//    }

    public void setOnRequestSessionSuccessListener(OnRequestSessionSuccessListener onRequestSessionSuccessListener) {
        this.onRequestSessionSuccessListener = onRequestSessionSuccessListener;
    }

    public interface OnRequestSessionSuccessListener {
        void onRequestSessionSuccess();

        void onRequestSessionFailed();
    }
}

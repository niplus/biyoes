package com.biyoex.app.common.utils;

import java.util.Timer;
import java.util.TimerTask;

public class OTCTimeUtils {

    private static OTCTimeUtils ins;

    private Timer timer;
    private TimerTask timerTask;

    private long remainTime;

    private OnTimeCountListener onTimeCountListener;

    /**
     * 是否在倒计时
     */
    private boolean isRunning;

    private OTCTimeUtils(){}

    public static OTCTimeUtils getIns(){
        if (ins == null){
            ins = new OTCTimeUtils();
        }
        return ins;
    }

    public void startCount(long startTime, long currentTime){
        if (timer == null) {
            timer = new Timer();
        }

        if (timerTask != null){
            timerTask.cancel();
            timerTask = null;
        }

        remainTime = (currentTime - startTime) / 1000;
        if (remainTime > 15 * 60){

            return;
        }

        //多3秒，防止误差
        remainTime = 15 * 60 + 3 - remainTime;
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (remainTime > 0){
                    remainTime--;

                    if (onTimeCountListener == null){
                        return;
                    }

                    if (remainTime <= 0){
                        stopTimer();
                        onTimeCountListener.onCount(0, 0);
                        return;
                    }

                    int min = (int) (remainTime / 60);
                    int sec = (int) (remainTime % 60);
                    onTimeCountListener.onCount(min, sec);
                }
            }
        };
        timer.schedule(timerTask, 1000, 1000);
        isRunning = true;
    }

    public void setOnTimeCountListener(OnTimeCountListener onTimeCountListener) {
        this.onTimeCountListener = onTimeCountListener;
    }

    public interface OnTimeCountListener{
        void onCount(int min, int sec);
    }

    public void reset(){
        if (timerTask != null){
            timerTask.cancel();
            timerTask = null;
        }
    }

    public void stopTimer(){
        isRunning = false;
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public boolean isRunning() {
        return isRunning;
    }
}

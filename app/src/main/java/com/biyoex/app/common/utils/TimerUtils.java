package com.biyoex.app.common.utils;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.biyoex.app.R;
import com.biyoex.app.VBTApplication;

import java.util.Timer;
import java.util.TimerTask;

/** 计时器工具类
 * Created by LG on 2017/3/13.
 */

public class TimerUtils {

    private static TimerUtils mTimerUtils;
    private Timer mTimer;
    private TextView tvTimer;
    private int mTotalTime;
    private boolean isCanClick=true;
    /**
     * 创建对象
     * @return
     */
    public static TimerUtils getInitialise(){
        if(mTimerUtils==null){
            mTimerUtils=new TimerUtils();
            return mTimerUtils;
        }else{
            return mTimerUtils;
        }
    }

    /**
     * 传入true为按钮可按
     * @param isCanClick
     */
    public void setCanOnClick(boolean isCanClick){
         this.isCanClick=isCanClick;
    }

    public boolean getCanOnClick(){
     return isCanClick;
    }
    /**
     * 启动计时器
     * @param tvTimer
     * @param totalTime
     */
    public void startTimer(TextView tvTimer,int totalTime){
        this.tvTimer=tvTimer;
        tvTimer.setClickable(false);
        tvTimer.setTextColor(Color.parseColor("#999999"));
        this.mTotalTime=totalTime;
        isCanClick=false;

        mTimer = new Timer();

        mTimer.schedule(new TimerTask(){
            @Override
            public void run() {
                if(mTotalTime<=0){
                    isCanClick=true;
                    handler.sendEmptyMessage(0314);
                }else{
                    isCanClick=false;
                    mTotalTime-=1;
                    handler.sendEmptyMessage(0313);
                }
            }
        },0,1000);
    }

    /**
     * 清空定时器
     */
    public void pauseTimer(){
        if(mTimer!=null){
            mTimer.cancel();
        }

    }

    /**
     * 重置计时器
     */
    public void resetTimer()
    {
        pauseTimer();
        setCanOnClick(true);

    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==0313){
                tvTimer.setText(mTotalTime+ VBTApplication.getContext().getString(R.string.second));
            }else{
                tvTimer.setText(R.string.resend);
                tvTimer.setClickable(true);
                tvTimer.setTextColor(Color.parseColor("#358dfa"));
                mTimer.cancel();
            }
        }
    };
}

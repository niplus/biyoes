package com.biyoex.app.trading.bean;

import androidx.lifecycle.LiveData;

import com.biyoex.app.common.http.RetrofitHelper;
import com.biyoex.app.common.mvpbase.BaseObserver;
import com.biyoex.app.home.bean.CoinTradeRankBean;

import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CoinMarketLiveData extends LiveData<CoinTradeRankBean> {

    private static CoinMarketLiveData ins;

    private Timer mTimer;
    private TimerTask timerTask;

    private CoinMarketLiveData() {
    }

    public static CoinMarketLiveData getIns() {
        if (ins == null)
            ins = new CoinMarketLiveData();
        return ins;
    }

    @Override
    public void setValue(CoinTradeRankBean value) {
        super.setValue(value);
    }

    /**
     * 开启定时刷新
     */
    public void startRefresh() {
        if (mTimer == null) {
            mTimer = new Timer();
        }
        if (timerTask == null) {
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    requestCoinBoardRank();
                }
            };
            mTimer.schedule(timerTask, 0, 5000);
        }
    }

    /**
     * 关闭定时器刷新
     */
    public void stopRefresh() {
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
    }

    @Override
    protected void onActive() {
        super.onActive();

        startRefresh();
    }

    @Override
    protected void onInactive() {
        super.onInactive();

        stopRefresh();
    }

    /**
     * @param
     * @param
     * @param
     */
    public void requestCoinBoardRank() {
        RetrofitHelper
                .getIns()
                .getZgtopApi()
                .requestCoinBoardRank()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

                .subscribe(new BaseObserver<CoinTradeRankBean>() {
                    @Override
                    public void success(CoinTradeRankBean coinTradeRankBean) {
                        CoinMarketLiveData.getIns().setValue(coinTradeRankBean);
                    }

                    @Override
                    public void failed(int code, String data, String msg) {

                    }
                });

    }
}

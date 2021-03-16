package com.biyoex.app.common.bean;

public class OtcFinanceChangeEvent {
    private int coinId;

    public OtcFinanceChangeEvent(int coinId) {
        this.coinId = coinId;
    }

    public int getCoinId() {
        return coinId;
    }

    public void setCoinId(int coinId) {
        this.coinId = coinId;
    }
}

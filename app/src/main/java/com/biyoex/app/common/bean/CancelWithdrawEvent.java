package com.biyoex.app.common.bean;

/**
 * Created by xxx on 2018/10/18.
 */

public class CancelWithdrawEvent {
    private String coinName;
    private int id;

    public CancelWithdrawEvent(String coinName, int id) {
        this.coinName = coinName;
        this.id = id;
    }

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

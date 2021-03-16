package com.biyoex.app.trading.bean;

/**
 * Created by LG on 2017/6/16.
 */

public class MarketRealBean {

    /**
     * high : 6554.17
     * vol : 14581.094
     * last : 6506.34
     * low : 6386.65
     * buy : 6501.86
     * sell : 6529.85
     * fupanddown : -0.4
     */

    private String high;
    private String vol;
    private String last;
    private String low;
    private String buy;
    private String sell;
    private String fupanddown;

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getVol() {
        return vol;
    }

    public void setVol(String vol) {
        this.vol = vol;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getBuy() {
        return buy;
    }

    public void setBuy(String buy) {
        this.buy = buy;
    }

    public String getSell() {
        return sell;
    }

    public void setSell(String sell) {
        this.sell = sell;
    }

    public String getFupanddown() {
        return fupanddown;
    }

    public void setFupanddown(String fupanddown) {
        this.fupanddown = fupanddown;
    }
}

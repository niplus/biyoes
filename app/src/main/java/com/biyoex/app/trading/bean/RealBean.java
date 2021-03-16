package com.biyoex.app.trading.bean;

/**
 * Created by LG on 2017/6/26.
 */

public class RealBean {

    /**
     * market : 10260000
     * high : 0.2899
     * vol : 3946221.659
     * last : 0.27
     * low : 0.2408
     * upWeek : 0.3719512195121952
     * buy : 0.2701
     * sell : 0.286
     * up : 0.029748283752860524
     * entrust : 0
     */

    private double high;
    private double vol;
    private double last;
    private double low;
    private double buy;
    private double sell;
    private double fupanddown;

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getVol() {
        return vol;
    }

    public void setVol(double vol) {
        this.vol = vol;
    }

    public double getLast() {
        return last;
    }

    public void setLast(double last) {
        this.last = last;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getBuy() {
        return buy;
    }

    public void setBuy(double buy) {
        this.buy = buy;
    }

    public double getSell() {
        return sell;
    }

    public void setSell(double sell) {
        this.sell = sell;
    }

    public double getFupanddown() {
        return fupanddown;
    }

    public void setFupanddown(double fupanddown) {
        this.fupanddown = fupanddown;
    }
}

package com.biyoex.app.common.bean;

/**
 * 用于深度图的数据，只需要价格和数量
 * Created by ndl on 2018/9/27.
 */

public class DepthBean {

    public DepthBean(double price, double volume) {
        this.price = price;
        this.volume = volume;
    }

    private double price;

    private double volume;

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    @Override
    public String toString() {
        return "DepthBean{" +
                "price=" + price +
                ", volume=" + volume +
                '}';
    }
}

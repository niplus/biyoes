package com.biyoex.app.common.bean;

import java.util.Map;

public class RateBean {
    Map<String, String> data;

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

    /**
     * 包含了人民币价格和usdt价格
     */
    public static class RateInfo{
        private double usdtPrice;
        private double rmbPrice;

        public double getUsdtPrice() {
            return usdtPrice;
        }

        public void setUsdtPrice(double usdtPrice) {
            this.usdtPrice = usdtPrice;
        }

        public double getRmbPrice() {
            return rmbPrice;
        }

        public void setRmbPrice(double rmbPrice) {
            this.rmbPrice = rmbPrice;
        }
    }
}

package com.biyoex.app.my.bean;

import java.util.List;

/**
 * Created by LG on 2017/8/23.
 */

public class DealDataBean {

    private List<ResultBean> result;

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * num_sell : 5000.0000
         * avg_buy : ￥0.28
         * avg_sell : ￥0.12
         * coinName : BCIA
         * num_buy : 5000.0000
         * shortName : BCIA
         * gain : ￥-787.00
         */

        private String num_sell;
        private String avg_buy;
        private String avg_sell;
        private String coinName;
        private String num_buy;
        private String shortName;
        private String gain;

        public String getNum_sell() {
            return num_sell;
        }

        public void setNum_sell(String num_sell) {
            this.num_sell = num_sell;
        }

        public String getAvg_buy() {
            return avg_buy;
        }

        public void setAvg_buy(String avg_buy) {
            this.avg_buy = avg_buy;
        }

        public String getAvg_sell() {
            return avg_sell;
        }

        public void setAvg_sell(String avg_sell) {
            this.avg_sell = avg_sell;
        }

        public String getCoinName() {
            return coinName;
        }

        public void setCoinName(String coinName) {
            this.coinName = coinName;
        }

        public String getNum_buy() {
            return num_buy;
        }

        public void setNum_buy(String num_buy) {
            this.num_buy = num_buy;
        }

        public String getShortName() {
            return shortName;
        }

        public void setShortName(String shortName) {
            this.shortName = shortName;
        }

        public String getGain() {
            return gain;
        }

        public void setGain(String gain) {
            this.gain = gain;
        }
    }
}

package com.biyoex.app.my.bean;

import java.util.List;

/**
 * Created by LG on 2017/6/23.
 */

public class TransferCoinRecordBean {

    /**
     * pageCount : 1
     * code : 200
     * data : [{"amount":0.005,"createTime":"2017-06-23 15:38:52","fee":0,"active":"转出","id":2766,"lastUpdateTime":"2017-06-23 15:39:21","status":"已到账"}]
     */

    private int pageCount;
    private int code;
    private List<DataBean> data;

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * amount : 0.005
         * createTime : 2017-06-23 15:38:52
         * fee : 0
         * active : 转出
         * id : 2766
         * lastUpdateTime : 2017-06-23 15:39:21
         * status : 已到账
         */

        private double amount;
        private String createTime;
        private double fee;
        private String active;
        private String id;
        private String lastUpdateTime;
        private String status;

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public double getFee() {
            return fee;
        }

        public void setFee(double fee) {
            this.fee = fee;
        }

        public String getActive() {
            return active;
        }

        public void setActive(String active) {
            this.active = active;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLastUpdateTime() {
            return lastUpdateTime;
        }

        public void setLastUpdateTime(String lastUpdateTime) {
            this.lastUpdateTime = lastUpdateTime;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}

package com.biyoex.app.my.bean;

import java.util.List;

/**
 * Created by xxx on 2018/6/4.
 */

public class TradeRecordBean {
    private int code;
    private int totalCount;
    private int page;
    private List<Record> data;

    public static class Record{
        private String amount;
        private String sellName;
        private String buyName;
        private String avgPrice;
        private double fee;
        private String count;
        private String successAmount;
        private int type;
        private String rightCount;
        private String price;
        private int id;
        private long time;
        private int status;

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getSellName() {
            return sellName;
        }

        public void setSellName(String sellName) {
            this.sellName = sellName;
        }

        public String getBuyName() {
            return buyName;
        }

        public void setBuyName(String buyName) {
            this.buyName = buyName;
        }

        public String getAvgPrice() {
            return avgPrice;
        }

        public void setAvgPrice(String avgPrice) {
            this.avgPrice = avgPrice;
        }

        public double getFee() {
            return fee;
        }

        public void setFee(double fee) {
            this.fee = fee;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public String getSuccessAmount() {
            return successAmount;
        }

        public void setSuccessAmount(String successAmount) {
            this.successAmount = successAmount;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getRightCount() {
            return rightCount;
        }

        public void setRightCount(String rightCount) {
            this.rightCount = rightCount;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<Record> getData() {
        return data;
    }

    public void setData(List<Record> data) {
        this.data = data;
    }
}

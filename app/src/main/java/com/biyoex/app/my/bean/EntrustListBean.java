package com.biyoex.app.my.bean;

import java.util.List;

/**
 * Created by LG on 2017/6/22.
 */

public class EntrustListBean {

    /**
     * pageCount : 1
     * code : 200
     * data : {"symbol":1,"list":[{"fname":"招股币","amount":1,"statusStr":"未成交","count":"1.0000","type":1,"percent":"0","leftCount":"1.0000","fShortName":"ZGC","typeStr":"卖出","price":"1.0000","successCount":"0","furl":"/upload/others/201607121403019_SR2NR.png","id":6971668,"status":1}]}
     */

    private int pageCount;
    private int code;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * symbol : 1
         * list : [{"fname":"招股币","amount":1,"statusStr":"未成交","count":"1.0000","type":1,"percent":"0","leftCount":"1.0000","fShortName":"ZGC","typeStr":"卖出","price":"1.0000","successCount":"0","furl":"/upload/others/201607121403019_SR2NR.png","id":6971668,"status":1}]
         */

        private String symbol;
        private List<ListBean> list;

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * fname : 招股币
             * amount : 1
             * statusStr : 未成交
             * count : 1.0000
             * type : 1
             * percent : 0
             * leftCount : 1.0000
             * fShortName : ZGC
             * typeStr : 卖出
             * price : 1.0000
             * successCount : 0
             * furl : /upload/others/201607121403019_SR2NR.png
             * id : 6971668
             * status : 1
             */

            private String fname;
            private double amount;
            private String statusStr;
            private double count;
            private int type;
            private String percent;
            private double leftCount;
            private String fShortName;
            private String typeStr;
            private double price;
            private double successCount;
            private String furl;
            private String id;
            private int status;

            public String getFname() {
                return fname;
            }

            public void setFname(String fname) {
                this.fname = fname;
            }

            public double getAmount() {
                return amount;
            }

            public void setAmount(double amount) {
                this.amount = amount;
            }

            public String getStatusStr() {
                return statusStr;
            }

            public void setStatusStr(String statusStr) {
                this.statusStr = statusStr;
            }

            public double getCount() {
                return count;
            }

            public void setCount(double count) {
                this.count = count;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getPercent() {
                return percent;
            }

            public void setPercent(String percent) {
                this.percent = percent;
            }

            public double getLeftCount() {
                return leftCount;
            }

            public void setLeftCount(double leftCount) {
                this.leftCount = leftCount;
            }

            public String getFShortName() {
                return fShortName;
            }

            public void setFShortName(String fShortName) {
                this.fShortName = fShortName;
            }

            public String getTypeStr() {
                return typeStr;
            }

            public void setTypeStr(String typeStr) {
                this.typeStr = typeStr;
            }

            public double getPrice() {
                return price;
            }

            public void setPrice(double price) {
                this.price = price;
            }

            public double getSuccessCount() {
                return successCount;
            }

            public void setSuccessCount(double successCount) {
                this.successCount = successCount;
            }

            public String getFurl() {
                return furl;
            }

            public void setFurl(String furl) {
                this.furl = furl;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }
        }
    }
}

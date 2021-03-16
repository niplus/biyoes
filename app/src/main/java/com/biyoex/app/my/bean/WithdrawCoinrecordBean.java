package com.biyoex.app.my.bean;

import android.os.Parcel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by LG on 2017/6/26.
 */

public class WithdrawCoinrecordBean {

    /**
     * code : 200
     * data : [{"amount":"9.9700","fees":"0","in_address":"z-2018","name":"GAT","txid":"1.11.237640842","id":100,"time":1530528842000,"confirmations":10,"url":"https://etherscan.io/tx/","status":3},{"amount":"0.0100","fees":"0","in_address":"z-2018","name":"GAT","txid":"1.11.237628457","id":99,"time":1530528002000,"confirmations":10,"url":"https://etherscan.io/tx/","status":3}]
     * totalCount : 2
     * page : 0
     * msg : null
     * id : null
     */

    private int code;
    private int totalCount;
    private int page;
    private String msg;
    private String id;
    private List<DataBean> data;

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

    public Object getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        /**
         * amount : 9.9700
         * fees : 0
         * in_address : z-2018
         * name : GAT
         * txid : 1.11.237640842
         * id : 100
         * time : 1530528842000
         * confirmations : 10
         * url : https://etherscan.io/tx/
         * status : 3
         */

        private String amount;
        private String fees;
        private String ffees;
        private String in_address;
        private String name;
        private String txid;
        private int id;
        private long time;
        private int confirmations;
        private String url;
        private int status;
        private int type;
        private String address;
        private String remark;

        protected DataBean(Parcel in) {
            amount = in.readString();
            fees = in.readString();
            ffees = in.readString();
            in_address = in.readString();
            name = in.readString();
            txid = in.readString();
            id = in.readInt();
            time = in.readLong();
            confirmations = in.readInt();
            url = in.readString();
            status = in.readInt();
            type = in.readInt();
            address = in.readString();
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getFfees() {
            return ffees;
        }

        public void setFfees(String ffees) {
            this.ffees = ffees;
        }

        public String getFees() {
            return fees;
        }

        public void setFees(String fees) {
            this.fees = fees;
        }

        public String getIn_address() {
            return in_address;
        }

        public void setIn_address(String in_address) {
            this.in_address = in_address;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTxid() {
            return txid;
        }

        public void setTxid(String txid) {
            this.txid = txid;
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

        public int getConfirmations() {
            return confirmations;
        }

        public void setConfirmations(int confirmations) {
            this.confirmations = confirmations;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }
}

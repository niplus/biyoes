package com.biyoex.app.my.bean;

import android.os.Parcel;

import java.io.Serializable;

public class TransferRecordBean implements Serializable {

    /**
     * count : 200
     * id : 3
     * coinName : USDT
     * type : 1
     * createTime : 1544060898000
     * status : 1
     */

    private double count;
    private int id;
    private String coinName;
    private int type;
    private long createTime;
    private int status;
    private String allName;
    private String url;

    protected TransferRecordBean(Parcel in) {
        count = in.readDouble();
        id = in.readInt();
        coinName = in.readString();
        type = in.readInt();
        createTime = in.readLong();
        status = in.readInt();
        allName = in.readString();
        url = in.readString();
    }


    public double getCount() {
        return count;
    }

    public void setCount(double count) {
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAllName() {
        return allName;
    }

    public void setAllName(String allName) {
        this.allName = allName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


}

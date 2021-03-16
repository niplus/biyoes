package com.biyoex.app.my.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by LG on 2017/6/20.
 */

public class RechargeCoinBean implements Parcelable{

    /**
     * "allName": "比特币",
     "total": "0",
     "name": "BTC",
     "frozen": "0",
     "isWithDraw": false,
     "id": 35,
     "isRecharge": false,
     "url": "/upload/others/201805261811028_dlGyi.png",
     "order": 0
     */


    private String allName;
    private String total;
    private String name;
    private String frozen;
    private boolean isWithDraw;
    private String id;
    private boolean isRecharge;
    private String url;
    private int order;
    private boolean innovate;
    private String cnyValue;

    public void setCnyValue(String cnyValue) {
        this.cnyValue = cnyValue;
    }

    public String getCnyValue() {
        return cnyValue;
    }

    /**
     * 换算成USDT的价格
     */
    private double value;
    private int equalId;

    public void setEqualId(int equalId) {
        this.equalId = equalId;
    }

    public int getEqualId() {
        return equalId;
    }

    protected RechargeCoinBean(Parcel in) {
        allName = in.readString();
        total = in.readString();
        name = in.readString();
        frozen = in.readString();
        isWithDraw = in.readByte() != 0;
        id = in.readString();
        isRecharge = in.readByte() != 0;
        url = in.readString();
        order = in.readInt();
        value = in.readDouble();
    }

    public void setInnovate(boolean innovate) {
        this.innovate = innovate;
    }
    public boolean getInnovate(){
        return innovate;
    }

    public static final Creator<RechargeCoinBean> CREATOR = new Creator<RechargeCoinBean>() {
        @Override
        public RechargeCoinBean createFromParcel(Parcel in) {
            return new RechargeCoinBean(in);
        }

        @Override
        public RechargeCoinBean[] newArray(int size) {
            return new RechargeCoinBean[size];
        }
    };

    public String getAllName() {
        return allName;
    }

    public void setAllName(String allName) {
        this.allName = allName;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFrozen() {
        return frozen;
    }

    public void setFrozen(String frozen) {
        this.frozen = frozen;
    }

    public boolean isWithDraw() {
        return isWithDraw;
    }

    public void setWithDraw(boolean withDraw) {
        isWithDraw = withDraw;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isRecharge() {
        return isRecharge;
    }

    public void setRecharge(boolean recharge) {
        isRecharge = recharge;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "RechargeCoinBean{" +
                "allName='" + allName + '\'' +
                ", total='" + total + '\'' +
                ", name='" + name + '\'' +
                ", frozen='" + frozen + '\'' +
                ", isWithDraw=" + isWithDraw +
                ", id='" + id + '\'' +
                ", isRecharge=" + isRecharge +
                ", url='" + url + '\'' +
                ", order=" + order +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(allName);
        dest.writeString(total);
        dest.writeString(name);
        dest.writeString(frozen);
        dest.writeByte((byte) (isWithDraw ? 1 : 0));
        dest.writeString(id);
        dest.writeByte((byte) (isRecharge ? 1 : 0));
        dest.writeString(url);
        dest.writeInt(order);
        dest.writeDouble(value);
    }
}

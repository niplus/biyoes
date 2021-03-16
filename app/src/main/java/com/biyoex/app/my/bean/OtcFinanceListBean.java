package com.biyoex.app.my.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class OtcFinanceListBean implements Parcelable {

    /**
     * coinId : 50
     * allName : 泰达币
     * total : 1110.0000
     * frozen : 0
     * coinName : USDT
     * marketId : 2
     */

    private int coinId;
    private String allName;
    private double total;
    private double frozen;
    private String coinName;
    private int marketId;
    private double usdtValue;
    private String url;

    protected OtcFinanceListBean(Parcel in) {
        coinId = in.readInt();
        allName = in.readString();
        total = in.readDouble();
        frozen = in.readDouble();
        coinName = in.readString();
        marketId = in.readInt();
        usdtValue = in.readDouble();
        url = in.readString();
    }

    public static final Creator<OtcFinanceListBean> CREATOR = new Creator<OtcFinanceListBean>() {
        @Override
        public OtcFinanceListBean createFromParcel(Parcel in) {
            return new OtcFinanceListBean(in);
        }

        @Override
        public OtcFinanceListBean[] newArray(int size) {
            return new OtcFinanceListBean[size];
        }
    };

    public int getCoinId() {
        return coinId;
    }

    public void setCoinId(int coinId) {
        this.coinId = coinId;
    }

    public String getAllName() {
        return allName;
    }

    public void setAllName(String allName) {
        this.allName = allName;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getFrozen() {
        return frozen;
    }

    public void setFrozen(double frozen) {
        this.frozen = frozen;
    }

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public int getMarketId() {
        return marketId;
    }

    public void setMarketId(int marketId) {
        this.marketId = marketId;
    }

    public double getUsdtValue() {
        return usdtValue;
    }

    public void setUsdtValue(double usdtValue) {
        this.usdtValue = usdtValue;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(coinId);
        dest.writeString(allName);
        dest.writeDouble(total);
        dest.writeDouble(frozen);
        dest.writeString(coinName);
        dest.writeInt(marketId);
        dest.writeDouble(usdtValue);
        dest.writeString(url);
    }
}

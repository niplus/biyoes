package com.biyoex.app.home.bean;

import android.os.Parcel;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by LG on 2017/6/16.
 */

public class CoinTradeRankBean {
    private Map<String, List<DealDatasBean>> dataMap;
    private List<String> sortMarket;
    private String btc;
    private String eth;

    public void setBtc(String btc) {
        this.btc = btc;
    }

    public String getBtc() {
        return btc;
    }

    public Map<String, List<DealDatasBean>> getDataMap() {
        return dataMap;
    }

    public void setDataMap(Map<String, List<DealDatasBean>> dataMap) {
        this.dataMap = dataMap;
    }

    public List<String> getSortMarket() {
        return sortMarket;
    }

    public void setSortMarket(List<String> sortMarket) {
        this.sortMarket = sortMarket;
    }

    public static class DealDatasBean implements Serializable {
        /**
         * fid : 35
         * fisShare : true
         * fname : Bitcoin
         * fShortName : BTC
         * fname_sn : BTC / USDT
         * furl : /upload/others/201807231458056_Nmk5d.png
         * fupanddown : -0.4
         * fupanddownweek : 0
         * fmarketValue : 6512.22
         * fentrustValue : 9.443494056E7
         * volumn : 14581.094
         * lastDealPrize : 6506.91
         * higestBuyPrize : 6501.86
         * lowestSellPrize : 6529.85
         * status : 1
         * openTrade : 00:00-24:00
         * coinTradeType : 37
         * group : USDT
         * homeShow : false
         * homeOrder : 0
         * typeOrder : 0
         * totalOrder : 0
         * lowestPrize24 : 6386.65
         * highestPrize24 : 6554.17
         * totalDeal24 : 14581.094
         * entrustValue24 : 9.44349405632E7
         * priceDecimals : 2
         * amountDecimals : 3
         * sort : 1
         */

        private int fid;
        private boolean fisShare;
        private String fname;
        private String fShortName;
        private String fname_sn;
        private String furl;
        private double fupanddown;
        private double fupanddownweek;
        private double fmarketValue;
        private double fentrustValue;
        private double volumn;
        private double lastDealPrize;
        private double higestBuyPrize;
        private double lowestSellPrize;
        private int status;
        private String openTrade;
        private int coinTradeType;
        private String group;
        private boolean homeShow;
        private int homeOrder;
        private int typeOrder;
        private int totalOrder;
        private double lowestPrize24;
        private double highestPrize24;
        private double totalDeal24;
        private double entrustValue24;
        private int priceDecimals;
        private int amountDecimals;
        private int sort;
        private int isNew;
        private String rate;
        private boolean burn;
        private double burnRate;

        public void setBurn(boolean burn) {
            this.burn = burn;
        }

        public double getBurnRate() {
            return burnRate;
        }

        public void setBurnRate(double burnRate) {
            this.burnRate = burnRate;
        }

        public boolean isBurn() {
            return burn;
        }

        public void setfShortName(String fShortName) {
            this.fShortName = fShortName;
        }

        public String getfShortName() {
            return fShortName;
        }

        public Double getRate() {
            return Double.valueOf(rate);
        }

        public void setRate(String rate) {
            this.rate = rate;
        }

        public void setIsNew(int isNew) {
            this.isNew = isNew;
        }

        public int getIsNew() {
            return isNew;
        }

        protected DealDatasBean(Parcel in) {
            fid = in.readInt();
            fisShare = in.readByte() != 0;
            fname = in.readString();
            fShortName = in.readString();
            fname_sn = in.readString();
            furl = in.readString();
            fupanddown = in.readDouble();
            fupanddownweek = in.readInt();
            fmarketValue = in.readDouble();
            fentrustValue = in.readDouble();
            volumn = in.readDouble();
            lastDealPrize = in.readDouble();
            higestBuyPrize = in.readDouble();
            lowestSellPrize = in.readDouble();
            status = in.readInt();
            openTrade = in.readString();
            coinTradeType = in.readInt();
            group = in.readString();
            homeShow = in.readByte() != 0;
            homeOrder = in.readInt();
            typeOrder = in.readInt();
            totalOrder = in.readInt();
            lowestPrize24 = in.readDouble();
            highestPrize24 = in.readDouble();
            totalDeal24 = in.readDouble();
            entrustValue24 = in.readDouble();
            priceDecimals = in.readInt();
            amountDecimals = in.readInt();
            sort = in.readInt();
            rate = in.readString();
            burnRate = in.readDouble();
        }


        @Override
        public DealDatasBean clone() {

            DealDatasBean dealDatasBean = null;
            try {
                dealDatasBean = (DealDatasBean) super.clone();
                return dealDatasBean;
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            return null;
        }

        public int getFid() {
            return fid;
        }

        public void setFid(int fid) {
            this.fid = fid;
        }

        public boolean isFisShare() {
            return fisShare;
        }

        public void setFisShare(boolean fisShare) {
            this.fisShare = fisShare;
        }

        public String getFname() {
            return fname;
        }

        public void setFname(String fname) {
            this.fname = fname;
        }

        public String getFShortName() {
            return fShortName;
        }

        public void setFShortName(String fShortName) {
            this.fShortName = fShortName;
        }

        public String getFname_sn() {
            return fname_sn;
        }

        public void setFname_sn(String fname_sn) {
            this.fname_sn = fname_sn;
        }

        public String getFurl() {
            return furl;
        }

        public void setFurl(String furl) {
            this.furl = furl;
        }

        public double getFupanddown() {
            return fupanddown;
        }

        public void setFupanddown(double fupanddown) {
            this.fupanddown = fupanddown;
        }

        public double getFupanddownweek() {
            return fupanddownweek;
        }

        public void setFupanddownweek(double fupanddownweek) {
            this.fupanddownweek = fupanddownweek;
        }

        public double getFmarketValue() {
            return fmarketValue;
        }

        public void setFmarketValue(double fmarketValue) {
            this.fmarketValue = fmarketValue;
        }

        public double getFentrustValue() {
            return fentrustValue;
        }

        public void setFentrustValue(double fentrustValue) {
            this.fentrustValue = fentrustValue;
        }

        public double getVolumn() {
            return volumn;
        }

        public void setVolumn(double volumn) {
            this.volumn = volumn;
        }

        public double getLastDealPrize() {
            return lastDealPrize;
        }

        public void setLastDealPrize(double lastDealPrize) {
            this.lastDealPrize = lastDealPrize;
        }

        public double getHigestBuyPrize() {
            return higestBuyPrize;
        }

        public void setHigestBuyPrize(double higestBuyPrize) {
            this.higestBuyPrize = higestBuyPrize;
        }

        public double getLowestSellPrize() {
            return lowestSellPrize;
        }

        public void setLowestSellPrize(double lowestSellPrize) {
            this.lowestSellPrize = lowestSellPrize;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getOpenTrade() {
            return openTrade;
        }

        public void setOpenTrade(String openTrade) {
            this.openTrade = openTrade;
        }

        public int getCoinTradeType() {
            return coinTradeType;
        }

        public void setCoinTradeType(int coinTradeType) {
            this.coinTradeType = coinTradeType;
        }

        public String getGroup() {
            return group;
        }

        public void setGroup(String group) {
            this.group = group;
        }

        public boolean isHomeShow() {
            return homeShow;
        }

        public void setHomeShow(boolean homeShow) {
            this.homeShow = homeShow;
        }

        public int getHomeOrder() {
            return homeOrder;
        }

        public void setHomeOrder(int homeOrder) {
            this.homeOrder = homeOrder;
        }

        public int getTypeOrder() {
            return typeOrder;
        }

        public void setTypeOrder(int typeOrder) {
            this.typeOrder = typeOrder;
        }

        public int getTotalOrder() {
            return totalOrder;
        }

        public void setTotalOrder(int totalOrder) {
            this.totalOrder = totalOrder;
        }

        public double getLowestPrize24() {
            return lowestPrize24;
        }

        public void setLowestPrize24(double lowestPrize24) {
            this.lowestPrize24 = lowestPrize24;
        }

        public double getHighestPrize24() {
            return highestPrize24;
        }

        public void setHighestPrize24(double highestPrize24) {
            this.highestPrize24 = highestPrize24;
        }

        public double getTotalDeal24() {
            return totalDeal24;
        }

        public void setTotalDeal24(double totalDeal24) {
            this.totalDeal24 = totalDeal24;
        }

        public double getEntrustValue24() {
            return entrustValue24;
        }

        public void setEntrustValue24(double entrustValue24) {
            this.entrustValue24 = entrustValue24;
        }

        public int getPriceDecimals() {
            return priceDecimals;
        }

        public void setPriceDecimals(int priceDecimals) {
            this.priceDecimals = priceDecimals;
        }

        public int getAmountDecimals() {
            return amountDecimals;
        }

        public void setAmountDecimals(int amountDecimals) {
            this.amountDecimals = amountDecimals;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        @Override
        public String toString() {
            return "DealDatasBean{" +
                    "fid=" + fid +
                    ", fisShare=" + fisShare +
                    ", fname='" + fname + '\'' +
                    ", fShortName='" + fShortName + '\'' +
                    ", fname_sn='" + fname_sn + '\'' +
                    ", furl='" + furl + '\'' +
                    ", fupanddown=" + fupanddown +
                    ", fupanddownweek=" + fupanddownweek +
                    ", fmarketValue=" + fmarketValue +
                    ", fentrustValue=" + fentrustValue +
                    ", volumn=" + volumn +
                    ", lastDealPrize=" + lastDealPrize +
                    ", higestBuyPrize=" + higestBuyPrize +
                    ", lowestSellPrize=" + lowestSellPrize +
                    ", status=" + status +
                    ", openTrade='" + openTrade + '\'' +
                    ", coinTradeType=" + coinTradeType +
                    ", group='" + group + '\'' +
                    ", homeShow=" + homeShow +
                    ", homeOrder=" + homeOrder +
                    ", typeOrder=" + typeOrder +
                    ", totalOrder=" + totalOrder +
                    ", lowestPrize24=" + lowestPrize24 +
                    ", highestPrize24=" + highestPrize24 +
                    ", totalDeal24=" + totalDeal24 +
                    ", entrustValue24=" + entrustValue24 +
                    ", priceDecimals=" + priceDecimals +
                    ", amountDecimals=" + amountDecimals +
                    ", sort=" + sort +
                    ", isNew=" + isNew +
                    ", rate='" + rate + '\'' +
                    ", burn=" + burn +
                    ", burnRate=" + burnRate +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "CoinTradeRankBean{" +
                "dataMap=" + dataMap +
                ", sortMarket=" + sortMarket +
                ", btc='" + btc + '\'' +
                ", eth='" + eth + '\'' +
                '}';
    }
}

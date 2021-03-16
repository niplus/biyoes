package com.biyoex.app.my.bean;

import java.util.List;

/**
 * Created by LG on 2017/6/22.
 */

public class PersonalAssetsBean {

    /**
     * pageCount : 1
     * code : 200
     * data : {"ftotalRmb":"0","ffrozenRmb":"0","vWallets":[{"fShortName":"BTC","coinId":15,"fname":"比特币","FIsWithDraw":false,"ffrozen":"0","furl":"/upload/others/201606152050058_pzlpu.png","fisShare":true,"ftotal":"0"}]}
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
         * ftotalRmb : 0
         * ffrozenRmb : 0
         * vWallets : [{"fShortName":"BTC","coinId":15,"fname":"比特币","FIsWithDraw":false,"ffrozen":"0","furl":"/upload/others/201606152050058_pzlpu.png","fisShare":true,"ftotal":"0"}]
         */

        private String ftotalRmb;
        private String ffrozenRmb;
        private List<VWalletsBean> vWallets;

        public String getFtotalRmb() {
            return ftotalRmb;
        }

        public void setFtotalRmb(String ftotalRmb) {
            this.ftotalRmb = ftotalRmb;
        }

        public String getFfrozenRmb() {
            return ffrozenRmb;
        }

        public void setFfrozenRmb(String ffrozenRmb) {
            this.ffrozenRmb = ffrozenRmb;
        }

        public List<VWalletsBean> getVWallets() {
            return vWallets;
        }

        public void setVWallets(List<VWalletsBean> vWallets) {
            this.vWallets = vWallets;
        }

        public static class VWalletsBean {
            /**
             * fShortName : BTC
             * coinId : 15
             * fname : 比特币
             * FIsWithDraw : false
             * ffrozen : 0
             * furl : /upload/others/201606152050058_pzlpu.png
             * fisShare : true
             * ftotal : 0
             */

            private String fShortName;
            private String coinId;
            private String fname;
            private boolean FIsWithDraw;
            private double ffrozen;
            private String furl;
            private boolean fisShare;
            private double ftotal;
            private double doubleTotal;
            private double fassert;

            public String getfShortName() {
                return fShortName;
            }

            public void setfShortName(String fShortName) {
                this.fShortName = fShortName;
            }

            public double getFassert() {
                return fassert;
            }

            public void setFassert(double fassert) {
                this.fassert = fassert;
            }

            public double getDoubleTotal() {
                return doubleTotal;
            }

            public void setDoubleTotal(double doubleTotal) {
                this.doubleTotal = doubleTotal;
            }

            public String getFShortName() {
                return fShortName;
            }

            public void setFShortName(String fShortName) {
                this.fShortName = fShortName;
            }

            public String getCoinId() {
                return coinId;
            }

            public void setCoinId(String coinId) {
                this.coinId = coinId;
            }

            public String getFname() {
                return fname;
            }

            public void setFname(String fname) {
                this.fname = fname;
            }

            public boolean isFIsWithDraw() {
                return FIsWithDraw;
            }

            public void setFIsWithDraw(boolean FIsWithDraw) {
                this.FIsWithDraw = FIsWithDraw;
            }

            public double getFfrozen() {
                return ffrozen;
            }

            public void setFfrozen(double ffrozen) {
                this.ffrozen = ffrozen;
            }

            public String getFurl() {
                return furl;
            }

            public void setFurl(String furl) {
                this.furl = furl;
            }

            public boolean isFisShare() {
                return fisShare;
            }

            public void setFisShare(boolean fisShare) {
                this.fisShare = fisShare;
            }

            public double getFtotal() {
                return ftotal;
            }

            public void setFtotal(double ftotal) {
                this.ftotal = ftotal;
            }
        }
    }
}

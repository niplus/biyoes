package com.biyoex.app.trading.bean;

import java.math.BigDecimal;
import java.util.List;

     /**
     * Created by LG on 2017/6/16.
     */

    public class RefreshUserInfoBean {

        /**
         * isLogin : 1
         * rmbfrozen : 0.0
     * rmbtotal : 194.47272
     * entrustList : []
     * virfrozen : 0.0
     * needTradePasswd : true
     * entrustListLog : [[1,0.2091,400,6970985,3,83.64],[0,0.22,400,6929387,3,88]]
     * virtotal : 0.0
     * recommendPrizesell : 0.2091
     * recommendPrizebuy : 0.225
     * token : FB444638372B740FCF27F1E76EF573C5-n1
     */

    private String isLogin;
    private String rmbfrozen;
    private String rmbtotal;
    private String virfrozen;
    private boolean needTradePasswd;
    private String virtotal;
    private double recommendPrizesell;
    private double recommendPrizebuy;
    private String token;
    private List<List<String>> entrustList;
    private List<List<String>> entrustListLog;

    public String getIsLogin() {
        return isLogin;
    }

    public void setIsLogin(String isLogin) {
        this.isLogin = isLogin;
    }

    public double getRmbfrozen() {
        return new BigDecimal(rmbfrozen).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public void setRmbfrozen(String rmbfrozen) {
        this.rmbfrozen = rmbfrozen;
    }

    public double getRmbtotal() {
        BigDecimal bigDecimal = new BigDecimal(rmbtotal).setScale(4, BigDecimal.ROUND_HALF_UP);
        return bigDecimal.doubleValue();
    }

    public void setRmbtotal(String rmbtotal) {
        this.rmbtotal = rmbtotal;
    }

    public double getVirfrozen() {
        return new BigDecimal(virfrozen).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public void setVirfrozen(String virfrozen) {
        this.virfrozen = virfrozen;
    }

    public boolean isNeedTradePasswd() {
        return needTradePasswd;
    }

    public void setNeedTradePasswd(boolean needTradePasswd) {
        this.needTradePasswd = needTradePasswd;
    }

    public double getVirtotal() {
        BigDecimal bigDecimal = new BigDecimal(virtotal).setScale(4, BigDecimal.ROUND_DOWN);
        return bigDecimal.doubleValue();
    }

    public void setVirtotal(String virtotal) {
        this.virtotal = virtotal;
    }

    public double getRecommendPrizesell() {
        return recommendPrizesell;
    }

    public void setRecommendPrizesell(double recommendPrizesell) {
        this.recommendPrizesell = recommendPrizesell;
    }

    public double getRecommendPrizebuy() {
        return recommendPrizebuy;
    }

    public void setRecommendPrizebuy(double recommendPrizebuy) {
        this.recommendPrizebuy = recommendPrizebuy;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<List<String>> getEntrustList() {
        return entrustList;
    }

    public void setEntrustList(List<List<String>> entrustList) {
        this.entrustList = entrustList;
    }

    public List<List<String>> getEntrustListLog() {
        return entrustListLog;
    }

    public void setEntrustListLog(List<List<String>> entrustListLog) {
        this.entrustListLog = entrustListLog;
    }

    @Override
    public String toString() {
        return "RefreshUserInfoBean{" +
                "isLogin='" + isLogin + '\'' +
                ", rmbfrozen=" + rmbfrozen +
                ", rmbtotal=" + rmbtotal +
                ", virfrozen=" + virfrozen +
                ", needTradePasswd=" + needTradePasswd +
                ", virtotal=" + virtotal +
                ", recommendPrizesell=" + recommendPrizesell +
                ", recommendPrizebuy=" + recommendPrizebuy +
                ", token='" + token + '\'' +
                ", entrustList=" + entrustList +
                ", entrustListLog=" + entrustListLog +
                '}';
    }
}

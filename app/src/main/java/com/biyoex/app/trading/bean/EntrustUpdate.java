package com.biyoex.app.trading.bean;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by LG on 2017/6/28.
 */

public class EntrustUpdate {

    /**
     * symbol : 3
     * rmbfrozen : 12
     * rmbtotal : 143.355829
     * entrustList : []
     * virfrozen : 0
     * entrustListLog : [[0,0.0331,0.005,6991577,3,1.66E-4],[0,0.0331,0.005,6991573,3,1.66E-4],[0,0.0332,0.05,6991565,3,0.00166],[0,0.0332,0.05,6991561,3,0.00166],[0,0.5,0.012,6991543,3,3.98E-4]]
     * virtotal : 35.6292
     */

    private String symbol;
    private String rmbfrozen;
    private String rmbtotal;
    private String virfrozen;
    private String virtotal;
    private List<List<String>> entrustList;
    private List<List<String>> entrustListLog;
    private int isLogin;

    public int getIsLogin() {
        return isLogin;
    }

    public void setIsLogin(int isLogin) {
        this.isLogin = isLogin;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getRmbfrozen() {
        return new BigDecimal(rmbfrozen).setScale(4,BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public void setRmbfrozen(String rmbfrozen) {
        this.rmbfrozen = rmbfrozen;
    }

    public double getRmbtotal() {
        return  new BigDecimal(rmbtotal).setScale(4,BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public void setRmbtotal(String rmbtotal) {
        this.rmbtotal = rmbtotal;
    }

    public double getVirfrozen() {
        return new BigDecimal(virfrozen).setScale(4,BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public void setVirfrozen(String virfrozen) {
        this.virfrozen = virfrozen;
    }

    public double getVirtotal() {
        return new BigDecimal(virtotal).setScale(4,BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public void setVirtotal(String virtotal) {
        this.virtotal = virtotal;
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
}

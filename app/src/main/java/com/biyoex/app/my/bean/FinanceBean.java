package com.biyoex.app.my.bean;

import com.biyoex.app.home.bean.CoinTradeRankBean;

import java.util.List;
import java.util.Map;

public class FinanceBean {
    private String rate;
    private Map<String, List<CoinTradeRankBean.DealDatasBean>> dataMap;
    private List<RechargeCoinBean> balanceList;
    private double totalUsdt;

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public List<RechargeCoinBean> getBalanceList() {
        return balanceList;
    }

    public void setBalanceList(List<RechargeCoinBean> balanceList) {
        this.balanceList = balanceList;
    }

    public Map<String, List<CoinTradeRankBean.DealDatasBean>> getDataMap() {
        return dataMap;
    }

    public void setDataMap(Map<String, List<CoinTradeRankBean.DealDatasBean>> dataMap) {
        this.dataMap = dataMap;
    }

    public double getTotalUsdt() {
        return totalUsdt;
    }

    public void setTotalUsdt(double totalUsdt) {
        this.totalUsdt = totalUsdt;
    }

    @Override
    public String toString() {
        return "FinanceBean{" +
                "rate='" + rate + '\'' +
                ", dataMap=" + dataMap +
                ", balanceList=" + balanceList +
                ", totalUsdt=" + totalUsdt +
                '}';
    }
}

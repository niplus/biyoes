package com.biyoex.app.property.datas;

import java.math.BigDecimal;

public class LockDataBean {
    private String minAmount;
    private boolean innovate;
    private String newFname;
    private String num;
    private int scale;
    private String Fname;
    private String fTotal;
    private String totalAmount;

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setFname(String fname) {
        Fname = fname;
    }

    public String getFname() {
        return Fname;
    }

    public void setfTotal(String fTotal) {
        this.fTotal = fTotal;
    }

    public double getfTotal() {
        return new BigDecimal(fTotal).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public boolean getInnovate() {
        return innovate;
    }

    public void setInnovate(boolean innovate) {
        this.innovate = innovate;
    }

    public double getMinAmount() {
        return new BigDecimal(minAmount).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public void setMinAmount(String minAmount) {
        this.minAmount = minAmount;
    }

    public String getNewFname() {
        return newFname;
    }

    public void setNewFname(String newFname) {
        this.newFname = newFname;
    }

    public double getNum() {
        return new BigDecimal(num).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public void setNum(String num) {
        this.num = num;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

}

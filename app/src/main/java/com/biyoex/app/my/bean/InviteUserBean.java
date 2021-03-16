package com.biyoex.app.my.bean;

import java.util.List;

/**
 * Created by xxx on 2018/6/26.
 */

public class InviteUserBean {

    private List<Intros> intros;
    private List<Num> num;

    private int hasRealValidateCount;

    public List<Intros> getIntros() {
        return intros;
    }

    public void setIntros(List<Intros> intros) {
        this.intros = intros;
    }

    public List<Num> getNum() {
        return num;
    }

    public void setNum(List<Num> num) {
        this.num = num;
    }

    public int getHasRealValidateCount() {
        return hasRealValidateCount;
    }

    public void setHasRealValidateCount(int hasRealValidateCount) {
        this.hasRealValidateCount = hasRealValidateCount;
    }

    public class Intros{
        private long createTime;
        private String loginName;
        private double num;
        private String coinName;

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public String getLoginName() {
            return loginName;
        }

        public void setLoginName(String loginName) {
            this.loginName = loginName;
        }

        public double getNum() {
            return num;
        }

        public void setNum(double num) {
            this.num = num;
        }

        public String getCoinName() {
            return coinName;
        }

        public void setCoinName(String coinName) {
            this.coinName = coinName;
        }
    }

    public class Num{
        private String coinName;
        private String sum;

        public String getCoinName() {
            return coinName;
        }

        public void setCoinName(String coinName) {
            this.coinName = coinName;
        }

        public String getSum() {
            return sum;
        }

        public void setSum(String sum) {
            this.sum = sum;
        }
    }

}

package com.biyoex.app.my.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xxx on 2018/5/31.
 */

public class ZGRechargeCoinBean {

    /**
     * "feeAmount": 2.0E-4,
     * "total": "10000000.0000",
     * "times": 10,
     * "addresses": [{
     * "id": 1512,
     * "address": "1111111111111111111111111",
     * "flag": "123"
     * }, {
     * "id": 1520,
     * "address": "22222222222222222222222222",
     * "flag": "ndl"
     * }],
     * "min": 0.01,
     * "max": 1000.0,
     * "feeName": "ETH",
     * "frozen": "0",
     * "minWithdraw": 0.01,
     * "feeRatio": 0.0
     */
    private double feeAmount;
    private String total;
    private int times;
    private double min;
    private double max;
    private String feeName;
    private String frozen;
    private String minWithdraw;
    private double feeRatio;
    private List<AddressBean> addresses;
    private boolean isremark;
    private String multiChain;
    private List<MultiChainListBean> multiChainList;

    public List<MultiChainListBean> getMultiChainList() {
        return multiChainList;
    }

    public void setMultiChainList(List<MultiChainListBean> multiChainList) {
        this.multiChainList = multiChainList;
    }

    public String getMultiChain() {
        return multiChain;
    }

    public void setMultiChain(String multiChain) {
        this.multiChain = multiChain;
    }

    public static class MultiChainListBean{
        private String type;
        private double feeAmount;
        private double minWithdraw;
        private String defaultFlag;

        public String getDefaultFlag() {
            return defaultFlag;
        }

        public void setDefaultFlag(String defaultFlag) {
            this.defaultFlag = defaultFlag;
        }

        public double getFeeAmount() {
            return feeAmount;
        }

        public void setFeeAmount(double feeAmount) {
            this.feeAmount = feeAmount;
        }

        public double getMinWithdraw() {
            return minWithdraw;
        }

        public void setMinWithdraw(double minWithdraw) {
            this.minWithdraw = minWithdraw;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    public static class AddressBean implements Serializable {
        private int id;
        private String address;
        private String flag;
        private boolean isremark;
        private String remark;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

        public boolean isIsremark() {
            return isremark;
        }

        public void setIsremark(boolean isremark) {
            this.isremark = isremark;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        @Override
        public String toString() {
            return "AddressBean{" +
                    "id=" + id +
                    ", address='" + address + '\'' +
                    ", flag='" + flag + '\'' +
                    '}';
        }

    }

    public void setIsremark(boolean isremark) {
        this.isremark = isremark;
    }

    public boolean getIsremark() {
        return isremark;
    }

    public double getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(double feeAmount) {
        this.feeAmount = feeAmount;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public String getFeeName() {
        return feeName;
    }

    public void setFeeName(String feeName) {
        this.feeName = feeName;
    }

    public String getFrozen() {
        return frozen;
    }

    public void setFrozen(String frozen) {
        this.frozen = frozen;
    }

    public String getMinWithdraw() {
        return minWithdraw;
    }

    public void setMinWithdraw(String minWithdraw) {
        this.minWithdraw = minWithdraw;
    }

    public double getFeeRatio() {
        return feeRatio;
    }

    public void setFeeRatio(double feeRatio) {
        this.feeRatio = feeRatio;
    }

    public List<AddressBean> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AddressBean> addresses) {
        this.addresses = addresses;
    }

}

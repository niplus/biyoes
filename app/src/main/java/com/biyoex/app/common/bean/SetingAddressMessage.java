package com.biyoex.app.common.bean;

/**
 * Created by LG on 2017/8/10.
 */

public class SetingAddressMessage {
    String address;

    public SetingAddressMessage() {
    }

    public SetingAddressMessage(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

package com.biyoex.app.common.base;

public class RxBusData {
    private String msgName;
    private Object msgData;

    public void setMsgData(Object msgData) {
        this.msgData = msgData;
    }

    public Object getMsgData() {
        return msgData;
    }

    public void setMsgName(String msgName) {
        this.msgName = msgName;
    }

    public String getMsgName() {
        return msgName;
    }
}

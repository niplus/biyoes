package com.biyoex.app.common.bean;

/**
 * Created by xxx on 2018/8/8.
 */

public class RequestResult<T> {
    private int code;
    private T data;
    private int totalCount;
    private int page;
    private String msg;

    //活动中才用到系统时间
    private long serverTime;

    public RequestResult(int code, T data) {
        this.code = code;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getServerTime() {
        return serverTime;
    }

    public void setServerTime(long serverTime) {
        this.serverTime = serverTime;
    }

    @Override
    public String toString() {
        return "RequestResult{" +
                "code=" + code +
                ", data=" + data +
                ", totalCount=" + totalCount +
                ", page=" + page +
                ", msg='" + msg + '\'' +
                '}';
    }
}

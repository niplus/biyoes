package com.biyoex.app.common.okhttp.bean;

import java.io.Serializable;

/**
 * Created by LG on 2017/3/26.
 */

public class ResultModel<T> implements Serializable{
    private static final long serialVersionUID = 5825649609421945811L;

    String code;

    String message;

    String leftCount;

    T data;

    int pageCount;

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLeftCount() {
        return leftCount;
    }

    public void setLeftCount(String leftCount) {
        this.leftCount = leftCount;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

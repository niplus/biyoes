package com.biyoex.app.my.bean;

/**
 * Created by LG on 2017/6/15.
 */

public class UserinfoBean {

    private ZGSessionInfoBean sessionInfoBean;
    private FrontSessionBean frontSessionBean;

    public ZGSessionInfoBean getSessionInfoBean() {
        return sessionInfoBean;
    }

    public void setSessionInfoBean(ZGSessionInfoBean sessionInfoBean) {
        this.sessionInfoBean = sessionInfoBean;
    }

    public FrontSessionBean getFrontSessionBean() {
        return frontSessionBean;
    }

    public void setFrontSessionBean(FrontSessionBean frontSessionBean) {
        this.frontSessionBean = frontSessionBean;
    }

    @Override
    public String toString() {
        return "UserinfoBean{" +
                ", sessionInfoBean=" + sessionInfoBean +
                ", frontSessionBean=" + frontSessionBean +
                '}';
    }
}

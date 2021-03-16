package com.biyoex.app.my.bean;

/**
 * Created by xxx on 2018/7/9.
 */

public class NoSessionEvent {
    private boolean isNoSession;

    public NoSessionEvent(boolean isNoSession) {
        this.isNoSession = isNoSession;
    }

    public boolean isNoSession() {
        return isNoSession;
    }

    public void setNoSession(boolean noSession) {
        isNoSession = noSession;
    }
}

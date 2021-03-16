package com.biyoex.app.home.bean;

/**
 * Created by LG on 2017/7/12.
 */

public class UploadApkBean {

    /**
     * version : 1.0.0
     * url : http://chudong.oss-cn-shenzhen.aliyuncs.com/upload/android/version.json
     * description
     * forcedUpdate : false
     */

    private String version;
    private String url;
    private String description;
    private boolean forcedUpdate;
    private int forceVersionCode;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isForcedUpdate() {
        return forcedUpdate;
    }

    public void setForcedUpdate(boolean forcedUpdate) {
        this.forcedUpdate = forcedUpdate;
    }

    public int getForceVersionCode() {
        return forceVersionCode;
    }

    public void setForceVersionCode(int forceVersionCode) {
        this.forceVersionCode = forceVersionCode;
    }
}

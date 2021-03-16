package com.biyoex.app.home.bean;

import android.widget.ImageView;

/**
 * Created by xxx on 2018/6/12.
 */

public class BannerBean{
    private String value;
    private String key;
    private String url;
    private transient ImageView imageView;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

}

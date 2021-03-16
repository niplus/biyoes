package com.biyoex.app.my.view;

import com.biyoex.app.common.mvpbase.BaseView;

public interface BindPayView extends BaseView {
    void showChoosePic(String path);
    void bindSuccess();
    void bindFailed();
}

package com.biyoex.app.common.mvpbase;

import android.content.Context;
import androidx.annotation.StringRes;

/**
 * Created by xxx on 2018/8/7.
 */

public interface BaseView {
    void showToast(String msg);
    void showToast(@StringRes int res);
    void showLoadingDialog();
    void hideLoadingDialog();
    int getLayoutId();
    Context getContext();
    void httpError();
}

package com.biyoex.app.common.mvpbase;

import androidx.lifecycle.LifecycleOwner;

/**
 * Created by xxx on 2018/8/7.
 */

public class BasePresent<T extends BaseView> {
    protected T mView;
    protected LifecycleOwner lifecycleOwner;

    public void attachView(BaseView view){
        this.mView = (T) view;
    }

    public void detachView(){
        this.mView = null;
    }

    public void getLifeCycle(LifecycleOwner lifecycleOwner){
        this.lifecycleOwner = lifecycleOwner;
    }

    public void unBindLifeCycle(){
        this.lifecycleOwner =null;
    }
}

package com.biyoex.app.my.view

import androidx.lifecycle.LifecycleOwner
import com.biyoex.app.common.mvpbase.BaseView
import com.biyoex.app.common.bean.GoogleSecret

public interface BindGoogleView : BaseView, LifecycleOwner {

    fun getGoogleSuccess(mdata: GoogleSecret)

    fun bindGoogleSuccess()
}
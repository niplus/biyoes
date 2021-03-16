package com.biyoex.app.trading.persenter

import androidx.lifecycle.Lifecycle
import androidx.appcompat.app.AppCompatActivity
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.kotlin.autoDisposable
import com.biyoex.app.common.bean.CoinInfoBean
import com.biyoex.app.common.bean.RequestResult
import com.biyoex.app.common.http.RetrofitHelper
import com.biyoex.app.common.mvpbase.BaseObserver
import com.biyoex.app.common.mvpbase.BasePresent
import com.biyoex.app.trading.view.CoinDetailsView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


/**
 * time = 2019/9/10 0010
 * CreatedName =
 */
class CoinDetailsPresenter : BasePresent<CoinDetailsView>() {

    fun getCoinInfo(id: String) {
    if(mView!=null){
        RetrofitHelper.getIns().zgtopApi
                .getCoinInfo(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .autoDisposable(AndroidLifecycleScopeProvider.from(mView?.context as AppCompatActivity, Lifecycle.Event.ON_PAUSE))//OnDestory时自动解绑
                .subscribe(object : BaseObserver<RequestResult<CoinInfoBean>>() {
                    override fun success(t: RequestResult<CoinInfoBean>) {
                        t?.data?.let { mView.getCoinInfo(it) }
                    }

                })
    }

    }
}
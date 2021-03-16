package com.biyoex.app.market.presenter

import androidx.lifecycle.Lifecycle
import androidx.appcompat.app.AppCompatActivity
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.kotlin.autoDisposable
import com.biyoex.app.common.http.RetrofitHelper
import com.biyoex.app.common.mvpbase.BaseObserver
import com.biyoex.app.common.mvpbase.BasePresent
import com.biyoex.app.market.view.NowMarketView
import com.biyoex.app.trading.bean.MarketRefreshBean
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class NowMarketPersenter : BasePresent<NowMarketView>() {
    /**
     * 币种交易信息
     */
    fun requestMarketRefresh(symbol: String) {
        RetrofitHelper
                .getIns().zgtopApi
                .getCoinMarketInfo(symbol, "${System.currentTimeMillis()}", "4")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .autoDisposable(AndroidLifecycleScopeProvider.from(mView.context as AppCompatActivity, Lifecycle.Event.ON_PAUSE))//OnDestory时自动解绑
                .subscribe(object : BaseObserver<MarketRefreshBean>() {
                    override fun success(response: MarketRefreshBean) {
                        mView.getCoinMarketInfoSuccess(response!!.recentDealList)
                    }
                })
    }

}
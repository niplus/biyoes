package com.biyoex.app.market.presenter


import androidx.lifecycle.Lifecycle
import androidx.appcompat.app.AppCompatActivity
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.kotlin.autoDisposable
import com.biyoex.app.common.bean.RequestResult
import com.biyoex.app.common.http.RetrofitHelper
import com.biyoex.app.common.mvpbase.BaseObserver
import com.biyoex.app.common.mvpbase.BasePresent
import com.biyoex.app.common.bean.RushBuyBean
import com.biyoex.app.common.bean.RushBuyListData
import com.biyoex.app.market.view.CoinRushBuyView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CoinRushBuyPresenter : BasePresent<CoinRushBuyView>() {
    fun getRushBuyData() {
        mView.showLoadingDialog()
        RetrofitHelper.getIns().zgtopApi
                .buyNb
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .autoDisposable(AndroidLifecycleScopeProvider.from(mView.context as AppCompatActivity, Lifecycle.Event.ON_DESTROY))//OnDestory时自动解绑
                .subscribe(object : BaseObserver<RequestResult<RushBuyBean>>() {
                    override fun success(t: RequestResult<RushBuyBean>) {
//                        mView.hideLoadingDialog()
                        mView.getRushBuySuccess(t!!.data)
                    }

                    override fun failed(code: Int, data: String?, msg: String?) {
                        super.failed(code, data, msg)
                        mView.httpError()
                    }
                })
    }

    fun postRushBuy(grade: Int) {
        mView.showLoadingDialog()
        RetrofitHelper.getIns().zgtopApi
                .postBuyNb(grade)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .autoDisposable(AndroidLifecycleScopeProvider.from(mView.context as AppCompatActivity, Lifecycle.Event.ON_DESTROY))//OnDestory时自动解绑
                .subscribe(object : BaseObserver<RequestResult<String>>() {
                    override fun success(t: RequestResult<String>) {
//                        mView.hideLoadingDialog()
                        mView.showToast("抢购成功")
                        getRushBuyRecord(1, 20)
//                        getRushBuyData()
                    }

                    override fun failed(code: Int, data: String?, msg: String?) {
                        super.failed(code, data, msg)
                        if (code == -102) {
//                            mView.hideLoadingDialog()
                            mView.getRushBuyError(msg!!)

                        } else {
//                            mView.hideLoadingDialog()
                            mView.showToast(msg)
                            mView.httpError()
                        }
//                        mView.hideLoadingDialog()
//                        mView.showToast(msg)
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        mView.httpError()
                    }
                })
    }

    fun getRushBuyRecord(page: Int, pageSize: Int) {
        mView.showLoadingDialog()
        RetrofitHelper.getIns().zgtopApi.getNBList(page, pageSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .autoDisposable(AndroidLifecycleScopeProvider.from(mView.context as AppCompatActivity, Lifecycle.Event.ON_DESTROY))//OnDestory时自动解绑
                .subscribe(object : BaseObserver<RequestResult<MutableList<RushBuyListData>>>() {
                    override fun success(t: RequestResult<MutableList<RushBuyListData>>) {
//                        var jsonObject = JSONArray(t)
//                        var gson = Gson()
//                        val data = Gson().fromJson<MutableList<RushBuyListData>>(t!!.data, object : TypeToken<MutableList<RushBuyListData>>() {}.type)
                        getRushBuyData()
                        mView.getRushBuyListSuccess(t!!.data, page, pageSize)
                    }

                    override fun failed(code: Int, data: String?, msg: String?) {
                        super.failed(code, data, msg)
                        mView.httpError()
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        mView.httpError()
                    }
                })
    }
}
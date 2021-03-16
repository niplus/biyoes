package com.biyoex.app.home.presenter

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.kotlin.autoDisposable
import com.biyoex.app.common.bean.*
import com.biyoex.app.common.data.AppData
import com.biyoex.app.common.http.RetrofitHelper
import com.biyoex.app.common.mvpbase.BaseObserver
import com.biyoex.app.common.mvpbase.BasePresent
import com.biyoex.app.home.view.CoinPosView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CoinPosPresenter : BasePresent<CoinPosView>() {

    //加入POS
    fun addPos(amount: String, coinId: Int) {
//        mView.showLoadingDialog()
        RetrofitHelper.getIns().zgtopApi
                .addPos(AppData.getRoleId(), coinId, amount)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .autoDisposable(AndroidLifecycleScopeProvider.from(mView.context as AppCompatActivity, Lifecycle.Event.ON_DESTROY))//OnDestory时自动解绑
                .subscribe(object : BaseObserver<RequestResult<Any>>(mView.context as AppCompatActivity) {
                    override fun success(t: RequestResult<Any>) {
//                    mView.hideLoadingDialog()
                        mView.showToast("加入成功")
                        getCoinData(coinId)
                    }

                    override fun failed(code: Int, data: String?, msg: String?) {
                        super.failed(code, data, msg)
                        if (code != 200) {
                            mView.showToast(msg)
                        }
//                        mView.hideLoadingDialog()
//                        when (code) {
//                            -100 -> mView.showToast("请输入参与数量")
//                            -300 -> mView.showToast(msg)
//                            -400 -> mView.showToast("余额不足，请先充值")
//                            -200 -> mView.showToast("系统异常，请稍后重试")
//                            else -> {
//                                if (msg.isNullOrEmpty()) {
//                                    mView.showToast("未知错误，请重试")
//                                } else {
//                                    mView.showToast(msg)
//                                }
//                            }
//                        }
                    }
                })
    }

    //退出pos
    fun cancelPos(coinId: Int) {
//        mView.showLoadingDialog()
        RetrofitHelper.getIns().zgtopApi
                .cancelPos(AppData.getRoleId(), coinId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .autoDisposable(AndroidLifecycleScopeProvider.from(mView.context as AppCompatActivity, Lifecycle.Event.ON_DESTROY))//OnDestory时自动解绑
                .subscribe(object : BaseObserver<RequestResult<Any>>() {
                    override fun success(t: RequestResult<Any>) {
//                        mView.hideLoadingDialog()
                        getCoinData(coinId)
                    }

                    override fun failed(code: Int, data: String?, msg: String?) {
                        super.failed(code, data, msg)
//                        mView.hideLoadingDialog()
                        when (code) {
                            -1001 -> mView.showToast("您还未参与")
                            else -> {
                                if (msg.isNullOrEmpty()) {
                                    mView.showToast("未知错误，请重试")
                                } else {
                                    mView.showToast(msg)
                                }
                            }
                        }
                    }
                })

    }

    //获取当前币种pos详情
    fun getCoinData(coinId: Int) {
        mView.showLoadingDialog()
        RetrofitHelper.getIns().zgtopApi
                .getPosEarningsHome(AppData.getRoleId(), coinId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .autoDisposable(AndroidLifecycleScopeProvider.from(mView.context as AppCompatActivity, Lifecycle.Event.ON_DESTROY))//OnDestory时自动解绑
                .subscribe(object : BaseObserver<RequestResult<CoinPosBean>>() {
                    override fun success(t: RequestResult<CoinPosBean>) {
                        mView.hideLoadingDialog()
                        mView.getCoinData(t!!.data)
                    }

                    override fun failed(code: Int, data: String?, msg: String?) {
                        super.failed(code, data, msg)
                        mView.hideLoadingDialog()
                    }
                })
    }

    //获得最近一周的收益
    fun getWeekEarNing(coinId: Int) {
        RetrofitHelper.getIns().zgtopApi.getWeekEarnings(coinId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .autoDisposable(AndroidLifecycleScopeProvider.from(mView.context as AppCompatActivity, Lifecycle.Event.ON_DESTROY))//OnDestory时自动解绑
                .subscribe(object : BaseObserver<RequestResult<MutableList<MutableList<String>>>>() {
                    override fun success(t: RequestResult<MutableList<MutableList<String>>>) {
                        mView.getWeekEarnings(t.data)
                        mView.hideLoadingDialog()
                    }

                })
    }

    //持币收益率k线图
    fun getHoldRateProfit(coinId: Int) {
        RetrofitHelper.getIns().zgtopApi
                .getPosHoldRateProfit(AppData.getRoleId(), coinId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : BaseObserver<RequestResult<MutableList<PosHoldRateProfitBean>>>() {
                    override fun success(t: RequestResult<MutableList<PosHoldRateProfitBean>>) {
                        mView.getHoldRateProfit(t!!.data)
                    }

                    override fun failed(code: Int, data: String?, msg: String?) {
                        super.failed(code, data, msg)
                        mView.showToast(msg)
                    }
                })
    }

    //最近一周的曲线增长图
    fun getWeekIncomeCurves(coinId: Int) {
        RetrofitHelper.getIns().zgtopApi
                .getPosWeekIncomeCurves(AppData.getRoleId(), coinId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : BaseObserver<RequestResult<MutableList<MutableList<String>>>>() {
                    override fun success(t: RequestResult<MutableList<MutableList<String>>>) {
                        if (t!!.data.size > 0) {
                            mView.getWeekIncomeCurves(t!!.data)
                        }
                    }

                    override fun failed(code: Int, data: String?, msg: String?) {
                        super.failed(code, data, msg)
                        mView.showToast(msg)
                    }
                })
    }

    //是否开启自动复投
    fun postAutoRecharge(coinId: Int) {
        mView.showLoadingDialog()
        RetrofitHelper.getIns().zgtopApi
                .getAutoRecharge(AppData.getRoleId(), coinId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .autoDisposable(AndroidLifecycleScopeProvider.from(mView.context as AppCompatActivity, Lifecycle.Event.ON_DESTROY))//OnDestory时自动解绑
                .subscribe(object : BaseObserver<RequestResult<Int>>() {
                    override fun success(t: RequestResult<Int>) {
                        getCoinData(coinId)
                    }

                    override fun failed(code: Int, data: String?, msg: String?) {
                        super.failed(code, data, msg)
                        mView.hideLoadingDialog()
                        mView.showToast(msg)
                    }
                })
    }
}
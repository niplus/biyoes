package com.biyoex.app.market.presenter

import android.app.Activity
import androidx.lifecycle.Lifecycle
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.kotlin.autoDisposable
import com.biyoex.app.R
import com.biyoex.app.common.bean.DepthBean
import com.biyoex.app.common.bean.RequestResult
import com.biyoex.app.common.http.RetrofitHelper
import com.biyoex.app.common.mvpbase.BaseObserver
import com.biyoex.app.common.mvpbase.BasePresent
import com.biyoex.app.common.utils.SharedPreferencesUtils
import com.biyoex.app.common.utils.log.Log
import com.biyoex.app.market.view.MarketView
import com.biyoex.app.trading.bean.MarketRealBean
import com.biyoex.app.trading.bean.MarketRefreshBean
import com.biyoex.app.trading.bean.RefreshUserInfoBean
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Double
import java.util.ArrayList

class MarketPresenter(var mContext: Context) : BasePresent<MarketView>() {
    var totalSell = 0.0;
    var maxSell = 0.0;
    var totalBuy = 0.0
    var maxBuy = 0.0
    var sellDepthList = mutableListOf<MutableList<String>>()
    var buyDepthList = mutableListOf<MutableList<String>>()

    //撤销当前所有委托
    fun cancelAllOrder(sytmol: Int) {
        mView.showLoadingDialog()
        RetrofitHelper.getIns().zgtopApi
                .cancelAllOrder(sytmol)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .autoDisposable(AndroidLifecycleScopeProvider.from(mView.context as AppCompatActivity, Lifecycle.Event.ON_DESTROY))//OnDestory时自动解绑
                .subscribe(object : BaseObserver<RequestResult<Any>>() {
                    override fun success(t: RequestResult<Any>) {
//                        Log.e("撤销成功")
                        mView.hideLoadingDialog()
                        mView.marketCancelOrderSuccess()
                        getRefreshUserInfo(sytmol)
                        requestMarketRefresh("$sytmol")
                    }
                    override fun failed(code: Int, data: String?, msg: String?) {
//                        Log.e("撤销失败")
                        super.failed(code, data, msg)
                        mView.showToast(msg)
                        mView.hideLoadingDialog()
                    }

                })
    }

    //刷新用户信息
    fun getRefreshUserInfo(sytmol: Int) {
//        Log.e("刷新用户信息")
        RetrofitHelper.getIns().zgtopApi
                .getMarketUserInfo(sytmol, "${System.currentTimeMillis()}")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .autoDisposable(AndroidLifecycleScopeProvider.from(mView.context as AppCompatActivity, Lifecycle.Event.ON_DESTROY))//OnDestory时自动解绑
                .subscribe(object : BaseObserver<RefreshUserInfoBean>() {
                    override fun success(t: RefreshUserInfoBean) {
                        if (t != null) {
//                            mView.showToast(R.string.title)
                            mView.getMarketUserInfoSuccess(t)

                        } else {
//                            mView.showToast("请求失败")
                        }
                    }
                })
    }

    /**
     * 币种交易信息
     */
    fun requestMarketRefresh(symbol: String) {
        RetrofitHelper
                .getIns().zgtopApi
                .getCoinMarketInfo(symbol, "${System.currentTimeMillis()}", "4")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .autoDisposable(AndroidLifecycleScopeProvider.from(mView.context as AppCompatActivity, Lifecycle.Event.ON_DESTROY))//OnDestory时自动解绑
                .subscribe(object : BaseObserver<MarketRefreshBean>() {
                    override fun success(response: MarketRefreshBean) {
                        val buydata = ArrayList<DepthBean>()
                        val sellData = ArrayList<DepthBean>()
                        //把卖出的交易数据变成倒序
                        totalSell = 0.0
                        maxSell = 0.0
                        sellDepthList.clear()
                        buyDepthList.clear()
                        //得到卖出的交易信息并且添加到list集合中
//                            val sellDepthList = response!!.sellDepthList
                        for (i in 0 until response!!.sellDepthList.size) {
                            if (i >= 6) {
                                break
                            }
                            val data = response.sellDepthList[i] + ""
                            val jsonObject = JSONArray(data)
                            val volume = jsonObject.getDouble(1)
                            sellData.add(DepthBean(jsonObject.getDouble(0), volume))

                            sellDepthList.add(0, response.sellDepthList[i])
                            totalSell += volume
                            if (volume > maxSell) {
                                maxSell = volume
                            }
                        }
                        totalBuy = 0.0
                        maxBuy = 0.0
                        //得到买入信息并且用循环加入到list集合当中
                        if (response.buyDepthList.isEmpty()) {

                        }
                        for (i in 0 until response.buyDepthList.size) {
                            if (i >= 6) {
                                break
                            }
                            val data = response.buyDepthList[i] + ""
                            val jsonObject = JSONArray(data)
                            val volume = jsonObject.getDouble(1)
                            buydata.add(DepthBean(jsonObject.getDouble(0), volume))
                            buyDepthList.add(response.buyDepthList[i])
                            totalBuy += volume
                            if (volume > maxBuy) {
                                maxBuy = volume
                            }
                        }
//                        Log.e("${sellDepthList.size}${mContext.getString(R.string.origin_size)}")
                        mView.getCoinMarketBuyData(buyDepthList, totalBuy, maxBuy)
                        mView.getCoinMarketInfoData(sellDepthList, totalSell, maxSell, 0)
                        mView.getCoinMarketInfoSuccess(response.recentDealList)
                    }
                })
    }

    //上传用户买单
    fun postUserBuySubmit(symbol: Int, tradeAmount: String, tradeCnyPrice: String, password: String) {
        RetrofitHelper.getIns().zgtopApi
                .requestBuyBtcSubmit(symbol, tradeAmount, tradeCnyPrice, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .autoDisposable(AndroidLifecycleScopeProvider.from(mView.context as AppCompatActivity, Lifecycle.Event.ON_DESTROY))//OnDestory时自动解绑
                .subscribe(object : BaseObserver<String>() {
                    override fun success(t: String) {
                        var jsonObject = JSONObject(t)
                        if (t == null) {
                            mView.showToast(mContext.getString(R.string.no_data))
                        }
                        val resultCode = jsonObject!!.getString("resultCode")
                        Log.i("requestBuyBtcSubmit result :$resultCode")
                        mView.hideLoadingDialog()
                        if (resultCode == "0") {
                            //                            ToastUtils.showToast(getString(R.string.make_order_success));
//                            showToast(R.string.make_order_success)
                            mView.showToast(mContext.getString(R.string.make_order_success))
                            mView.postUserBuyOrderSuccess()
                        } else if (resultCode == "-5") {
                            mView.marketUserNoPassword()
                        } else {
                            if (resultCode == "-1" || resultCode == "-3" || resultCode == "-35") {
                                mView.showToast(transactionMessage(resultCode, jsonObject.getString("value")))
                            } else if (resultCode == "-999") {
                                mView.showToast(jsonObject.getString("data"))
                            } else {
                                mView.showToast(transactionMessage(resultCode, ""))
                            }
                        }
                    }

                })
    }

    /**
     * 获取币种信息
     * */
    fun getCoinData(symbol: Int) {
//        mView.showLoadingDialog()
        RetrofitHelper.getIns().zgtopApi
                .getCoinData(symbol, "${System.currentTimeMillis()}")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .autoDisposable(AndroidLifecycleScopeProvider.from(mView.context as AppCompatActivity, Lifecycle.Event.ON_DESTROY))//OnDestory时自动解绑
                .subscribe(object : BaseObserver<MarketRealBean>() {
                    override fun success(t: MarketRealBean) {
//                        mView.hideLoadingDialog()
                        mView.getCoinData(t!!)
                    }

                    override fun failed(code: Int, data: String?, msg: String?) {
                        super.failed(code, data, msg)
//                        mView.hideLoadingDialog()
                    }
                })
    }

    /**
     * 提交用户卖出
     * */
    fun postUserSellSubmit(symbol: Int, tradeAmount: String, tradeCnyPrice: String, password: String) {
        RetrofitHelper.getIns().zgtopApi
                .requestSellBtcSubmit(symbol, tradeAmount, tradeCnyPrice, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .autoDisposable(AndroidLifecycleScopeProvider.from(mView.context as AppCompatActivity, Lifecycle.Event.ON_DESTROY))//OnDestory时自动解绑
                .subscribe(object : BaseObserver<String>() {
                    override fun success(t: String) {
                        var jsonObject = JSONObject(t)
                        if (t == null) {
                            mView.showToast(mContext.getString(R.string.no_data))
                        }
                        val resultCode = jsonObject!!.getString("resultCode")
                        Log.i("requestBuyBtcSubmit result :$resultCode")
                        mView.hideLoadingDialog()
                        if (resultCode == "0") {
//                            showToast(R.string.make_order_success)
                            mView.showToast(mContext.getString(R.string.make_order_success))
                            mView.postUserBuyOrderSuccess()
                        } else if (resultCode == "-5") {
                            mView.marketUserNoPassword()
                        } else {
                            if (resultCode == "-1" || resultCode == "-3" || resultCode == "-35") {
                                mView.showToast(transactionMessage(resultCode, jsonObject.getString("value")))
                            } else if (resultCode == "-999") {
                                mView.showToast(transactionMessage(resultCode, jsonObject.getString("data")))
                            } else {
                                mView.showToast(transactionMessage(resultCode, jsonObject.getString("data")))
                            }
                        }
                    }
                    override fun onError(e: Throwable) {
                        super.onError(e)
                        mView.hideLoadingDialog()

                    }
                })
    }


    /**
     * 交易提示语
     *
     * @param resultCode
     * @return
     */
    fun transactionMessage(resultCode: String, value: String): String {
        var messag = ""
        when (resultCode) {
            "-1" -> messag = "${mContext.getString(R.string.message_min_volume)} $value"
            "-3" -> messag = "${mContext.getString(R.string.message_min_amount)} $value"
            "-35" -> messag = "${mContext.getString(R.string.message_max_amount)} $value"
            "-100", "-101" -> mContext.getString(R.string.message_not_open_trade)
            "-400" -> messag = mContext.getString(R.string.message_not_trade_time)
            "-4" -> messag = mContext.getString(R.string.message_not_enough_balance)
            "-50" -> messag = mContext.getString(R.string.message_trade_pass_empty)
            "-2" -> messag = mContext.getString(R.string.message_pass_error)
            "-200" -> messag = mContext.getString(R.string.message_wallet_exception)
            "-900" -> messag = mContext.getString(R.string.message_trade_price_error)
            "-901" -> messag = "${mContext.getString(R.string.sellresult_error)}"
            "-999" -> messag = value
        }
        if (messag == "") {
            messag = mContext.getString(R.string.net_error)
        }
        return messag
    }

    //处理卖单逻辑
    fun getSellData(mContext: Activity, response: String) {
        if(mView==null) return
        mContext.runOnUiThread {
            if (sellDepthList.size > 0) {
                sellDepthList.clear()
            }
            val selldata = ArrayList<DepthBean>()
            val data = Gson().fromJson<MutableList<MutableList<String>>>(response, object : TypeToken<MutableList<MutableList<String>>>() {
            }.type)

            totalSell = 0.0
            maxSell = 0.0
            for (i in data.indices) {
                if (i >= 6) {
                    break
                }
                sellDepthList.add(0, data[i])
                val volume = Double.valueOf(data[i][1])!!
                selldata.add(DepthBean(Double.valueOf(data[i][0])!!, volume))
                totalSell += volume

                if (volume > maxSell) {
                    maxSell = volume
                }
            }
            mView?.getCoinMarketInfoData(sellDepthList, totalSell, maxSell, 1)
        }
    }

    //处理买单逻辑
    fun getBuyDaata(mContext: Activity, response: String) {
        if(mView==null){
            return
        }
        mContext.runOnUiThread {
            if (buyDepthList.size > 0) {
                buyDepthList.clear()
            }
            val buydata = ArrayList<DepthBean>()
            val data = Gson().fromJson<MutableList<MutableList<String>>>(response, object : TypeToken<MutableList<MutableList<String>>>() {
            }.type)

            totalBuy = 0.0
            maxBuy = 0.0
            for (i in data.indices) {
                if (i >= 6) {
                    break
                }
                buyDepthList.add(data[i])
                val volume = Double.valueOf(data[i][1])!!
                buydata.add(DepthBean(Double.valueOf(data[i][0])!!, volume))
                totalBuy += volume
                if (volume > maxBuy) {
                    maxBuy = volume
                }
            }
            mView.getCoinMarketBuyData(buyDepthList, totalBuy, maxBuy)
        }
    }

    fun requestCancelCollection(mString: Int) {
        var coins = SharedPreferencesUtils.getInstance().getString("coin_id", "")
        if (coins.contains(",$mString,")) {
            coins = coins.replace(",$mString,", ",")
        }
        SharedPreferencesUtils.getInstance().saveString("coin_id", coins)
//        ToastUtils.showToast(getString(R.string.cancel_collect_success));
//        mView.showToast(mContext.resources.getString(R.string.cancel_collect_success))
    }

    fun requestAddCollection(mId: Int) {
        val coin = SharedPreferencesUtils.getInstance().getString("coin_id", "")
        if (coin == "") {
            SharedPreferencesUtils.getInstance().saveString("coin_id", ",$mId,")
        } else {
            SharedPreferencesUtils.getInstance().saveString("coin_id", "$coin$mId,")
        }

//        ToastUtils.showToast(getString(R.string.add_collection_success))
    }
}
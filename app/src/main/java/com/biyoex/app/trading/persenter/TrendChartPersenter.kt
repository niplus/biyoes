package com.biyoex.app.trading.persenter

import android.app.Activity
import androidx.lifecycle.Lifecycle

import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.github.fujianlian.klinechart.KLineEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.kotlin.autoDisposable
import com.biyoex.app.R
import com.biyoex.app.common.activity.LoginActivity
import com.biyoex.app.common.bean.DepthBean
import com.biyoex.app.common.bean.RequestResult
import com.biyoex.app.common.http.RetrofitHelper
import com.biyoex.app.common.mvpbase.BaseObserver
import com.biyoex.app.common.mvpbase.BasePresent
import com.biyoex.app.common.utils.DateUtilsl
import com.biyoex.app.common.utils.SharedPreferencesUtils
import com.biyoex.app.common.utils.log.Log
import com.biyoex.app.trading.bean.MarketRealBean
import com.biyoex.app.trading.bean.MarketRefreshBean
import com.biyoex.app.trading.bean.RefreshUserInfoBean
import com.biyoex.app.trading.view.TrendChartView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONArray
import java.util.ArrayList

class TrendChartPersenter : BasePresent<TrendChartView>() {
    var totalSell = 0.0
    var maxSell = 0.0
    var totalBuy = 0.0
    var maxBuy = 0.0
    var sellDepthList = mutableListOf<MutableList<String>>()
    var buyDepthList = mutableListOf<MutableList<String>>()
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
                .subscribe(object : BaseObserver<MarketRefreshBean>(mView.context as AppCompatActivity) {
                    override fun success(response: MarketRefreshBean) {
                        val buydata = ArrayList<DepthBean>()
                        val sellData = ArrayList<DepthBean>()
                        //把卖出的交易数据变成倒序
                        buyDepthList.clear()
                        sellDepthList.clear()
                        totalSell = 0.0
                        maxSell = 0.0
                        //得到卖出的交易信息并且添加到list集合中
//                            val sellDepthList = response!!.sellDepthList
                        for (i in 0 until response!!.sellDepthList.size) {
                            if (i >= 30) {
                                break
                            }
                            val data = response.sellDepthList[i] + ""
                            val jsonObject = JSONArray(data)
                            val volume = jsonObject.getDouble(1)
                            sellData.add(DepthBean(jsonObject.getDouble(0), volume))

                            //卖一价作为推荐买入价格
                            //                            if (i == 0 && Double.valueOf(strRecommendedPrice) == 0) {
                            //                                strRecommendedPrice = MoneyUtils.decimalByUp(coin.getPriceDecimals(), new BigDecimal(jsonObject.getDouble(0))).toString();
                            //                                if (isBuy){
                            //                                    edPrice.setText(strRecommendedPrice);
                            //                                }
                            //                            }

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
                            if (i >= 30) {
                                break
                            }
                            val data = response.buyDepthList[i] + ""
                            val jsonObject = JSONArray(data)
                            val volume = jsonObject.getDouble(1)
                            buydata.add(DepthBean(jsonObject.getDouble(0), volume))
                            //                            买一价作为推荐卖出价格
                            //                            if (i == 0 && Double.valueOf(strSellOutPrice) == 0) {
                            //                                strSellOutPrice = MoneyUtils.decimalByUp(coin.getPriceDecimals(), new BigDecimal(jsonObject.getDouble(0))).toString();
                            //                                if (!isBuy){
                            //                                    edPrice.setText(strSellOutPrice);
                            //                                }
                            //                            }

                            buyDepthList.add(response.buyDepthList[i])

                            totalBuy += volume
                            if (volume > maxBuy) {
                                maxBuy = volume
                            }
                        }
                        sellDepthList.reverse()
                        mView.getCoinMarketBuyData(buyDepthList, totalBuy, maxBuy)
                        mView.getCoinMarketInfoData(sellDepthList, totalSell, maxSell)
                        mView.getCoinMarketInfoSuccess(response.recentDealList)
                    }

                })
    }

    /**
        * 获取币种K线图
        * */
        fun requestKlineInfo(step: String, symbol: String) {
//        KlineLiveData.getIns(mView.context).getKlineData(step, symbol)
//        KlineLiveData.getIns(mView.context).observe(mView.context as TrendChartNewActivity, Observer {
//            if (it != null) {
//                mView.getCoinNewKlineData(it)
//            }
//        })
//c
        mView.showLoadingDialog()
        RetrofitHelper
                .getIns().zgtopApi
                .getCoinKlineData(step, symbol)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .autoDisposable(AndroidLifecycleScopeProvider.from(mView.context as AppCompatActivity, Lifecycle.Event.ON_DESTROY))//OnDestory时自动解绑
                .subscribe(object : BaseObserver<String>() {
                    @RequiresApi(Build.VERSION_CODES.N)
                    override fun success(response: String) {
                        Log.i("nidongliang response : $response")
                        getUserMarketInfo(symbol.toInt())
                        mView.hideLoadingDialog()
                        var hisDataList: MutableList<KLineEntity>? = mutableListOf()
                        try {
                            val dataJson = JSONArray(response)
//                            //如果初始化没数据就直接返回
//                            if (hisDataList.size() == 0 && dataJson.length() == 0) {
//                                return;
//                            }
                            var recordTime: Long = 0
                            hisDataList!!.clear()
                            for (i in 0 until dataJson.length()) {
                                val jsonArray = dataJson.getJSONArray(i)
                                val open = java.lang.Float.parseFloat(jsonArray.getString(3))
                                val high = java.lang.Float.parseFloat(jsonArray.getString(5))
                                val low = java.lang.Float.parseFloat(jsonArray.getString(6))
                                val close = java.lang.Float.parseFloat(jsonArray.getString(4))
                                val volume = java.lang.Float.parseFloat(jsonArray.getString(7))
                                var lastprice = 0f
                                if (i != 0) {
                                    val jsonArray1 = dataJson.getJSONArray(i - 1)
//                                    val subtract = BigDecimal(jsonArray.getString(4)).subtract(BigDecimal(jsonArray1.getString(4)))
                                    lastprice = java.lang.Float.parseFloat(jsonArray1.getString(4))
                                }
                                val timer = jsonArray.getLong(0) * 1000
                                if (recordTime == timer) {
                                    continue
                                }
//                                Log.e("测试一下噢$low")
                                recordTime = timer
                                val hisData = KLineEntity()
                                hisData.Close = close
                                hisData.Low = low
                                hisData.Volume = volume
                                hisData.Open = open
                                hisData.High = high
                                hisData.Date = DateUtilsl.getKlineDate("$timer")
                                hisData.lasePrice = lastprice
                                hisDataList.add(hisData)
                            }
                            mView.getCoinNewKlineData(hisDataList)
                        } catch (exception: Exception) {

                        }

                    }

                    override fun failed(code: Int, data: String?, msg: String?) {
                        super.failed(code, data, msg)
                        mView.hideLoadingDialog()
                    }
                })
    }

    fun getSellData(mContext: Activity, response: String) {
        if (mView == null) {
            return
        }
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
                if (i >= 15) {
                    break
                }
                sellDepthList.add(0, data[i])
                val volume = java.lang.Double.valueOf(data[i][1])!!
                selldata.add(DepthBean(java.lang.Double.valueOf(data[i][0])!!, volume))
                totalSell += volume

                if (volume > maxSell) {
                    maxSell = volume
                }
            }
            sellDepthList.reverse()
            if (mView != null) mView.getCoinMarketInfoData(sellDepthList, totalSell, maxSell)
        }

    }

    fun getBuyDaata(mContext: Activity, response: String) {
        if (mView == null) {
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
                if (i >= 15) {
                    break
                }
                buyDepthList.add(data[i])
                val volume = java.lang.Double.valueOf(data[i][1])!!
                buydata.add(DepthBean(java.lang.Double.valueOf(data[i][0])!!, volume))
                totalBuy += volume
                if (volume > maxBuy) {
                    maxBuy = volume
                }
            }
            if (mView != null) mView.getCoinMarketBuyData(buyDepthList, totalBuy, maxBuy)
        }
    }

    /**
     * 获取用户数据
     * */
    fun getUserMarketInfo(sytmol: Int) {
        RetrofitHelper.getIns().zgtopApi
                .getMarketUserInfo(sytmol, "${System.currentTimeMillis()}")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .autoDisposable(AndroidLifecycleScopeProvider.from(mView.context as AppCompatActivity, Lifecycle.Event.ON_DESTROY))//OnDestory时自动解绑
                .subscribe(object : BaseObserver<RefreshUserInfoBean>() {
                    override fun success(t: RefreshUserInfoBean) {
                        if (t != null) {
//                            mView.showToast(R.string.title)
                            mView.getUserMarketData(t)

                        } else {
                            mView.showToast("请求失败")
                        }
                    }
                })
    }

    /**
     * 获取币种信息
     * */
    fun getCoinData(symbol: Int) {
        mView?.showLoadingDialog()
        RetrofitHelper.getIns().zgtopApi
                .getCoinData(symbol, "${System.currentTimeMillis()}")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .autoDisposable(AndroidLifecycleScopeProvider.from(mView.context as AppCompatActivity, Lifecycle.Event.ON_DESTROY))//OnDestory时自动解绑
                .subscribe(object : BaseObserver<MarketRealBean>() {
                    override fun success(t: MarketRealBean) {
                        mView.hideLoadingDialog()
                        mView.getCoinData(t!!)
                    }

                    override fun failed(code: Int, data: String?, msg: String?) {
                        super.failed(code, data, msg)
                        mView.hideLoadingDialog()
                    }
                })
    }

    fun updateFonsCoin(type: Boolean, fid: Int) {
        var coinId = SharedPreferencesUtils.getInstance().getString("coin_id", "")
        if (type) {
            coinId = coinId.replace(",$fid,", ",")
            SharedPreferencesUtils.getInstance().saveString("coin_id", coinId)
        } else {
            if (coinId == "") {
                SharedPreferencesUtils.getInstance().saveString("coin_id", ",$fid,")
            } else {
                SharedPreferencesUtils.getInstance().saveString("coin_id", "$coinId$fid,")
            }
        }
//        val SelectCoinId =
        RetrofitHelper.getIns().zgtopApi
                .updateUserSelfToken(SharedPreferencesUtils.getInstance().getString("coin_id", ""))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .autoDisposable(AndroidLifecycleScopeProvider.from(mView.context as AppCompatActivity, Lifecycle.Event.ON_DESTROY))//OnDestory时自动解绑
                .subscribe(object : BaseObserver<RequestResult<Any>>() {
                    override fun success(requestResult: RequestResult<Any>) {
                        if (type) {
                            mView.retrunFons(false)
                            mView.showToast(mView.context.resources.getString(R.string.cancel_collect_success))
                        } else {
                            mView.retrunFons(true)
                            mView.showToast(mView.context.resources.getString(R.string.add_collection_success))
                        }
                    }

                    override fun failed(code: Int, data: String?, msg: String?) {
                        super.failed(code, data, msg)
                        var coinId = SharedPreferencesUtils.getInstance().getString("coin_id", "")
                        if (type) {
                            if (coinId == "") {
                                SharedPreferencesUtils.getInstance().saveString("coin_id", ",$fid,")
                            } else {
                                SharedPreferencesUtils.getInstance().saveString("coin_id", "$coinId$fid,")
                            }
                        } else {
                            coinId = coinId.replace(",$fid,", ",")
                            SharedPreferencesUtils.getInstance().saveString("coin_id", coinId)
                        }
                        if (code == 401) {
                            mView.context.startActivity(Intent(mView.context, LoginActivity::class.java))
                        }
                    }
                })
    }
}
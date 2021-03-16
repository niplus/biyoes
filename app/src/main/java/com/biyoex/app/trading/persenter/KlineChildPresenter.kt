package com.biyoex.app.trading.persenter

import android.app.Activity
import androidx.lifecycle.Lifecycle
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.kotlin.autoDisposable
import com.biyoex.app.common.bean.DepthBean
import com.biyoex.app.common.http.RetrofitHelper
import com.biyoex.app.common.mvpbase.BaseObserver
import com.biyoex.app.common.mvpbase.BasePresent
import com.biyoex.app.common.utils.log.Log
import com.biyoex.app.trading.bean.MarketRefreshBean
import com.biyoex.app.trading.view.KlineChildView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONArray
import java.util.ArrayList

class KlineChildPresenter : BasePresent<KlineChildView>() {
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
//        mView.showLoadingDialog()
        Log.e("kline_childPresenterGo")
        RetrofitHelper
                .getIns().zgtopApi
                .getCoinMarketInfo(symbol, "${System.currentTimeMillis()}", "4")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .autoDisposable(AndroidLifecycleScopeProvider.from(mView.context as AppCompatActivity, Lifecycle.Event.ON_PAUSE))//OnDestory时自动解绑
                .subscribe(object : BaseObserver<MarketRefreshBean>() {
                    override fun success(response: MarketRefreshBean) {
//                        mView.hideLoadingDialog()
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
                            if (i >= 15) {
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
                            if (i >= 15) {
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
                        Log.e("kline_childPresenterSuccess")
                        mView.getCoinMarketBuyData(buyDepthList, totalBuy, maxBuy)
                        mView.getCoinMarketInfoData(sellDepthList, totalSell, maxSell)
                        mView.getCoinMarketInfoSuccess(response.recentDealList)
                    }

                    override fun failed(code: Int, data: String?, msg: String?) {
                        super.failed(code, data, msg)
                        Log.e("kline_childPresenterError")
                    }
                })
    }

    fun getSellData(mContext: Activity, response: String) {

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
            if (mView != null) {
                mView.getCoinMarketInfoData(sellDepthList, totalSell, maxSell)
            }
        }

    }

    fun getBuyDaata(mContext: Activity, response: String) {
        if (mView == null) return
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
            if (mView != null) {
                mView.getCoinMarketBuyData(buyDepthList, totalBuy, maxBuy)
            }
        }
    }

}
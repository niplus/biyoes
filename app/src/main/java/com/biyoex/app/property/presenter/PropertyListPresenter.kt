package com.biyoex.app.property.presenter

import androidx.lifecycle.Lifecycle
import androidx.appcompat.app.AppCompatActivity
import android.text.TextUtils
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.kotlin.autoDisposable
import com.biyoex.app.common.http.RetrofitHelper
import com.biyoex.app.common.mvpbase.BaseObserver
import com.biyoex.app.common.mvpbase.BasePresent
import com.biyoex.app.common.utils.GsonUtil
import com.biyoex.app.home.bean.CoinTradeRankBean
import com.biyoex.app.my.bean.RechargeCoinBean
import com.biyoex.app.property.view.PropertyListView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus
import org.json.JSONObject
import java.util.ArrayList

class PropertyListPresenter : BasePresent<PropertyListView>() {
    private var temp: MutableList<RechargeCoinBean>? = mutableListOf()
    private var totalValue: String? = null
    private var usdtValue: String? = null
    fun requestPersonalFinance(coinName: String, type: Int) {
        RetrofitHelper.getIns().zgtopApi.accountBanlance
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .autoDisposable(AndroidLifecycleScopeProvider.from(mView.context as AppCompatActivity, Lifecycle.Event.ON_DESTROY))//OnDestory时自动解绑
                .subscribe(object : BaseObserver<String>() {
                    override fun success(response: String) {
                        mView?.let {

                                val jsonObject = JSONObject(response)
                                val code = jsonObject.getInt("code")
                                mView.hideLoadingDialog()
                                if (200 == code) {
                                    val data = jsonObject.getJSONObject("data")
                                    val balanceList = data.getString("balanceList")

                                    val rechargeCoinBeen = GsonUtil.jsonToList(balanceList, RechargeCoinBean::class.java)
                                    temp!!.clear()
                                    when (type) {
                                        1 -> for (item in rechargeCoinBeen) {
                                            if (!item.innovate) {
                                                temp!!.add(item)
                                            }
                                        }
                                        2 -> for (item in rechargeCoinBeen) {
                                            if (item.innovate) {
                                                temp!!.add(item)
                                            }
                                        }
                                        else -> temp!!.addAll(rechargeCoinBeen)
                                    }
                                    if (temp!!.size == 0) {
                                        return
                                    }
                                    val tradeInfo = data.getJSONObject("dataMap")
                                    val keys = tradeInfo.keys()
                                    val dealDatasBeanList = ArrayList<CoinTradeRankBean.DealDatasBean>()
                                    while (keys.hasNext()) {
                                        val area = tradeInfo.getString(keys.next())
                                        dealDatasBeanList.addAll(GsonUtil.jsonToList(area, CoinTradeRankBean.DealDatasBean::class.java))
                                    }


                                    temp!!.sortWith(Comparator { o1, o2 -> o1.name.compareTo(o2.name) })
                                    //若是撤单导致的刷新，重新使用eventbus传数据到详情页
                                    if (!TextUtils.isEmpty(coinName)) {
                                        for (rechargeCoinBean in temp!!) {
                                            if (coinName == rechargeCoinBean.name) {
                                                EventBus.getDefault().post(rechargeCoinBean)
                                            }
                                        }
                                    }
                                    usdtValue = data.getString("totalBtcValue")
                                    totalValue = data.getString("totalCnyValaue")
                                    mView.httpSuccess(temp, coinName, totalValue!!, usdtValue!!)
                                } else {
                                    mView.httpErrorView(code)
                                }

                        }
                    }

                    override fun failed(code: Int, data: String?, msg: String?) {
                        super.failed(code, data, msg)
                        mView.httpErrorView(code)
                    }
                })
    }
}
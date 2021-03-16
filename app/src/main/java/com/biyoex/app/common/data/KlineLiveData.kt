package com.biyoex.app.common.data

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import android.content.Context

import com.github.fujianlian.klinechart.KLineEntity
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.kotlin.autoDisposable
import com.biyoex.app.common.http.RetrofitHelper
import com.biyoex.app.common.mvpbase.BaseActivity
import com.biyoex.app.common.mvpbase.BaseObserver
import com.biyoex.app.common.utils.DateUtilsl
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONArray
import org.json.JSONException

class KlineLiveData(var mContext: Context) : LiveData<MutableList<KLineEntity>>() {

    companion object {
        private var ins: KlineLiveData? = null
        fun getIns(mContext: Context): KlineLiveData {
            if (ins == null) {
                ins = KlineLiveData(mContext)
            }
            return ins as KlineLiveData
        }
    }

    override fun setValue(value: MutableList<KLineEntity>?) {
        super.setValue(value)
    }

    fun getKlineData(step: String, symbol: String) {
        RetrofitHelper
                .getIns().zgtopApi
                .getCoinKlineData(step, symbol)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .autoDisposable(AndroidLifecycleScopeProvider.from(mContext as BaseActivity<*>, Lifecycle.Event.ON_DESTROY))//OnDestory时自动解绑
                .subscribe(object : BaseObserver<String>() {
                    override fun success(response: String) {
                        var hisDataList: MutableList<KLineEntity>? = mutableListOf()
                        try {
                            val dataJson = JSONArray(response)

                            //                        //如果初始化没数据就直接返回
                            //                        if (hisDataList.size() == 0 && dataJson.length() == 0){
                            //                            return;
                            //                        }

                            var recordTime: Long = 0
                            hisDataList!!.clear()
                            for (i in 0 until dataJson.length()) {
                                val jsonArray = dataJson.getJSONArray(i)
                                val open = java.lang.Float.parseFloat(jsonArray.getString(3))
                                val high = java.lang.Float.parseFloat(jsonArray.getString(5))
                                val low = java.lang.Float.parseFloat(jsonArray.getString(6))
                                val close = java.lang.Float.parseFloat(jsonArray.getString(4))
                                val volume = java.lang.Float.parseFloat(jsonArray.getString(7))

                                val timer = jsonArray.getLong(0) * 1000
                                if (recordTime == timer) {
                                    continue
                                }
                                recordTime = timer
                                val hisData = KLineEntity()
                                hisData.Close = close
                                hisData.Low = low
                                hisData.Volume = volume
                                hisData.Open = open
                                hisData.High = high
                                hisData.Date = DateUtilsl.getKlineDate("$timer")
                                hisDataList.add(hisData)
                            }
                            value = hisDataList
                        } catch (exception: JSONException) {

                        }

                    }
                })
    }
}
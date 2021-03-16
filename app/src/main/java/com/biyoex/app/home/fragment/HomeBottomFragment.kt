package com.biyoex.app.home.fragment


import android.annotation.SuppressLint
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.View.MeasureSpec
import android.view.ViewGroup
import com.biyoex.app.R
import com.biyoex.app.common.base.RxBus
import com.biyoex.app.common.base.RxBusData

import com.biyoex.app.common.mvpbase.BaseFragment
import com.biyoex.app.common.mvpbase.BasePresent
import com.biyoex.app.common.mvpbase.BaseView
import com.biyoex.app.common.utils.log.Log
import com.biyoex.app.common.widget.MyViewPager
import com.biyoex.app.home.adapter.CurrencyTrendAdapter
import com.biyoex.app.home.bean.CoinTradeRankBean
import com.biyoex.app.home.presenter.HomeBottomPresenter
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_home_bottom.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * //首頁币种涨跌幅
 *
 */
@SuppressLint("ValidFragment")
class HomeBottomFragment() : BaseFragment<BasePresent<BaseView>>() {
    var listData = mutableListOf<CoinTradeRankBean.DealDatasBean>()
    lateinit var currencyTrendAdapter: CurrencyTrendAdapter
    lateinit var subscribe: Disposable
    var mType: Int = 0
    var myViewPager: MyViewPager? = null
    override fun createPresent(): BasePresent<BaseView> = HomeBottomPresenter() as BasePresent<BaseView>
    override fun getLayoutId(): Int = R.layout.fragment_home_bottom

    constructor(mType: Int, myViewPager: MyViewPager) : this() {
        this.mType = mType
        this.myViewPager = myViewPager
    }

    override fun initComp() {

    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = super.onCreateView(inflater, container, savedInstanceState)
        myViewPager?.setObjectForPosition(rootView, mType)
        

        return rootView
    }

    @SuppressLint("CheckResult")
    override fun initData() {

//        val observable = p_recyclerview.viewTreeObserver
//        observable.addOnGlobalLayoutListener {
//            val height = p_recyclerview.measuredHeight
//            Log.i("nidongliang height : $height")
//        }

        p_recyclerview.isNestedScrollingEnabled = false;
        currencyTrendAdapter = CurrencyTrendAdapter(R.layout.item_home_trend, listData)
        currencyTrendAdapter.setFupanddownState(mType)
        p_recyclerview.layoutManager = LinearLayoutManager(context)
        p_recyclerview.adapter = currencyTrendAdapter
        currencyTrendAdapter.emptyView = View.inflate(context, R.layout.view_empty, null)
        //事件总线，目前只有刷新数据
        subscribe = RxBus.get().toFlowable().map { o -> o as RxBusData }.subscribe { rxBusData ->

            if (rxBusData.msgName == "HomeBottom") {
                var dataList = rxBusData.msgData as List<CoinTradeRankBean.DealDatasBean>
                if (dataList != null) {
                    var result = mutableListOf<CoinTradeRankBean.DealDatasBean>()
                    when (mType) {
                        0 -> {
                            dataList = dataList.sortedByDescending { t1 -> t1.fupanddownweek }
                            dataList = dataList.filter { ti -> ti.fupanddownweek > 0 }
                        }
                        1 -> {
                            dataList = dataList.sortedByDescending { t1 -> t1.fupanddown }
                            dataList = dataList.filter { ti -> ti.fupanddown >= 0 }
                        }
                        2 -> {
                            dataList = dataList.sortedBy { t1 -> t1.fupanddown }
                            dataList = dataList.filter { ti -> ti.fupanddown < 0 }
                        }
                    }
                    if (dataList.size > 10) {
                        dataList = dataList.subList(0, 10)
                    }
                    Log.i("nidongliang home bottom refresh")
//
                    listData.clear()
                    listData.addAll(dataList)
                    currencyTrendAdapter.setNewData(listData)

                    p_recyclerview.measure(0, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED))
                    val height = p_recyclerview.measuredHeight
                    myViewPager!!.initHeight(height)
//                    Log.i("nidongliang measure height: $height")
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        subscribe.dispose()
    }
}

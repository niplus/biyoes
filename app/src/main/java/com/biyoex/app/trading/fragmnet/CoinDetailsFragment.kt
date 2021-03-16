package com.biyoex.app.trading.fragmnet


import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.biyoex.app.R
import com.biyoex.app.common.activity.WebPageLoadingActivity
import com.biyoex.app.common.base.RxBus
import com.biyoex.app.common.base.RxBusData
import com.biyoex.app.common.bean.CoinInfoBean
import com.biyoex.app.common.mvpbase.BaseFragment
import com.biyoex.app.common.widget.MyViewPager
import com.biyoex.app.home.bean.CoinTradeRankBean
import com.biyoex.app.trading.persenter.CoinDetailsPresenter
import com.biyoex.app.trading.view.CoinDetailsView
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_coin_details.*

/**
 * 币种详情介绍。
 */
@SuppressLint("ValidFragment")
class CoinDetailsFragment() : BaseFragment<CoinDetailsPresenter>(), CoinDetailsView {
    override fun getCoinInfo(coinInfoBean: CoinInfoBean) {
        tv_coin_name.text = coinInfoBean.cnName
        tv_coin_price.text = coinInfoBean.pushPrice
        tv_coin_info.text = coinInfoBean.cnDesc
        tv_coin_http.text = coinInfoBean.webSite
        tv_coin_start_time.text = coinInfoBean.pushTime
        tv_coin_sum.text = coinInfoBean.total
        tv_while_book.text = coinInfoBean.whitepaper
    }
    var coinId:String?=null
    var myViewPager:MyViewPager?=null
    var mType:Int = 0
    constructor( coinId: String,  myViewPager: MyViewPager,  mType: Int):this(){
        this.myViewPager = myViewPager
        this.mType = mType
        this.coinId = coinId
    }
    constructor(coinId: String):this(){
        this.coinId = coinId
    }

    override fun initImmersionBar() {
        super.initImmersionBar()
        immersionBar?.statusBarDarkFont(false)?.init()
    }
    private var subscribe: Disposable? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = super.onCreateView(inflater, container, savedInstanceState)
        myViewPager?.setObjectForPosition(rootView, mType)
        return rootView
    }

    override fun getLayoutId(): Int = R.layout.fragment_coin_details

    override fun createPresent(): CoinDetailsPresenter = CoinDetailsPresenter()

    override fun initComp() {
        subscribe = RxBus.get().toFlowable().map {
            o -> o as RxBusData }.subscribe { rxBusData ->
            if (rxBusData.msgName == "KlineData") {
                val dealDatasBean = rxBusData.msgData as CoinTradeRankBean.DealDatasBean?
                dealDatasBean?.let {
                    mPresent?.getCoinInfo("${dealDatasBean.fid}")
                }
            }
        }
        tv_while_book.setOnClickListener {
                        val intent = Intent()
            intent.action = "android.intent.action.VIEW"
//            val content_url =
            intent.data = Uri.parse(tv_while_book.text.toString())
            startActivity(intent)
        }

            tv_coin_http.setOnClickListener {
            if(!tv_coin_http.text.toString().isNullOrEmpty()){
                val itWebPageLoading = Intent(activity, WebPageLoadingActivity::class.java)
                itWebPageLoading.putExtra("url", tv_coin_http.text.toString())
                itWebPageLoading.putExtra("type", "url")
                itWebPageLoading.putExtra("title", getString(R.string.coin_details))
                activity?.startActivity(itWebPageLoading)
            }
                }
    }
    override fun initData() {
      coinId?.let {
          mPresent?.getCoinInfo(it)
      }
    }

}

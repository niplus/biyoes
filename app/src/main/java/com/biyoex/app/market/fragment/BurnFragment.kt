package com.biyoex.app.market.fragment


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.biyoex.app.R
import com.biyoex.app.common.activity.WebPageLoadingActivity
import com.biyoex.app.common.base.RxBus
import com.biyoex.app.common.base.RxBusData
import com.biyoex.app.common.bean.BurnInfoBean
import com.biyoex.app.common.mvpbase.BaseFragment
import com.biyoex.app.common.mvpbase.BasePresent
import com.biyoex.app.common.mvpbase.BaseView
import com.biyoex.app.common.utils.log.Log
import com.biyoex.app.common.widget.MyViewPager
import com.biyoex.app.home.bean.CoinTradeRankBean
import com.biyoex.app.market.presenter.BurnPersenter
import com.biyoex.app.market.view.BurnView
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_burn.*
import java.math.BigDecimal

/**
 * A simple [Fragment] subclass.
 */
class BurnFragment()  : BaseFragment<BasePresent<BaseView>>(),BurnView{
    override fun getLayoutId(): Int = R.layout.fragment_burn
    override fun createPresent(): BasePresent<BaseView>  = BurnPersenter() as BasePresent<BaseView>
    lateinit var subscribe: Disposable
    var mType:Int = 0
    var myViewPager:MyViewPager?=null
    var coin:CoinTradeRankBean.DealDatasBean?=null
    override fun initComp() {
        subscribe = RxBus.get().toFlowable().map { o -> o as RxBusData }.subscribe { rxBusData ->
            if (rxBusData.msgName == "CoinData") {
                coin = rxBusData.msgData as CoinTradeRankBean.DealDatasBean
                textView17?.text =  "${context!!.getString(R.string.destory_total)}${coin!!.fShortName}"
                tv_burn_rate?.text = "${context!!.getString(R.string.burn_rate)}${BigDecimal(coin!!.burnRate*100).setScale(2,BigDecimal.ROUND_DOWN)}%"
                Log.d("是否接收到币种")
                if(coin!!.isBurn){
                    (mPresent as BurnPersenter).getBurnRate(coin!!.fid)
                }
            }
        }
    }

    constructor( mType: Int,  myViewPager: MyViewPager,  coin: CoinTradeRankBean.DealDatasBean):this(){
        this.mType = mType
        this.myViewPager = myViewPager
        this.coin = coin
    }

    override fun initData() {
      coin?.let {
          if(it.isBurn){
              (mPresent as BurnPersenter).getBurnRate(it.fid)
          }
          textView17.text =  "${context!!.getString(R.string.destory_total)}${it.fname}"
          tv_burn_rate.text = "${context!!.getString(R.string.burn_rate)}${BigDecimal(it.burnRate*100).setScale(2,BigDecimal.ROUND_DOWN)}%"
          tv_contract_address.setOnClickListener {
              val intent = Intent(context, WebPageLoadingActivity::class.java)
              intent.putExtra("url", tv_contract_address.text)
              intent.putExtra("type", "url")
              startActivity(intent)
          }
      }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = super.onCreateView(inflater, container, savedInstanceState)
        myViewPager?.setObjectForPosition(rootView, mType)
        return rootView
    }

    override fun getBurnSuccess(data: BurnInfoBean) {
        Log.d("是否切换比重")
       coin?.let {
           textView17?.text = "${context!!.getString(R.string.destory_total)}${BigDecimal(data.realTotal).setScale(2,BigDecimal.ROUND_DOWN)} ${it.fShortName}"
           tv_real_destory?.text = "${context!!.getString(R.string.real_destory_total)}${BigDecimal(data.blockBurnTotal).setScale(2,BigDecimal.ROUND_DOWN)} ${it.fShortName}"
            tv_share_total?.text = "${context!!.getString(R.string.share_total)}${BigDecimal(data.shareTotal).setScale(2,BigDecimal.ROUND_DOWN)}${it.fShortName}"
           tv_contract_address?.text = "${data.contracAddress}"
       }
    }

    override fun onDestroy() {
        super.onDestroy()
        subscribe.dispose()
    }
}

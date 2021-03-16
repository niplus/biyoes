package com.biyoex.app.home.view

import android.content.Context
import android.content.Intent
import android.view.Gravity
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import com.biyoex.app.MainActivity
import com.biyoex.app.R
import com.biyoex.app.common.Constants
import com.biyoex.app.common.base.RxBus
import com.biyoex.app.common.base.RxBusData
import com.biyoex.app.common.data.SessionLiveData
import com.biyoex.app.common.utils.ShareWechatDialog
import com.biyoex.app.common.utils.androidutilcode.ScreenUtils
import com.biyoex.app.common.widget.BottomDialog

class CoinPosDialog(var mcontext:Context,var mCoinName:String) :BottomDialog(mcontext, R.layout.dialog_coinpos) {
    init {

        val window = window
        window!!.decorView.setPadding(ScreenUtils.dp2px(25f), 0, ScreenUtils.dp2px(25f), 0)
        // 获取Window的LayoutParams
        val attributes = window.attributes
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT
//        attributes.height = WindowManager.LayoutParams.WRAP_CONTENT
        attributes.gravity = Gravity.CENTER
        // 一定要重新设置, 才能生效
        window.attributes = attributes
        findViewById<ImageView>(R.id.iv_back).setOnClickListener { dismiss() }
        findViewById<TextView>(R.id.tv_go_market).text = "购买${mCoinName}"
        findViewById<TextView>(R.id.tv_dialog_pos_tv).text = "1.持有更多${mCoinName}参与Pos"
        findViewById<TextView>(R.id.tv_go_market).setOnClickListener {
            Constants.coinName = mCoinName
            val rxBusData = RxBusData()
            rxBusData.msgName = "MarketFragment"
            rxBusData.msgData = "refreshData"
            RxBus.get().post(rxBusData)
//            EventBus.getDefault().post("MarketFragmentRefresh");
            mcontext.startActivity(Intent(mcontext,MainActivity::class.java).putExtra("isToMarket",1))
//            ActivityManagerUtils.getInstance().mainActivity.turnPage(2)
//            ActivityManagerUtils.getInstance().closeAllExceptThis(MainActivity::class.java)

        }

        findViewById<TextView>(R.id.tv_dialog_share).setOnClickListener {
            ShareWechatDialog(context, Constants.REALM_NAME + "user/register?intro=" + SessionLiveData.getIns().value!!.id,"我正在参加VB Global的POS理财，邀您共享持币挖矿和分享收益","VB Global 面向全球的数字资产交易所，致力于为全球用户构建一个更加开放、透明、公平的数字资产服务生态").show()
        }
    }
}
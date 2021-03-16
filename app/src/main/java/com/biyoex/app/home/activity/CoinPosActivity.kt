package com.biyoex.app.home.activity

import android.content.Intent
import android.text.InputFilter
import android.widget.CompoundButton
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.biyoex.app.R
import com.biyoex.app.common.Constants
import com.biyoex.app.common.activity.WebPageLoadingActivity
import com.biyoex.app.common.bean.CoinPosBean
import com.biyoex.app.common.bean.PosHoldRateProfitBean
import com.biyoex.app.common.data.SessionLiveData
import com.biyoex.app.common.mvpbase.BaseActivity
import com.biyoex.app.common.utils.Arith
import com.biyoex.app.common.utils.CashierInputFilterUtils
import com.biyoex.app.common.utils.DateUtilsl
import com.biyoex.app.common.utils.ShareWechatDialog
import com.biyoex.app.home.presenter.CoinPosPresenter
import com.biyoex.app.home.view.CoinPosDialog
import com.biyoex.app.home.view.CoinPosView
import com.biyoex.app.home.view.DrawView
import kotlinx.android.synthetic.main.activity_coin_pos.*
import kotlinx.android.synthetic.main.layout_newtitle_bar.*
import java.text.NumberFormat
import java.util.*

/**
 * Created by mac on 20/7/30.
 * 参与Pos理财
 */
class CoinPosActivity : BaseActivity<CoinPosPresenter>(), CoinPosView, CompoundButton.OnCheckedChangeListener {
    var btn_state = 0
    var coinId = 0
    var coinName = ""
    var webSite = ""
    var openReset = 0
    var mAdapter: PosWeekAdapter = PosWeekAdapter(R.layout.item_coin_pos)

    //    var holdRateProfitData: MutableList<PosHoldRateProfitBean> = arrayListOf()//持币收益率k线图
//    var weekIncomeCurvesData = mutableListOf<MutableList<String>>()//最近一周的曲线增长图数据
    override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
        btn_state = 2
        showNormalDialog(getString(R.string.dialog_title), if (p1) getString(R.string.is_open_pos_reset) else getString(R.string.is_close_pos_reset))
    }

    //获取币种信息
    override fun getCoinData(mdata: CoinPosBean) {
        webSite = mdata.webSite
        tv_pos_total.text = Constants.numberFormat(mdata.cumulativeIncome)
        tv_yesterday_price.text = Constants.numberFormat(mdata.holdingYesterday)
        tv_min_number.text = Constants.numberFormat(mdata.minHolding)
        tv_account_number.text = Constants.numberFormat(mdata.vbtBalance)
        tv_pos_account_number.text = Constants.numberFormat(mdata.posBalance)
//        mPresent.getWeekEarNing(coinId)
        mPresent.getHoldRateProfit(coinId)//持币收益率k线图
        mPresent.getWeekIncomeCurves(coinId)//最近一周的曲线增长图
        switch_btn.setOnCheckedChangeListener(null)
        switch_btn.isChecked = mdata.openReset == 1
        switch_btn.invalidate()
        switch_btn.setOnCheckedChangeListener(this)
        tv_is_auto.text = if (mdata.openReset == 1) "关闭自动复投" else "打开自动复投"
    }

    override fun getWeekEarnings(mListData: MutableList<MutableList<String>>) {
        mAdapter.setNewData(mListData)

    }

    override fun getHoldRateProfit(mListData: MutableList<PosHoldRateProfitBean>) {
//        holdRateProfitData.addAll(mListData)
        val view = DrawView(this)
        val listData: ArrayList<String> = arrayListOf()
        val yLabel: ArrayList<String> = arrayListOf()
        val xLabel: ArrayList<String> = arrayListOf()
        var max = "0"
        var distanceTop = 0
        var distanceLeft = 0
        xLabel.add("")
        if (mListData.size > 0) {
//        xLabel.add("收益率(%)")
            for (i: Int in mListData.indices) {
                val Rate: String = mListData[i].rate
                val holdAmount: String = mListData[i].holdAmount
                listData.add(Rate)
                xLabel.add(holdAmount)
                if (Arith.CompareTheSize(max, Rate) == -1) {
                    max = Rate
                }
            }
//            xLabel.add("持币数(个)")
            val num: Int = Arith.div("10", max, 0).toInt()
            var aMax: Int = Arith.round(max, 0).toInt()
            if (num < 1) {
                yLabel.add("0")
                yLabel.add("0.3")
                yLabel.add("0.4")
                yLabel.add("0.5")
                yLabel.add("0.6")
                yLabel.add("0.7")
                yLabel.add("0.8")
                yLabel.add("0.9")
                yLabel.add("1.0")
            } else {
                aMax += 10
                for (i: Int in 0..aMax step num) {//之前默认递增值是1，step 3将递增值改为了3 即10、13、16、19、21...
                    yLabel.add(i.toString())
                }
            }

        } else {
            xLabel.add("")
            xLabel.add("")
            xLabel.add("")
            xLabel.add("暂无数据")
            xLabel.add("")
            xLabel.add("")
            xLabel.add("")

            yLabel.add("0")
            yLabel.add("0.3")
            yLabel.add("0.4")
            yLabel.add("0.5")
            yLabel.add("0.6")
            yLabel.add("0.7")
            yLabel.add("0.8")
            yLabel.add("0.9")
            yLabel.add("1.0")
        }
        view.setDate(yLabel.toTypedArray(), xLabel.toTypedArray(), listData.toTypedArray(), null, distanceTop, distanceLeft)
        one_curve_graph.addView(view);
    }

    override fun getWeekIncomeCurves(mListData: MutableList<MutableList<String>>) {
//        weekIncomeCurvesData.addAll(mListData)
        val views = DrawView(this)
        val listData: ArrayList<String> = arrayListOf()
        val yLabels: ArrayList<String> = arrayListOf()
        val xLabels: ArrayList<String> = arrayListOf()
        var max = "0"
        xLabels.add("")
        if (mListData.size > 0) {
            for (i: Int in mListData.indices) {
                val x: String = mListData[i][0]
                val y: String = mListData[i][1]
                listData.add(x)
                xLabels.add(y)
                if (Arith.CompareTheSize(max, x) == -1) {
                    max = x
                }
            }
            val num: Int = Arith.div("7", max, 0).toInt()
            var bMax: Int = Arith.round(max, 0).toInt()
            if (num < 1) {
                yLabels.add("0")
                yLabels.add("0.5")
                yLabels.add("0.6")
                yLabels.add("0.7")
                yLabels.add("0.8")
                yLabels.add("0.9")
                yLabels.add("1.0")
            } else {
                bMax += 10
                for (i: Int in 0..bMax step num) {//之前2默认递增值是1，step 3将递增值改为了3 即10、13、16、19、21...
                    yLabels.add(i.toString())
                }
            }
//            Log.d("$bMax-------------------")
        } else {
            xLabels.add("")
            xLabels.add("")
            xLabels.add("")
            xLabels.add("暂无数据")
            xLabels.add("")
            xLabels.add("")
            xLabels.add("")

            yLabels.add("0")
            yLabels.add("200")
            yLabels.add("400")
            yLabels.add("600")
            yLabels.add("800")
            yLabels.add("1000")
            yLabels.add("1200")
        }
        views.setDate(yLabels.toTypedArray(), xLabels.toTypedArray(), listData.toTypedArray(), null, 10, 10)
        two_curve_graph.addView(views);
    }


    override fun getLayoutId(): Int = R.layout.activity_coin_pos
    override fun createPresent(): CoinPosPresenter = CoinPosPresenter()
    override fun initComp() {
        iv_menu.setOnClickListener { finish() }
        tv_right.text = getString(R.string.rule)
        tv_title.text = "${coinName}-Pos"
        textView25.text = "${getString(R.string.all_earnings)}($coinName)"
        tv_coin_info.text = "${coinName}${getString(R.string.coin_manager)}"
        tv_block_browser.text = "${coinName}${getString(R.string.block_browser)}"
        edit_pos_number.hint = "请输入要参与Pos的${coinName}数量"
        tv_yesterday_name.text = "${getString(R.string.last_good_price)}($coinName)"
        tv_min_name.text = "${getString(R.string.pos_min_price).format(coinName)}"
        tv_account_name.text = getString(R.string.pos_account).format(coinName)
        tv_pos_account.text = getString(R.string.user_pos_account).format(coinName)
        textView31.text = getString(R.string.pos_week_earnings).format(coinName)
        textView28.text = "${getString(R.string.my_invite_code)}${Constants.shareId}"

    }

    override fun initData() {
//        val views = DrawView(this)
//        val DataStrs = arrayOf("100", "1230", "1231", "1232", "1233", "1234", "1235", "1236") //数据
////        val DataStr1 = arrayOf("33", "44", "77", "66", "50", "100", "99", "110", "76") //数据
//        val YLabels = arrayOf("1230", "1231", "1232", "1233", "1234", "1235", "1236")
//        val XLabels = arrayOf("", "07-21", "07-22", "07-23", "07-24", "07-25", "07-26", "07-27")
////        view.setBackgroundResource(R.mipmap.bg_cash_list);//给整个View加背景
//        views.setDate(YLabels, XLabels, DataStrs, null, 10, 10)
//        two_curve_graph.addView(views);

        tv_right.setOnClickListener {
            val intent = Intent(context, WebPageLoadingActivity::class.java)
            intent.putExtra("url", Constants.PosRule)
            intent.putExtra("type", "url")
            intent.putExtra("title", "Pos规则")
            intent.putExtra("isShare", false)
            context!!.startActivity(intent)
        }

        switch_btn.setOnCheckedChangeListener(this)
        tv_add_pos.setOnClickListener {
            btn_state = 0
            showNormalDialog("", "您是否确认加入POS")
        }
        tv_cancel_pos.setOnClickListener {
            btn_state = 1
            showNormalDialog("", "您是否确认退出POS")
        }
        imageView11.setOnClickListener {
            startActivity(Intent(this, PosEarningsActivity::class.java).putExtra("coinId", coinId))
        }
        //Pos算力和邀请明细
        tv_pos_sum.setOnClickListener {
            startActivity(Intent(this, PosComputingActivity::class.java).putExtra("coinId", coinId).putExtra("coinName", coinName))
        }
        //币种介绍
        tv_coin_info.setOnClickListener {
            startActivity(Intent(this, CoinInfoActivity::class.java).putExtra("coinId", coinId))
        }
        //区块浏览器
        tv_block_browser.setOnClickListener {
            val intent = Intent(context, WebPageLoadingActivity::class.java)
            intent.putExtra("url", webSite)
            intent.putExtra("type", "url")
            intent.putExtra("title", "${coinName}区块浏览器")
            intent.putExtra("isShare", false)
            context!!.startActivity(intent)
        }
        //POS持币排行榜
        tv_holding_list.setOnClickListener {
            startActivity(Intent(this, PosCashListActivity::class.java).putExtra("coinId", coinId))
        }
        //站内转账
        tv_internal_transfer.setOnClickListener {
            startActivity(Intent(this, PosInternalTransferActivity::class.java).putExtra("coinId", coinId))
        }
        //微信分享
        imageView13.setOnClickListener {
            ShareWechatDialog(context, Constants.REALM_NAME + "/user/register?intro=" + SessionLiveData.getIns().value!!.id, "我正在参加VB Global的POS理财，邀您共享持币挖矿和分享收益", "VB Global 面向全球的数字资产交易所，致力于为全球用户构建一个更加开放、透明、公平的数字资产服务生态").show()
        }
        tv_open_dialog.setOnClickListener {
            CoinPosDialog(this, coinName).show()
        }
        coinId = intent.getIntExtra("coinId", 0)
        coinName = intent.getStringExtra("coinName")
        mPresent.getCoinData(coinId)
        rv_pos_earnings.layoutManager = LinearLayoutManager(this)
        rv_pos_earnings.adapter = mAdapter
        val cashierInputFilterUtils1 = CashierInputFilterUtils()
        cashierInputFilterUtils1.POINTER_LENGTH = 2
        val isTradingQuantity = arrayOf<InputFilter>(cashierInputFilterUtils1)
        edit_pos_number.filters = isTradingQuantity
    }

    //公用一个dialog回调
    override fun setDialogOnClick() {
        if (btn_state == 1) {
            mPresent.cancelPos(coinId)
        } else if (btn_state == 0) {
            mPresent.addPos(edit_pos_number.text.toString(), coinId)
            edit_pos_number.setText("")
        } else {
            mPresent.postAutoRecharge(coinId)
        }
    }

    //
    override fun dialogCancel() {
        if (btn_state == 2) {
            switch_btn.setOnCheckedChangeListener(null)
            switch_btn.isChecked = !switch_btn.isChecked
            switch_btn.invalidate()
            switch_btn.setOnCheckedChangeListener(this)
        }
    }


}


class PosWeekAdapter(mlayout: Int) : BaseQuickAdapter<MutableList<String>, BaseViewHolder>(mlayout) {
    override fun convert(helper: BaseViewHolder?, item: MutableList<String>?) {
        helper!!.setText(R.id.tv_item_time, DateUtilsl.time_today(item!![0]))
                .setText(R.id.tv_item_number, item[1])
                .setText(R.id.tv_item_earnings, NumberFormat.getInstance().format(item[2].toDouble()))
    }
}


package com.biyoex.app.market

import android.content.Intent
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener
import com.biyoex.app.R
import com.biyoex.app.common.mvpbase.BaseActivity
import com.biyoex.app.common.bean.Amount
import com.biyoex.app.common.bean.RushBuyBean
import com.biyoex.app.common.bean.RushBuyListData
import com.biyoex.app.market.adapter.CoinRushBuyAdapter
import com.biyoex.app.market.adapter.RushBuyRecordAdapter
import com.biyoex.app.market.presenter.CoinRushBuyPresenter
import com.biyoex.app.market.view.CoinRushBuyView
import com.biyoex.app.my.activity.VipActivity
import kotlinx.android.synthetic.main.activity_coin_rush_buy.*
import java.math.BigDecimal

//NB抢购
class CoinRushBuyActivity : BaseActivity<CoinRushBuyPresenter>(), CoinRushBuyView, OnRefreshLoadmoreListener {

    var mpage: Int = 1
    var mpageSize: Int = 20
    var mListPrice = mutableListOf<Amount>()
    var mDataList = mutableListOf<RushBuyListData>()
    lateinit var mPriceAdapter: CoinRushBuyAdapter   //选购价格
    lateinit var mRecordAdapter: RushBuyRecordAdapter
    var mPriceIndex = 0   //默认选中第一位价格
    lateinit var mData: RushBuyBean
    var lastClickTime: Long = 0
    override fun createPresent(): CoinRushBuyPresenter = CoinRushBuyPresenter()
    override fun getLayoutId(): Int = R.layout.activity_coin_rush_buy
    override fun initComp() {
        iv_menu.setOnClickListener { finish() }
        btn_rushbuy_sure.setOnClickListener {
            //            var now = System.currentTimeMillis();
//            if (now - lastClickTime > 1000) {
//                lastClickTime = now
//                Log.e(TAG, "perform click!!!");
            mpage = 1
            mPresent.postRushBuy(mPriceIndex)
//            }else{
//                showToast("请不要重复点击")
//            }
        }

        iv_right.setOnClickListener {
            startActivity(Intent(this, RushBuyInviteRecordActivity::class.java))
        }
        mPriceAdapter.setOnItemClickListener { _, _, position ->
            run {
                mPriceIndex = position
                mPriceAdapter.getSelectPosition(position)
                tv_getnb.text = "${getString(R.string.predict_getcoin_number)}${BigDecimal(mListPrice[mPriceIndex].amount).divide(BigDecimal(mData.currentPrice), 4, BigDecimal.ROUND_HALF_UP).toPlainString()}NB"
            }
        }
        smartrefresh.setOnRefreshLoadmoreListener(this)
    }

    override fun initData() {
        immersionBar.statusBarDarkFont(false)
        rv_rushbuy.layoutManager = GridLayoutManager(this, 5)
        mPriceAdapter = CoinRushBuyAdapter()
        mRecordAdapter = RushBuyRecordAdapter()
        rv_rushbuy.adapter = mPriceAdapter
        rv_rushbuy_record.layoutManager = LinearLayoutManager(this)
        rv_rushbuy_record.adapter = mRecordAdapter
//        val view =
        mRecordAdapter.emptyView = View.inflate(context, R.layout.layout_no_data, null)
//        mPresent.getRushBuyData()

    }

    override fun getRushBuySuccess(mData: RushBuyBean) {
        hideLoadingDialog()
        if (mData.num % 5 == 0 && mData.num >= 10) {
            tv_title.text = "NB(第${mData.num}期)阳光普照"
        } else {
            tv_title.text = "NB(第${mData.num}期)抢购"
        }
        this.mData = mData
        tv_scale.text = "${BigDecimal(mData.scale).multiply(BigDecimal(100)).setScale(2, BigDecimal.ROUND_DOWN)}% (${mData.relAmount} / ${mData.amount})"
        tv_rushbuy_time.text = mData.buyTime
        ratio_rushbuy_progress.setRatio(mData.scale.toDouble())
        tv_now_price.text = mData.currentPrice
        tv_next_price.text = mData.nextPrice
        tv_market_price.text = mData.marketPrice
        tv_sum_amount.text = "${getString(R.string.nb_all_buy_number)}${BigDecimal(mData.nbTotalAmount).setScale(4, BigDecimal.ROUND_DOWN)}"
        tv_relaoumt.text = "${getString(R.string.nb_all_rel_number)}:${BigDecimal(mData.nbRelAmount).setScale(4, BigDecimal.ROUND_DOWN)}"
        mListPrice.clear()
        mListPrice.addAll(mData.amountList)
        mPriceAdapter.setNewData(mListPrice)
        tv_getnb.text = "${getString(R.string.predict_getcoin_number)}${BigDecimal(mListPrice[mPriceIndex].amount).divide(BigDecimal(mData.currentPrice), 4, BigDecimal.ROUND_DOWN).toPlainString()}NB"
    }

    override fun setStatusBar() {
        super.setStatusBar()
        immersionBar.statusBarDarkFont(false)
    }

    override fun getRushBuyError(msg: String) {
        hideLoadingDialog()
        val builder = AlertDialog.Builder(this)
        builder.setTitle(com.lzy.imagepicker.R.string.hint)
        builder.setMessage(msg)
        builder.setNegativeButton(com.lzy.imagepicker.R.string.cancel, null)
        builder.setPositiveButton(R.string.to_buyvip) { _, _ ->
            //移除当前图片刷新界面
            var intent = Intent(this, VipActivity::class.java)
            startActivity(intent)
        }
        builder.show()
    }

    override fun getRushBuyListSuccess(mListData: MutableList<RushBuyListData>, page: Int, pageSize: Int) {
        hideLoadingDialog()
        smartrefresh.finishRefresh()
        smartrefresh.finishLoadmore()
        mpage = page
        mpageSize = mpageSize
//        Log.e("page$page")
        if (page == 1) {
            mDataList.clear()
        }
        mDataList.addAll(mListData)
        mRecordAdapter.setNewData(mDataList)
    }

    override fun onResume() {
        super.onResume()
        mPresent.getRushBuyRecord(1, mpageSize)
    }

    override fun onLoadmore(refreshlayout: RefreshLayout?) {
        mpage++
        mPresent.getRushBuyRecord(mpage, mpageSize)
    }

    override fun onRefresh(refreshlayout: RefreshLayout?) {
        mpage = 1
        mPresent.getRushBuyRecord(mpage, mpageSize)
    }
}

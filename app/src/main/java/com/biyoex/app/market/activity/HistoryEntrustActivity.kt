package com.biyoex.app.market.activity


import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.kotlin.autoDisposable
import com.biyoex.app.R
import com.biyoex.app.VBTApplication
import com.biyoex.app.common.base.BaseActivity
import com.biyoex.app.common.http.RetrofitHelper
import com.biyoex.app.common.mvpbase.BaseObserver
import com.biyoex.app.common.utils.MoneyUtils
import com.biyoex.app.common.widget.RatioLinearLayout
import com.biyoex.app.home.bean.CoinTradeRankBean
import com.biyoex.app.trading.bean.RefreshUserInfoBean
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_history_entrust.rv_current_entrust
import kotlinx.android.synthetic.main.layout_newtitle_bar.*
import java.math.BigDecimal
import java.text.SimpleDateFormat
    /**
     * 我的历史委托
     * */
class HistoryEntrustActivity : BaseActivity() {

    private var mCurrentEntrustAdapter: BaseQuickAdapter<List<String>, BaseViewHolder>? = null
    private var mUnfinishedList: MutableList<List<String>> = mutableListOf()
    private lateinit var coin: CoinTradeRankBean.DealDatasBean
    override fun initView() {
        mCurrentEntrustAdapter = object : BaseQuickAdapter<List<String>, BaseViewHolder>(R.layout.item_market_entrust, mUnfinishedList) {
            override fun convert(helper: BaseViewHolder, item: List<String>) {
                //                helper.setText(R.id.tv_currency_abbreviated, coin.getFname_sn().replaceAll(" ", ""));
                //0:买卖，1:委托价格，2:未成交数量，3:主键ID，4:创建时间，5:状态（1未成交 ，2 部分成交，3完全成交，4已取消），6:委托数量，7:成交均价
                val ratioRightLayout = helper.itemView.findViewById<RatioLinearLayout>(R.id.item_market_entrust_progressbar)
                if (item[0] == "1") {
                    helper.setTextColor(R.id.item_market_entrust_isbuy, VBTApplication.FALL_COLOR)
                    helper.setText(R.id.item_market_entrust_isbuy, R.string.sell)
                    helper.setBackgroundColor(R.id.item_market_entrust_isbuy, resources.getColor(R.color.price_bg_red))
                            .setTextColor(R.id.item_market_entrust_number, resources.getColor(R.color.price_red))
                            .setImageDrawable(R.id.item_market_entrust_buyandsell, resources.getDrawable(R.mipmap.icon_small_sell))
                    ratioRightLayout.setPaintColor(VBTApplication.FALL_COLOR)
                } else {
                    helper.setText(R.id.item_market_entrust_isbuy, R.string.buy)
                    helper.setTextColor(R.id.item_market_entrust_isbuy, VBTApplication.RISE_COLOR)
                    helper.setBackgroundColor(R.id.item_market_entrust_isbuy, resources.getColor(R.color.price_bg_green))
                            .setTextColor(R.id.item_market_entrust_number, resources.getColor(R.color.price_green))
                            .setImageDrawable(R.id.item_market_entrust_buyandsell, resources.getDrawable(R.mipmap.icon_small_buy))
                    ratioRightLayout.setPaintColor(VBTApplication.RISE_COLOR)
                }

                //                    int status = dataJson.getInt(5);
                //                helper.setVisible(R.id.tv_cancel, true);
                //                helper.setVisible(R.id.tv_status, false);
                //                arr[0]买卖类型 0买，1卖
                //                arr[1] 单价
                //                arr[2] 剩余未成交数量
                //                arr[3] 订单id
                //                arr[4] 时间戳
                //                arr[5] 状态 1未成交 ，2 部分成交，3完全成交，4已取消
                //                arr[6] 挂单总数量
                //                arr[7]成交均价
                val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                helper.setText(R.id.item_market_entrust_time, simpleDateFormat.format(java.lang.Long.valueOf(item[6])))
                if (item[7] == null) {
                    helper.setText(R.id.item_market_entrust_success_price, getString(R.string.user_entrust_success_price) + "(" + coin.group + ") --")
                } else
                    helper.setText(R.id.item_market_entrust_success_price, getString(R.string.user_entrust_success_price) + "(" + coin.group + ")" + BigDecimal(item[8]).setScale(coin.priceDecimals, BigDecimal.ROUND_DOWN))

                helper.setVisible(R.id.item_market_entrust_cancel, false)
                        .setText(R.id.item_market_entrust_coinname, coin.fname_sn)
                        .setText(R.id.item_market_entrust_user_price, getString(R.string.user_entrust_price) + "(" + coin.group + ")" + MoneyUtils.decimalByUp(coin.priceDecimals, BigDecimal(item[1])).toPlainString())
                        .setText(R.id.item_market_entrust_number, MoneyUtils.decimalByUp(coin.amountDecimals, BigDecimal(item[7])).setScale(coin.amountDecimals, BigDecimal.ROUND_DOWN).toPlainString())
                        .setText(R.id.item_market_entrust_success_number, getString(R.string.user_entrust_success) + "(" + coin.fShortName + ")" + BigDecimal(item[2]).setScale(coin.amountDecimals, BigDecimal.ROUND_DOWN).toPlainString())
                        .setOnClickListener(R.id.item_market_entrust_cancel) {
                            cancelOrder(item[3])
                        }
                ratioRightLayout.setRatio(BigDecimal(item[2]).setScale(5, BigDecimal.ROUND_DOWN).divide(BigDecimal(item[7]).setScale(5, BigDecimal.ROUND_DOWN), 4, BigDecimal.ROUND_DOWN).setScale(5, BigDecimal.ROUND_DOWN).toDouble())
            }
        }
        rv_current_entrust.layoutManager = LinearLayoutManager(this)
        rv_current_entrust.adapter = mCurrentEntrustAdapter
        mCurrentEntrustAdapter!!.emptyView = View.inflate(this, R.layout.view_empty, null)
        getdata()
    }

    private fun cancelOrder(get: String) {
        showLoadingDialog()
        RetrofitHelper
                .getIns().zgtopApi
                .cancelEntrush(get)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .autoDisposable(AndroidLifecycleScopeProvider.from(this, Lifecycle.Event.ON_DESTROY))//OnDestory时自动解绑
                .subscribe(object : BaseObserver<String>() {
                    override fun success(t: String) {
                        hideLoadingDialog()
                        showToast(getString(R.string.already_revoke))
                        getdata()
                    }

                    override fun onError(e: Throwable) {
                        showToast(getString(R.string.revoke_failed))
                        hideLoadingDialog()
                    }
                })
    }

    override fun initData() {
        tv_title.text = getString(R.string.entrust_history)
        coin = intent.getSerializableExtra("data") as CoinTradeRankBean.DealDatasBean
        iv_menu.setOnClickListener { finish() }
    }

    private fun getdata() {

        RetrofitHelper.getIns().zgtopApi
                .getMarketUserInfo(coin.fid, "${System.currentTimeMillis()}")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .autoDisposable(AndroidLifecycleScopeProvider.from(this, Lifecycle.Event.ON_DESTROY))//OnDestory时自动解绑
                .subscribe(object : BaseObserver<RefreshUserInfoBean>() {
                    override fun success(t: RefreshUserInfoBean) {
                        if (t != null) {
                            if (t.isLogin != "0") {
                                mUnfinishedList.clear()
                                mUnfinishedList.addAll(t.entrustList)
                                mUnfinishedList.addAll(t.entrustListLog)
                                mCurrentEntrustAdapter!!.setNewData(mUnfinishedList)
                            }
                        }
                    }
                })
    }

    override fun getLayoutId(): Int = R.layout.activity_history_entrust
}

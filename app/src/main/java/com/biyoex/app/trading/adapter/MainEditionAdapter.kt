package com.biyoex.app.trading.adapter

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.uber.autodispose.AutoDispose
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.biyoex.app.R
import com.biyoex.app.VBTApplication
import com.biyoex.app.common.activity.LoginActivity
import com.biyoex.app.common.bean.RequestResult
import com.biyoex.app.common.http.RetrofitHelper
import com.biyoex.app.common.mvpbase.BaseObserver
import com.biyoex.app.common.utils.LanguageUtils
import com.biyoex.app.common.utils.MoneyUtils
import com.biyoex.app.common.utils.SharedPreferencesUtils
import com.biyoex.app.common.utils.ToastUtils
import com.biyoex.app.home.bean.CoinTradeRankBean.DealDatasBean
import com.biyoex.app.trading.activity.TrendChartNewActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.math.BigDecimal

/**
 * Created by LG on 2017/5/19.
 */
class MainEditionAdapter(context: Context?, @LayoutRes layoutResId: Int, data: List<DealDatasBean?>?, group: String) : BaseQuickAdapter<DealDatasBean, BaseViewHolder>(layoutResId, data) {
    private lateinit var mContext: Context
    private val group: String
    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun convert(helper: BaseViewHolder, item: DealDatasBean) {
        val coins = SharedPreferencesUtils.getInstance().getString("coin_id", "")
//        if (coins.contains("" + item.fid)) {
//            helper.setImageDrawable(R.id.is_fons, mContext.resources.getDrawable(R.mipmap.icon_home_select))
//        } else {
//            helper.setImageDrawable(R.id.is_fons, mContext.resources.getDrawable(R.mipmap.icon_home_normal))
//        }
        helper.getView<View>(R.id.is_fons).setOnClickListener { v: View? -> addSelectCoin(item.fid, coins.contains("" + item.fid)) }
        val textView = helper.getView<TextView>(R.id.tv_percentage)
        val ratio = MoneyUtils.decimal2ByUp(BigDecimal(item.fupanddown))
        textView.background = mContext.resources.getDrawable(if (item.fupanddown >= 0) R.drawable.bg_item_homecoin_green else R.drawable.bg_item_homecoin_red)
        textView.setTextColor(mContext.resources.getColor(if (item.fupanddown >= 0) R.color.price_green else R.color.price_red))
        textView.text = (if (item.fupanddown >= 0) "+" else "") + ratio + "%"
        val ivCurrencyIcon = helper.getView<ImageView>(R.id.iv_coin_icon)
        //        GlideUtils.getInstance().displayCurrencyImage(mContext, VBTApplication.appPictureUrl + item.getFurl(), ivCurrencyIcon);
        helper.setText(R.id.tv_coin_name, item.getfShortName())
        //        if (!group.equals(mContext.getString(R.string.self_selected_area))) {
//            helper.getView(R.id.tv_trade_area).setVisibility(View.GONE);
//        } else {
        helper.setText(R.id.tv_trade_area, "/" + item.group)
//                .setVisible(R.id.iv_hot, item.isBurn)
        helper.setText(R.id.tv_coin_price, MoneyUtils.decimalByUp(item.priceDecimals, BigDecimal(item.lastDealPrize)).toPlainString())
        when {
            item.volumn > 100000000 -> {
                helper.setText(R.id.tv_turnover, MoneyUtils.decimalByUp(item.amountDecimals, BigDecimal(item.volumn / 100000000)).toPlainString() + mContext.getString(R.string.million))
            }
            item.volumn > 10000 -> {
                helper.setText(R.id.tv_turnover, MoneyUtils.decimalByUp(item.amountDecimals, BigDecimal(item.volumn / 10000)).toPlainString() + mContext.getString(R.string.ten_thousand))
            }
            else -> {
                helper.setText(R.id.tv_turnover, "" + MoneyUtils.decimalByUp(item.amountDecimals, BigDecimal(item.volumn)).toPlainString())
            }
        }
        Log.i(TAG, "convert: " + MoneyUtils.decimalByUp(if (LanguageUtils.currentLanguage == 1) 2 else 4, BigDecimal(MoneyUtils.mul(java.lang.Double.valueOf(item.lastDealPrize), java.lang.Double.valueOf(item.rate)))))
        helper.setText(R.id.tv_usdt_price, mContext.getString(R.string.money) + MoneyUtils.decimalByUp(if (LanguageUtils.currentLanguage == 1) 2 else 4, BigDecimal(MoneyUtils.mul(java.lang.Double.valueOf(item.lastDealPrize), java.lang.Double.valueOf(item.rate)))))
                .setTextColor(R.id.tv_coin_price, if (item.fupanddown < 0) mContext.resources.getColor(R.color.price_red) else mContext.resources.getColor(R.color.price_green))
        helper.getView<View>(R.id.ll_coin_info).setOnClickListener { v: View? ->
            val itTrendChart = Intent(VBTApplication.getContext(), TrendChartNewActivity::class.java)
            itTrendChart.putExtra("coin", item)
            if (group == "POC+") {
                itTrendChart.putExtra("isnow", 1)
            } else {
                itTrendChart.putExtra("isnow", item.isNew)
            }
            mContext.startActivity(itTrendChart)
        }
    }

    private fun addSelectCoin(fid: Int, type: Boolean) {
        var coinId = SharedPreferencesUtils.getInstance().getString("coin_id", "")
        if (type) {
            coinId = coinId.replace(",$fid,", ",")
            SharedPreferencesUtils.getInstance().saveString("coin_id", coinId)
        } else {
            if (coinId == "") {
                SharedPreferencesUtils.getInstance().saveString("coin_id", ",$fid,")
            } else {
                SharedPreferencesUtils.getInstance().saveString("coin_id", "$coinId$fid,")
            }
        }
        val SelectCoinId = SharedPreferencesUtils.getInstance().getString("coin_id", "")
        RetrofitHelper.getIns().zgtopApi.updateUserSelfToken(SelectCoinId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .`as`(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(mContext as AppCompatActivity, Lifecycle.Event.ON_DESTROY)))
                .subscribe(object : BaseObserver<RequestResult<Any>>() {
                    protected override fun success(requestResult: RequestResult<Any>) {
//                        String coins2 = SharedPreferencesUtils.getInstance().getString("coin_id", "");
                        if (type) {
                            ToastUtils.showToast(mContext.resources.getString(R.string.cancel_collect_success))
                        } else {
                            ToastUtils.showToast(mContext.resources.getString(R.string.add_collection_success))
                        }
                        notifyDataSetChanged()
                    }

                    override fun failed(code: Int, data: String?, msg: String?) {
                        super.failed(code, data, msg)
                        var coinId = SharedPreferencesUtils.getInstance().getString("coin_id", "")
                        if (type) {
                            if (coinId == "") {
                                SharedPreferencesUtils.getInstance().saveString("coin_id", ",$fid,")
                            } else {
                                SharedPreferencesUtils.getInstance().saveString("coin_id", "$coinId$fid,")
                            }
                        } else {
                            coinId = coinId.replace(",$fid,", ",")
                            SharedPreferencesUtils.getInstance().saveString("coin_id", coinId)
                        }
                        if (code == 401) {
                            mContext.startActivity(Intent(mContext, LoginActivity::class.java))
                        }
                    }
                })
    }

    init {
        this.mContext = context
        this.group = group
    }
}
package com.biyoex.app.my.activity

import com.biyoex.app.common.base.BaseAppCompatActivity
import butterknife.BindView
import com.biyoex.app.R
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.biyoex.app.my.bean.RechargeCoinBean
import com.chad.library.adapter.base.BaseQuickAdapter
import com.biyoex.app.common.utils.GlideUtils
import com.biyoex.app.VBTApplication
import android.text.TextUtils
import com.biyoex.app.common.utils.MoneyUtils
import com.biyoex.app.common.utils.LanguageUtils
import androidx.recyclerview.widget.LinearLayoutManager
import com.biyoex.app.trading.bean.CoinMarketLiveData
import com.biyoex.app.home.bean.CoinTradeRankBean
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.GridLayoutManager
import butterknife.OnClick
import com.chad.library.adapter.base.BaseViewHolder
import com.biyoex.app.common.http.RetrofitHelper
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers
import com.biyoex.app.common.bean.RequestResult
import com.uber.autodispose.AutoDispose
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.biyoex.app.common.mvpbase.BaseObserver
import com.biyoex.app.common.utils.log.Log
import java.math.BigDecimal
import java.util.ArrayList

/*
*
* **/
class FinanceDetailActivity : BaseAppCompatActivity() {
    @JvmField
    @BindView(R.id.iv_coin)
    var ivCoin: ImageView? = null

    @JvmField
    @BindView(R.id.tv_shortName)
    var tvShortName: TextView? = null

    @JvmField
    @BindView(R.id.tv_allName)
    var tvAllName: TextView? = null

    @JvmField
    @BindView(R.id.tv_total)
    var tvTotal: TextView? = null

    @JvmField
    @BindView(R.id.tv_value)
    var tvValue: TextView? = null

    @JvmField
    @BindView(R.id.tv_frozen)
    var tvFrozen: TextView? = null

    @JvmField
    @BindView(R.id.tv_valuations)
    var tvValuations: TextView? = null

    @JvmField
    @BindView(R.id.recycler)
    var recyclerView: RecyclerView? = null

    @JvmField
    @BindView(R.id.tv_title)
    var tvTitle: TextView? = null
    private var rechargeCoinBean: RechargeCoinBean? = null
    private var datas: MutableList<CoinTradeRankBean.DealDatasBean>? = null
    private var adapter: BaseQuickAdapter<CoinTradeRankBean.DealDatasBean, BaseViewHolder>? = null
    private var page = 1
    private var isRefresh = false

    /**
     * 单个币的估值
     */
    private var singlePrice = 0.0
    override fun initView() {
        tvTitle!!.text = rechargeCoinBean!!.name
        GlideUtils.getInstance().displayCurrencyImage(this, VBTApplication.appPictureUrl + rechargeCoinBean!!.url, ivCoin)
        tvShortName!!.text = rechargeCoinBean!!.name
        tvAllName!!.text = "(" + rechargeCoinBean!!.allName + ")"
        var value = 0.0
        if (!TextUtils.isEmpty(rechargeCoinBean!!.total)) {
            value = java.lang.Double.valueOf(rechargeCoinBean!!.total)
        }
        var frozen = 0.0
        if (!TextUtils.isEmpty(rechargeCoinBean!!.frozen)) {
            frozen = java.lang.Double.valueOf(rechargeCoinBean!!.frozen)
        }
        tvTotal!!.text = MoneyUtils.decimal4ByUp(BigDecimal(MoneyUtils.add(value, frozen))).toPlainString()
        tvValue!!.text = MoneyUtils.decimal4ByUp(BigDecimal(value)).toPlainString()
        tvFrozen!!.text = MoneyUtils.decimal4ByUp(BigDecimal(frozen)).toPlainString()
        if (LanguageUtils.currentLanguage != 1) {
            tvValuations!!.text = MoneyUtils.decimal4ByUp(BigDecimal(rechargeCoinBean!!.value)).toPlainString() + getString(R.string.money_unit)
        } else {
            tvValuations!!.text = MoneyUtils.decimal2ByUp(BigDecimal(rechargeCoinBean!!.value)).toPlainString() + getString(R.string.money_unit)
        }
        if (MoneyUtils.add(value, frozen) != 0.0) {
            singlePrice = MoneyUtils.divide4ByUp(BigDecimal(rechargeCoinBean!!.value), BigDecimal(MoneyUtils.add(value, frozen))).toDouble()
        }
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        linearLayoutManager.isSmoothScrollbarEnabled = true
        linearLayoutManager.isAutoMeasureEnabled = true
        recyclerView!!.layoutManager = linearLayoutManager
        recyclerView!!.isNestedScrollingEnabled = false
        recyclerView!!.layoutManager = GridLayoutManager(this, 2)
        recyclerView!!.adapter = adapter

    }

    override fun initData() {
        rechargeCoinBean = intent.getParcelableExtra("finance")
        datas = ArrayList()
        CoinMarketLiveData.getIns().observe(this, { coinTradeRankBean: CoinTradeRankBean? ->
            if (coinTradeRankBean != null) {
                Log.i("nidongliang, 交易对： $coinTradeRankBean")

                val areaList = coinTradeRankBean.dataMap.map {
                    it.value
                }

                areaList.forEach {
                    it.forEach { coin->
                        if (coin.getfShortName() == rechargeCoinBean!!.name){
                            datas!!.add(coin)
                        }
                    }
                }
            }
        })
        adapter = object : BaseQuickAdapter<CoinTradeRankBean.DealDatasBean, BaseViewHolder>(R.layout.item_finance_list, datas) {
            override fun convert(helper: BaseViewHolder, item: CoinTradeRankBean.DealDatasBean) {
                helper.setText(R.id.tv_coin_name, item.fname_sn)
                helper.setText(R.id.tv_usdt_value, getString(R.string.money) + MoneyUtils.decimalByUp(if (LanguageUtils.currentLanguage == 1) 2 else 4, BigDecimal(MoneyUtils.mul(java.lang.Double.valueOf(item.lastDealPrize), java.lang.Double.valueOf(item.rate)))))

                helper.setText(R.id.tv_value, MoneyUtils.decimalByUp(2, BigDecimal(item.lastDealPrize)).toPlainString())
                val ratio = MoneyUtils.decimal2ByUp(BigDecimal(item.fupanddown))
                val text = (if (item.fupanddown >= 0) "+" else "") + ratio + "%"

                helper.setText(R.id.tv_range, text).setTextColor(R.id.tv_range, if (item.fupanddown < 0) resources.getColor(R.color.price_red) else resources.getColor(R.color.price_green))
            }
        }
//        adapter.setOnItemClickListener(BaseQuickAdapter.OnItemClickListener { adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int ->
//            val intent = Intent(this@FinanceDetailActivity, WithDrawRecordDetailActivity::class.java)
//            intent.putExtra("type", if (datas.get(position).type == 1) getString(R.string.recharge) else getString(R.string.withdraw))
//            intent.putExtra("recordDetail", datas.get(position))
//            intent.putExtra("img_url", rechargeCoinBean.getUrl())
//            intent.putExtra("currency_name", rechargeCoinBean.getName())
//            startActivityForResult(intent, 0x1)
//        })
//        adapter.setOnLoadMoreListener(RequestLoadMoreListener {
//            isRefresh = false
//            page++
//            requestFinanceList()
//        }, recyclerView)
        adapter!!.setEmptyView(R.layout.layout_no_data, recyclerView!!.parent as ViewGroup)
        val emptyView = adapter!!.getEmptyView()
        val imageView = emptyView.findViewById<ImageView>(R.id.iv_message)
        val params = imageView.layoutParams as RelativeLayout.LayoutParams
        params.setMargins(0, 0, 0, 0)
        params.width = 300
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_finance_detail
    }

    @OnClick(R.id.iv_menu)
    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onResume() {
        super.onResume()
        page = 1
        isRefresh = true
//        requestFinanceList()
        requesetFinanceDetail()
    }

    private fun requesetFinanceDetail() {
        RetrofitHelper
                .getIns()
                .zgtopApi
                .getCoinFinanceDetail(Integer.valueOf(rechargeCoinBean!!.id))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .`as`(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this, Lifecycle.Event.ON_DESTROY)))
                .subscribe(object : BaseObserver<RequestResult<RechargeCoinBean>>() {
                    override fun success(rechargeCoinBeanRequestResult: RequestResult<RechargeCoinBean>) {
                        val rechargeCoinBean = rechargeCoinBeanRequestResult.data
                        var value = 0.0
                        if (!TextUtils.isEmpty(rechargeCoinBean.total)) {
                            value = java.lang.Double.valueOf(rechargeCoinBean.total)
                        }
                        var frozen = 0.0
                        if (!TextUtils.isEmpty(rechargeCoinBean.frozen)) {
                            frozen = java.lang.Double.valueOf(rechargeCoinBean.frozen)
                        }
                        val total = MoneyUtils.add(value, frozen)
                        tvTotal!!.text = MoneyUtils.decimal4ByUp(BigDecimal(total)).toPlainString()
                        tvValue!!.text = MoneyUtils.decimal4ByUp(BigDecimal(value)).toPlainString()
                        tvFrozen!!.text = MoneyUtils.decimal4ByUp(BigDecimal(frozen)).toPlainString()
                        if (LanguageUtils.currentLanguage != 1) {
                            tvValuations!!.text = MoneyUtils.decimal4ByUp(BigDecimal(MoneyUtils.mul(total, singlePrice))).toPlainString() + getString(R.string.money_unit)
                        } else {
                            tvValuations!!.text = MoneyUtils.decimal4ByUp(BigDecimal(MoneyUtils.mul(total, singlePrice))).toPlainString() + getString(R.string.money_unit)
                        }
                    }

                    public override fun failed(code: Int, data: String?, msg: String?) {}
                })
    }

//    private fun requestFinanceList() {
//        OkHttpUtils
//                .get()
//                .addHeader("X-Requested-With", "XMLHttpReques")
//                .url(Constants.BASE_URL + "v1/account/transferCoinRecord")
//                .addParams("symbol", rechargeCoinBean!!.id)
//                .addParams("page", page.toString() + "")
//                .addParams("pageSize", "20")
//                .build()
//                .execute(object : StringCallback() {
//                    override fun onError(call: Call, e: Exception, id: Int) {
//                        adapter!!.setEnableLoadMore(false)
//                    }
//
//                    @Throws(JSONException::class)
//                    override fun onResponse(response: String, id: Int) {
//                        val jsonObject = JSONObject(response)
//                        if (jsonObject.getInt("code") == 200) {
//                            val tempData = GsonUtil.jsonToList(jsonObject.getString("data"), WithdrawCoinrecordBean.DataBean::class.java)
//                            if (tempData.size < 20) {
//                                adapter!!.setEnableLoadMore(false)
//                            }
//                            if (isRefresh) {
//                                datas!!.clear()
//                                isRefresh = false
//                                adapter!!.setEnableLoadMore(true)
//                            } else {
//                                adapter!!.loadMoreComplete()
//                            }
//                            datas!!.addAll(tempData)
//                            adapter!!.notifyDataSetChanged()
//                        }
//                    }
//                })
//    }

    @OnClick(R.id.btn_recharge, R.id.btn_withdraw)
    fun onClick(view: View) {
        when (view.id) {
            R.id.btn_recharge -> if (rechargeCoinBean!!.isRecharge) {
                val itCurrencyReCharge = Intent(this, CurrencyReChargeActivity::class.java)
                itCurrencyReCharge.putExtra("currency_name", rechargeCoinBean!!.allName)
                itCurrencyReCharge.putExtra("symbol", rechargeCoinBean!!.id)
                itCurrencyReCharge.putExtra("shortName", rechargeCoinBean!!.name)
                itCurrencyReCharge.putExtra("img_url", rechargeCoinBean!!.url)
                startActivity(itCurrencyReCharge)
            } else {
//                    ToastUtils.showToast(getString(R.string.stop_recharge));
                showToast(getString(R.string.stop_recharge))
            }
            R.id.btn_withdraw -> if (rechargeCoinBean!!.isWithDraw) {
                val itWithDrawCurrency = Intent(this, WithdrawCurrencyActivity::class.java)
                itWithDrawCurrency.putExtra("currency_name", rechargeCoinBean!!.allName)
                itWithDrawCurrency.putExtra("symbol", rechargeCoinBean!!.id)
                itWithDrawCurrency.putExtra("shortName", rechargeCoinBean!!.name)
                itWithDrawCurrency.putExtra("frozen", rechargeCoinBean!!.frozen)
                itWithDrawCurrency.putExtra("total", rechargeCoinBean!!.total)
                itWithDrawCurrency.putExtra("img_url", rechargeCoinBean!!.url)
                startActivity(itWithDrawCurrency)
            } else {
//                    ToastUtils.showToast(getString(R.string.stop_withdraw));
                showToast(getString(R.string.stop_withdraw))
            }
        }
    }
}
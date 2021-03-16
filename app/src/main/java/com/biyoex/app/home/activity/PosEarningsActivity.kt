package com.biyoex.app.home.activity

import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.kotlin.autoDisposable
import com.biyoex.app.R
import com.biyoex.app.common.base.BaseActivity
import com.biyoex.app.common.bean.PosEarningsBean
import com.biyoex.app.common.bean.RequestResult
import com.biyoex.app.common.data.AppData
import com.biyoex.app.common.http.RetrofitHelper
import com.biyoex.app.common.mvpbase.BaseObserver
import com.biyoex.app.common.utils.DateUtilsl
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.base_refresh_recyclerview.*
import kotlinx.android.synthetic.main.layout_title_while_bar.*
import java.text.NumberFormat

class PosEarningsActivity : BaseActivity(), OnRefreshLoadmoreListener {
    override fun onLoadmore(refreshlayout: RefreshLayout?) {
        page++
        initView()
    }

    override fun onRefresh(refreshlayout: RefreshLayout?) {
        page = 1
        initView()
    }

    var mAdapter = PosEarningsAdapter()
    var coinId = 0
    var page = 1
    var pageSize = 10
    override fun initView() {
        showLoadingDialog()
        RetrofitHelper.getIns().zgtopApi
                .getMyPosEarningsList(AppData.getRoleId(), coinId, page, pageSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .autoDisposable(AndroidLifecycleScopeProvider.from(this, Lifecycle.Event.ON_DESTROY))//OnDestory时自动解绑
                .subscribe(object : BaseObserver<RequestResult<MutableList<PosEarningsBean>>>() {
                    override fun success(t: RequestResult<MutableList<PosEarningsBean>>) {
                        hideLoadingDialog()
                        smartrefresh.finishRefresh()
                        smartrefresh.finishLoadmore()
                        if (t!!.data.isNullOrEmpty()) {
                            showToast("暂没有数据")
                        } else {
                            if (page == 1) {
                                mAdapter.setNewData(t.data)
                            } else {
                                mAdapter.data.addAll(t.data)
                                mAdapter.notifyDataSetChanged()
                            }
                        }
                    }

                    override fun failed(code: Int, data: String?, msg: String?) {
                        super.failed(code, data, msg)
                        hideLoadingDialog()
                    }

                })
    }

    override fun initData() {
        iv_menu.setOnClickListener { finish() }
        tv_title.text = "收益明细"
        coinId = intent.getIntExtra("coinId", 0)
        base_recyclerview.layoutManager = LinearLayoutManager(this)
        base_recyclerview.adapter = mAdapter
        mAdapter.emptyView = View.inflate(this, R.layout.layout_no_data, null)
        smartrefresh.setOnRefreshLoadmoreListener(this)
    }

    override fun getLayoutId(): Int = R.layout.activity_pos_earnings
}


class PosEarningsAdapter : BaseQuickAdapter<PosEarningsBean, BaseViewHolder>(R.layout.item_coin_pos) {
    override fun convert(helper: BaseViewHolder?, item: PosEarningsBean) {
        helper!!.setText(R.id.tv_item_time, DateUtilsl.time_today(item!!.createTime))
                .setText(R.id.tv_item_number, NumberFormat.getInstance().format(item.holdAmount))
        if (item.spreadProfit.isNotEmpty()) {
//            val df = DecimalFormat("#.0000")
//            val oneNumber = df.format(item.holdProfit);
//            val twoNumber = df.format(item.spreadProfit);
            val size = item.holdProfit + item.coinName;
            val ss = SpannableString(item.holdProfit + item.coinName + "+" + item.spreadProfit + item.spreadCoinName)
//            val ss = SpannableString("${NumberFormat.getInstance().format(item.holdProfit.toDouble())}+${NumberFormat.getInstance().format(item.spreadProfit.toDouble())}")
            val foregroundColorSpan = ForegroundColorSpan(mContext.resources.getColor(R.color.price_red))
            ss.setSpan(foregroundColorSpan, size.length, ss.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            helper.setText(R.id.tv_item_earnings, ss)
        } else helper.setText(R.id.tv_item_earnings, item.holdProfit)
    }

}



package com.biyoex.app.property


import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View

import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener
import com.biyoex.app.R
import com.biyoex.app.common.bean.RequestResult
import com.biyoex.app.common.mvpbase.BaseActivity

import com.biyoex.app.property.adapter.LockHistoryAdapter
import com.biyoex.app.property.datas.LockHistoryBean
import com.biyoex.app.property.datas.LockHistoryDataBean
import com.biyoex.app.property.presenter.PropertyHistoryPresenter
import com.biyoex.app.property.view.PropertyHistoryView
import kotlinx.android.synthetic.main.activity_property_history.*
import kotlinx.android.synthetic.main.layout_newtitle_bar.*
import java.math.BigDecimal

//锁仓历史记录
class LockHistoryActivity : BaseActivity<PropertyHistoryPresenter>(), PropertyHistoryView, OnRefreshLoadmoreListener {
    override fun onLoadmore(refreshlayout: RefreshLayout?) {
        page++;
        initComp()
    }

    override fun onRefresh(refreshlayout: RefreshLayout?) {
        data.clear()
        page = 1;
        initComp()
    }

    lateinit var mAdapter: LockHistoryAdapter
    var data: MutableList<LockHistoryDataBean> = mutableListOf()
    override fun getLockHistorySuccess(datas: RequestResult<LockHistoryBean>) {
        smartrefresh.finishLoadmore()
        smartrefresh.finishRefresh()
        tv_locksum.text = "${BigDecimal(datas!!.data.lockNum).setScale(2, BigDecimal.ROUND_HALF_UP)}"
        tv_lockret.text = "${BigDecimal(datas!!.data.releaseNum).setScale(2, BigDecimal.ROUND_HALF_UP)}"
        tv_lockinfo_new.text = "${BigDecimal(datas!!.data.cnyValue).setScale(2, BigDecimal.ROUND_HALF_UP)}"
        tv_title.text = datas!!.data.coinName
        page = datas!!.page
        pageSize = datas!!.totalCount
        data!!.addAll(datas!!.data!!.data)
        mAdapter.setNewData(data)
    }

    override fun getLockHistoryError() {
        smartrefresh.finishLoadmore()
        smartrefresh.finishRefresh()
    }

    override fun getLayoutId(): Int = R.layout.activity_property_history
    override fun createPresent(): PropertyHistoryPresenter = PropertyHistoryPresenter()

    override fun initData() {
        tv_title.text = intent.getStringExtra("coinName")
        iv_right.visibility = View.GONE
        rv_lockhistory.layoutManager = LinearLayoutManager(this)
        mAdapter = LockHistoryAdapter()
        rv_lockhistory.adapter = mAdapter
        smartrefresh.setOnRefreshLoadmoreListener(this)
        val view = LayoutInflater.from(this).inflate(R.layout.layout_no_data, null, false)
        mAdapter.emptyView = view
        iv_menu.setOnClickListener { finish() }
    }

    override fun initComp() {
        mPresent.getLockHistory(intent.getStringExtra("coidId"), page, pageSize)
    }


    override fun setStatusBar() {
        super.setStatusBar()
        immersionBar.transparentNavigationBar()
        immersionBar.statusBarDarkFont(true)
    }
}

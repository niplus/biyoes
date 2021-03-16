package com.biyoex.app.property


import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.ViewGroup

import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener
import com.biyoex.app.R

import com.biyoex.app.common.mvpbase.BaseActivity

import com.biyoex.app.property.adapter.CoinRecordAdapter
import com.biyoex.app.property.datas.CoidRecordData
import com.biyoex.app.property.datas.CoinRecordBean
import com.biyoex.app.property.presenter.PropertyDetailsPresenter
import com.biyoex.app.property.view.PropertyDetailsView
import kotlinx.android.synthetic.main.activity_property_details.*
import kotlinx.android.synthetic.main.layout_newtitle_bar.*


//币种资产财务记录
class PropertyRecordActivity : BaseActivity<PropertyDetailsPresenter>(), PropertyDetailsView, OnRefreshLoadmoreListener {
    var type: Int? = null
    lateinit var mAdapter: CoinRecordAdapter
    var mdata = mutableListOf<CoidRecordData>()
    override fun createPresent(): PropertyDetailsPresenter = PropertyDetailsPresenter()

    override fun getLayoutId(): Int = R.layout.activity_property_details
    override fun initData() {
        type = intent.getIntExtra("type", 1)
//        datas = intent.getParcelableExtra("data")
        iv_menu.setOnClickListener { finish() }
        recyclerview_record.layoutManager = LinearLayoutManager(this)
        mAdapter = CoinRecordAdapter()
        recyclerview_record.adapter = mAdapter
        smartrefresh.setOnRefreshLoadmoreListener(this)
        mAdapter.emptyView = LayoutInflater.from(this).inflate(R.layout.view_empty, recyclerview_record.parent as ViewGroup, false)
    }

    override fun initComp() {
        mPresent.getRecordData(intent.getIntExtra("coidId", 0), page, pageSize)
    }

    override fun getRecordDataSuccess(mCoinRecordBean: CoinRecordBean, page: Int, pageSize: Int) {
        smartrefresh.finishRefresh()
        smartrefresh.finishLoadmore()
        tv_title.text = "${mCoinRecordBean.coinName}${resources.getString(R.string.financial_record)}"
        val list = mCoinRecordBean.data
        mdata!!.addAll(mCoinRecordBean.data)
        mAdapter.setNewData(mdata)
        this.page = page
        this.pageSize = pageSize
    }

    override fun getRecordDataError() {
        smartrefresh.finishRefresh()
        smartrefresh.finishLoadmore()
    }

    override fun onRefresh(refreshlayout: RefreshLayout?) {
        mdata.clear()
        page = 1
        initComp()
    }
    override fun onLoadmore(refreshlayout: RefreshLayout?) {
        page++
        initComp()
    }
}


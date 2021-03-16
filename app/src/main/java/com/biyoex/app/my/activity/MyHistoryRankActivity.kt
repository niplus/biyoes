package com.biyoex.app.my.activity

import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener
import com.biyoex.app.R
import com.biyoex.app.common.mvpbase.BaseActivity
import com.biyoex.app.common.bean.HistoryRankBean
import com.biyoex.app.common.bean.MyHistoryBean
import com.biyoex.app.my.adapter.MyHistoryRankAdapter
import com.biyoex.app.my.presenter.MyHistoryRankPresenter
import com.biyoex.app.my.view.MyHistoryRankView
import kotlinx.android.synthetic.main.activity_my_history_rank.*
import kotlinx.android.synthetic.main.base_refresh_recyclerview.*


//我的历史排名
class MyHistoryRankActivity : BaseActivity<MyHistoryRankPresenter>(), MyHistoryRankView, OnRefreshLoadmoreListener {

    var mAdapter: MyHistoryRankAdapter = MyHistoryRankAdapter()
    var mDataList: MutableList<HistoryRankBean> = mutableListOf()

    override fun createPresent(): MyHistoryRankPresenter = MyHistoryRankPresenter()
    override fun getLayoutId(): Int = R.layout.activity_my_history_rank
    override fun initComp() {
        tv_title.text = resources.getString(R.string.my_history_rank)
        iv_menu.setOnClickListener { finish() }
        mPresent.getMyHistoryRankData(page, pageSize)
    }
    override fun initData() {
        smartrefresh.setOnRefreshLoadmoreListener(this)
        base_recyclerview.layoutManager = LinearLayoutManager(this)
        base_recyclerview.adapter = mAdapter
        mAdapter.emptyView = View.inflate(context, R.layout.layout_no_data, null)
    }

    //获取历史排名数据
    override fun getHistoryRankSuccess(myHistoryBean: MyHistoryBean) {
        hideLoadingDialog()
        smartrefresh.finishRefresh()
        smartrefresh.finishLoadmore()
        if (page == 1) {
            mDataList.clear()
        }
        mDataList.addAll(myHistoryBean.weekList)
        mAdapter.setNewData(mDataList)
        tv_rushbuy_invite_parent_num.text = "${myHistoryBean.totalReward}NB"
        tv_rushbuy_invite_coin_num.text = "${myHistoryBean.releaseReward}NB"
    }

    override fun onLoadmore(refreshlayout: RefreshLayout?) {
        page++
        mPresent.getMyHistoryRankData(page, pageSize)
    }

    override fun onRefresh(refreshlayout: RefreshLayout?) {
        page = 1
        mPresent.getMyHistoryRankData(page, pageSize)
    }

    override fun setStatusBar() {
        super.setStatusBar()
        immersionBar.statusBarDarkFont(false)
    }
}

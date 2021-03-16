package com.biyoex.app.my.activity

import android.annotation.SuppressLint
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener
import com.biyoex.app.R
import com.biyoex.app.common.mvpbase.BaseActivity
import com.biyoex.app.common.bean.MyInviteVipBean
import com.biyoex.app.common.bean.MyInviteVipData
import com.biyoex.app.my.adapter.MyInviteVipAdapter
import com.biyoex.app.my.presenter.MyInviteVipPresenter
import com.biyoex.app.my.view.MyInviteVipView
import kotlinx.android.synthetic.main.activity_my_invite_vip.*
import java.math.BigDecimal
    /**
     * 我的会员邀请
     * */
class MyInviteVipActivity : BaseActivity<MyInviteVipPresenter>(), MyInviteVipView, OnRefreshLoadmoreListener {

    var mPage: Int = 1
    var mPageSzie = 20
    var mAdapter: MyInviteVipAdapter = MyInviteVipAdapter()
    var mInviteVipList: MutableList<MyInviteVipData> = mutableListOf()
    override fun createPresent(): MyInviteVipPresenter = MyInviteVipPresenter()

    override fun getLayoutId(): Int = R.layout.activity_my_invite_vip
    override fun initComp() {
        mPresent.getMyInviteVip(mPage, mPageSzie)
    }

    @SuppressLint("ResourceType")
    override fun initData() {
        iv_menu.setOnClickListener { finish() }
        tv_title.text = resources.getString(R.string.my_invite_vip_record)
        rv_invite_reward.layoutManager = LinearLayoutManager(this)
        rv_invite_reward.adapter = mAdapter
        val view = View.inflate(context, R.layout.layout_no_data, null)
        mAdapter.emptyView = view
        smartrefresh.setOnRefreshLoadmoreListener(this)
    }

    override fun getMyInviteSuccess(mData: MyInviteVipBean) {
        smartrefresh.finishLoadmore()
        smartrefresh.finishRefresh()
        tv_my_invite_amount.text = BigDecimal(mData.sumBalance).setScale(4, BigDecimal.ROUND_HALF_UP).toPlainString()
        if (mPage == 1) {
            mInviteVipList.clear()
            mInviteVipList.addAll(mData.data)
            mAdapter.setNewData(mInviteVipList)
        }
    }

    override fun setStatusBar() {
        super.setStatusBar()
        immersionBar.statusBarDarkFont(false)
    }

    override fun onRefresh(refreshlayout: RefreshLayout?) {
        mPage = 1
        initComp()
    }

    override fun onLoadmore(refreshlayout: RefreshLayout?) {
        mPage++
        initComp()
    }
}

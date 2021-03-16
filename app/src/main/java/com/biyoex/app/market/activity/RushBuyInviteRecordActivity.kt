package com.biyoex.app.market

import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener
import com.biyoex.app.R
import com.biyoex.app.common.mvpbase.BaseActivity
import com.biyoex.app.common.bean.RushBuyInviteBean
import com.biyoex.app.common.bean.WeekRewardBean
import com.biyoex.app.market.adapter.RushBuyInviteAdapter
import com.biyoex.app.market.presenter.RushBuyInviteRecordPresenter
import com.biyoex.app.market.view.RushBuyInviteRecordView
import com.biyoex.app.my.activity.MyRankActivity
import kotlinx.android.synthetic.main.activity_rush_buy_invite_record.*
import kotlinx.android.synthetic.main.activity_rush_buy_invite_record.iv_menu
import kotlinx.android.synthetic.main.activity_rush_buy_invite_record.iv_right

/**
 * 抢购邀请记录
 * */
class RushBuyInviteRecordActivity : BaseActivity<RushBuyInviteRecordPresenter>(), RushBuyInviteRecordView, OnRefreshLoadmoreListener {

    override fun getUserRecordRewardSuccess(mWeekRewardBean: WeekRewardBean) {
        tv_rushbuy_invite_parent_num.text = mWeekRewardBean.usdtAmount
        tv_rushbuy_invite_coin_num.text = mWeekRewardBean.ndAmount
        tv_rushbuy_invite_coin_released.text = "${getString(R.string.coin_rel)}${mWeekRewardBean.ndReleaseAmount}"
        tv_user_rank.text = mWeekRewardBean.sort
    }

    override fun onLoadmore(refreshlayout: RefreshLayout?) {

        page++
        mPresent.getUserRushInviteData(mIndex, page, pageSize)
    }

    override fun onRefresh(refreshlayout: RefreshLayout?) {
        page = 1
        mPresent.getUserRushInviteData(mIndex, page, pageSize)
    }

    var mIndex = 1;
    var mInviteRewardList = mutableListOf<RushBuyInviteBean>()
    var mAdapter: RushBuyInviteAdapter = RushBuyInviteAdapter(1)
    var mMoneyAdapter: RushBuyInviteAdapter = RushBuyInviteAdapter(2)
    override fun getLayoutId(): Int = R.layout.activity_rush_buy_invite_record

    override fun createPresent(): RushBuyInviteRecordPresenter = RushBuyInviteRecordPresenter()

    override fun initComp() {
        showLoadingDialog()
        mPresent.getUserRushInviteData(mIndex, page, pageSize)
        mPresent.getRewardWeek()
        rv_invite_record.layoutManager = LinearLayoutManager(this)
        rv_invite_record.adapter = mAdapter

        rv_invite_amount.layoutManager = LinearLayoutManager(this)
        rv_invite_amount.adapter = mMoneyAdapter
//        val view =
//        val view2 = View.inflate(context, R.layout.layout_no_data, null)
        mAdapter.emptyView = View.inflate(context, R.layout.layout_no_data, null)
        mMoneyAdapter.emptyView = View.inflate(context, R.layout.layout_no_data, null)
        smartrefresh.setOnRefreshLoadmoreListener(this)
    }

    override fun initData() {
        tv_user_invite.setOnClickListener {
            mIndex = 1
            page = 1
            mInviteRewardList.clear()
            mPresent.getUserRushInviteData(mIndex, page, pageSize)
            rv_invite_amount.visibility = View.GONE
            rv_invite_record.visibility = View.VISIBLE
            tv_user_invite.setTextColor(resources.getColor(R.color.black))
            tv_user_weward.setTextColor(resources.getColor(R.color.gray_to_black))
        }
        tv_user_weward.setOnClickListener {
            mIndex = 2
            page = 1
            mInviteRewardList.clear()
            mPresent.getUserRushInviteData(mIndex, page, pageSize)
            rv_invite_amount.visibility = View.VISIBLE
            rv_invite_record.visibility = View.GONE
            tv_user_weward.setTextColor(resources.getColor(R.color.black))
            tv_user_invite.setTextColor(resources.getColor(R.color.gray_to_black))
        }
        tv_see_details.setOnClickListener { startActivity(Intent(this, MyRankActivity::class.java)) }
        iv_right.setOnClickListener { finish() }
        iv_menu.setOnClickListener { finish() }
    }

    override fun getUserRecordSuccess(mData: MutableList<RushBuyInviteBean>, type: Int) {
        hideLoadingDialog()
        smartrefresh.finishRefresh()
        smartrefresh.finishLoadmore()
        if (page == 1) {
            mInviteRewardList.clear()
        }
        mInviteRewardList.addAll(mData)
        if (type == 1) {
            mAdapter.setNewData(mInviteRewardList)
        } else {
            mMoneyAdapter.setNewData(mInviteRewardList)
        }
    }

    override fun setStatusBar() {
        super.setStatusBar()
        immersionBar.statusBarDarkFont(false)
    }
}

package com.biyoex.app.my.activity

import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.biyoex.app.R
import com.biyoex.app.common.mvpbase.BaseActivity
import com.biyoex.app.common.bean.WeekListData
import com.biyoex.app.common.bean.WeekRankBean
import com.biyoex.app.market.adapter.MyRankAdapter
import com.biyoex.app.my.presenter.MyRankPresenter
import com.biyoex.app.my.view.MyRankView
import kotlinx.android.synthetic.main.activity_my_rank.*
import kotlinx.android.synthetic.main.activity_my_rank.iv_menu
import kotlinx.android.synthetic.main.activity_my_rank.iv_right
import kotlinx.android.synthetic.main.activity_my_rank.tv_title

//我的排名
class MyRankActivity : BaseActivity<MyRankPresenter>(), MyRankView, OnRefreshListener {

    var mAdapter: MyRankAdapter = MyRankAdapter()
    var mDataList = mutableListOf<WeekListData>()
    var mWeekIndex = 1
    var tag = 0
    override fun createPresent(): MyRankPresenter = MyRankPresenter()
    override fun getLayoutId(): Int = R.layout.activity_my_rank
    override fun initComp() {
        rv_invite_reward.layoutManager = LinearLayoutManager(this)
        rv_invite_reward.adapter = mAdapter
        val view = View.inflate(context, R.layout.layout_no_data, null)
        mAdapter.emptyView = view
        smartrefresh.setOnRefreshListener(this)
        smartrefresh.isEnableLoadmore = false
    }
    override fun initData() {
        mWeekIndex = intent.getIntExtra("week", 0)
        tag = intent.getIntExtra("tag", 0)
        if (tag == 1) {
            iv_right.visibility = View.GONE
        }
        immersionBar.statusBarDarkFont(false)
        tv_title.text = resources.getString(R.string.my_rank)
        iv_right.text = resources.getString(R.string.history_rank_record)
        iv_menu.setOnClickListener { finish() }
        iv_right.setOnClickListener {
            startActivity(Intent(this, MyHistoryRankActivity::class.java))
        }
        mPresent.getWeekRank(mWeekIndex, page, pageSize)
    }

    override fun getWeekRankSuccess(weekRankBean: WeekRankBean) {
        tv_now_time.text = "第${weekRankBean.week}周排名"
        tv_now_rank.text = "${weekRankBean.sort}"
        tv_now_number.text = "第${weekRankBean.week}周NB销量数量:${weekRankBean.salesVolume}"
        tv_now_weward.text = "我的${weekRankBean.week}周排名奖励：${weekRankBean.rankingReward}"
        tv_relweward.text = "已释放奖励:${weekRankBean.releaseReward}"
        tv_account_myrank_one.text = weekRankBean.firstSortName
        tv_num_myrank_one.text = "${weekRankBean.firstUnderNum}人"
        tv_money_myrank_one.text = "${weekRankBean.firstUserId}"
        tv_account_myrank_two.text = weekRankBean.secondSortName
        tv_num_myrank_two.text = "${weekRankBean.secondUnderNum}人"
        tv_money_myrank_two.text = "${weekRankBean.secondUserId}"
        tv_account_myrank_three.text = weekRankBean.thirdSortName
        tv_num_myrank_three.text = "${weekRankBean.thirdUnderNum}人"
        tv_money_myrank_three.text = "${weekRankBean.thirdUserId}"
        smartrefresh.finishLoadmore()
        smartrefresh.finishRefresh()
        if (page == 1) mDataList.clear()
        mDataList.addAll(weekRankBean.data)
        mAdapter.setNewData(mDataList)
    }

    override fun onRefresh(refreshlayout: RefreshLayout?) {
        page = 1
        mPresent.getWeekRank(mWeekIndex, page, pageSize)
    }

    override fun setStatusBar() {
        super.setStatusBar()
        immersionBar.statusBarDarkFont(false)
    }
}

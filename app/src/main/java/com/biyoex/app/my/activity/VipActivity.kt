package com.biyoex.app.my.activity

import android.content.Intent
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import com.biyoex.app.R
import com.biyoex.app.common.mvpbase.BaseActivity
import com.biyoex.app.common.utils.DateUtilsl
import com.biyoex.app.common.utils.log.Log
import com.biyoex.app.common.bean.VipData
import com.biyoex.app.common.bean.VipPriceBean
import com.biyoex.app.my.adapter.VipPriceAdapter
import com.biyoex.app.my.presenter.VipPresenter
import com.biyoex.app.my.view.VipView
import kotlinx.android.synthetic.main.activity_vip.*

class VipActivity : BaseActivity<VipPresenter>(), VipView {

    lateinit var mAdapter: VipPriceAdapter
    var mIndex: Int = 0
    var mLevel: Int = 0
    var mVipList: MutableList<VipData> = mutableListOf()
    override fun createPresent(): VipPresenter = VipPresenter()
    override fun getLayoutId(): Int = R.layout.activity_vip
    override fun initData() {
        immersionBar.statusBarDarkFont(false)
        mPresent.getVipPriceData()
        tv_title.text = resources.getString(R.string.vip_title)
        iv_right.text = resources.getString(R.string.vip_invite)
        mAdapter = VipPriceAdapter()
        rv_invite_reward.layoutManager = LinearLayoutManager(this)
        rv_invite_reward.adapter = mAdapter
        val view = View.inflate(context, R.layout.layout_no_data, null)
        mAdapter.emptyView = view
        seekbar_viprank.setUserSeekAble(false)
        }
    override fun initComp() {
        iv_menu.setOnClickListener { finish() }
        iv_right.setOnClickListener {
            var intent = Intent(this, MyInviteVipActivity::class.java)
            startActivity(intent)
        }

        mAdapter.setOnItemClickListener { _, _, position ->
            if (position >= mLevel) {
                mIndex = mVipList[position].grade
                mAdapter.selectPosition(position)
            }
        }
        btn_confirm.setOnClickListener {
            if (mIndex > 4) {
                showToast(getString(R.string.max_level_vip))
            } else {
                mPresent.getBuyVipPrice(mIndex)
            }
        }
    }

    override fun getVipPriceSuccess(mData: VipPriceBean) {
        Log.e("${mData.level}")
        if (mData.level != 0) {
//            Log.e("${mData.level}更新时间")
            tv_vipend_time.visibility = View.VISIBLE
            if (!mData.endTime.equals("--")) {
                tv_vipend_time.text = "${getString(R.string.end_time)}${DateUtilsl.time_meddle(mData.endTime)}"
            }
        } else {
            tv_vipend_time.visibility = View.GONE
        }
        mVipList = mData.data
        mAdapter.setNewData(mData.data)
        mLevel = mData.level
        mIndex = mData.level + 1
        mAdapter.getLevel(mData.level)
        mAdapter.selectPosition(mData.level)
        seekbar_viprank.setProgress(mData.level.toFloat() * 25)
    }
    override fun setStatusBar() {
        super.setStatusBar()
        immersionBar.statusBarDarkFont(false)
    }
    override fun getBuyVipPriceSuccess(mPrice: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(com.lzy.imagepicker.R.string.hint)
        builder.setMessage("${getString(R.string.is_buy)}$mPrice"+getString(R.string.update_vip))
        builder.setNegativeButton(com.lzy.imagepicker.R.string.cancel, null)
        builder.setPositiveButton(R.string.sure) { _, _ ->
            mPresent.postBuyVip(mIndex)
        }
        builder.show()
    }

}

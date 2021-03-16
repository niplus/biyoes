package com.biyoex.app.my.activity

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import com.biyoex.app.R
import com.biyoex.app.common.Constants
import com.biyoex.app.common.bean.InviteReturnRankBean
import com.biyoex.app.common.bean.MineInviteReturnBean
import com.biyoex.app.common.data.SessionLiveData
import com.biyoex.app.common.mvpbase.BaseActivity
import com.biyoex.app.common.widget.SharePostersDialog
import com.biyoex.app.my.adapter.InviteReturnAdapter
import com.biyoex.app.my.adapter.MyInviteRetrunAdapter
import com.biyoex.app.my.presenter.InviteDetailsPresenter
import com.biyoex.app.my.view.InviteDetailsView
import kotlinx.android.synthetic.main.activity_invite_return_details.*
import kotlinx.android.synthetic.main.layout_title_bar.*
import java.math.BigDecimal

/**
 * 邀请返佣页面
 * */
class InviteReturnDetailsActivity : BaseActivity<InviteDetailsPresenter>(), InviteDetailsView {
    var isOpen = false
    lateinit var mInviteRankAdapter: InviteReturnAdapter
    lateinit var mMineInviteAdapter: MyInviteRetrunAdapter
    override fun getLayoutId(): Int = R.layout.activity_invite_return_details
    override fun createPresent(): InviteDetailsPresenter = InviteDetailsPresenter()

    override fun initData() {
        recyclerview.layoutManager = LinearLayoutManager(this)
        rv_mine_invite.layoutManager = LinearLayoutManager(this)
        mInviteRankAdapter = InviteReturnAdapter()
        mMineInviteAdapter = MyInviteRetrunAdapter()
        mMineInviteAdapter.emptyView = View.inflate(context, R.layout.view_empty, null)
        recyclerview.adapter = mInviteRankAdapter
        rv_mine_invite.adapter = mMineInviteAdapter
        tv_invite_code.text = "${getString(R.string.my_invite_code)}${Constants.shareId}"
        tv_title.text = getString(R.string.invite_retrun)
    }

    //获取邀请排行
    override fun getInviteRankDataSuccess(mRankData: MutableList<InviteReturnRankBean>) {
        if (!mRankData.isNullOrEmpty() && mRankData.size > 3) {
            tv_one_userid.text = "${mRankData[0].userId}"
            tv_one_usdt.text = "${BigDecimal(mRankData[0].balance).setScale(2, BigDecimal.ROUND_DOWN)}USDT"
            tv_two_userid.text = "${mRankData[1].userId}"
            tv_two_usdt.text = "${BigDecimal(mRankData[1].balance).setScale(2, BigDecimal.ROUND_DOWN)}USDT"
            tv_three_usdt.text = "${BigDecimal(mRankData[2].balance).setScale(2, BigDecimal.ROUND_DOWN)}USDT"
            tv_three_userid.text = "${mRankData[2].userId}"
            if (isOpen) {
                tv_see_details.visibility = View.GONE
                mInviteRankAdapter.setNewData(mRankData.subList(3, mRankData.size))
            }
        }
    }

    override fun initComp() {
        mPresent.getInviteRankData()
        mPresent.getMyInviteReturnData(page, pageSize)

        tv_see_details.setOnClickListener {
            isOpen = true
            mPresent.getInviteRankData()
        }

        tv_share_btn.setOnClickListener {
            val clipData1 = ClipData.newPlainText(null, "${Constants.REALM_NAME}/user/register?intro=${SessionLiveData.getIns().value!!.id}")
            val cmb = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            cmb.setPrimaryClip(clipData1)
            showToast(getString(R.string.copy_success))
        }
        iv_menu.setOnClickListener { finish() }

        tv_create_posters.setOnClickListener {
            SharePostersDialog(this).show()
        }
    }


    override fun getMyInviteSuccess(mInviteData: MutableList<MineInviteReturnBean>) {
        mMineInviteAdapter.setNewData(mInviteData)
    }

}

package com.biyoex.app.home.activity

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener
import com.biyoex.app.R
import com.biyoex.app.common.Constants
import com.biyoex.app.common.base.BaseActivity
import com.biyoex.app.common.bean.PosComputingBean
import com.biyoex.app.common.bean.PosInvitationNumberBean
import com.biyoex.app.common.bean.RequestResult
import com.biyoex.app.common.data.AppData
import com.biyoex.app.common.http.RetrofitHelper
import com.biyoex.app.common.mvpbase.BaseObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_pos_computing.*
import kotlinx.android.synthetic.main.base_refresh_recyclerview.*
import kotlinx.android.synthetic.main.layout_title_while_bar.*
import java.text.NumberFormat

/**
 * Created by mac on 20/7/30.
 * Pos算力和邀请明细
 */

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class PosComputingActivity : BaseActivity(), OnRefreshLoadmoreListener {
    var page = 1
    var pageSize = 10
    var coinid = 0
    var coinName = ""
    var mAdapter = PosComputingAdapter()
    var mData = mutableListOf<MutableList<String>>()

    @SuppressLint("SetTextI18n")
    override fun initView() {
        //获取全网pos总量
        RetrofitHelper.getIns().zgtopApi
                .getPosComputings(AppData.getRoleId(),coinid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : BaseObserver<RequestResult<PosComputingBean>>() {
                    override fun success(t: RequestResult<PosComputingBean>) {
                        textView32.text = Constants.numberFormat(t!!.data.totalComputingPower.toDouble())
                        tv_share_computing.text = Constants.numberFormat(t.data.holdPos.toDouble())
                        tv_total_computing.text = Constants.numberFormat(t.data.spreadPos.toDouble())
                    }
                })

        //我的上级，我的邀请人数，小区邀请人数
        RetrofitHelper.getIns().zgtopApi
                .getPosInvitation(AppData.getRoleId(),coinid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : BaseObserver<RequestResult<PosInvitationNumberBean>>() {
                    override fun success(t: RequestResult<PosInvitationNumberBean>) {
                        tv_My_supervisor_id.text = t!!.data.mySuperior
                        tv_myInvite_number.text = t.data.myInvited
                        tv_team_total_cash.text = t.data.teamTotalHold
                        tv_communityInvite_number.text = t.data.minRegion;
                    }
                })

//        //之前的团队明细
//        RetrofitHelper.getIns().zgtopApi
//                .getmyInviteionDetails(coinid)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(object : BaseObserver<RequestResult<MutableList<MutableList<String>>>>() {
//                    override fun success(t: RequestResult<MutableList<MutableList<String>>>?) {
//                        mAdapter.setNewData(t!!.data)
//                    }
//
//                })

    }

    override fun onLoadmore(refreshlayout: RefreshLayout?) {
        page++
        getData();
    }

    override fun onRefresh(refreshlayout: RefreshLayout?) {
        page = 1
        getData();
    }

    //获取我的直推团队持仓
    private fun getData() {
        showLoadingDialog()
        RetrofitHelper.getIns().zgtopApi
                .getPosMyInvitationDetails(AppData.getRoleId(), coinid, page, pageSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : BaseObserver<RequestResult<MutableList<MutableList<String>>>>() {
                    override fun success(t: RequestResult<MutableList<MutableList<String>>>) {
                        hideLoadingDialog()
                        smartrefresh.finishRefresh()
                        smartrefresh.finishLoadmore()
                        if (page == 1) {
                            mData.clear()
                        }
                        mData.addAll(t!!.data)
                        mAdapter.setNewData(mData)
                    }
                    override fun failed(code: Int, data: String?, msg: String?) {
                        super.failed(code, data, msg)
                        hideLoadingDialog()
                    }
                })
    }

    @SuppressLint("SetTextI18n")
    override fun initData() {
        smartrefresh.setOnRefreshLoadmoreListener(this)
        iv_menu.setOnClickListener { finish() }
        tv_title.text = "POS算力"
        coinid = intent.getIntExtra("coinId", 0)
        coinName = intent.getStringExtra("coinName");
        textView26.text = "全网POS总量($coinName)"
        tv_coin_number.text = "持币数($coinName)"
        base_recyclerview.layoutManager = LinearLayoutManager(this)
        base_recyclerview.adapter = mAdapter
        mAdapter.emptyView = View.inflate(this, R.layout.layout_no_data, null)
        getData();

    }

    override fun getLayoutId(): Int = R.layout.activity_pos_computing
}

class PosComputingAdapter : BaseQuickAdapter<MutableList<String>, BaseViewHolder>(R.layout.item_poscomputing) {
    override fun convert(helper: BaseViewHolder?, item: MutableList<String>?) {
        helper!!.setText(R.id.item_poscomputing_phone, item!![0])
                .setText(R.id.item_poscomputing_number, NumberFormat.getInstance().format(item[1].toDouble()))
    }
}


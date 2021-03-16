package com.biyoex.app.my.activity

import androidx.recyclerview.widget.LinearLayoutManager
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener
import com.biyoex.app.R
import com.biyoex.app.common.base.BaseActivity
import com.biyoex.app.common.bean.RequestResult
import com.biyoex.app.common.http.RetrofitHelper
import com.biyoex.app.common.mvpbase.BaseObserver
import com.biyoex.app.my.adapter.MyPartnerInviteAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.base_refresh_recyclerview.*
import kotlinx.android.synthetic.main.layout_title_bar.*

class PartnerInviteActivity : BaseActivity(), OnRefreshLoadmoreListener {
    override fun onLoadmore(refreshlayout: RefreshLayout?) {
        page++
        getData()
    }

    override fun onRefresh(refreshlayout: RefreshLayout?) {
        page = 1
        getData()
    }

    var page = 1
    var pagesize = 10
    var mAdapter: MyPartnerInviteAdapter? = null
    var mData = mutableListOf<MutableList<String>>()
    override fun getLayoutId(): Int = R.layout.activity_partner_invite

    override fun initView() {
        smartrefresh.setOnRefreshLoadmoreListener(this)
    }

    override fun initData() {
        rl_menu.setOnClickListener { finish() }
        tv_title.text = getString(R.string.invite_detail)
        base_recyclerview.layoutManager = LinearLayoutManager(this)
        mAdapter = MyPartnerInviteAdapter()
//        mAdapter?.emptyView =  View.inflate(context, R.layout.view_empty, null)
        base_recyclerview.adapter = mAdapter
        getData()
    }
    private fun getData() {
        showLoadingDialog()
        RetrofitHelper.getIns().zgtopApi
                .getMyPartnerInvite(page, pagesize)
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
                        mAdapter?.setNewData(mData)
                    }

                    override fun failed(code: Int, data: String?, msg: String?) {
                        super.failed(code, data, msg)
                       data?.let {
                           showToast(it)
                       }
                    }
                })
    }
}

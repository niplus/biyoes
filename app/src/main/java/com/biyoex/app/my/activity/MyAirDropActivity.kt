package com.biyoex.app.my.activity

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener
import com.biyoex.app.R
import com.biyoex.app.common.base.BaseActivity
import com.biyoex.app.common.bean.RequestResult
import com.biyoex.app.common.http.RetrofitHelper
import com.biyoex.app.common.mvpbase.BaseObserver
import com.biyoex.app.common.utils.DateUtilsl
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.base_refresh_recyclerview.*
import kotlinx.android.synthetic.main.layout_title_bar.*

class MyAirDropActivity : BaseActivity(), OnRefreshLoadmoreListener {
    var page = 1
    var pageSize = 10
    var mAdapter: MyAirDropAdapter? = null
    var mData = mutableListOf<MutableList<String>>()
    override fun initView() {
    }

override fun initData() {
    smartrefresh.setOnRefreshLoadmoreListener(this)
    rl_menu.setOnClickListener { finish() }
    tv_title.text = "我的空投"
    base_recyclerview.layoutManager = LinearLayoutManager(this)
    mAdapter = MyAirDropAdapter()
        mAdapter!!.emptyView =  View.inflate(this, R.layout.view_empty, null)
    base_recyclerview.adapter = mAdapter
    getData()
}
override fun getLayoutId(): Int = R.layout.activity_my_air_drop
    override fun onLoadmore(refreshlayout: RefreshLayout?) {
        page++
        getData()
    }

    override fun onRefresh(refreshlayout: RefreshLayout?) {
        page = 1
        getData()
    }

    private fun getData() {
        showLoadingDialog()
        RetrofitHelper.getIns().zgtopApi.getMyAirdrop(page,pageSize)
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

                })
    }


}

class MyAirDropAdapter  : BaseQuickAdapter<MutableList<String>, BaseViewHolder>(R.layout.item_my_airdrop) {
    override fun convert(helper: BaseViewHolder?, item: MutableList<String>) {
        helper!!.setText(R.id.tv_start_item_invite_retrun, item[0])
                .setText(R.id.tv_center_item_invite_retrun,DateUtilsl.times(item[1]) )
                .setText(R.id.tv_end_item_invite_retrun, if(item[2]=="1") "未到账" else "已到账")
                .setText(R.id.tv_item_airdrop_num,item[3])
                .setText(R.id.tv_item_airdrop_info,item[4])
    }
}
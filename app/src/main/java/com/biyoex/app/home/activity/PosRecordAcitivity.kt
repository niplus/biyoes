package com.biyoex.app.home.activity

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.kotlin.autoDisposable
import com.biyoex.app.R
import com.biyoex.app.common.base.BaseActivity
import com.biyoex.app.common.bean.RequestResult
import com.biyoex.app.common.data.AppData
import com.biyoex.app.common.http.RetrofitHelper
import com.biyoex.app.common.mvpbase.BaseObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_pos_record_acitivity.*
import kotlinx.android.synthetic.main.base_refresh_recyclerview.*
import kotlinx.android.synthetic.main.layout_title_bar.*

/**
 * Created by mac on 20/7/28.
 * Pos记录
 */

class PosRecordAcitivity : BaseActivity(), OnRefreshLoadmoreListener {


    var mAdapter = PosWeekAdapter(R.layout.item_pos_record)
    var page = 1
    var pageSize = 10
    var mData = mutableListOf<MutableList<String>>()
    var mSearchData = mutableListOf<MutableList<String>>()
    override fun getLayoutId(): Int = R.layout.activity_pos_record_acitivity
    @SuppressLint("SetTextI18n")
    override fun initData() {
        iv_menu.setOnClickListener { finish() }
        tv_title.text = "Pos记录"
        base_recyclerview.layoutManager = LinearLayoutManager(this)
        base_recyclerview.adapter = mAdapter
        mAdapter.emptyView = View.inflate(this, R.layout.layout_no_data, null)
        smartrefresh.setOnRefreshLoadmoreListener(this)

        edit_search_coin.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val toString = edit_search_coin.text.toString()
                if (toString.isNullOrEmpty()) {
                    mAdapter.setNewData(mData)
                } else {
                    mSearchData.clear()
                    for (item in mData) {
                        if (item[1].toUpperCase().contains(toString.toUpperCase())) {
                            mSearchData.add(item)
                        }
                    }
                    mAdapter.setNewData(mSearchData)
                }
            }
        })

    }

    override fun initView() {
        //pos记录
        RetrofitHelper.getIns().zgtopApi
                .getPosRecord(AppData.getRoleId(),"", page, pageSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .autoDisposable(AndroidLifecycleScopeProvider.from(this, Lifecycle.Event.ON_DESTROY))//OnDestory时自动解绑
                .subscribe(object : BaseObserver<RequestResult<MutableList<MutableList<String>>>>() {
                    override fun success(t: RequestResult<MutableList<MutableList<String>>>) {
                        smartrefresh.finishLoadmore()
                        smartrefresh.finishRefresh()

                        if (page == 1) {
                            mData.clear()
                        }

                        mData.addAll(t!!.data)
                        val toString = edit_search_coin.text.toString()
                        if (toString.isNullOrEmpty()) {
                            mAdapter.setNewData(mData)
                        } else {
                            mSearchData.clear()
                            for (item in mData) {
                                if (item[1].toUpperCase().contains(toString.toUpperCase())) {
                                    mSearchData.add(item)
                                }
                            }
                            mAdapter.setNewData(mSearchData)
                        }
                    }

                })
    }

    override fun onLoadmore(refreshlayout: RefreshLayout?) {
        page++
        initView()
    }

    override fun onRefresh(refreshlayout: RefreshLayout?) {
        page = 1
        initView()
    }

}



package com.biyoex.app.trading.activity

import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.biyoex.app.R
import com.biyoex.app.common.mvpbase.BaseActivity
import com.biyoex.app.common.utils.SharedPreferencesUtils
import com.biyoex.app.common.utils.log.Log
import com.biyoex.app.home.bean.CoinTradeRankBean
import com.biyoex.app.trading.adapter.SearchCoinAdapter

import com.biyoex.app.trading.persenter.SearchCoinPresenter
import com.biyoex.app.trading.view.SearchCoinView
import kotlinx.android.synthetic.main.activity_search_coin.*

/**
 * 搜索币种
 * */
class SearchCoinActivity : BaseActivity<SearchCoinPresenter>(), SearchCoinView {
    override fun getCoinData(mCoinData: MutableList<CoinTradeRankBean.DealDatasBean>) {
        val string_history = SharedPreferencesUtils.getInstance().getString("search_history", "")
        mCoinAllList.addAll(mCoinData)
        if (!string_history.isNullOrEmpty()) {
            Log.e("${string_history}${getString(R.string.history)}")
            for (item in mCoinData) {
                if (string_history.contains(item.fShortName)) {
                    mHistoryList.add(item)
                }
            }
            mAdapter.setNewData(mHistoryList)
        }
    }

    var mCoinAllList: MutableList<CoinTradeRankBean.DealDatasBean> = mutableListOf()    //全部数据
    var mSearchList: MutableList<CoinTradeRankBean.DealDatasBean> = mutableListOf()
    var mHistoryList: MutableList<CoinTradeRankBean.DealDatasBean> = mutableListOf()
    lateinit var mAdapter: SearchCoinAdapter
    override fun getLayoutId(): Int = R.layout.activity_search_coin
    override fun createPresent(): SearchCoinPresenter = SearchCoinPresenter()
    override fun initComp() {
        tv_clear.setOnClickListener { finish() }
        mAdapter.setOnItemClickListener { adapter, view, position ->
            if (ed_search.text.toString().isNullOrEmpty()) {
                addHistory(mHistoryList[position].fShortName)
                startActivity(Intent(this, TrendChartNewActivity::class.java).putExtra("coin", mHistoryList[position]))

            } else {
                addHistory(mSearchList[position].fShortName)
                startActivity(Intent(this, TrendChartNewActivity::class.java).putExtra("coin", mSearchList[position]))
            }
            finish()
        }
        //搜索框筛选
        ed_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!ed_search.text.toString().isNullOrEmpty()) {
                    tv_history_search.visibility = View.GONE
                    mSearchList.clear()
                    for (item in mCoinAllList) {
                        if (item.fname_sn.toUpperCase().contains(ed_search.text.toString().toUpperCase())) {
                            mSearchList!!.add(item)
                        }
                    }
                    mAdapter.setNewData(mSearchList)
                } else {
                    tv_history_search.visibility = View.VISIBLE
                    mAdapter.setNewData(mHistoryList)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })
    }
    //添加历史记录币种
    private fun addHistory(fShortName: String?) {
        val string = SharedPreferencesUtils.getInstance().getString("search_history", "");
        if (string.isNullOrEmpty()) {
            SharedPreferencesUtils.getInstance().saveString("search_history", "$fShortName,")
        } else {
            SharedPreferencesUtils.getInstance().saveString("search_history", "$string,$fShortName,")
        }
    }

    override fun initData() {
        rv_search.layoutManager = LinearLayoutManager(this)
        mAdapter = SearchCoinAdapter()
        mAdapter.emptyView = getEmptyView(1)
        rv_search.adapter = mAdapter
        mAdapter.emptyView = getEmptyView(1)
        mPresent.getCoinData()
    }
}

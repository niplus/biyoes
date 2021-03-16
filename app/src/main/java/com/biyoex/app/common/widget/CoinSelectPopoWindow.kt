package com.biyoex.app.common.widget

import androidx.lifecycle.Observer
import android.content.Context
import com.google.android.material.tabs.TabLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.PopupWindow
import android.widget.TextView
import com.flyco.tablayout.listener.CustomTabEntity
import com.biyoex.app.R
import com.biyoex.app.common.adapter.CoinPopuAdapter
import com.biyoex.app.common.utils.SharedPreferencesUtils
import com.biyoex.app.common.utils.log.Log
import com.biyoex.app.home.bean.CoinTradeRankBean
import com.biyoex.app.trading.bean.CoinMarketLiveData
import com.biyoex.app.trading.bean.TabEntity

@Suppress("NAME_SHADOWING")
class CoinSelectPopoWindow : PopupWindow, TextWatcher, TabLayout.OnTabSelectedListener {
    override fun onTabReselected(tab: TabLayout.Tab?) {
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {

    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        Log.e("${tab!!.position}當前postion")
        mEditText.setText("")
        mSelectIndex = tab.position
        setdata()
    }
    var mtitleList: MutableList<CustomTabEntity> = mutableListOf()
    var mSelectIndex: Int = 0  //判断当前在那个key下面
    var isSearsh = false
    var mDatasBean: CoinTradeRankBean? = null
    var mIndexListData: MutableList<CoinTradeRankBean.DealDatasBean> = mutableListOf()
    var mListener: OnPopupWindowItemClickLiseser? = null
    var newListData = mutableListOf<CoinTradeRankBean.DealDatasBean>()
    var tabLayout: TabLayout
    var mRecyclerView: RecyclerView
    var mEditText: EditText
    var mAdapter: CoinPopuAdapter
    var mActivity: AppCompatActivity

    constructor(mcontext: Context, mType: Int) {
        mActivity = mcontext as AppCompatActivity
        contentView = View.inflate(mcontext, if (mType == 0) R.layout.popupwindow_coin else R.layout.view_market_right, null)
        animationStyle = R.style.pop_out_and_int
        this.width = ViewGroup.LayoutParams.WRAP_CONTENT
        this.height = ViewGroup.LayoutParams.MATCH_PARENT
        isOutsideTouchable = true
        isFocusable = true
        tabLayout = contentView.findViewById(R.id.market_right_tab)
        mRecyclerView = contentView.findViewById(R.id.rv_market_right)
        mEditText = contentView.findViewById(R.id.edit_market_right)
        mAdapter = CoinPopuAdapter(mType)
        mRecyclerView.layoutManager = LinearLayoutManager(mcontext)
        mRecyclerView.adapter = mAdapter
        CoinMarketLiveData.getIns().observe(mcontext, Observer {
            if (mDatasBean == null) {
                mDatasBean = it
                val keys = mDatasBean!!.dataMap.keys
                mtitleList.add(TabEntity(mcontext.getString(R.string.self_selected_area), 0, 0))
                val view = View.inflate(mcontext, if (mType == 0) R.layout.item_tab_popupwindow else R.layout.item_market_coin_select_tab, null)
                view.findViewById<TextView>(R.id.tv_tab_name).text = mcontext.getString(R.string.self_selected_area)
                val newTab = tabLayout.newTab()
                newTab.customView = view
                tabLayout.addTab(newTab)
                for (item in keys) {
                    mtitleList.add(TabEntity(item, 0, 0))
                    val view = View.inflate(mcontext, if (mType == 0) R.layout.item_tab_popupwindow else R.layout.item_market_coin_select_tab, null)
                    view.findViewById<TextView>(R.id.tv_tab_name).text = item
                    val newTab = tabLayout.newTab()
                    newTab.customView = view
                    tabLayout.addTab(newTab)
                }
                setdata()
            }
        })
        tabLayout.setOnTabSelectedListener(this)
//        setdata()
        mEditText.addTextChangedListener(this)
        mAdapter.setOnItemClickListener { _, _, position ->
            if (mListener != null) {
                if (isSearsh) {
                    mListener!!.OnItemListener(newListData[position])
                    dismiss()
                } else {
                    mListener!!.OnItemListener(mIndexListData[position])
                    dismiss()
                }
            }
        }
    }

    fun setOnPopuwindowListener(mListener: OnPopupWindowItemClickLiseser) {
        this.mListener = mListener
    }

    private fun setdata() {
        if (mDatasBean != null) {
//            mtitleList.clear()
            mIndexListData.clear()
            val keys = mDatasBean!!.dataMap.keys
            if (mSelectIndex != 0) {
                mIndexListData.addAll(mDatasBean!!.dataMap.getValue(mtitleList[mSelectIndex].tabTitle))
            } else {
                val coins = SharedPreferencesUtils.getInstance().getString("coin_id", "")
                for (item in keys) {
                    for (item in mDatasBean!!.dataMap.getValue(item)) {
                        if (coins.contains("${item.fid}")) {
                            mIndexListData.add(item)
                        }
                    }
                }
            }
            mAdapter.setNewData(mIndexListData)
        }
    }

    override fun afterTextChanged(s: Editable?) {
        if (!mEditText.text.toString().isNullOrEmpty() && !mIndexListData.isNullOrEmpty()) {
            isSearsh = true
            newListData.clear()
            for (item in mIndexListData) {
                if (item.fShortName.toUpperCase().contains(mEditText.text.toString().toUpperCase())) {
                    newListData.add(item)
                }
            }
            mAdapter.setNewData(newListData)
        } else if (!mIndexListData.isNullOrEmpty()) {
            isSearsh = false
            mAdapter.setNewData(mIndexListData)
        }
    }

    //屏幕阴影
    override fun showAsDropDown(anchor: View?) {
        super.showAsDropDown(anchor)
        val attributes = mActivity.window.attributes
        attributes.alpha = 0.7f
        mActivity.window.attributes = attributes
    }

    //取消屏幕阴影
    override fun dismiss() {
        super.dismiss()
        val attributes = mActivity.window.attributes
        attributes.alpha = 1f
        mActivity.window.attributes = attributes
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

    }

    interface OnPopupWindowItemClickLiseser {
        fun OnItemListener(mCoinTradeRankBean: CoinTradeRankBean.DealDatasBean)
    }
}
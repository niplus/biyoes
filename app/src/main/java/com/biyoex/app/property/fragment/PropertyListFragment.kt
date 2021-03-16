package com.biyoex.app.property.fragment


import android.annotation.SuppressLint
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton

import com.biyoex.app.R
import com.biyoex.app.common.base.RxBus
import com.biyoex.app.common.base.RxBusData
import com.biyoex.app.common.mvpbase.BaseFragment

import com.biyoex.app.my.bean.RechargeCoinBean
import com.biyoex.app.property.adapter.PropertyListAdapter
import com.biyoex.app.property.datas.PropertyListBean
import com.biyoex.app.property.presenter.PropertyListPresenter
import com.biyoex.app.property.view.PropertyListView

import kotlinx.android.synthetic.main.fragment_property_list.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@SuppressLint("ValidFragment")
/**
 * 资产列表  type=0普通区  1  创新区
 *
 */

class PropertyListFragment() : BaseFragment<PropertyListPresenter>(), PropertyListView, CompoundButton.OnCheckedChangeListener {
    private var type :Int = 0
    private var temp: MutableList<RechargeCoinBean>? = mutableListOf()
    private var financeAdapter: PropertyListAdapter? = null
    private var recyclerViewDataList: MutableList<PropertyListBean> = mutableListOf()
    constructor(type:Int):this(){
        this.type = type
    }
    /**
     * 懒加载过
     */
    private var isLazyLoaded: Boolean = false
    /**
     * Fragment的View加载完毕的标记
     */
    private var isPrepared: Boolean = false

    override fun createPresent(): PropertyListPresenter = PropertyListPresenter()

    override fun getLayoutId(): Int = R.layout.fragment_property_list

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(layoutId, container, false)
    }

    override fun initImmersionBar() {
        immersionBar?.statusBarDarkFont(false)?.init()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isPrepared = true
        initData()
        lazyData()
        val subscribe = RxBus.get().toFlowable().map { o -> o as RxBusData }.subscribe { rxBusData ->
            if (rxBusData.msgName == "propertyList") {
                temp = rxBusData.msgData as MutableList<RechargeCoinBean>?
                filterList()
            }
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        lazyData()
    }

    private fun lazyData() {
        if (userVisibleHint && isPrepared && !isLazyLoaded) {
            firstInitData()
            isLazyLoaded = true
        }
        if (userVisibleHint && isPrepared && isLazyLoaded) {
            reInitData()
        }
    }

    private fun reInitData() {

    }

    private fun firstInitData() {
    }


    override fun initComp() {

    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        filterList()
    }

    override fun initData() {
        financeAdapter = PropertyListAdapter(type)
        recyclerview_property.layoutManager = LinearLayoutManager(context)
        recyclerview_property.adapter = financeAdapter
        financeAdapter?.emptyView = View.inflate(context,R.layout.view_empty,null)
        layout_toggle.setOnClickListener {
            toggle_button.isChecked = !toggle_button.isChecked
        }
        toggle_button.setOnCheckedChangeListener(this)
        edt_search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable) {
                filterList()
            }
        })
    }

    override fun httpErrorView(code: Int) {
        hideLoadingDialog()
//        recyclerViewDataList.clear()
//        financeAdapter!!.notifyRecyclerViewData()
    }

    @SuppressLint("SetTextI18n")
    override fun httpSuccess(response: MutableList<RechargeCoinBean>?, coinName: String, totalValue: String, usdtValue: String) {
//        hideLoadingDialog()
//        temp = response
//        filterList()
    }

    private fun filterList() {
        if (isDetached) {
            return
        }
        if (isResumed) {
            recyclerViewDataList.clear()
            for (rechargeCoinBean in temp!!) {
                if (type == 2) {
                    if (!rechargeCoinBean.innovate) continue
                } else {
                    if (rechargeCoinBean.innovate) continue
                }
                if (toggle_button.isChecked && (TextUtils.isEmpty(rechargeCoinBean.cnyValue) || java.lang.Double.valueOf(rechargeCoinBean.cnyValue) < 1) && (TextUtils.isEmpty(rechargeCoinBean.cnyValue) || java.lang.Double.valueOf(rechargeCoinBean.cnyValue) < 1)) {
                    continue
                }
                val word = edt_search.text.toString().toUpperCase()
                if (rechargeCoinBean.name.toUpperCase().contains(word)) {
                    //                mCurrencyCapitalList.add(rechargeCoinBean);
                    // TODO: 2019/4/28 时间太紧来不及研究
                    val useless = arrayListOf<Any>()
                    useless.add(Any())
                    recyclerViewDataList.add(PropertyListBean(false, rechargeCoinBean))
                }/* || rechargeCoinBean.getAllName().toUpperCase().contains(word)*/
            }
            financeAdapter!!.setNewData(recyclerViewDataList)
        }
        //        mCurrencyCapitalAdpater.notifyDataSetChanged();
    }
}


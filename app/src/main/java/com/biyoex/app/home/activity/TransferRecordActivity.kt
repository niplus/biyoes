package com.biyoex.app.home.activity

import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener
import com.biyoex.app.R
import com.biyoex.app.common.base.BaseActivity
import com.biyoex.app.common.bean.PosCheckBalanceRecordsBean
import com.biyoex.app.common.bean.RequestResult
import com.biyoex.app.common.data.AppData
import com.biyoex.app.common.http.RetrofitHelper
import com.biyoex.app.common.mvpbase.BaseObserver
import com.biyoex.app.common.utils.DateUtilsl
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.base_refresh_recyclerview.*
import kotlinx.android.synthetic.main.layout_title_bar.*


/**
 * Created by mac on 20/7/29.
 * 转账记录
 */
class TransferRecordActivity : BaseActivity(), OnRefreshLoadmoreListener {

    var coinId = 0
    var page = 1
    var pageSize = 10
    var mAdapter = TransferRecordAdapter()
    override fun initView() {
    }

    override fun initData() {
        smartrefresh.setOnRefreshLoadmoreListener(this)
        rl_menu.setOnClickListener { finish() }
        coinId = intent.getIntExtra("coinId", 0)
        tv_title.text = "转账记录"
        base_recyclerview.layoutManager = LinearLayoutManager(this)
        mAdapter = TransferRecordAdapter()
        mAdapter.emptyView = View.inflate(this, R.layout.view_empty, null)
        base_recyclerview.adapter = mAdapter
        getData()
    }

    override fun getLayoutId(): Int = R.layout.activity_pos_transfer_record
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
        RetrofitHelper.getIns().zgtopApi
                .getPosCheckBalanceRecords(AppData.getRoleId(), coinId, page, pageSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : BaseObserver<RequestResult<MutableList<PosCheckBalanceRecordsBean>>>() {
                    override fun success(t: RequestResult<MutableList<PosCheckBalanceRecordsBean>>) {
                        hideLoadingDialog()
                        smartrefresh.finishRefresh()
                        smartrefresh.finishLoadmore()
                        if (t!!.data.isNullOrEmpty()) {
                            showToast("暂没有数据")
                        } else {
                            if (page == 1) {
                                mAdapter.setNewData(t.data)
                            } else {
                                mAdapter.data.addAll(t.data)
                                mAdapter.notifyDataSetChanged()
                            }
                        }
                    }
                })
    }

}

class TransferRecordAdapter : BaseQuickAdapter<PosCheckBalanceRecordsBean, BaseViewHolder>(R.layout.item_pos_transfer_record) {
    override fun convert(helper: BaseViewHolder?, item: PosCheckBalanceRecordsBean) {
        val status: TextView = helper!!.getView(R.id.tv_PosTransfer_money)
        if (item.type == "3") {
            status.setTextColor(Color.parseColor("#333333"))
        } else {
            status.setTextColor(Color.parseColor("#FA4747"))
        }
        helper.setText(R.id.tv_PosTransfer_type, if (item.type == "3") "转出" else "转入")
                .setText(R.id.tv_PosTransfer_time, DateUtilsl.times(item.createTime))
                .setText(R.id.tv_PosTransfer_money, if (item.type == "3") "-${item.amount}" else "+${item.amount}")
                .setText(R.id.tv_PosTransfer_account, item.userName)
    }
}
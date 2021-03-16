package com.biyoex.app.home.activity

import android.annotation.SuppressLint
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.biyoex.app.R
import com.biyoex.app.common.Constants
import com.biyoex.app.common.base.BaseActivity
import com.biyoex.app.common.bean.PosHoldRankingBean
import com.biyoex.app.common.bean.RequestResult
import com.biyoex.app.common.data.AppData
import com.biyoex.app.common.http.RetrofitHelper
import com.biyoex.app.common.mvpbase.BaseObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_pos_cash_list.*
import kotlinx.android.synthetic.main.layout_title_while_bar.*

/**
 * Created by mac on 20/7/29.
 * 持币排行榜
 */

class PosCashListActivity : BaseActivity() {

    var coinId = 0
    lateinit var mAdapter: PosCashListAdapter
    override fun initView() {
        showLoadingDialog()
        RetrofitHelper.getIns().zgtopApi
                .getPosHoldRanking(AppData.getRoleId(), coinId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : BaseObserver<RequestResult<MutableList<PosHoldRankingBean>>>() {
                    override fun success(t: RequestResult<MutableList<PosHoldRankingBean>>) {
                        hideLoadingDialog()
                        t!!.data?.let {
                            mAdapter.setNewData(it)
                        }
                    }

                    override fun failed(code: Int, data: String?, msg: String?) {
                        super.failed(code, data, msg)
                        hideLoadingDialog()
                    }
                })

    }

    @SuppressLint("SetTextI18n")
    override fun initData() {
        iv_menu.setOnClickListener { finish() }
        coinId = intent.getIntExtra("coinId", 0)
        recyclerview_pos_cash.layoutManager = LinearLayoutManager(this)
        mAdapter = PosCashListAdapter()
        mAdapter.emptyView = View.inflate(this, R.layout.view_empty, null)
        recyclerview_pos_cash.adapter = mAdapter

    }

    override fun getLayoutId(): Int = R.layout.activity_pos_cash_list

}

class PosCashListAdapter : BaseQuickAdapter<PosHoldRankingBean, BaseViewHolder>(R.layout.item_pos_cash_list) {
    override fun convert(helper: BaseViewHolder?, item: PosHoldRankingBean) {
        helper!!.setText(R.id.tv_cash_ranking_uid, item.UID)
                .setText(R.id.tv_cash_amount, Constants.numberFormat(item.holdAmount.toDouble()))
                .setText(R.id.tv_cash_ranking, "")
//                .setText(R.id.tv_cash_ranking_digits, item[0])
        val oneStatus: RelativeLayout = helper.getView(R.id.re_CashList_top_three)
        oneStatus.visibility = View.VISIBLE
//        val twoStatus: ImageView = helper.getView(R.id.iv_cash_ranking_type)
//        twoStatus.visibility = View.VISIBLE
        val threeStatus: TextView = helper.getView(R.id.tv_cash_ranking)
        threeStatus.visibility = View.VISIBLE
        threeStatus.visibility = View.GONE
        when (item.sort) {
            "1" -> {
                helper.setImageResource(R.id.iv_CashList_top_three, R.mipmap.cash_list_one)
            }
            "2" -> {
                helper.setImageResource(R.id.iv_CashList_top_three, R.mipmap.cash_list_two)
            }
            "3" -> {
                helper.setImageResource(R.id.iv_CashList_top_three, R.mipmap.cash_list_three)
            }
            else -> {
                oneStatus.visibility = View.GONE
                threeStatus.visibility = View.VISIBLE
                helper.setText(R.id.tv_cash_ranking, item.sort)
            }
        }
//        //上涨还是下跌
//        when (item[3]) {
//            "1" -> {
//                helper.setImageResource(R.id.iv_cash_ranking_type, R.mipmap.cash_ranking_rose)
//            }
//            "2" -> {
//                helper.setImageResource(R.id.iv_cash_ranking_type, R.mipmap.cash_ranking_fall)
//            }
//            else -> {
//                helper.setText(R.id.tv_cash_ranking_digits, "")
//                twoStatus.visibility = View.GONE
//            }
//        }
        //点击刷新数据
        helper.itemView.setOnClickListener {

        }
    }
}

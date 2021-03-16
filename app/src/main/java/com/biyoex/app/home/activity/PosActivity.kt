package com.biyoex.app.home.activity

import android.content.Intent
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.biyoex.app.R
import com.biyoex.app.VBTApplication
import com.biyoex.app.common.Constants
import com.biyoex.app.common.base.BaseActivity
import com.biyoex.app.common.bean.PosHomeBean
import com.biyoex.app.common.bean.RequestResult
import com.biyoex.app.common.http.RetrofitHelper
import com.biyoex.app.common.mvpbase.BaseObserver
import com.biyoex.app.common.utils.GlideUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_pos.*
import kotlinx.android.synthetic.main.layout_title_while_bar.*
import java.math.BigDecimal

/**
 * Created by mac on 20/7/28.
 * Pos理财
 */

class PosActivity : BaseActivity() {

    lateinit var mAdapter: PosAdapter

    override fun initView() {
        showLoadingDialog()
        RetrofitHelper.getIns().zgtopApi
                .getPosHome()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : BaseObserver<RequestResult<MutableList<PosHomeBean>>>() {
                    override fun success(t: RequestResult<MutableList<PosHomeBean>>) {
                        hideLoadingDialog()
                        t.data?.let {
                            mAdapter.setNewData(it)
                        }
                    }

                    override fun failed(code: Int, data: String?, msg: String?) {
                        super.failed(code, data, msg)
                        hideLoadingDialog()
                    }
                })

    }

    override fun initData() {
        tv_title.text = getString(R.string.pos_manager)
        tv_right.text = getString(R.string.record)
        //更多
        tv_right.setOnClickListener {
            rty_pos_more.visibility = View.VISIBLE
        }
        //隐藏外层布局
        rty_pos_more.setOnClickListener {
            rty_pos_more.visibility = View.GONE
        }
        //记录
        tv_pos_record.setOnClickListener {
            rty_pos_more.visibility = View.GONE
            startActivity(Intent(this, PosRecordAcitivity::class.java))
        }
        //切换账号
        tv_icon_switch_account.setOnClickListener {
            rty_pos_more.visibility = View.GONE
            startActivity(Intent(this, SwitchingPosRolesActivity::class.java))
        }

        rv_pos.layoutManager = LinearLayoutManager(this)
        mAdapter = PosAdapter()
        rv_pos.adapter = mAdapter
        iv_menu.setOnClickListener { finish() }


    }

    override fun getLayoutId(): Int = R.layout.activity_pos
}


class PosAdapter : BaseQuickAdapter<PosHomeBean, BaseViewHolder>(R.layout.item_home_pos) {

    override fun convert(helper: BaseViewHolder?, item: PosHomeBean) {
        helper!!.setText(R.id.tv_item_coin_name, item.coinName)
                .setText(R.id.tv_item_min_price, Constants.numberFormat(item.optimalHold.toDouble()))
                .setText(R.id.tv_item_good_price, Constants.numberFormat(item.minHold.toDouble()))
                .setText(R.id.tv_total_output_yesterday, Constants.numberFormat(item.yesterdayProfitTotal.toDouble()))
                .setText(R.id.tv_item_rate, "${BigDecimal(item.rate).multiply(BigDecimal(100)).setScale(0)}%")
        GlideUtils.getInstance().displayCricleImage(mContext, "${VBTApplication.appPictureUrl}${item.Icon}", helper.itemView.findViewById(R.id.item_iv_src))
        val oneStatus: ImageView = helper.getView(R.id.iv_qs_pos)
        val twoStatus: ImageView = helper.getView(R.id.iv_home_pos)
        if(item.hotState == "1"){
            oneStatus.visibility = View.VISIBLE
            twoStatus.visibility = View.VISIBLE
        }
        helper.itemView.setOnClickListener {
            val intent = Intent(mContext, CoinPosActivity::class.java)
            intent.putExtra("coinId", item.coinId)
            intent.putExtra("coinName", item.coinName)
            mContext.startActivity(intent)
        }
    }


}

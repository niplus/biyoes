package com.biyoex.app.my.activity

import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.biyoex.app.R
import com.biyoex.app.common.base.BaseActivity
import com.biyoex.app.common.bean.Bonus
import com.biyoex.app.common.bean.MyPartnerBean
import com.biyoex.app.common.bean.RequestResult
import com.biyoex.app.common.http.RetrofitHelper
import com.biyoex.app.common.mvpbase.BaseObserver
import com.biyoex.app.my.adapter.LockListener
import com.biyoex.app.my.adapter.MyPartnerPlanAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_mypartner.*
import kotlinx.android.synthetic.main.layout_newtitle_bar.*
import kotlinx.android.synthetic.main.layout_title_bar.rl_menu
import kotlinx.android.synthetic.main.layout_title_bar.tv_title
import java.math.BigDecimal

class MypartnerActivity :BaseActivity(), LockListener {


    var myAdapter:MyPartnerPlanAdapter?=null
    var myBurnAdapter:MyBurnAdapter?=null
    override fun getLayoutId(): Int = R.layout.activity_mypartner
    override fun initView() {
        showLoadingDialog()
        RetrofitHelper.getIns().zgtopApi.airdroprecord
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object :BaseObserver<RequestResult<MyPartnerBean>>(this){
                    override fun success(t: RequestResult<MyPartnerBean>) {
                        hideLoadingDialog()
                        myAdapter?.setNewData(t!!.data.dataList)
                        tv_myLimit.text = "您的锁仓VBT额度为:${BigDecimal(t!!.data.total).setScale(0,BigDecimal.ROUND_DOWN)}${t!!.data.coinName}"
                        myBurnAdapter?.setNewData(t.data.bonusList)
                    }

                    override fun failed(code: Int, data: String?, msg: String?) {
                        super.failed(code, data, msg)
                        hideLoadingDialog()
                    }
                })

    }


    override fun initData() {
        tv_title.text = getString(R.string.my_right)
        tv_right.text = getString(R.string.invite_record)
        tv_right.setOnClickListener {
            startActivity(Intent(this,PartnerInviteActivity::class.java))
        }
        rl_menu.setOnClickListener { finish() }
        recyclerView.layoutManager = LinearLayoutManager(this)
        rv_burn.layoutManager = LinearLayoutManager(this)
        myAdapter = MyPartnerPlanAdapter()
        myBurnAdapter = MyBurnAdapter()
        rv_burn.adapter = myBurnAdapter
        recyclerView.adapter = myAdapter
        myAdapter?.lockListener = this

    }

    override fun onLockListener(id: Int) {
        showLoadingDialog()
        RetrofitHelper.getIns().zgtopApi
                .postLockAirdrop(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object:BaseObserver<RequestResult<Any>>(this){
                    override fun success(t: RequestResult<Any>) {
                        hideLoadingDialog()
                        showToast(getString(R.string.partner_lock_success))
                        initView()
                    }
                    override fun failed(code: Int, data: String?, msg: String?) {
                        super.failed(code, data, msg)
                        hideLoadingDialog()
                        setShowToast(code,data!!)
                    }
                })
    }

    private fun setShowToast(code: Int,msg:String) {
        when(code){
             -1 -> showToast("参数错误")
             -2 -> showToast("此权益已解锁")
            -3 -> showToast("此权益暂不能解锁")
            -4 -> showToast("系统异常")
            else -> {
                showToast(msg)
            }
        }
    }
}

class MyBurnAdapter :BaseQuickAdapter<Bonus,BaseViewHolder>(R.layout.item_myburn_partner){
    override fun convert(helper: BaseViewHolder?, item: Bonus?) {
        helper!!.setText(R.id.tv_coin_price,item!!.coinName)
                .setText(R.id.tv_lock_sum,item!!.bonusNum)
    }

}

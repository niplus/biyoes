package com.biyoex.app.my.activity

import android.content.Intent

import com.biyoex.app.R
import com.biyoex.app.common.Constants
import com.biyoex.app.common.activity.WebPageLoadingActivity
import com.biyoex.app.common.base.BaseActivity
import com.biyoex.app.common.bean.InvitationPartnerBean
import com.biyoex.app.common.bean.RequestResult
import com.biyoex.app.common.http.RetrofitHelper
import com.biyoex.app.common.mvpbase.BaseObserver
import com.biyoex.app.common.widget.BaseDialog
import com.biyoex.app.common.widget.OnClicklisteners
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_buy_partner.*
import kotlinx.android.synthetic.main.layout_title_bar.*
import java.math.BigDecimal

class BuyPartnerActivity : BaseActivity(), OnClicklisteners {
    override fun sureOnclicklistener() {
        buyPartner()
    }

    var shareId:String = ""
    lateinit var data:InvitationPartnerBean
    var baseDialog:BaseDialog?=null
    override fun getLayoutId(): Int  = R.layout.activity_buy_partner

    override fun initView() {

    }

    override fun initData() {
        rl_menu.setOnClickListener { finish() }
        ;
        baseDialog = BaseDialog(this,this)
        tv_title.text = getString(R.string.buy_right)
        //    var common:String = intent.getStringExtra()
         shareId = intent.getStringExtra("ShareId")
        data = intent.getSerializableExtra("data") as InvitationPartnerBean
        tv_partner_remain.text ="${ data.common}份"
        tv_super_partner_remain.text = "${data.supers}份"
        tv_partner_price.text ="1000USDT"
        tv_partner_number.text ="${data.myInvite}人"
        tv_partner_price.text = "${BigDecimal(data.amount).setScale(0,BigDecimal.ROUND_DOWN)}USDT"
        tv_complete.text = "支付${BigDecimal(data.amount).setScale(0,BigDecimal.ROUND_DOWN)}USDT"

        tv_rule.setOnClickListener {
            val intent = Intent(this, WebPageLoadingActivity::class.java)
            intent.putExtra("url", "${Constants.REALM_NAME}/resonance/rules")
            intent.putExtra("type", "url")
            intent.putExtra("title", getString(R.string.vb_partner))
            startActivity(intent)
        }

        tv_share_info.setOnClickListener {
                startActivity(Intent(this,PartnerInviteActivity::class.java))
        }

        tv_complete.setOnClickListener {
            baseDialog?.show()
        }
    }

    private fun buyPartner() {
        RetrofitHelper.getIns().zgtopApi
                .partnerComnection(shareId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object :BaseObserver<RequestResult<Any>>(){
                    override fun success(t: RequestResult<Any>) {
                        showToast(getString(R.string.buy_success))
                        finish()
                    }
                    override fun failed(code: Int, data: String?, msg: String?) {
                        super.failed(code, data, msg)
                        setToast(code,data)
                    }
                })
    }

    private fun setToast(code: Int,data:String?) {
        when(code){
             -2 ->{
                 showToast(getString(R.string.share_not))
             }
            -3 -> showToast(getString(R.string.your_is_vb_partner))
            -4 -> showToast(getString(R.string.assets_small))
            -5 -> showToast("系统异常，请稍后再试")
            else ->{
                data?.let {
                    showToast(data)
                }
            }
        }
    }
}

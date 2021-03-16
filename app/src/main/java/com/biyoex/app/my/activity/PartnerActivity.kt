package com.biyoex.app.my.activity

import android.os.Bundle
import android.view.View
import com.biyoex.app.R
import com.biyoex.app.common.bean.InvitationPartnerBean
import com.biyoex.app.common.mvpbase.BaseActivity
import com.biyoex.app.my.presenter.PartnerPresenter
import com.biyoex.app.my.view.DialogIsPartner
import com.biyoex.app.my.view.EditShareCodeDIalog
import com.biyoex.app.my.view.PartnerView
import kotlinx.android.synthetic.main.activity_worker.*
import kotlinx.android.synthetic.main.layout_title_bar.*

class PartnerActivity : BaseActivity<PartnerPresenter>(),PartnerView, DialogIsPartner {
    override fun postShareIdSuccess(data: InvitationPartnerBean) {
        dialog?.dismiss()
        var bundle = Bundle()
        bundle.putSerializable("data",data)
        bundle.putString("ShareId",shareId)
        openActivity(BuyPartnerActivity::class.java,bundle)
    }
    var userStateCode = 1
    var shareId:String = ""
    var dialog:EditShareCodeDIalog?=null
    override fun getLayoutId(): Int = R.layout.activity_worker

    override fun createPresent(): PartnerPresenter  = PartnerPresenter(context)

    override fun initComp() {
        mPresent.getPartner()

    }

    override fun initData() {
        rl_menu.setOnClickListener { finish() }
        tv_title.text = getString(R.string.vb_partner)
      dialog =   EditShareCodeDIalog(this,this)

        tv_buy_right.setOnClickListener {
            dialog?.show()
        }
        tv_my_right.setOnClickListener {
            openActivity(MypartnerActivity::class.java)
        }
    }

    override fun onResume() {
        super.onResume()
        mPresent.getPartner()
    }

    override fun getPartner(code: Int ) {
        userStateCode = code
        when(code){
            1 -> {
                tv_my_right.visibility = View.GONE
                tv_buy_right.visibility = View.VISIBLE
                tv_partner_info.text = getString(R.string.partner_warning)
                tv_buy_right.background = getDrawable(R.drawable.btn_bg_yellow_ciecle)
            }
            2 ,3->{
                tv_buy_right.visibility = View.GONE
                tv_my_right.visibility = View.VISIBLE
                tv_partner_info.text = getString(if(code==2)R.string.partner_commomn else R.string.partner_super)
                tv_my_right.background = getDrawable(R.drawable.btn_bg_yellow_ciecle)
            }
        }
    }
    override fun OnNextClickListener(userId: String) {
        shareId = userId
        mPresent.postShareCode(userId)
    }

    override fun onCancel() {
      dialog?.dismiss()
    }

}

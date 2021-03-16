package com.biyoex.app.property

import android.content.Intent
import android.view.View
import android.widget.Toast
import com.biyoex.app.R

import com.biyoex.app.common.mvpbase.BaseActivity
import com.biyoex.app.property.datas.LockDataBean
import com.biyoex.app.property.presenter.LockPropertyPresenter
import com.biyoex.app.property.view.LockPropertyView
import kotlinx.android.synthetic.main.activity_lock_property.*
import kotlinx.android.synthetic.main.layout_newtitle_bar.*
import java.math.BigDecimal

//锁仓页面
class LockPropertyActivity : BaseActivity<LockPropertyPresenter>(), LockPropertyView {
    var tag: Int = 0
    override fun updateSuccess() {
        if (tag == 0) showToast(getString(R.string.lock_success)) else showToast(getString(R.string.add_shares_success))
        var intent2 = Intent(this, LockHistoryActivity::class.java)
        intent2.putExtra("coinName", mData!!.newFname)
        intent2.putExtra("coidId", intent.getStringExtra("coidId"))
        startActivity(intent2)
    }

    override fun getLockDataSuccess(lockDataBean: LockDataBean) {
        mData = lockDataBean
        tv_start_name.text = lockDataBean.fname
        tv_end_name.text = lockDataBean.newFname
        tv_name_now.text = lockDataBean.fname
        edit_locknum.hint = if (tag == 0) "${getString(R.string.lock_minsum)}${BigDecimal(lockDataBean.minAmount).setScale(2, BigDecimal.ROUND_HALF_UP)}" else "${getString(R.string.lock_minsum)}${BigDecimal(lockDataBean.minAmount).setScale(2, BigDecimal.ROUND_HALF_UP)}"
        user_allsum.text = "${getString(R.string.useful)}${BigDecimal(lockDataBean.getfTotal()).setScale(4, BigDecimal.ROUND_HALF_UP)}${lockDataBean.fname}"
        text_prcent.defaultPercent = lockDataBean.scale
        max_locksum.text = if (tag == 0) "${getString(R.string.lock_sum)}  ${BigDecimal(lockDataBean.num).setScale(2, BigDecimal.ROUND_HALF_UP)}/${BigDecimal(lockDataBean.totalAmount).setScale(2, BigDecimal.ROUND_HALF_UP)}(${lockDataBean.scale}%)" else {
            "加仓数量(${BigDecimal(lockDataBean.num).setScale(2, BigDecimal.ROUND_HALF_UP)}(${lockDataBean.scale}%))"
        }
    }

    override fun initComp() {

        btn_lock.setOnClickListener {
            if (edit_locknum.text.isEmpty()) {
                Toast.makeText(this, resources.getString(R.string.lock_null), Toast.LENGTH_LONG).show()
            } else {
                mPresent.postLockProperty(intent.getStringExtra("coidId"), edit_locknum.text.toString())
            }
        }
        iv_right.setImageResource(R.mipmap.icon_history)
        iv_right.visibility = View.VISIBLE
        iv_right.setOnClickListener {
            if (mData != null) {
                var intent2 = Intent(this, LockHistoryActivity::class.java)
                intent2.putExtra("coinName", mData!!.newFname)
                intent2.putExtra("coidId", intent.getStringExtra("coidId"))
                startActivity(intent2)
            }
        }
        iv_menu.setOnClickListener { finish() }
    }

    override fun createPresent(): LockPropertyPresenter = LockPropertyPresenter()

    private var mData: LockDataBean? = null;

    override fun getLayoutId(): Int = if (intent.getIntExtra("tag", 0) == 1) R.layout.activity_add_shares else R.layout.activity_lock_property

    override fun initData() {
        tag = intent.getIntExtra("tag", 0)
        mPresent.getLockProperty(intent.getStringExtra("coidId"))
        iv_right.setImageResource(R.mipmap.icon_lockhistory)
        tv_title.text = resources.getString(R.string.lock_shares)
        tv_title.text = if (intent.getIntExtra("tag", 0) == 1) resources.getString(R.string.add_shares_title) else resources.getString(R.string.lock_shares)
        tv_user_allsum.setOnClickListener {
            if (mData != null) {
                edit_locknum.setText("${BigDecimal(mData!!.getfTotal()).setScale(4, BigDecimal.ROUND_HALF_UP)}")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mPresent.getLockProperty(intent.getStringExtra("coidId"))
    }

    override fun setStatusBar() {
        super.setStatusBar()
//        immersionBar.transparentNavigationBar()
        immersionBar.statusBarDarkFont(true)
    }
}

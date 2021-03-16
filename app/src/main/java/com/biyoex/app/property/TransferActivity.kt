package com.biyoex.app.property

import android.widget.Toolbar
import com.biyoex.app.R
import com.biyoex.app.common.mvpbase.BaseActivity
import com.biyoex.app.property.datas.LockDataBean
import com.biyoex.app.property.presenter.TransferPresenter
import com.biyoex.app.property.view.TransferView
import kotlinx.android.synthetic.main.activity_transfer.*
import kotlinx.android.synthetic.main.layout_newtitle_bar.*
import java.math.BigDecimal

/**
 * 划转页面
 * */
class TransferActivity : BaseActivity<TransferPresenter>(), TransferView {
    override fun updateDataSuccess() {
        showToast(getString(R.string.transfer_success))
        finish()
    }

    private var data: LockDataBean? = null

    override fun createPresent(): TransferPresenter = TransferPresenter()
    override fun getLayoutId(): Int = R.layout.activity_transfer

    override fun setActionBar(toolbar: Toolbar?) {
        super.setActionBar(toolbar)
    }

    override fun initComp() {
        tv_transfer_all.setOnClickListener {
            edit_transfer.setText("${data!!.getfTotal()}")
        }
        iv_menu.setOnClickListener { finish() }
        btn_transfer.setOnClickListener {
            if (edit_transfer.text.toString().isEmpty()) {
                showToast(getString(R.string.transfer_hint))
            } else {
                mPresent.postTransferData(intent.getStringExtra("coidId"), edit_transfer.text.toString())
            }
        }
    }

    override fun initData() {
        tv_title.text = resources.getString(R.string.transfer)
//        mPresent.getTransferData(intent.getStringExtra("coidId"))
    }

    override fun onResume() {
        super.onResume()
        mPresent.getTransferData(intent.getStringExtra("coidId"))
    }

    override fun HttpSuccess(datas: LockDataBean) {
        data = datas
        tv_start_name.text = datas.newFname
        tv_end_name.text = datas.fname
        tv_user_allsum.text = "${getString(R.string.useful)}${BigDecimal(datas.getfTotal()).setScale(4, BigDecimal.ROUND_DOWN)}"
    }
}

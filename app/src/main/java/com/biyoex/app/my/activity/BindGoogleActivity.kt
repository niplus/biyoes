package com.biyoex.app.my.activity

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import com.biyoex.app.R
import com.biyoex.app.common.Constants
import com.biyoex.app.common.activity.WebPageLoadingActivity
import com.biyoex.app.common.mvpbase.BaseActivity
import com.biyoex.app.common.bean.GoogleSecret
import com.biyoex.app.my.presenter.BindGooglePresenter
import com.biyoex.app.my.view.BindGoogleView
import kotlinx.android.synthetic.main.activity_google_bind.*
    /**
     * 綁定google
     * */
class BindGoogleActivity : BaseActivity<BindGooglePresenter>(), BindGoogleView {
    override fun bindGoogleSuccess() {

    }

    override fun getLayoutId(): Int = R.layout.activity_google_bind

    override fun createPresent(): BindGooglePresenter = BindGooglePresenter(this)
    override fun initComp() {

    }

    override fun initData() {
        iv_back.setOnClickListener { finish() }
        mPresent.getGooglePass()
        tv_copy.setOnClickListener {
            val clipData1 = ClipData.newPlainText(null, tv_googlesecret.text.toString())
            var cmb = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
          cmb.setPrimaryClip(clipData1)
            showToast(getString(R.string.copy_success))
        }
        btn_next.setOnClickListener {
            startActivityForResult(Intent(this, BindGoogleNextActivity::class.java), 55)
        }
        tv_bind_google.setOnClickListener {
            val intent = Intent(context, WebPageLoadingActivity::class.java)
            intent.putExtra("url", Constants.BIND_Google)
            intent.putExtra("title","绑定Google验证")
            intent.putExtra("type", "url")
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == 55) {
            finish()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
    override fun getGoogleSuccess(mdata: GoogleSecret) {
        tv_googlesecret.text = mdata.secret
    }
}

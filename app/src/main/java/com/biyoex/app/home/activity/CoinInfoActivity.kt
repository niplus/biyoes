package com.biyoex.app.home.activity

import com.biyoex.app.R
import com.biyoex.app.common.base.BaseActivity
import com.biyoex.app.trading.fragmnet.CoinDetailsFragment
import kotlinx.android.synthetic.main.layout_title_while_bar.*

class CoinInfoActivity :BaseActivity() {
    override fun initView() {
    }

    override fun initData() {
        var fragmentManager = supportFragmentManager
        iv_menu.setOnClickListener { finish() }
        tv_title.text = "币种介绍"
        var ft = fragmentManager.beginTransaction()
        ft.add(R.id.layout_fl,CoinDetailsFragment(intent.getIntExtra("coinId",0).toString()))
        ft.commit()
    }

    override fun getLayoutId(): Int = R.layout.activity_coin_info

}

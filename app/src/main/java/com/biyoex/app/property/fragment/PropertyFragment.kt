package com.biyoex.app.property.fragment


import android.annotation.SuppressLint
import androidx.lifecycle.Observer
import android.content.Intent

import androidx.fragment.app.Fragment
import android.text.InputType
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import com.biyoex.app.R
import com.biyoex.app.common.activity.LoginActivity
import com.biyoex.app.common.base.RxBus
import com.biyoex.app.common.base.RxBusData
import com.biyoex.app.common.data.SessionLiveData
import com.biyoex.app.common.mvpbase.BaseFragment
import com.biyoex.app.common.utils.MoneyUtils
import com.biyoex.app.my.activity.CertificatesInfoActivity
import com.biyoex.app.my.activity.CurrencyListActivity
import com.biyoex.app.my.bean.RechargeCoinBean
import com.biyoex.app.my.bean.ZGSessionInfoBean
import com.biyoex.app.property.adapter.PropertyVPAdapter
import com.biyoex.app.property.presenter.PropertyListPresenter
import com.biyoex.app.property.view.PropertyListView
import kotlinx.android.synthetic.main.fragment_property.*
import java.math.BigDecimal


/**
 * 資產fragment
 *
 */
class PropertyFragment : BaseFragment<PropertyListPresenter>(), PropertyListView {


    @JvmField
    @BindView(R.id.layout_property_draw)
    var layoutPropertyDraw: View? = null

    @JvmField
    @BindView(R.id.layout_property_recharge)
    var layoutPropertyRecharge: View? = null

    @JvmField
    @BindView(R.id.tv_property)
    var tvProperty: TextView? = null

    @JvmField
    @BindView(R.id.is_open)
    var isOpen: ImageView? = null

    var titles = mutableListOf<String>()
    var fragments = mutableListOf<Fragment>()
    var sessionInfo: ZGSessionInfoBean? = null
    private var temp: MutableList<RechargeCoinBean>? = mutableListOf()
    private var isOpenEyes: Int = 0
    override fun createPresent(): PropertyListPresenter = PropertyListPresenter()
    override fun getLayoutId(): Int = R.layout.fragment_property1
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            mPresent?.requestPersonalFinance("", 0)
        }
    }

    override fun initImmersionBar() {
//        immersionBar?.statusBarDarkFont(false)?.init()
    }

    override fun initData() {
        titles.clear()
        fragments.clear()
        titles.add(getString(R.string.ordinary_assets))
//        titles.add(getString(R.string.innovation_zone_assets))
        fragments.add(PropertyListFragment(1))
//        fragments.add(PropertyListFragment(2))
        viewpager_proerty.adapter = PropertyVPAdapter(childFragmentManager, context!!, titles, fragments)
        tablayout_property.setupWithViewPager(viewpager_proerty)
        smartrefresh.isEnableLoadmore = false
        mPresent?.requestPersonalFinance("", 0)
    }

    override fun initComp() {
        smartrefresh.setOnRefreshListener {
            mPresent?.requestPersonalFinance("", 0)
        }
        isOpen!!.setOnClickListener {
            if (isOpenEyes == 0) {
                isOpenEyes = 1
//                tv_usdt_value.inputType = InputType.TYPE_NUMBER_VARIATION_PASSWORD
                isOpen!!.setImageResource(R.mipmap.eve_open)
                tvProperty!!.inputType = InputType.TYPE_NUMBER_VARIATION_PASSWORD
//                tv_valuation_money.setSelection(tv_usdt_value.text.toString().length)
            } else {
                isOpenEyes = 0
//                tv_usdt_value.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
                isOpen!!.setImageResource(R.mipmap.eve_close)
                tvProperty!!.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
            }
        }
        SessionLiveData.getIns().observe(this, Observer {
            if (it != null) {
                sessionInfo = it
            }
        })
        //提现
        layoutPropertyDraw!!.setOnClickListener {
            isLogin(0)
        }
        //充值
        layoutPropertyRecharge!!.setOnClickListener {
            isLogin(1)
        }
    }

    override fun onResume() {
        super.onResume()
//        mPresent.requestPersonalFinance("", 0)
    }

    private fun isLogin(i: Int) {
        if (sessionInfo != null) {
            if (sessionInfo!!.isAuth) {
                val itCurrencyRecharge = Intent(activity, CurrencyListActivity::class.java)
                itCurrencyRecharge.putExtra("type", if (i == 1) getString(R.string.recharge) else getString(R.string.withdraw))
                startActivity(itCurrencyRecharge)
            } else {
                val itCertificatesInfo = Intent(activity, CertificatesInfoActivity::class.java)
                startActivity(itCertificatesInfo)
            }
        } else {
            val itLogin = Intent(activity, LoginActivity::class.java)
            itLogin.putExtra("type", "info")
            startActivity(itLogin)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun httpSuccess(response: MutableList<RechargeCoinBean>?, coinname: String, totalValue: String, usdtValue: String) {
        smartrefresh.finishRefresh()
//        Log.e("登录完成")
        hideLoadingDialog()
        tvProperty!!.text = "¥ " + MoneyUtils.decimal2ByUp(BigDecimal(totalValue)) + "CNY"
//        tv_usdt_value.setText("${MoneyUtils.decimal4ByUp(BigDecimal(usdtValue))}")
        temp = response
        var data = RxBusData()
        data.msgData = temp
        data.msgName = "propertyList"
        RxBus.get().post(data)
    }

    override fun httpErrorView(code: Int) {
        super.httpErrorView(code)
        hideLoadingDialog()
//        tv_usdt_value.setText("0.000")
        tvProperty!!.text = "0.000"
        if (code == 401) {
            val intent = Intent(context, LoginActivity::class.java)
            intent.putExtra("tag", "property")
            context!!.startActivity(intent)
        }
    }
}

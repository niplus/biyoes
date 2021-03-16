package com.biyoex.app.my.activity

import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import butterknife.BindView
import butterknife.OnClick
import com.uber.autodispose.AutoDispose
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.biyoex.app.R
import com.biyoex.app.VBTApplication
import com.biyoex.app.common.activity.WebPageLoadingActivity
import com.biyoex.app.common.base.BaseAppCompatActivity
import com.biyoex.app.common.bean.CancelWithdrawEvent
import com.biyoex.app.common.bean.RequestResult
import com.biyoex.app.common.http.RetrofitHelper
import com.biyoex.app.common.mvpbase.BaseObserver
import com.biyoex.app.common.utils.MoneyUtils
import com.biyoex.app.common.utils.ToastUtils
import com.biyoex.app.my.bean.WithdrawCoinrecordBean
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus
import java.math.BigDecimal
import java.text.SimpleDateFormat

//提现详情
class WithDrawRecordDetailActivity : BaseAppCompatActivity() {
    @JvmField
    @BindView(R.id.tv_title)
    var tvTitle: TextView? = null

    @JvmField
    @BindView(R.id.tv_coin_shortName)
    var tvCoinShorName: TextView? = null

    @JvmField
    @BindView(R.id.tv_coin_name)
    var tvCoinName: TextView? = null

    @JvmField
    @BindView(R.id.tv_amount)
    var tvAmount: TextView? = null

    @JvmField
    @BindView(R.id.tv_fee)
    var tvFee: TextView? = null

    @JvmField
    @BindView(R.id.tv_status)
    var tvStatus: TextView? = null

    @JvmField
    @BindView(R.id.tv_address)
    var tvAddress: TextView? = null

    @JvmField
    @BindView(R.id.tv_txid)
    var tvTxid: TextView? = null

    @JvmField
    @BindView(R.id.tv_time)
    var tvTime: TextView? = null

    @JvmField
    @BindView(R.id.btn_left)
    var btnLeft: Button? = null

    @JvmField
    @BindView(R.id.btn_right)
    var btnRight: Button? = null

    @JvmField
    @BindView(R.id.layout_remark)
    var layout_remark: RelativeLayout? = null

    @JvmField
    @BindView(R.id.tv_remark)
    var tvRemark: TextView? = null
    private var strRecordType: String? = null
    private var coinName: String? = null
    private var record: WithdrawCoinrecordBean.DataBean? = null
    private var clipboard: ClipboardManager? = null
    override fun initView() {
        if (strRecordType == getString(R.string.recharge)) {
            tvTitle!!.setText(R.string.recharge_detail)
            tvAmount!!.text = "+" + MoneyUtils.decimal4ByUp(BigDecimal(record!!.amount))
            when (record!!.status) {
                0 -> {
                    tvStatus!!.setTextColor(resources.getColor(R.color.commonBlue))
                    tvStatus!!.text = getString(R.string.lock_dealing)
                }
                3 -> {
                    tvStatus!!.setTextColor(VBTApplication.RISE_COLOR)
                    tvStatus!!.text = getString(R.string.recharge_success)
                }
                2 -> {
                    tvStatus!!.setTextColor(resources.getColor(R.color.commonBlue))
                    tvStatus!!.text = getString(R.string.lock_dealing)
                }
                1 -> {
                    tvStatus!!.setTextColor(resources.getColor(R.color.commonBlue))
                    tvStatus!!.text = getString(R.string.lock_dealing)
                }
            }
        } else {
            tvTitle!!.setText(R.string.withdraw_detail)
            tvAmount!!.text = "-" + MoneyUtils.decimal4ByUp(BigDecimal(record!!.amount))
            when (record!!.status) {
                4 -> {
                    tvStatus!!.setTextColor(resources.getColor(R.color.price_red))
                    tvStatus!!.text = getString(R.string.user_revocation)
                    btnRight!!.visibility = View.GONE
                    btnLeft!!.visibility = View.GONE
                }
                3 -> {
                    tvStatus!!.setTextColor(VBTApplication.RISE_COLOR)
                    tvStatus!!.text = getString(R.string.withdraw_success)
                }
                2 -> {
                    tvStatus!!.setTextColor(resources.getColor(R.color.commonBlue))
                    tvStatus!!.text = getString(R.string.lock_dealing)
                }
                1 -> {
                    tvStatus!!.setTextColor(Color.parseColor("#f5bb1e"))
                    tvStatus!!.text = getString(R.string.wait_deal)
                    btnRight!!.text = getString(R.string.revoke)
                }
            }
            if (record!!.remark != null) {
                layout_remark!!.visibility = if (record!!.remark.isEmpty()) View.GONE else View.VISIBLE
                tvRemark!!.text = record!!.remark
            }
        }
        tvCoinShorName!!.text = record!!.name
        tvCoinName!!.text = coinName
        if (!TextUtils.isEmpty(record!!.in_address)) {
            tvAddress!!.text = record!!.in_address
        } else if (!TextUtils.isEmpty(record!!.address)) {
            tvAddress!!.text = record!!.address
        } else {
            tvAddress!!.text = getString(R.string.not_have)
        }
        if (!TextUtils.isEmpty(record!!.fees)) {
            tvFee!!.text = MoneyUtils.decimal2ByUp(BigDecimal(record!!.fees)).toString() + ""
        } else if (!TextUtils.isEmpty(record!!.ffees)) {
            tvFee!!.text = MoneyUtils.decimal2ByUp(BigDecimal(record!!.ffees)).toString() + ""
        } else {
            tvFee!!.text = "0.00"
        }
        if (TextUtils.isEmpty(record!!.txid)) {
            tvTxid!!.setText(R.string.not_have)
            tvTxid!!.setTextColor(resources.getColor(R.color.text_normal))
        } else {
            tvTxid!!.text = record!!.txid
            tvTxid!!.setTextColor(resources.getColor(R.color.commonBlue))
        }
        val sp = SimpleDateFormat("yyyy-MM-dd  HH:mm:ss")
        tvTime!!.text = sp.format(record!!.time)
    }

    @OnClick(R.id.iv_menu)
    override fun onBackPressed() {
        super.onBackPressed()
    }

    @OnClick(R.id.btn_right, R.id.btn_left, R.id.tv_txid)
    fun onClick(view: View) {
        when (view.id) {
            R.id.tv_txid -> {
                if (TextUtils.isEmpty(record!!.txid)) {
                    return
                }
                val webLoadingActivity = Intent(this, WebPageLoadingActivity::class.java)
                webLoadingActivity.putExtra("type", "url")
                webLoadingActivity.putExtra("url", record!!.url)
                webLoadingActivity.putExtra("title", "TXID")
                startActivity(webLoadingActivity)
            }
            R.id.btn_right -> if (btnRight!!.text.toString() == getString(R.string.revoke)) {
                AlertDialog.Builder(this)
                        .setTitle(R.string.hint_message)
                        .setMessage(R.string.confirm_cancel)
                        .setNegativeButton(R.string.confirm) { dialog: DialogInterface?, which: Int -> requestCancelWithdrawListener() }
                        .setPositiveButton(R.string.cancel) { dialog: DialogInterface?, which: Int -> }.show()
            } else {
                val clipData = ClipData.newPlainText(null, tvTxid!!.text.toString())
                clipboard!!.setPrimaryClip(clipData)
                ToastUtils.showToast("已" + getString(R.string.copy_txid))
            }
            R.id.btn_left -> {
                //                EventBus.getDefault().post(new CancelWithdrawEvent(record.getName(), record.getId()));
//                finish();
                val clipData = ClipData.newPlainText(null, tvAddress!!.text.toString())
                clipboard!!.setPrimaryClip(clipData)
                ToastUtils.showToast(getString(R.string.already_copy_address))
            }
        }
    }

    override fun initData() {
        val itRecord = intent
        strRecordType = itRecord.getStringExtra("type")
        coinName = itRecord.getStringExtra("currency_name")
        record = itRecord.getSerializableExtra("recordDetail") as WithdrawCoinrecordBean.DataBean
        clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_with_draw_record_detail
    }

    fun requestCancelWithdrawListener() {
        showLoadingDialog()
        RetrofitHelper
                .getIns()
                .zgtopApi
                .cancelWithdraw(record!!.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .`as`(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this, Lifecycle.Event.ON_DESTROY)))
                .subscribe(object : BaseObserver<RequestResult<Any>>() {
                    protected override fun success(result: RequestResult<Any>) {
                        ToastUtils.showToast(getString(R.string.cancel_success))
                        tvStatus!!.text = getString(R.string.user_revocation)
                        EventBus.getDefault().post(CancelWithdrawEvent(record!!.name, record!!.id))
                        tvStatus!!.setTextColor(resources.getColor(R.color.price_red))
                        btnRight!!.visibility = View.GONE
                        btnLeft!!.visibility = View.GONE
                    }

                    override fun failed(code: Int, data: String?, msg: String?) {
                        ToastUtils.showToast(getString(R.string.cancel_failed))
                    }

                    override fun endOperate() {
                        hideLoadingDialog()
                    }
                })
    }
}
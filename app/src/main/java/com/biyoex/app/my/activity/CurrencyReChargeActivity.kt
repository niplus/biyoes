package com.biyoex.app.my.activity

import butterknife.BindView
import com.biyoex.app.R
import android.graphics.Bitmap
import com.biyoex.app.my.bean.ChainNameBean
import android.annotation.SuppressLint
import android.content.Intent
import io.reactivex.ObservableOnSubscribe
import io.reactivex.ObservableEmitter
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder
import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import butterknife.OnClick
import android.content.ClipData
import android.content.ClipboardManager
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.biyoex.app.common.okhttp.OkHttpUtils
import com.biyoex.app.common.okhttp.callback.StringCallback
import kotlin.Throws
import com.biyoex.app.common.Constants
import com.biyoex.app.common.base.BaseActivity
import com.biyoex.app.common.utils.CommonUtil
import com.biyoex.app.common.utils.ToastUtils
import com.biyoex.app.common.utils.androidutilcode.ScreenUtils
import com.biyoex.app.common.utils.log.Log
import io.reactivex.Observable
import io.reactivex.Observer
import okhttp3.Call
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception

/**
 * 虚拟币充值页面
 * Created by LG on 2017/6/7.
 */
class CurrencyReChargeActivity : BaseActivity() {
    @JvmField
    @BindView(R.id.iv_qr_code)
    var ivQrCode: ImageView? = null

    @JvmField
    @BindView(R.id.tv_address)
    var tvAddress: TextView? = null

    @JvmField
    @BindView(R.id.tv_recharge_hint)
    var tvRechargeHint: TextView? = null

    //    @BindView(R.id.tv_lable_name)
    //    TextView tvLableName;
    //    @BindView(R.id.ll_label)
    //    View llLabel;
    @JvmField
    @BindView(R.id.tv_shortName)
    var tvShortName: TextView? = null

    @JvmField
    @BindView(R.id.switchover)
    var tv_swithover: TextView? = null

    //    @BindView(R.id.tv_allName)
    //    TextView tvAllName;
    @JvmField
    @BindView(R.id.iv_save_iamge)
    var ivSavaImage: TextView? = null

    @JvmField
    @BindView(R.id.iv_copy)
    var tvCopy: TextView? = null

    @JvmField
    @BindView(R.id.tv_payment_hint)
    var tvPaymentHint: TextView? = null

    @JvmField
    @BindView(R.id.tv_payment_address)
    var tvPaymentAddress: TextView? = null

    @JvmField
    @BindView(R.id.tv_copy_payment)
    var tvCopyPayment: TextView? = null

    @JvmField
    @BindView(R.id.tv_chainName_shortName)
    var tvChainName: TextView? = null

    @JvmField
    @BindView(R.id.layout_chainName)
    var layout_chainName: ConstraintLayout? = null

    @JvmField
    @BindView(R.id.rg_chain)
    var rgChain: RadioGroup? = null
    private var strCurrencyName: String? = null
    private var strSymbol: String? = null
    private var strShortName: String? = null
    private var url: String? = null
    private var cmb: ClipboardManager? = null
    private var bitmapImage: Bitmap? = null
//    private var adapter: ChainNameAdapter? = null
//    private val list: MutableList<ChainNameBean> = ArrayList()
//    private var dialog: TransferChainNameDialog? = null
    override fun initView() {
        tvShortName!!.text = strShortName
        //        tvAllName.setText(strCurrencyName);
    }

    @SuppressLint("SetTextI18n")
    override fun initData() {
        val itCurrencyName = intent
        strCurrencyName = itCurrencyName.getStringExtra("currency_name")
        strSymbol = itCurrencyName.getStringExtra("symbol")
        strShortName = itCurrencyName.getStringExtra("shortName")
        url = itCurrencyName.getStringExtra("img_url")
        cmb = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager

        rgChain!!.setOnCheckedChangeListener { group, checkedId ->
            for (i in 0 until rgChain!!.childCount){
                val child = rgChain!!.getChildAt(i) as RadioButton
                if (child.isChecked){
                    val chainInfo = child.tag as ChainNameBean
                    val Address = chainInfo.address
                    val type = chainInfo.type
                    tvChainName!!.text = "链名称：USDT-$type"
                    createEnglishQRCodeWithLogo(Address) //生成地址二维码
                    tvAddress!!.text = Address
                }
            }
        }
//        adapter = ChainNameAdapter(context, R.layout.item_account_name, list)
//        adapter!!.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int ->
//            dialog!!.dismiss()
//            val Address = list[position].address
//            val type = list[position].type
//            tvChainName!!.text = "链名称：USDT-$type"
//            createEnglishQRCodeWithLogo(Address) //生成地址二维码
//            tvAddress!!.text = Address
//        }
//        dialog = TransferChainNameDialog(context, adapter!!)
        showLoadingDialog()
        requestRechargeCoinpage()
    }

    private fun createEnglishQRCodeWithLogo(address: String) {
        Observable.create(ObservableOnSubscribe { e: ObservableEmitter<Bitmap> ->
            e.onNext(QRCodeEncoder.syncEncodeQRCode(address, BGAQRCodeUtil.dp2px(this@CurrencyReChargeActivity, 125f)))
            e.onComplete()
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(object : Observer<Bitmap> {
                    override fun onSubscribe(d: Disposable) {}
                    override fun onNext(bitmap: Bitmap) {
                        if (bitmap != null) {
                            ivQrCode!!.setImageBitmap(bitmap)
                            bitmapImage = bitmap
                        } else {
                            ToastUtils.showToast(getString(R.string.create_qr_failed))
                        }
                    }

                    override fun onError(e: Throwable) {}
                    override fun onComplete() {}
                })
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_currency_newrecharge
    }

    @OnClick(R.id.iv_back, R.id.iv_menu, R.id.iv_copy, R.id.switchover, R.id.iv_save_iamge, R.id.tv_copy_payment)
    fun onClick(view: View) {
        when (view.id) {
            R.id.switchover, R.id.iv_back -> finish()
            R.id.iv_copy -> {
                val clipData = ClipData.newPlainText(null, tvAddress!!.text.toString())
                cmb!!.setPrimaryClip(clipData)
                ToastUtils.showToast(getString(R.string.already_copy_address))
            }
            R.id.iv_menu -> {
                val itWithdrawAndRechargeCurrencyRecord = Intent(this, WithdrawAndRechargeCurrencyRecordActivity::class.java)
                itWithdrawAndRechargeCurrencyRecord.putExtra("type", getString(R.string.recharge))
                itWithdrawAndRechargeCurrencyRecord.putExtra("symbol", strSymbol)
                itWithdrawAndRechargeCurrencyRecord.putExtra("img_url", url)
                itWithdrawAndRechargeCurrencyRecord.putExtra("currency_name", strCurrencyName)
                startActivity(itWithdrawAndRechargeCurrencyRecord)
            }
            R.id.tv_copy_payment -> {
                val clipData1 = ClipData.newPlainText(null, tvPaymentAddress!!.text)
                cmb!!.setPrimaryClip(clipData1)
                ToastUtils.showToast(getString(R.string.copy_success))
            }
            R.id.iv_save_iamge -> if (bitmapImage != null) {
                CommonUtil.saveBitmap2file(bitmapImage, this)
            }
//            R.id.tv_chainName_switchover -> dialog!!.show()
        }
    }

    fun requestRechargeCoinpage() {
        OkHttpUtils
                .get()
                .addHeader("X-Requested-With", "XMLHttpReques")
                .url(Constants.BASE_URL + "v1/account/getCoinAddress")
                .addParams("symbol", strSymbol)
                .addParams("create", "1")
                .build()
                .execute(object : StringCallback() {
                    override fun onError(call: Call, e: Exception, id: Int) {
                        Log.e(e, "requestRechargeCoinpage")
                        hideLoadingDialog()
                    }

                    @SuppressLint("SetTextI18n")
                    @Throws(JSONException::class)
                    override fun onResponse(response: String, id: Int) {
                        hideLoadingDialog()
                        val `object` = JSONObject(response)
                        val code = `object`.getInt("code")
                        val data = `object`.getJSONObject("data")
                        if (code == 200) {
                            val address = data.getString("address")
                            val multiChain = data.getString("multiChain")
                            if ("true" == multiChain) {
                                layout_chainName!!.visibility = View.VISIBLE
                                val multiChainList = data.getJSONArray("multiChainList") //链名称列表
                                for (i in 0 until multiChainList.length()) {
                                    val bean = ChainNameBean()
                                    val Default = multiChainList.getJSONObject(i).getString("default")
                                    val Address = multiChainList.getJSONObject(i).getString("address")
                                    val type = multiChainList.getJSONObject(i).getString("type")

                                    bean.address = Address
                                    bean.type = type

                                    val radioButton = RadioButton(this@CurrencyReChargeActivity)
                                    radioButton.setBackgroundResource(R.drawable.selector_recharge_chose)
                                    radioButton.setTextColor(resources.getColorStateList(R.color.recharge_textcolor_chose))
                                    radioButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
                                    radioButton.setButtonDrawable(0)
                                    radioButton.tag = bean
                                    radioButton.text = type
                                    radioButton.gravity = Gravity.CENTER
                                    val layoutParams = ConstraintLayout.LayoutParams(ScreenUtils.dp2px(60f), ScreenUtils.dp2px(25f))
                                    if (rgChain!!.childCount != 0)
                                        layoutParams.marginStart = ScreenUtils.dp2px(15f)

                                    radioButton.layoutParams = layoutParams
                                    rgChain!!.addView(radioButton)

                                    if ("true" == Default) {
                                        tvChainName!!.text = "链名称：USDT-$type"
                                        createEnglishQRCodeWithLogo(Address) //生成地址二维码
                                        tvAddress!!.text = Address
                                        rgChain!!.check(radioButton.id)
                                    }
                                }
//                                adapter!!.notifyDataSetChanged()
                            } else {
                                layout_chainName!!.visibility = View.GONE
                                createEnglishQRCodeWithLogo(address) //生成地址二维码
                                tvAddress!!.text = address
                            }
                            val confirm = data.getString("confirm")
                            val minIn = data.getString("min_in")
                            val name = data.getString("name")
//                            tvRechargeHint!!.text = String.format("请勿向上述地址充值任何非%s资产，否则资产将不可找回。\n您充值至上述充币地址后，需要整个网络节点的确认。\n最小充值金额为%s%s，小于最小金额的充值将不会上账且无法退回。\n您的充值地址不会经常改变，可以重复充值；\n如有更改我们尽量通过邮件，短信或者公告提前告知您。", name, minIn, name)
                            tvRechargeHint!!.text = String.format("说明：请勿向上述地址充值任何非%s资产，否则资产将不可找回。", name)
                            val lable = data.getString("lable")
                            if (lable != "0") {
                                tvPaymentAddress!!.text = lable
                                tvPaymentHint!!.visibility = View.VISIBLE
                                tvCopyPayment!!.visibility = View.VISIBLE
                                tvPaymentAddress!!.visibility = View.VISIBLE
                                //                                tvLableName.setText(lable);
//                                llLabel.setVisibility(View.VISIBLE);
                            } else {
                                tvPaymentHint!!.visibility = View.GONE
                                tvCopyPayment!!.visibility = View.GONE
                                tvPaymentAddress!!.visibility = View.GONE
                            }
                        } else {
                            Toast.makeText(this@CurrencyReChargeActivity, R.string.create_qr_failed, Toast.LENGTH_SHORT).show()
                        }
                    }
                })
    }
}


package com.biyoex.app.home.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.text.Editable
import android.text.InputFilter
import android.view.WindowManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.kotlin.autoDisposable
import com.biyoex.app.R
import com.biyoex.app.common.bean.CoinPosBean
import com.biyoex.app.common.bean.PosInvitationNumberBean
import com.biyoex.app.common.bean.PosRoleListBean
import com.biyoex.app.common.bean.RequestResult
import com.biyoex.app.common.data.AppData
import com.biyoex.app.common.http.RetrofitHelper
import com.biyoex.app.common.mvpbase.BaseActivity
import com.biyoex.app.common.mvpbase.BaseObserver
import com.biyoex.app.common.utils.CashierInputFilterUtils
import com.biyoex.app.common.utils.ToastUtils
import com.biyoex.app.home.presenter.PosRolesPresenter
import com.biyoex.app.home.view.PosRolesView
import com.biyoex.app.my.view.DialogAccountPartner
import com.biyoex.app.my.view.PosInternalTransferAdapter
import com.biyoex.app.my.view.TransferAccountsDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_internal_transfer.*
import kotlinx.android.synthetic.main.layout_title_while_bar.*


/**
 * Created by mac on 20/7/29.
 * 站内转账
 */

class PosInternalTransferActivity : BaseActivity<PosRolesPresenter>(), DialogAccountPartner, PosRolesView {

    var coinId = 0
    var roleId = 0
    var userId = 0
    var type = 0
    var balance = ""
    var amount = ""
    var dialog: TransferAccountsDialog? = null
    lateinit var mAdapter: PosInternalTransferAdapter

    @SuppressLint("SetTextI18n")
    override fun initData() {
        iv_menu.setOnClickListener { finish() }
        tv_right.text = "转账记录"
        tv_title.text = "站内转账"
        //转账记录
        tv_right.setOnClickListener {
            startActivity(Intent(this, TransferRecordActivity::class.java).putExtra("coinId", coinId))
        }
        //选择转账账号
        layout_choose_transfer_account.setOnClickListener {
            if (AppData.getRoleId() != 0) {//如果一进来默认是子账号
                ToastUtils.showToast("子账号只能给主账号转账")
            } else {
                dialog?.show()
            }
        }
        showSoftInputFromWindow(edit_transfer_inputNumber)
        //全部
        tv_transfer_all_number.setOnClickListener {
            amount = balance
            edit_transfer_inputNumber.text = Editable.Factory.getInstance().newEditable(balance)
        }
        //确定转账
        tv_determine_transfer.setOnClickListener {
            amount = findViewById<EditText>(R.id.edit_transfer_inputNumber).text.toString();
//            //type=1主账号转子账户   type=2子账户转主账号
//            type = if (userId == 0) {
//                1
//            } else {
//                2
//            }
            if ("" == amount) {
                ToastUtils.showToast("请输入转账金额")
            } else {
                getPosTransferToUserData()
            }
        }
        val cashierInputFilterUtils1 = CashierInputFilterUtils()
        cashierInputFilterUtils1.POINTER_LENGTH = 2
        val isTradingQuantity = arrayOf<InputFilter>(cashierInputFilterUtils1)
        edit_transfer_inputNumber.filters = isTradingQuantity
    }

    fun showSoftInputFromWindow(editText: EditText) {
        editText.isFocusable = true
        editText.isFocusableInTouchMode = true
        editText.requestFocus()
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
    }

    //获取当前币种pos详情
    fun getCoinData() {
        showLoadingDialog()
        RetrofitHelper.getIns().zgtopApi
                .getPosEarningsHome(AppData.getRoleId(), coinId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .autoDisposable(AndroidLifecycleScopeProvider.from(context as AppCompatActivity, Lifecycle.Event.ON_DESTROY))//OnDestory时自动解绑
                .subscribe(object : BaseObserver<RequestResult<CoinPosBean>>() {
                    override fun success(t: RequestResult<CoinPosBean>) {
                        hideLoadingDialog()
                        balance = t!!.data.vbtBalance.toString()
                        edit_transfer_inputNumber.hint = "请输入数量（可用数量$balance）";
                    }

                    override fun failed(code: Int, data: String?, msg: String?) {
                        super.failed(code, data, msg)
                        hideLoadingDialog()
                    }
                })
    }

    //站内转账
    private fun getPosTransferToUserData() {
        RetrofitHelper.getIns().zgtopApi
                .getPosTransferToUser(roleId, coinId, type, amount)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : BaseObserver<RequestResult<PosInvitationNumberBean>>() {
                    override fun success(t: RequestResult<PosInvitationNumberBean>) {
                        ToastUtils.showToast("转账成功")
                        edit_transfer_inputNumber.text = Editable.Factory.getInstance().newEditable("")
                        getCoinData()
                    }

                    override fun failed(code: Int, data: String?, msg: String?) {
                        super.failed(code, data, msg)
                        ToastUtils.showToast(msg)
                    }
                })

    }

    override fun getRoleListData(mListData: MutableList<PosRoleListBean>) {
        mAdapter.setNewData(mListData)
        //type=1主账号转子账户   type=2子账户转主账号
        if (AppData.getRoleId() == 0) {//如果一进来默认是主账号
            type = 1
            tv_internal_account_name.text = "子账号名称"
            if (mListData.size > 1) {
                roleId = mListData[1].id
                tv_main_account_name.text = mListData[1].nickName
            }
        } else {
            type = 2
            roleId = AppData.getRoleId()
            tv_internal_account_name.text = "主账号名称"
            for (i: Int in mListData.indices) {
                if (mListData[i].userId == 0) {
                    tv_main_account_name.text = mListData[i].nickName
                }
            }
        }
    }

    override fun getLayoutId(): Int = R.layout.activity_internal_transfer


    //选择的账号
    override fun onAccountPosRoles(name: String, RoleId: Int, UserId: Int) {
        dialog?.dismiss()
        roleId = RoleId
        userId = UserId
        tv_main_account_name.text = name
    }

    override fun createPresent(): PosRolesPresenter = PosRolesPresenter()

    override fun initComp() {
        coinId = intent.getIntExtra("coinId", 0)
        getCoinData()
        mPresent.getSwiPosRoleListData()//获取角色列表
        mAdapter = PosInternalTransferAdapter(this)
        dialog = TransferAccountsDialog(this, mAdapter, this)
    }

}

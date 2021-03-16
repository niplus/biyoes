package com.biyoex.app.common.mvpbase

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import butterknife.ButterKnife
import butterknife.Unbinder
import com.gyf.barlibrary.ImmersionBar
import com.gyf.barlibrary.ImmersionFragment
import com.biyoex.app.VBTApplication
import com.biyoex.app.common.utils.DialogUtils
import com.biyoex.app.common.utils.SharedPreferencesUtils
import com.biyoex.app.common.utils.ToastUtils

/**
 * A simple [Fragment] subclass.
 */
abstract class BaseFragment<T : BasePresent<*>?> : ImmersionFragment(), BaseView {
    private var unbinder: Unbinder? = null
    @JvmField
    protected var mPresent: T? = null
    private var loadingDialog: Dialog? = null
    @JvmField
    var immersionBar: ImmersionBar? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val inflate = inflater.inflate(layoutId, container, false)
        unbinder = ButterKnife.bind(this@BaseFragment, inflate)
        return inflate
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresent = createPresent()
        if (mPresent != null) {
            mPresent?.attachView(this@BaseFragment)
        }
        immersionBar = ImmersionBar.with(this)
        //        immersionBar.transparentNavigationBar();
        initData()
        initComp()
    }

    override fun initImmersionBar() {
//        immersionBar.transparentNavigationBar();
        val color = SharedPreferencesUtils.getInstance().getString("color", "0")
        immersionBar!!.statusBarDarkFont(true).init()
    }

    /**
     * 跳转activity 带bundle
     */
    protected fun openActivity(activity: Class<*>?, bundle: Bundle?) {
        val intent = Intent()
        intent.setClass(context!!, activity!!)
        intent.putExtras(bundle!!)
        startActivity(intent)
    }

    protected fun openActivity(activity: Class<*>?) {
        val intent = Intent()
        intent.setClass(getActivity()!!, activity!!)
        startActivity(intent)
    }

    abstract override fun getLayoutId(): Int
    protected abstract fun createPresent(): T?
    abstract fun initComp()
    abstract fun initData()
    override fun showToast(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }

    override fun showToast(@StringRes msg: Int) {
        ToastUtils.showToast(msg)
    }

    override fun showLoadingDialog() {
        if (loadingDialog == null) {
            loadingDialog = DialogUtils.getLoadDialog(activity)
        }
        loadingDialog!!.show()
    }

    override fun hideLoadingDialog() {
        if (loadingDialog != null && loadingDialog!!.isShowing) {
            loadingDialog!!.dismiss()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (unbinder != null) {
            unbinder!!.unbind()
        }
        if (mPresent != null) {
            mPresent?.detachView()
        }
    }

    override fun onPause() {
        super.onPause()
        hideLoadingDialog()
    }

    override fun getContext(): Context? {
        return activity
    }

    override fun httpError() {
        hideLoadingDialog()
    }

    companion object {
        val language: String
            get() {
                val resources = VBTApplication.getContext().resources
                val configuration = resources.configuration
                return configuration.locale.country
            }
    }
}
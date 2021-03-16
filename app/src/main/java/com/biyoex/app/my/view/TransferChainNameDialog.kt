package com.biyoex.app.my.view

import android.app.Activity
import android.view.Gravity
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.biyoex.app.R
import com.biyoex.app.common.widget.BottomDialog
import com.biyoex.app.my.adapter.ChainNameAdapter
import kotlinx.android.synthetic.main.dialog_internal_transfer.*

/**
 * Created by mac on 20/9/2.
 * 链类型切换
 */

class TransferChainNameDialog(context: Activity, var mAdapter: ChainNameAdapter) : BottomDialog(context, R.layout.dialog_chain_name) {

    init {
        val window = window
        window!!.decorView.setPadding(0, 0, 0, 0)
        // 获取Window的LayoutParams
        val attributes = window.attributes
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT
        attributes.gravity = Gravity.BOTTOM
        // 一定要重新设置, 才能生效
        window.attributes = attributes
        rv_internal_transfer.layoutManager = LinearLayoutManager(context)
        rv_internal_transfer.adapter = mAdapter
    }
}

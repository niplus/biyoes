package com.biyoex.app.my.view

import android.app.Activity
import android.view.Gravity
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.biyoex.app.R
import com.biyoex.app.common.bean.PosRoleListBean
import com.biyoex.app.common.widget.BottomDialog
import com.biyoex.app.home.activity.PosInternalTransferActivity
import kotlinx.android.synthetic.main.dialog_internal_transfer.*

/**
 * Created by mac on 20/7/29.
 * 站内转账
 */


class TransferAccountsDialog(context: Activity, var mAdapter: PosInternalTransferAdapter, var listener: PosInternalTransferActivity) : BottomDialog(context, R.layout.dialog_internal_transfer) {

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


class PosInternalTransferAdapter(var listener: PosInternalTransferActivity) : BaseQuickAdapter<PosRoleListBean, BaseViewHolder>(R.layout.item_account_name) {

    override fun convert(helper: BaseViewHolder?, item: PosRoleListBean?) {
        if (item?.userId == 0) {
            helper!!.setText(R.id.tv_transfer_account_name, "请选择子账号")
        }else{
            helper!!.setText(R.id.tv_transfer_account_name, item?.nickName)
        }
        //点击刷新数据
        helper.itemView.setOnClickListener {
            if (item?.userId != 0) {
                listener.onAccountPosRoles(item!!.nickName, item.id, item.userId)
            }
        }
    }

}

interface DialogAccountPartner {
    fun onAccountPosRoles(name: String, roleId: Int, userId: Int)
}
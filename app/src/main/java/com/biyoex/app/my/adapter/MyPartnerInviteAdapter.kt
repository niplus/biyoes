package com.biyoex.app.my.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.biyoex.app.R
import com.biyoex.app.common.utils.DateUtilsl

class MyPartnerInviteAdapter  : BaseQuickAdapter<MutableList<String>, BaseViewHolder>(R.layout.item_invite_return_rank) {
    override fun convert(helper: BaseViewHolder?, item: MutableList<String>) {
        helper!!.setText(R.id.tv_start_item_invite_retrun, DateUtilsl.times(item[2]))
                .setText(R.id.tv_center_item_invite_retrun,item[0] )
                .setText(R.id.tv_end_item_invite_retrun, if(item[1]=="1") mContext.getString(R.string.partner_common) else if (item[1]=="0") "普通用户" else mContext.getString(R.string.super_partners))
    }
}
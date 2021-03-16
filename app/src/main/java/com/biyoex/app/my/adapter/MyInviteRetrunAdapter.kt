package com.biyoex.app.my.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.biyoex.app.R
import com.biyoex.app.common.bean.MineInviteReturnBean
import com.biyoex.app.common.utils.DateUtilsl


/**
 * time = 2019/8/22 0022
 * CreatedName =我的返现邀请
 */
class MyInviteRetrunAdapter : BaseQuickAdapter<MineInviteReturnBean, BaseViewHolder>(R.layout.item_invite_return_rank) {
    override fun convert(helper: BaseViewHolder?, item: MineInviteReturnBean?) {
        helper!!.setText(R.id.tv_start_item_invite_retrun, DateUtilsl.time(item!!.fRegisterTime))
                .setText(R.id.tv_center_item_invite_retrun, item.floginName)
                .setText(R.id.tv_end_item_invite_retrun, item.invString)
    }
}
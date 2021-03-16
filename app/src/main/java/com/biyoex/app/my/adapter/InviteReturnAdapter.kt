package com.biyoex.app.my.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.biyoex.app.R
import com.biyoex.app.common.bean.InviteReturnRankBean
import java.math.BigDecimal


/**
 * time = 2019/8/22 0022
 * CreatedName =邀请排行
 */
class InviteReturnAdapter() : BaseQuickAdapter<InviteReturnRankBean, BaseViewHolder>(R.layout.item_invite_return_rank) {
    override fun convert(helper: BaseViewHolder?, item: InviteReturnRankBean?) {
        helper!!.setText(R.id.tv_start_item_invite_retrun, "NO.${helper.adapterPosition + 4}")
                .setText(R.id.tv_center_item_invite_retrun, "${item!!.userId}")
                .setText(R.id.tv_end_item_invite_retrun, "${BigDecimal(item.balance).setScale(2,BigDecimal.ROUND_DOWN)}USDT")
    }
}
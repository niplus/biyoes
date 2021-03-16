package com.biyoex.app.market.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.biyoex.app.R
import com.biyoex.app.common.bean.WeekListData

class MyRankAdapter : BaseQuickAdapter<WeekListData, BaseViewHolder>(R.layout.item_my_rank) {
    override fun convert(helper: BaseViewHolder?, item: WeekListData?) {
        helper!!.setText(R.id.tv_rank_item_my_rank, "NO.${item!!.sort}")
                .setText(R.id.tv_account_item_my_rank, item.accountName)
                .setText(R.id.tv_perpor_num_item_my_rank, item.theUnderNum)
                .setText(R.id.tv_sum_money_item_my_rank, item.userId)
    }

}
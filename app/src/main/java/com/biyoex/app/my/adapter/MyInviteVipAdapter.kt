package com.biyoex.app.my.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.guoziwei.klinelib.util.DateUtils
import com.biyoex.app.R
import com.biyoex.app.common.bean.MyInviteVipData
import java.math.BigDecimal

class MyInviteVipAdapter : BaseQuickAdapter<MyInviteVipData, BaseViewHolder>(R.layout.item_rushbuy_invite_record) {
    override fun convert(helper: BaseViewHolder?, item: MyInviteVipData?) {
        helper!!.setText(R.id.tv_account_rushbuy_invite_record, item!!.floginName)
                .setText(R.id.tv_time_reshbuy_invite_record, DateUtils.formatDateTime(item.createTime))
                .setText(R.id.tv_price_item_rushbuy, item.invString)
                .setText(R.id.tv_reward_rushbuy_invite_record, "${R.string.coin_award}${BigDecimal(item.balance).setScale(4, BigDecimal.ROUND_HALF_UP)}VBT")
                .setText(R.id.tv_amount_item_rushbuy, "VIP${item.vipGrade}")
    }
}
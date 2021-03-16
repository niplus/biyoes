package com.biyoex.app.market.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.guoziwei.klinelib.util.DateUtils
import com.biyoex.app.R
import com.biyoex.app.common.bean.RushBuyListData
import java.math.BigDecimal

class RushBuyRecordAdapter : BaseQuickAdapter<RushBuyListData, BaseViewHolder>(R.layout.item_rushbuy_invite_record) {
    override fun convert(helper: BaseViewHolder?, item: RushBuyListData?) {
        helper!!.setText(R.id.tv_account_rushbuy_invite_record, "第${item!!.num}期")
                .setText(R.id.tv_time_reshbuy_invite_record, DateUtils.formatDateTime(item.createTime.toLong()))
                .setText(R.id.tv_price_item_rushbuy, mContext.getString(R.string.to_snap_up_money) + (if (item.total == "--") item.total else BigDecimal(item.total).setScale(2, BigDecimal.ROUND_DOWN)) + "USDT")
                .setText(R.id.tv_amount_item_rushbuy, mContext.getString(R.string.to_snap_up_number) + (if (item.amount == "--") item.amount else BigDecimal(item.amount).setScale(4, BigDecimal.ROUND_DOWN)) + "NB")
                .setText(R.id.tv_reward_rushbuy_invite_record, mContext.getString(R.string.released_quantity) + if (item.relAmount == "--") item.relAmount else BigDecimal(item.relAmount).setScale(4, BigDecimal.ROUND_DOWN))
                .setText(R.id.tv_time_reward_two_reshbuy_invite_record, mContext.getString(R.string.unreleased_quantity) + if (item.leftAmount == "--") item.leftAmount else BigDecimal(item.leftAmount).setScale(4, BigDecimal.ROUND_DOWN))
    }
}

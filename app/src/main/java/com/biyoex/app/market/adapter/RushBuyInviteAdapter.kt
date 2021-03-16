package com.biyoex.app.market.adapter

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.guoziwei.klinelib.util.DateUtils
import com.biyoex.app.R
import com.biyoex.app.common.utils.MoneyUtils
import com.biyoex.app.common.bean.RushBuyInviteBean
import java.math.BigDecimal

class RushBuyInviteAdapter(var mIndex: Int) : BaseQuickAdapter<RushBuyInviteBean, BaseViewHolder>(R.layout.item_rushbuy_invite_record) {
    override fun convert(helper: BaseViewHolder?, item: RushBuyInviteBean?) {
        if (mIndex == 2) {
            helper!!.setText(R.id.tv_account_rushbuy_invite_record, item!!.userLoginName)
                    .setText(R.id.tv_price_item_rushbuy, item.gradeName)
                    .setText(R.id.tv_time_reshbuy_invite_record, DateUtils.formatDateTime(item.createTime))
                    .setText(R.id.tv_reward_rushbuy_invite_record, "${mContext.getString(R.string.coin_award)}${MoneyUtils.getDecimalString(4, "${item.amount}")}${item.coinName}")
                    .setText(R.id.tv_time_reward_two_reshbuy_invite_record, "${mContext.getString(R.string.coin_rel)}${BigDecimal(item.amount).subtract(BigDecimal(item.leftAmount)).setScale(4, BigDecimal.ROUND_HALF_UP)}${item.coinName} ")
        } else {
            helper!!.setText(R.id.tv_account_rushbuy_invite_record, item!!.loginName)
                    .setText(R.id.tv_price_item_rushbuy, DateUtils.formatDateTime(item.time))
                    .setText(R.id.tv_reward_rushbuy_invite_record, item.gradle)
                    .setVisible(R.id.layout_item_two, false)
        }
    }

    override fun getEmptyView(): View {
        return View.inflate(mContext, R.layout.layout_no_data, null)
    }
}
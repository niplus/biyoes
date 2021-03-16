package com.biyoex.app.common.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.biyoex.app.R
import com.biyoex.app.VBTApplication.FALL_COLOR
import com.biyoex.app.VBTApplication.RISE_COLOR
import com.biyoex.app.common.utils.MoneyUtils
import com.biyoex.app.home.bean.CoinTradeRankBean
import java.math.BigDecimal

class CoinPopuAdapter(var mType: Int) : BaseQuickAdapter<CoinTradeRankBean.DealDatasBean, BaseViewHolder>(if (mType == 0) R.layout.item_popupwindow else R.layout.item_market_right) {
    override fun convert(helper: BaseViewHolder?, item: CoinTradeRankBean.DealDatasBean?) {
        val ratio = MoneyUtils.decimal2ByUp(BigDecimal(item!!.fupanddown))
        if (item.fupanddown >= 0) {
            //            helper.setTextColor(R.id.tv_coin_price,RISE_COLOR);
            helper!!.setTextColor(R.id.item_market_right_money, RISE_COLOR)
            helper.setText(R.id.item_market_right_money, "+$ratio%")
        } else if (item.fupanddown < 0) {
            //            helper.setTextColor(R.id.tv_coin_price,FALL_COLOR);
            helper!!.setTextColor(R.id.item_market_right_money, FALL_COLOR)
            helper.setText(R.id.item_market_right_money, "$ratio%")
            mContext
        }
        helper!!.setText(R.id.item_market_right_tv, item.fShortName)
    }
}
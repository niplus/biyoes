package com.biyoex.app.market.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.biyoex.app.R
import com.biyoex.app.common.bean.Amount

class CoinRushBuyAdapter() : BaseQuickAdapter<Amount, BaseViewHolder>(R.layout.item_rushbuy_price) {
    var mIndex: Int = 0
    override fun convert(helper: BaseViewHolder?, item: Amount?) {
        var textView = helper!!.itemView.findViewById<TextView>(R.id.tv_item_rushbuy_price)
        if (mIndex == helper!!.adapterPosition) {
            textView.background = mContext.resources.getDrawable(R.drawable.bg_item_rushbuy_price_select)
            textView.setTextColor(mContext.resources.getColor(R.color.white))
        } else {
            textView.background = mContext.resources.getDrawable(R.drawable.bg_item_rushbuy_price)
            textView.setTextColor(mContext.resources.getColor(R.color.black))
        }
        helper!!.setText(R.id.tv_item_rushbuy_price, "${item!!.amount}USDT")
    }

    fun getSelectPosition(mSelectPosition: Int) {
        mIndex = mSelectPosition
        notifyDataSetChanged()
    }
}
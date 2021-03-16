package com.biyoex.app.my.adapter

import android.graphics.Paint
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.biyoex.app.R
import com.biyoex.app.common.bean.VipData

class VipPriceAdapter() : BaseQuickAdapter<VipData, BaseViewHolder>(R.layout.item_vip_rank) {
    var mIndex = 0;
    var mLevel = 0
    override fun convert(helper: BaseViewHolder?, item: VipData?) {
        when (helper!!.adapterPosition) {
            0 -> helper.setImageDrawable(R.id.iv_item_vip, mContext.getDrawable(R.mipmap.icon_vip_one))
            1 -> helper.setImageDrawable(R.id.iv_item_vip, mContext.getDrawable(R.mipmap.icon_vip_two))
            2 -> helper.setImageDrawable(R.id.iv_item_vip, mContext.getDrawable(R.mipmap.icon_vip_three))
            3 -> helper.setImageDrawable(R.id.iv_item_vip, mContext.getDrawable(R.mipmap.icon_vip_four))
        }

        var tv_before = helper.getView<TextView>(R.id.tv_before_price_item_vip)
        tv_before.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
        tv_before.text = "${item!!.price}VBT/${mContext.getString(R.string.day_year)}"
        helper.setText(R.id.tv_now_price_item_vip, "${item!!.discount}VBT/${mContext.getString(R.string.day_year)}")
        helper.setText(R.id.tv_everyday_num_item_vip, "${mContext.getString(R.string.every_day_buy_sum)}${item.singlePrice}USDT")
                .setText(R.id.tv_rank_item_vip, "VIP${item.grade}")
        if (helper.adapterPosition == mIndex && mLevel <= helper.adapterPosition) {
            helper.setVisible(R.id.iv_select_item_vip, true)
        } else {
            helper.setVisible(R.id.iv_select_item_vip, false)
        }
    }

    fun getLevel(level: Int) {
        mLevel = level
    }

    fun selectPosition(index: Int) {
        mIndex = index
        notifyDataSetChanged()
    }
}
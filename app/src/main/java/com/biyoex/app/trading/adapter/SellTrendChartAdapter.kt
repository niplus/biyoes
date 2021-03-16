package com.biyoex.app.trading.adapter

import android.content.Context
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.biyoex.app.R
import com.biyoex.app.common.utils.MoneyUtils
import com.biyoex.app.common.widget.RatioLinearLayout
import java.math.BigDecimal

class SellTrendChartAdapter(var context: Context, var priceDecimasl: Int, var amountDecimals: Int) : BaseQuickAdapter<MutableList<String>, BaseViewHolder>(R.layout.item_trendchart_sell) {
    var maxBuy = 0.0;
    private var totalBuy = 0.0
    fun setMaxBuy(maxBuy: Double, totalBuy: Double) {
        this.maxBuy = maxBuy
        this.totalBuy = totalBuy
        notifyDataSetChanged()
    }

    override fun convert(holder: BaseViewHolder?, item: MutableList<String>?) {
        val ratioLinearLayout = holder!!.getView<RatioLinearLayout>(R.id.ll_layout_trade_info)
        ratioLinearLayout.setStartRight(false)
        ratioLinearLayout.setPaintColor(0x00ffffff and mContext.resources.getColor(R.color.price_red) or (0x26 shl 24))
        //币种单价
        holder.setText(R.id.tv_price, MoneyUtils.decimalByUp(priceDecimasl, BigDecimal(item!![0])).toPlainString())
        holder.setTextColor(R.id.tv_price, mContext.resources.getColor(R.color.price_red))
        //购买币的数量
        val amount = java.lang.Double.valueOf(item!![1])!!
        var showAmount = ""
//        if (amount / 1000000 > 1) {
//            showAmount = MoneyUtils.decimalByUp(amountDecimals, BigDecimal(amount / 1000000)).toPlainString() + "M"
//        } else if (amount / 1000 > 1) {
//            showAmount = MoneyUtils.decimalByUp(amountDecimals, BigDecimal(amount / 1000)).toPlainString() + "K"
//        } else {
        showAmount = MoneyUtils.decimalByUp(amountDecimals, BigDecimal(amount)).toPlainString()
//        }
        holder.setText(R.id.tv_amount, showAmount)
                .setText(R.id.tv_item_rank,"${holder.adapterPosition+1}")
//        holder.setTextColor(R.id.tv_amount, mContext.resources.getColor(R.color.price_red))
//                holder.setText(R.id.tv_num, (1 + position) + "");
//                    ratioLinearLayout.setRatio(amount / totalBuy);
        var beforePrice = 0.0
        for (i in 0 until mData.size) {
            if(i<=holder.adapterPosition){
                beforePrice += java.lang.Double.valueOf(mData[i][1])
            }
        }

        ratioLinearLayout.setRatio(BigDecimal(beforePrice).divide(BigDecimal(totalBuy), 4, BigDecimal.ROUND_DOWN).toDouble())

//        ratioLinearLayout.setValues(amount, maxBuy, totalBuy)
    }
}
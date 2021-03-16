package com.biyoex.app.my.adapter

import android.content.Intent
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.biyoex.app.R
import com.biyoex.app.common.bean.HistoryRankBean
import com.biyoex.app.my.activity.MyRankActivity

class MyHistoryRankAdapter : BaseQuickAdapter<HistoryRankBean, BaseViewHolder>(R.layout.item_histroy_rank) {
    override fun convert(helper: BaseViewHolder?, item: HistoryRankBean?) {
        helper!!.setText(R.id.tv_week_item_history_rank, "第${item!!.week}周")
                .setText(R.id.tv_rank_item_history_rank, "${item.sort}")
                .setText(R.id.tv_money_item_history_rank, "${R.string.coin_award}${item.amount}NB")
                .setOnClickListener(R.id.tv_see_details_item_history_rank) {
                    var intent = Intent(mContext, MyRankActivity::class.java)
                    intent.putExtra("week", item.week)
                    intent.putExtra("tag", 1)
                    mContext.startActivity(intent)
                }
    }
}
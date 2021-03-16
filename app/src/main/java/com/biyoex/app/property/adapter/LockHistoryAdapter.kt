package com.biyoex.app.property.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.biyoex.app.R
import com.biyoex.app.common.utils.DateUtilsl

import com.biyoex.app.property.datas.LockHistoryDataBean
import java.math.BigDecimal

class LockHistoryAdapter : BaseQuickAdapter<LockHistoryDataBean, BaseViewHolder>(R.layout.item_lock_history) {
    override fun convert(helper: BaseViewHolder?, item: LockHistoryDataBean?) {
        helper!!.setText(R.id.item_tv_time, DateUtilsl.time_type("${item!!.createTime / 1000}"))
                .setText(R.id.item_tv_status, if (item!!.status == 1) mContext.getString(R.string.locking) else if (item!!.status == 3) {
                    mContext.getString(R.string.cancel)
                } else mContext.getString(R.string.useful))
                .setText(R.id.item_tv_locksum, "${BigDecimal(item!!.amount).setScale(4, BigDecimal.ROUND_HALF_UP)}")
                .setText(R.id.item_tv_unlocksum, "${BigDecimal(item!!.unlockAmount).setScale(4, BigDecimal.ROUND_HALF_UP)}")
    }
}
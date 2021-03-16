package com.biyoex.app.property.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.biyoex.app.R
import com.biyoex.app.common.utils.DateUtilsl
import com.biyoex.app.property.datas.CoidRecordData

class CoinRecordAdapter() : BaseQuickAdapter<CoidRecordData, BaseViewHolder>(R.layout.item_coin_record) {
    override fun convert(helper: BaseViewHolder?, item: CoidRecordData?) {
        helper!!.setText(R.id.item_tv_locksum, "${item!!.amount}")
                .setText(R.id.item_tv_time, "${mContext.getString(R.string.complete)}")
                .setText(R.id.item_tv_unlocksum, "${DateUtilsl.time_monthday(item.createTime)}")
                .setText(R.id.item_tv_status, if (item.type == 1) mContext.getString(R.string.lock_relase) else if (item.type == 2) mContext.getString(R.string.transfer) else if (item.type == 4) mContext.getString(R.string.transfer_out) else mContext.getString(R.string.transfer_in))

    }
}
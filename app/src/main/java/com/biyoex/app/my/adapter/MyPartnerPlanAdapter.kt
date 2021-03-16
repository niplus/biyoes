package com.biyoex.app.my.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.biyoex.app.R
import com.biyoex.app.common.bean.MyPartnerChildBean
import java.math.BigDecimal

class MyPartnerPlanAdapter :BaseQuickAdapter<MyPartnerChildBean,BaseViewHolder>(R.layout.item_my_partner) {
    var lockListener:LockListener?=null
    override fun convert(helper: BaseViewHolder?, item: MyPartnerChildBean) {
        helper!!.setText(R.id.tv_coin_price,item.unlockPrice)
                .setText(R.id.tv_lock_sum,"${BigDecimal(item.unlockRate).multiply(BigDecimal(100)).setScale(2,BigDecimal.ROUND_DOWN)}%")
        val tvLockState = helper!!.itemView.findViewById<TextView>(R.id.tv_lock_state)
        tvLockState.text = if(item.status==3) "解锁" else if(item.status==2) "已解锁" else "未解锁"
        tvLockState.background = if(item.status!=3) mContext.getDrawable(R.drawable.bg_gray_circle) else mContext.getDrawable(R.drawable.btn_bg_yellow_ciecle)
        if(item.status==3){
            tvLockState.setOnClickListener {
                lockListener?.onLockListener(item.id)
            }
        }
    }
}
interface LockListener{
    fun onLockListener(id:Int)
}
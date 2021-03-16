package com.biyoex.app.property.datas

import com.biyoex.app.my.bean.RechargeCoinBean
import java.math.BigDecimal


data class LockHistoryBean(
        val `data`: MutableList<LockHistoryDataBean>,
        val lockNum: Double,
        val releaseNum: Double,
        val coinName: String,
        val cnyValue: String
)

data class LockHistoryDataBean(
        val amount: Double,
        val coinId: Int,
        val createTime: Long,
        val id: Int,
        val status: Int,
        val unlockAmount: Double,
        val userId: Int
)

data class CoinRecordBean(
        val `data`: MutableList<CoidRecordData>,
        val coinName: String,
        val equalId: Int,
        val fFrozen: Double,
        val innovate: Boolean,
        val lockPosition: Double,
        val useAmount: Double
)

data class CoidRecordData(
        val amount: BigDecimal,
        val coinId: Int,
        val createTime: String,
        val id: Int,
        val status: Int,
        val type: Int,
        val userId: Int
)

data class PropertyListBean(
        val isopen: Boolean,
        val rechargeCoinBean: RechargeCoinBean
)
package com.biyoex.app.common.bean

import com.biyoex.app.my.bean.RechargeCoinBean
import java.io.Serializable

//home頁公告
data class IndexNoticeBean(
        val articles: List<Article>,
        val count: Int,
        val next_page: Any,
        val page: Int,
        val page_count: Int,
        val per_page: Int,
        val previous_page: Any,
        val sort_by: String,
        val sort_order: String
)

data class BaseUrlBean(
        val http_protocol: String,
        val socket_protocol: String,
        val domain_name: String
)

data class Article(
        val author_id: Long,
        val body: String,
        val comments_disabled: Boolean,
        val created_at: String,
        val draft: Boolean,
        val edited_at: String,
        val html_url: String,
        val id: Long,
        val label_names: List<Any>,
        val locale: String,
        val name: String,
        val outdated: Boolean,
        val outdated_locales: List<Any>,
        val permission_group_id: Int,
        val position: Int,
        val promoted: Boolean,
        val section_id: Long,
        val source_locale: String,
        val title: String,
        val updated_at: String,
        val url: String,
        val user_segment_id: Any,
        val vote_count: Int,
        val vote_sum: Int
)

//抢购币种价格
data class RushBuyBean(
        val amount: String,
        val amountList: MutableList<Amount>,
        val buyTime: String,
        val currentPrice: String,
        val marketPrice: String,
        val nextPrice: String,
        val relAmount: String,
        val scale: String,
        val nbRelAmount: String,
        val nbTotalAmount: String,
        val num: Int
)

data class Amount(
        val amount: String,
        val grade: Int
)

//抢购尽量
data class RushBuyListData(
        val amount: String,
        val buyCoinId: Int,
        val createTime: String,
        val id: Int,
        val leftAmount: String,
        val num: String,
        val price: String,
        val relAmount: String,
        val sellCoinId: String,
        val status: String,
        val total: String,
        val updateTime: String,
        val userId: Int
)

data class VipPriceBean(
        val `data`: MutableList<VipData>,
        val endTime: String,
        val level: Int
)

data class VipData(
        val coinId: Int,
        val discount: Double,
        val endTime: Long,
        val grade: Int,
        val id: Int,
        val price: Double,
        val singlePrice: Double,
        val startTime: Long,
        val status: Int
)

data class MyInviteVipBean(
        val `data`: MutableList<MyInviteVipData>,
        val sumBalance: Double
)

data class MyInviteVipData(
        val balance: String,
        val createTime: Long,
        val fShortName: String,
        val floginName: String,
        val invString: String,
        val vipGrade: Int
)

data class RushBuyInviteBean(
        val amount: Double,
        val coinId: Int,
        val coinName: String,
        val createTime: Long,
        val firstInvId: Int,
        val grade: String,
        val gradeName: String,
        val id: Int,
        val leftAmount: String,
        val logId: Int,
        val secondInvId: Int,
        val status: Int,
        val updateTime: Long,
        val userId: Int,
        val userLoginName: String,
        val version: Int,
        val loginName: String,
        val time: Long,
        val gradle: String
)

data class WeekRankBean(
        val `data`: List<WeekListData>,
        val firstUserId: String,
        val firstSortName: String,
        val firstUnderNum: String,
        val rankingReward: String,
        val salesVolume: String,
        val secondUserId: String,
        val secondSortName: String,
        val secondUnderNum: String,
        val sort: String,
        val thirdUserId: String,
        val thirdSortName: String,
        val thirdUnderNum: String,
        val week: Int,
        val releaseReward: String
)

data class WeekListData(
        val accountName: String,
        val amount: String,
        val sort: String,
        val theUnderNum: String,
        val userId: String
)

data class HistoryRankBean(
        val id: Int,
        val week: Int,
        val userId: Int,
        val sort: Int,
        val status: Int,
        val amount: String
)

data class WeekRewardBean(
        val nbName: String,
        val ndAmount: String,
        val ndReleaseAmount: String,
        val sort: String,
        val usdtAmount: String,
        val usdtName: String
)

data class MyHistoryBean(
        val weekList: MutableList<HistoryRankBean>,
        val totalReward: String,
        val releaseReward: String
)

data class VipBuyBean(
        val ducePrices: String
)

data class CurrencyAddressBean(
        val addresses: List<Addresse>,
        val feeAmount: String,
        val feeName: String,
        val feeRatio: String,
        val frozen: String,
        val max: String,
        val min: String,
        val minWithdraw: String,
        val times: Int,
        val total: String
)

data class Addresse(
        val address: String,
        val flag: String,
        val id: Int,
        val isremark: Boolean,
        val remark: String
)

data class CoinNameListBean(
        val balanceList: List<RechargeCoinBean>
)

data class GoogleSecret(
        val secret: String,
        val url: String
)

data class InviteReturnRankBean(
        val balance: String,
        val coinName: String,
        val userId: Int
)

data class MineInviteReturnBean(
        val fRegisterTime: String,
        val floginName: String,
        val invString: String
)

data class CoinInfoBean(
        val cnDesc: String,
        val cnName: String,
        val enDesc: String,
        val enName: String,
        val id: Int,
        val pushPrice: String,
        val pushTime: String,
        val shortName: String,
        val total: String,
        val webSite: String,
        val whitepaper: String
)

data class SignToCoin(
        val id: Int,
        val coinId: Int,
        val name: String,
        val rate: String,
        val num: String,
        val createTime: String,
        val amount: String
)

data class BurnInfoBean(
        val contracAddress: String,
        val burnTotal: String,
        val shareTotal: String,
        val blockBurnTotal: String,
        val realTotal: String
)

data class PartnerCode(val type: Int)

data class InvitationPartnerBean(
        val common: String,
        val supers: String,
        val myInvite: String,
        val amount: String
) : Serializable

data class MyPartnerBean(
        val bonusList: List<Bonus>,
        val dataList: List<MyPartnerChildBean>,
        val total: String,
        val coinName: String
)

data class Bonus(
        val bonusNum: String,
        val coinName: String
)

data class MyPartnerChildBean(
        val id: Int,
        val status: Int,
        val unlockPrice: String,
        val unlockRate: String
)

data class PosHomeBean(
        val minHold: String,
        val rate: String,
        val Icon: String,
        val coinName: String,
        val hotState: String,
        val optimalHold: String,
        val yesterdayProfitTotal: String,
        val coinId: Int
)

data class CoinPosBean(
        val webSite: String,
        val coinName: String,
        val cumulativeIncome: Double,
        val holdingYesterday: Double,
        val minHolding: Double,
        val posBalance: Double,
        val vbtBalance: Double,
        val openReset: Int
)

data class PosEarningsBean(
        val coinId: Int,
        val coinName: String,
        val createTime: String,
        val holdAmount: Double,
        val holdProfit: String,
        val spreadCoinName: String,
        val id: Int,
        val spreadProfit: String,
        val status: Int,
        val userId: Int,
        val version: Int
)

data class PosComputingBean(
        var holdPos: String,
        var spreadPos: String,
        var totalComputingPower: String
)

data class BankAndNameBean(
        val respCode: String,
        val result: String,
        val respMsg: String
)

data class PosInvitationNumberBean(
        val myInvited: String,
        val mySuperior: String,
        val minRegion: String,
        val teamTotalHold: String
)

data class PosHoldRankingBean(
        val UID: String,
        val holdAmount: String,
        val sort: String
)

data class PosRoleListBean(
        val id: Int,
        val userId: Int,
        val version: String,
        val nickName: String
)

data class PosHoldRateProfitBean(
        val rate: String,
        val holdAmount: String

)

data class PosCheckBalanceRecordsBean(
        val type: String,
        val userName: String,
        val amount: String,
        val createTime: String

)
package com.biyoex.app.common.http

import com.google.gson.JsonObject
import com.biyoex.app.VBTApplication
import com.biyoex.app.common.bean.*
import com.biyoex.app.home.bean.CoinTradeRankBean
import com.biyoex.app.home.bean.UploadApkBean
import com.biyoex.app.my.bean.RechargeCoinBean
import com.biyoex.app.my.bean.WithdrawCoinrecordBean
import com.biyoex.app.my.bean.ZGRechargeCoinBean
import com.biyoex.app.my.bean.ZGSessionInfoBean
import com.biyoex.app.property.datas.CoinRecordBean
import com.biyoex.app.property.datas.LockDataBean
import com.biyoex.app.property.datas.LockHistoryBean
import com.biyoex.app.trading.bean.MarketRealBean
import com.biyoex.app.trading.bean.MarketRefreshBean
import com.biyoex.app.trading.bean.RefreshUserInfoBean
import com.biyoex.app.trading.bean.ResonateBean
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.*

interface ZgtopApi {
    @get:GET("https://biyocn.oss-accelerate.aliyuncs.com/request_url/url.json")
    val baseUrl: Observable<BaseUrlBean>

    @get:POST("v1/account/balances")
    val property: Observable<RequestResult<JsonObject>>

    /***
     * 币种地址删除
     *
     */
    @POST("v1/account/deleteAddress")
    @FormUrlEncoded
    fun delectAddress(@Field("id") id: Int): Observable<RequestResult<Any>>

    /**
     * 锁仓数据
     */
    @GET("/api/lockRecord/getLockDataById")
    fun getLockData(@Query("id") coidId: String?): Observable<RequestResult<LockDataBean>>

    /**
     * 划转数据
     */
    @GET("/api/lockRecord/getTransferDataById")
    fun getTransferData(@Query("id") coidId: String?): Observable<RequestResult<LockDataBean>>

    /**
     * 划转
     */
    @FormUrlEncoded
    @POST("/api/lockRecord/Delimitation")
    fun postTransfer(@Field("coidId") coidId: String?, @Field("amount") amount: String?): Observable<RequestResult<Any>>

    /**
     * 锁仓记录
     */
    @GET("lockRecord/findLockPositionRecordMutableList")
    fun getLockHistory(@Query("coidId") coidId: String?, @Query("page") page: Int, @Query("pageSize") pageSum: Int): Observable<RequestResult<LockHistoryBean>>

    /**
     * 锁仓
     */
    @FormUrlEncoded
    @POST("/api/lockRecord/setLockPosition")
    fun postLock(@Field("coidId") coidId: String?, @Field("amount") amount: String?): Observable<RequestResult<Any>>

    /**
     * 财务记录
     */
    @GET("lockRecord/findBalanceRecordsMutableList")
    fun getRecordData(@Query("coidId") coidId: Int, @Query("page") page: Int, @Query("pageSize") pageSize: Int): Observable<RequestResult<CoinRecordBean>>

    @get:GET("v1/session")
    val session: Observable<RequestResult<ZGSessionInfoBean>>

    /**
     * otc下单
     *
     * @param marketId    交易市场id
     * @param id          广告单id
     * @param tradePrice  交易价格
     * @param tradeCount  交易数量
     * @param tradeAmount 交易金额
     * @param type        买卖类型
     * @param payWay      付款方式（多种以逗号隔开1.支付宝2微信3银行卡 ）
     * @return
     */
    @POST("v2/order/sureOrder")
    @FormUrlEncoded
    fun makeOrder(@Field("marketId") marketId: Int,
                  @Field("adId") id: Int?,
                  @Field("tradePrice") tradePrice: Double,
                  @Field("tradeCount") tradeCount: Double,
                  @Field("tradeAmount") tradeAmount: Double,
                  @Field("type") type: Int,
                  @Field("payWay") payWay: String?,
                  @Field("tradePwd") password: String?): Observable<RequestResult<Int>>

    @POST("v2/account/validatePhone")
    @FormUrlEncoded
    fun bindPhone(@Field("phone") phoneNum: String?, @Field("code") code: String?): Observable<RequestResult<Any>>

    /**
     * 实名认证
     */
    @POST("v1/account/auth.html")
    @FormUrlEncoded
    fun accountBind(@Field("type") type: Int, @Field("name") name: String?): Observable<RequestResult<Any>>

    /**
     * 帐号验证码
     *
     * @param type 传1
     * @return
     */
    @POST("v1/account/sendCode")
    @FormUrlEncoded
    fun sendAccountCode(@Field("type") type: Int, @Field("checkcode") code: String?): Observable<RequestResult<Any>>

    /**
     * 注冊验证码
     *
     * @param name
     * @return
     */
    @POST("v1/sendRegCode")
    @FormUrlEncoded
    fun sendRegCode(@Field("name") name: String?, @Field("code") code: String?): Observable<RequestResult<Any>>

    /**
     * 忘记密码验证码
     *
     * @param name
     * @param code
     * @return
     */
    @POST("v1/sendFindCode")
    @FormUrlEncoded
    fun sendFindCode(@Field("name") name: String?, @Field("checkcode") code: String?): Observable<RequestResult<Any>>

    @GET("v2/account/sendBindPayCode")
    fun sendBindPayCode(@Query("phone") phone: String?): Observable<RequestResult<Any>>

    /**
     * 登陆验证码
     *
     * @param code
     * @return
     */
    @GET("v1/sendLoginCode")
    fun sendLoginCode(@Query("checkcode") code: String?, @Query("name") name: String?): Observable<RequestResult<Any>>

    @get:GET("v1/cacheip/count")
    val count: Observable<String>

    @POST("v1/register")
    @FormUrlEncoded
    fun register(@Field("name") name: String?, @Field("code") code: String?, @Field("pwd") pwd: String?, @Field("inviteCode") inviteCode: String?): Observable<RequestResult<Any>>

    @GET
    fun getImageCode(@Url url: String?): Observable<ResponseBody>

    /**
     * 忘记密码
     */
    @POST("v1/findPwd")
    @FormUrlEncoded
    fun forgetPassword(@Field("name") name: String?, @Field("code") code: String?, @Field("pwd") password: String?): Observable<RequestResult<Any>>

    /**
     * 实体验证四要素
     */
    @POST("v1/faceVerify")
    @FormUrlEncoded
    fun checkBankAndName(@Field("name") name: String?, @Field("number") cardName: String?, @Field("bankCard") bankID: String?): Observable<String>

    /**
     * 实体验证提交
     */
    @POST("v1/account/auth1")
    @FormUrlEncoded
    fun postAuth(@Field("name") name: String?, @Field("no") cardID: String?, @Field("bankCard") bankID: String?, @Field("type") type: Int, @Field("fIdentityPath1") fIdentityPath1: String?,
                 @Field("fIdentityPath2") fIdentityPath2: String?, @Field("fIdentityPath3") fIdentityPath3: String?
    ): Observable<RequestResult<Any>>

    /**
     * 重置密码
     */
    @POST("v1/account/modPassWord")
    @FormUrlEncoded
    fun updagePassword(@Field("pwd") pwd: String?, @Field("code") code: String?, @Field("oldPwd") oldPwd: String?): Observable<RequestResult<Any>>

    /***
     * 设置密码
     */
    @POST("v1/account/modSafeWord")
    @FormUrlEncoded
    fun savePassword(@Field("pwd") pwd: String?, @Field("code") code: String?): Observable<RequestResult<Any>>

    /**
     * 获取googgoogle密钥
     */
    @get:GET("v1/account/genGoogle")
    val googlePass: Observable<RequestResult<GoogleSecret>>

    /**
     * 获取谷歌验证码
     */
    @POST("v1/account/sendCode")
    @FormUrlEncoded
    fun getGoogleCode(@Field("type") type: Int): Observable<RequestResult<Any>>

    /**
     * 绑定google
     */
    @POST("v1/account/authGoogle")
    @FormUrlEncoded
    fun authGoogle(@Field("password") password: String?, @Field("code") emailCode: String?, @Field("emailCode") code: String?): Observable<RequestResult<Any>>

    /**
     * 解除谷歌绑定
     */
    @POST("v1/account/unbindGoogle")
    @FormUrlEncoded
    fun unbindGoogle(@Field("password") password: String?, @Field("code") code: String?): Observable<RequestResult<Any>>

    @GET("v1/account/balance")
    fun getCoinFinanceDetail(@Query("coinId") coinId: Int): Observable<RequestResult<RechargeCoinBean>>

    @GET("v2/market/coins")
    fun requestCoinBoardRank(): Observable<CoinTradeRankBean>

    @POST("v1/login")
    @FormUrlEncoded
    fun login(@Field("name") name: String?, @Field("pwd") pwd: String?): Observable<String>

    @POST("v1/login2")
    @FormUrlEncoded
    fun login2(@Field("name") name: String?, @Field("pwd") pwd: String?, @Field("sessionId") session: String?, @Field("token") token: String?, @Field("sig") sig: String?): Observable<String>

    @GET(VBTApplication.appDownloadUrl)
    fun checkVersion(): Observable<UploadApkBean>

    @POST("v1/account/cancelWithdraw")
    @FormUrlEncoded
    fun cancelWithdraw(@Field("id") id: Int): Observable<RequestResult<Any>>

    @POST("v1/account/{type}")
    @FormUrlEncoded
    fun withdrawAndRechargRecord(@Path("type") type: String?, @Field("symbol") symbol: String?, @Field("page") page: Int, @Field("pageSize") pageSize: Int): Observable<WithdrawCoinrecordBean>

    /**
     * 币种共振
     */
    @get:GET("v1/getResonateStatus")
    val resonateStatus: Observable<RequestResult<ResonateBean>>

    //獲取用戶可用余额
    @GET("v1/account/getTransferBalance")
    fun getTransferBanlance(@Query("coinId") coidId: Int): Observable<String>

    /**
     * 获取用户资产
     */
    @get:GET("v1/account/balances")
    val accountBanlance: Observable<String>

    //转账
    @POST("v1/account/transferToUser")
    @FormUrlEncoded
    fun postTransferToUse(@Field("id") coidId: Int, @Field("googleCode") googleCode: String?, @Field("uid") uid: String?, @Field("amount") amount: String?, @Field("code") code: String?, @Field("safeWord") safeWord: String?): Observable<String>

    /**
     * 获取用户数据  symbol 币种id  t  当前时间戳
     */
    @GET("v2/market/refreshUserInfo")
    fun getMarketUserInfo(@Query("symbol") symbol: Int, @Query("t") time: String?): Observable<RefreshUserInfoBean>

    /**
     * 获取用户自选币种
     */
    @get:GET("v1/account/getUserSelfToken")
    val userSelfToken: Observable<String>

    /**
     * 上传用户自选
     */
    @POST("v1/account/updateUserSelfToken")
    @FormUrlEncoded
    fun updateUserSelfToken(@Field("user_self_token") token: String?): Observable<RequestResult<Any>>

    /**
     * 上传用户买单数据
     *
     * @param symbol       币种id
     * @param tradeAmount  交易数量
     * @param tadeCnyPrice 交易价格
     * @param password     交易密码
     */
    @POST("v2/market/buyBtcSubmit.html")
    @FormUrlEncoded
    fun requestBuyBtcSubmit(@Field("symbol") symbol: Int, @Field("tradeAmount") tradeAmount: String?, @Field("tradeCnyPrice") tadeCnyPrice: String?, @Field("tradePwd") password: String?): Observable<String>

    /**
     * 上传用户卖出数据
     */
    @POST("v2/market/sellBtcSubmit.html")
    @FormUrlEncoded
    fun requestSellBtcSubmit(@Field("symbol") symbol: Int, @Field("tradeAmount") tradeAmount: String?, @Field("tradeCnyPrice") tadeCnyPrice: String?, @Field("tradePwd") password: String?): Observable<String>

    /**
     * 撤销挂单
     */
    @POST("v2/market/cancelEntrust.html")
    @FormUrlEncoded
    fun cancelEntrush(@Field("id") id: String?): Observable<String>

    @POST("v2/market/cancelAllEntrust")
    @FormUrlEncoded
    fun cancelAllOrder(@Field("symbol") id: Int): Observable<RequestResult<Any>>

    /**
     * 获得币种交易信息
     */
    @GET("v2/market/marketRefresh.html")
    fun getCoinMarketInfo(@Query("symbol") symbol: String?, @Query("t") time: String?, @Query("deep") deep: String?): Observable<MarketRefreshBean>
    //    /**
    //     * 获取K线数据
    //     */
    //    @GET("v2/market/period.html")
    //    Observable<String> getCoinKlineData(@Query("step") String step, @Query("symbol") String symbol);
    /**
     * 获取K线数据
     */
    @GET("https://hk.biyocn.com/kline/{symbol}/{step}")
    fun getCoinKlineData(@Path("step") step: String?, @Path("symbol") symbol: String?): Observable<String>

    /**
     * 获取币种信息
     */
    @GET("v2/market/real")
    fun getCoinData(@Query("symbol") symbol: Int, @Query("t") time: String?): Observable<MarketRealBean>

    /**
     * 币种介绍信息
     */
    @GET("v1/coinDesc")
    fun getCoinInfo(@Query("symbol") id: String?): Observable<RequestResult<CoinInfoBean>>

    /**
     * 会员列表
     */
    @get:GET("vipBuy/account/getAllVipPriceMutableList")
    val vipPrice: Observable<RequestResult<VipPriceBean>>

    /**
     * 查询补差升级
     */
    @GET("vipBuy/account/ducePrices")
    fun getBuyVipPrice(@Query("vipGrade") grade: Int): Observable<RequestResult<VipBuyBean>>

    /**
     * 开通会员
     */
    @POST("vipBuy/account/openMember")
    @FormUrlEncoded
    fun postBuyVip(@Field("vipGrade") vipGrade: Int): Observable<RequestResult<Any>>

    /**
     * 邀请会员收益
     */
    @GET("vipBuy/account/invitationRevenueRecord")
    fun getMyInviteVip(@Query("page") page: Int, @Query("pageSize") pageSize: Int): Observable<RequestResult<MyInviteVipBean>>

    /**
     * 邀请注册会员排行
     */
    @get:GET("v1/invitationtoReturn")
    val inviteRankData: Observable<RequestResult<MutableList<InviteReturnRankBean>>>

    /**
     * 我的邀请注册
     */
    @POST("v1/account/myInvitation")
    @FormUrlEncoded
    fun getMyInviteReturn(@Field("page") page: Int, @Field("pageSize") pageSize: Int): Observable<RequestResult<MutableList<MineInviteReturnBean>>>

    /**
     * 币种抢购进度
     */
    @get:GET("v1/account/getBuyNb")
    val buyNb: Observable<RequestResult<RushBuyBean>>

    /**
     * 抢购NB
     */
    @POST("v1/account/buyNb")
    @FormUrlEncoded
    fun postBuyNb(@Field("grade") grade: Int): Observable<RequestResult<String>>

    /**
     * 抢购记录
     */
    @GET("v1/account/MutableList")
    fun getNBList(@Query("page") page: Int, @Query("pageSize") pageSize: Int): Observable<RequestResult<MutableList<RushBuyListData>>>

    /**
     * 获取抢购邀请记录
     */
    @GET("v1/account/userIntroMutableList")
    fun getUserInviteData(@Query("type") type: Int, @Query("page") page: Int, @Query("pageSize") pageSize: Int): Observable<RequestResult<MutableList<RushBuyInviteBean>>>

    /**
     * 当前周排名
     */
    @GET("v1/account/buyNbWeekSort")
    fun getWeekRank(@Query("week") index: Int): Observable<RequestResult<WeekRankBean>>

    /**
     * 我的历史排名记录
     */
    @GET("v1/account/historyWeekSort")
    fun getUserHistoryRank(@Query("page") page: Int, @Query("pageSize") pageSize: Int): Observable<RequestResult<MyHistoryBean>>

    /**
     * 本周奖励排名
     */
    @get:GET("v1/account/rewardRecordandWeek")
    val rewardRecordWeek: Observable<RequestResult<WeekRewardBean>>

    /**
     * 获取币种的地址
     */
    @GET("v1/account/getParamsByCoin")
    fun getParamsByCoin(@Query("id") id: String?): Observable<RequestResult<ZGRechargeCoinBean>>

    /**
     * 币种列表
     */
    @GET("v1/account/balances")
    fun getCoinList(): Observable<RequestResult<CoinNameListBean>>

    /**
     * 获取签到奖金接口
     */
    @GET("v1/signToCoin")
    fun getSignToCoin(): Observable<RequestResult<MutableList<SignToCoin>>>

    /**
     * 获取奖金下标
     */
    @get:GET("v1/account/clickSignCoin")
    val luckyIndex: Observable<RequestResult<String>>

    /**
     * 签到记录
     */
    @GET("v1/account/signRecord")
    fun getSignRecord(@Query("page") page: Int, @Query("pageSize") pageSize: Int): Observable<RequestResult<String>>

    /**
     * 获取延烧费率
     */
    @GET("v2/market/get_burn_count")
    fun getBurnInfo(@Query("symbol") symbol: Int): Observable<RequestResult<BurnInfoBean>>

    /**
     * 获取是否合伙人
     */
    @get:GET("account/getPartner")
    val partner: Observable<RequestResult<PartnerCode>>

    /**
     * 上传邀请人ID
     */
    @POST("v1/getPartnerId")
    @FormUrlEncoded
    fun postShareCode(@Field("partnerId") partnerID: String?): Observable<RequestResult<String>>

    /**
     * 获取购买合伙人的信息
     */
    @get:GET("account/InvitationPartner")
    val imnvitation: Observable<RequestResult<InvitationPartnerBean>>

    /**
     * 合伙人邀请明细
     */
    @GET("v1/account/findPartnerConnection")
    fun getMyPartnerInvite(@Query("page") page: Int, @Query("pageSize") pageSize: Int): Observable<RequestResult<MutableList<MutableList<String>>>>

    /**
     * 成为合伙人
     */
    @GET("v1/account/partnerConnection")
    fun partnerComnection(@Query("partnerId") partnerId: String?): Observable<RequestResult<Any>>

    /**
     * 我的燃烧分红
     */
    @get:GET("v1/account/findairdroprecord")
    val airdroprecord: Observable<RequestResult<MyPartnerBean>>

    /**
     * 解锁合伙人权益额度
     */
    @GET("v1/account/unlockAirdrop")
    fun postLockAirdrop(@Query("id") id: Int): Observable<RequestResult<Any>>

    /**
     * 我的空投
     */
    @GET("v1/account/checkAirdropRecord")
    fun getMyAirdrop(@Query("page") page: Int, @Query("pageSize") pageSize: Int): Observable<RequestResult<MutableList<MutableList<String>>>>

    /**
     * 提现页面获取当前币种信息
     */
    @GET("v1/account/getParamsByCoin")
    fun getUserParamsByCoin(@Query("id") id: String?): Observable<RequestResult<ZGRechargeCoinBean>>

    /**
     * 获取POS币种的列表
     */
    @GET("pos/indexHome")
    fun getPosHome(): Observable<RequestResult<MutableList<PosHomeBean>>>

    /**
     * 获取当前币种pos详情
     */
    @GET("pos/account/posEarningsHome")
    fun getPosEarningsHome(@Query("roleId") roleId: Int, @Query("coinId") coinId: Int): Observable<RequestResult<CoinPosBean>>

    /**
     * 获得最近一周的收益
     */
    @GET("pos/account/weekIncome")
    fun getWeekEarnings(@Query("coinId") coidId: Int): Observable<RequestResult<MutableList<MutableList<String>>>>

    /**
     * 加入POS
     */
    @GET("pos/account/participatePOS")
    fun addPos(@Query("roleId") roleId: Int, @Query("coinId") coinId: Int, @Query("amount") amount: String?): Observable<RequestResult<Any>>

    /**
     * 退出pos
     */
    @GET("pos/account/exitsPOS")
    fun cancelPos(@Query("roleId") roleId: Int, @Query("coinId") coinId: Int): Observable<RequestResult<Any>>

    /**
     * pos收益列表
     */
    @GET("pos/account/posIncomeDetails")
    fun getMyPosEarningsList(@Query("roleId") roleId: Int, @Query("coinId") coidId: Int, @Query("page") page: Int, @Query("pageSize") pageSize: Int): Observable<RequestResult<MutableList<PosEarningsBean>>>

    /**
     * pos算力
     */
    @GET("pos/account/computingPower")
    fun getPosComputings(@Query("roleId") roleId: Int, @Query("coinId") coinId: Int): Observable<RequestResult<PosComputingBean>>

    /**
     * 我的pos邀请收益
     */
    @GET("pos/account/myInvitationDetails")
    fun getmyInviteionDetails(@Query("coinId") coinId: Int): Observable<RequestResult<MutableList<MutableList<String>>>>

    /**
     * pos记录
     */
    @GET("pos/account/posRecord")
    fun getPosRecord(@Query("roleId") roleId: Int, @Query("coinName") coinName: String?, @Query("page") page: Int, @Query("pageSize") pageSize: Int): Observable<RequestResult<MutableList<MutableList<String>>>>

    /**
     * 是否开启自动复投
     */
    @GET("pos/account/startAutomaticReinvestment?")
    fun getAutoRecharge(@Query("roleId") roleId: Int, @Query("coinId") coinId: Int): Observable<RequestResult<Int>>

    /**
     * 我的上级，我的邀请人数，小区邀请人数
     */
    @GET("pos/account/myInvitedandSuperior?")
    fun getPosInvitation(@Query("roleId") roleId: Int, @Query("coinId") coinId: Int): Observable<RequestResult<PosInvitationNumberBean>>

    /**
     * 我的直推团队持仓
     */
    @GET("pos/account/newMyInvitationDetails")
    fun getPosMyInvitationDetails(@Query("roleId") roleId: Int, @Query("coinId") coinId: Int, @Query("page") page: Int, @Query("pageSize") pageSize: Int): Observable<RequestResult<MutableList<MutableList<String>>>>

    /**
     * 持币排行榜 前20名
     */
    @GET("pos/account/holdRanking")
    fun getPosHoldRanking(@Query("roleId") roleId: Int, @Query("coinId") coinId: Int): Observable<RequestResult<MutableList<PosHoldRankingBean>>>

    /**
     * 站内转账 (获取角色ID=roleId  type=1主账号转子账户   type=2子账户转主账号  amount金额)
     */
    @GET("v1/account/transferToUser")
    fun getPosTransferToUser(@Query("roleId") roleId: Int, @Query("coinId") coinId: Int, @Query("type") type: Int, @Query("amount") amount: String?): Observable<RequestResult<PosInvitationNumberBean>>

    /**
     * 站内转账记录
     */
    @GET("v1/account/checkBalanceRecords")
    fun getPosCheckBalanceRecords(@Query("roleId") roleId: Int, @Query("coinId") coinId: Int, @Query("page") page: Int, @Query("pageSize") pageSize: Int): Observable<RequestResult<MutableList<PosCheckBalanceRecordsBean>>>

    /**
     * 创建角色
     */
    @GET("v1/account/createPosRole2")
    fun getPosCreatePosRole(@Query("nickName") nickName: String?, @Query("invitationCode") invitationCode: String?): Observable<RequestResult<PosInvitationNumberBean>>

    /**
     * 获取角色列表
     */
    @get:GET("v1/account/getPosRoleMutableList")
    val posRoleList: Observable<RequestResult<MutableList<PosRoleListBean>>>

    /**
     * 持币收益率k线图
     */
    @GET("pos/account/holdRateProfit")
    fun getPosHoldRateProfit(@Query("roleId") roleId: Int, @Query("coinId") coinId: Int): Observable<RequestResult<MutableList<PosHoldRateProfitBean>>>

    /**
     * 最近一周的曲线增长图
     */
    @GET("pos/account/weekIncomeCurves")
    fun getPosWeekIncomeCurves(@Query("roleId") roleId: Int, @Query("coinId") coinId: Int): Observable<RequestResult<MutableList<MutableList<String>>>>

//    @POST("/v1/account/initFaceidApp")
//    @FormUrlEncoded
//    fun getAuthInitInfo(
//            @Field("appName") appName: String?,
//            @Field(" appSign") appSign: String?,
//            @Field("bizId") bizId: String?,
//            @Field("packageName") packageName: String?,
//            @Field("identityParam") identityParam: String?,
//            @Field("metaInfo") metaInfo: String?,
//            @Field("platform") platform: String?
//    ): Observable<RequestResult<String>>

    @POST("v1/account/initFaceidApp")
    @FormUrlEncoded
    suspend fun getAuthInitInfo(
            @Field("appName") appName: String?,
            @Field("appSign") appSign: String?,
            @Field("bizId") bizId: String?,
            @Field("packageName") packageName: String?,
            @Field("identityParam") identityParam: String?,
            @Field("metaInfo") metaInfo: String?,
            @Field("platform") platform: String?
    ): RequestResult<AuthInitMsg>

    @POST("v1/account/getFaceidResultApp")
    @FormUrlEncoded
    suspend fun uploadResult(
            @Field("bizId") bizId: String?,
            @Field("certifyId") certifyId: String?
    ): RequestResult<String>
}
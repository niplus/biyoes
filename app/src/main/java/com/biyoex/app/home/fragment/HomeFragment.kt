package com.biyoex.app.home.fragment

import com.biyoex.app.common.mvpbase.BaseFragment
import com.youth.banner.listener.OnBannerListener
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import butterknife.BindView
import com.biyoex.app.R
import android.widget.RelativeLayout
import me.bakumon.library.view.BulletinView
import androidx.viewpager.widget.ViewPager
import com.biyoex.app.common.widget.MyViewPager
import androidx.recyclerview.widget.RecyclerView
import com.biyoex.app.home.bean.BannerBean
import com.biyoex.app.home.adapter.CurrencyTrendAdapter
import com.biyoex.app.home.bean.CoinTradeRankBean.DealDatasBean
import android.widget.LinearLayout
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import com.biyoex.app.common.activity.WebPageLoadingActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.biyoex.app.home.adapter.HomeVpAdapter
import android.view.LayoutInflater
import android.widget.TextView
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.biyoex.app.common.http.RetrofitHelper
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers
import com.biyoex.app.common.mvpbase.BaseObserver
import com.biyoex.app.common.bean.RequestResult
import com.biyoex.app.common.bean.SignToCoin
import com.biyoex.app.home.view.HomeSignDialog
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.biyoex.app.trading.bean.CoinMarketLiveData
import com.biyoex.app.home.bean.CoinTradeRankBean
import com.biyoex.app.common.mvpbase.BasePresent
import com.biyoex.app.VBTApplication
import com.biyoex.app.trading.activity.TrendChartNewActivity
import com.biyoex.app.common.okhttp.OkHttpUtils
import com.biyoex.app.common.okhttp.callback.ResultModelCallback
import com.biyoex.app.common.okhttp.callback.ResponseCallBack
import com.biyoex.app.common.bean.IndexNoticeBean
import q.rorbin.badgeview.QBadgeView
import me.bakumon.library.adapter.BulletinAdapter
import com.biyoex.app.common.base.RxBusData
import com.biyoex.app.common.base.RxBus
import androidx.viewpager.widget.PagerAdapter
import com.biyoex.app.common.okhttp.callback.StringCallback
import kotlin.Throws
import org.json.JSONException
import org.json.JSONObject
import butterknife.OnClick
import com.chad.library.adapter.base.BaseViewHolder
import com.google.android.material.tabs.TabLayout
import com.biyoex.app.common.Constants
import com.biyoex.app.common.data.SessionLiveData
import com.biyoex.app.market.CoinRushBuyActivity
import com.biyoex.app.common.activity.LoginActivity
import com.biyoex.app.common.base.HomeMenuBean
import com.biyoex.app.common.bean.HomeNoticeBean
import com.biyoex.app.common.mvpbase.BaseView
import com.biyoex.app.common.utils.*
import com.biyoex.app.common.utils.androidutilcode.ScreenUtils
import com.biyoex.app.my.RegistInviteActivity
import com.biyoex.app.my.activity.*
import com.youth.banner.Banner
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home_bottom.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.Call
import zendesk.chat.Chat
import zendesk.chat.ChatConfiguration
import zendesk.chat.VisitorInfo
import zendesk.messaging.MessagingActivity
import zendesk.chat.ChatEngine
import java.lang.Exception
import java.math.BigDecimal
import java.util.*

//首页
class HomeFragment : BaseFragment<BasePresent<BaseView>>(), OnBannerListener, SwipeRefreshLayout.OnRefreshListener {
    @JvmField
    @BindView(R.id.swipeRefresh)
    var swipeRefreshLayout: SwipeRefreshLayout? = null

    @JvmField
    @BindView(R.id.vp_home_banner)
    var vpHomeBanner: Banner? = null

    @JvmField
    @BindView(R.id.rl_notice_layout)
    var rlNoticeLayout: RelativeLayout? = null

    @JvmField
    @BindView(R.id.iv_notice)
    var ivNotice: ImageView? = null

    @JvmField
    @BindView(R.id.notice)
    var bulletinView: BulletinView? = null

    @JvmField
    @BindView(R.id.vp_main_coin)
    var vpMainCoin: ViewPager? = null

    @JvmField
    @BindView(R.id.home_tab)
    var mTabLayout: TabLayout? = null

    @JvmField
    @BindView(R.id.home_vp)
    var mBottomVp: MyViewPager? = null

    @JvmField
    @BindView(R.id.rv_menu)
    var rvMenu: RecyclerView? = null
    private var mTabList: ArrayList<Fragment>? = null
    private var paths //banner图片路径
            : MutableList<String?>? = null
    private var bannerBeans: MutableList<BannerBean>? = null
    var currencyTrendAdapter: CurrencyTrendAdapter? = null
    private var mCoinTradeRankList: MutableList<DealDatasBean>? = null
    private var notices //公告
            : MutableList<String>? = null
    private var noticesUrlList: MutableList<String>? = null
    private var mainCoinsData: MutableList<DealDatasBean> = mutableListOf()
    private var mainCoinPage //BTC  ETH  VBT
            : MutableList<LinearLayout> = mutableListOf()
    private var tabtitleList //首页三个tab
            : MutableList<String>? = null

    var listData = mutableListOf<CoinTradeRankBean.DealDatasBean>()
    var listType1 = mutableListOf<CoinTradeRankBean.DealDatasBean>()
    var listType2 = mutableListOf<CoinTradeRankBean.DealDatasBean>()

    var isFirst = true

    private var mType = 1

    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun initComp() {
        swipeRefreshLayout?.setOnRefreshListener(this)
        //设置公告点击事件
        bulletinView?.setOnBulletinItemClickListener { position: Int ->
            if (notices?.size == 0) {
                return@setOnBulletinItemClickListener
            }
            val intent = Intent(context, WebPageLoadingActivity::class.java)
            intent.putExtra("url", noticesUrlList!![position])
            intent.putExtra("type", "url")
            intent.putExtra("title", notices!![position])
            intent.putExtra("isShare", true)
            context!!.startActivity(intent)
        }

        //
        vpMainCoin?.adapter = MainCoinAdapter()
        vpMainCoin?.offscreenPageLimit = 3
        //banner点击事件
        vpHomeBanner?.setOnBannerListener { position: Int ->
            val nWebLoadingActivity = Intent(activity, WebPageLoadingActivity::class.java)
            nWebLoadingActivity.putExtra("url", bannerBeans!![position].url)
            nWebLoadingActivity.putExtra("type", "url")
            nWebLoadingActivity.putExtra("title", "详情")
            startActivity(nWebLoadingActivity)
        }
        val menuList = mutableListOf(
                HomeMenuBean(R.mipmap.home_gift, "邀请好友", "更多奖励随时领"),
                HomeMenuBean(R.mipmap.home_kefu, "联系客服", "更多问题咨询"),
                HomeMenuBean(R.mipmap.home_charge, "极速充值", "极速闪电充值"),
                HomeMenuBean(R.mipmap.home_withdraw, "极速提现", "极速闪电提现")
        )
        rvMenu?.layoutManager = GridLayoutManager(requireContext(), 2)
        rvMenu?.adapter = object : BaseQuickAdapter<HomeMenuBean, BaseViewHolder>(R.layout.layout_home_pos, menuList) {
            protected override fun convert(helper: BaseViewHolder, item: HomeMenuBean) {
                helper.setImageResource(R.id.iv_menu, item.icon)
                helper.setText(R.id.tv_title, item.title)
                helper.setText(R.id.tv_hint, item.hint)
                helper.itemView.setOnClickListener {
                    when(item.title){
                        "邀请好友" -> {
                            val sessionInfo = SessionLiveData.getIns().value
                            if (sessionInfo == null){
                                val itLogin = Intent(activity, LoginActivity::class.java)
                                itLogin.putExtra("type", "info")
                                startActivity(itLogin)
                            }else{
                                startActivity(Intent(requireContext(), RegistInviteActivity::class.java))
                            }

                        }
                        "联系客服" -> startActivity(Intent(activity, ContactActivity::class.java))
                        "极速充值" -> {
                            isLogin(1)
//                            val itCurrencyReCharge = Intent(requireContext(), CurrencyReChargeActivity::class.java)
//                            itCurrencyReCharge.putExtra("currency_name", rechargeCoinBean!!.allName)
//                            itCurrencyReCharge.putExtra("symbol", rechargeCoinBean!!.id)
//                            itCurrencyReCharge.putExtra("shortName", rechargeCoinBean!!.name)
//                            itCurrencyReCharge.putExtra("img_url", rechargeCoinBean!!.url)
//                            startActivity(itCurrencyReCharge)
                        }
                        "极速提现"->{
                            isLogin(0)
                        }
                    }
                }
            }

            override fun getItemCount(): Int {
                return 4
            }
        }
        rvMenu?.addItemDecoration(object : RecyclerView.ItemDecoration(){
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                val position = parent.getChildAdapterPosition(view)
                when(position){
                    0-> outRect.right = 34
                    2-> {
                        outRect.top = 28
                        outRect.right = 34
                    }
                    3-> outRect.top = 28

                }
            }
        })

        //底册三个fragment
//        mTabList?.add(HomeBottomFragment(0, mBottomVp!!))
        mTabList?.add(HomeBottomFragment(1, mBottomVp!!))
        mTabList?.add(HomeBottomFragment(2, mBottomVp!!))
//        tabtitleList!!.add(getString(R.string.week_star))
        tabtitleList!!.add(getString(R.string.up_rank))
        tabtitleList!!.add(getString(R.string.down_rank))
        mBottomVp!!.offscreenPageLimit = 2
        //        CommonUtil.setTabWidth(mTabLayout);
        mTabLayout!!.setupWithViewPager(mBottomVp)
        mBottomVp!!.adapter = HomeVpAdapter(childFragmentManager!!, mTabList!!, tabtitleList!!)
        //切换底部tab选中view
        for (i in tabtitleList!!.indices) {
            val tab = LayoutInflater.from(activity).inflate(R.layout.item_home_tab, null)
            val textView = tab.findViewById<View>(R.id.tv_tab_name) as TextView
            textView.text = tabtitleList!![i]
            if (i == 0) {
                textView.typeface = Typeface.DEFAULT_BOLD
            }
            mTabLayout!!.getTabAt(i)!!.customView = tab
        }

        //修改选中tab字体变粗
        mTabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                updateTabView(tab, true)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                updateTabView(tab, false)
            }

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })


        //为了适配viewpager动态修改高度  不然viewpager高度=0
        mBottomVp!!.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
//                mIndex = position;
                mBottomVp!!.resetHeight(position)
                mType = position+1
                listData.clear()
                if (mType == 1) {
                    listData.addAll(listType1)
                }else{
                    listData.addAll(listType2)
                }
                currencyTrendAdapter!!.setNewData(listData)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })


        rv_bottom.isNestedScrollingEnabled = false;
        currencyTrendAdapter = CurrencyTrendAdapter(R.layout.item_home_trend, listData)
        currencyTrendAdapter!!.setFupanddownState(1)
        rv_bottom.layoutManager = LinearLayoutManager(context)
        rv_bottom.adapter = currencyTrendAdapter
        currencyTrendAdapter!!.emptyView = View.inflate(context, R.layout.view_empty, null)
    }

    //请求签到奖励数据
    private val signToCoin: Unit
        private get() {
            showLoadingDialog()
            RetrofitHelper.getIns().zgtopApi
                    .getSignToCoin()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : BaseObserver<RequestResult<MutableList<SignToCoin>>>() {

                        override fun success(listRequestResult: RequestResult<MutableList<SignToCoin>>) {
                            hideLoadingDialog()
                            HomeSignDialog(context!!, listRequestResult.data).show()
                        }
                    })
        }

    override fun initData() {
        paths = ArrayList()
        bannerBeans = ArrayList()
        mCoinTradeRankList = ArrayList()
        notices = ArrayList()
        noticesUrlList = ArrayList()
        mainCoinsData = ArrayList()
        mainCoinPage = ArrayList()
        mTabList = ArrayList()
        tabtitleList = ArrayList()
        requestBannerImage()
        ////        //重新设置banner高度，防止图片变形，图片像素为750 * 350
        val params = vpHomeBanner?.layoutParams
        params?.height = ((ScreenUtils.getScreenWidth() - ScreenUtils.dp2px(40f)) / 1.89).toInt()
        vpHomeBanner?.layoutParams = params
        showNotice()
        /**
         * 定时器，循环一分钟请求首页涨幅榜
         */
        CoinMarketLiveData.getIns().observe(this, androidx.lifecycle.Observer{ coinTradeRankBean: CoinTradeRankBean ->
            if (coinTradeRankBean != null) {
                val stringListMap = coinTradeRankBean.dataMap
                val usdt = stringListMap["USDT"]
                val innovation1 = stringListMap["POC+"]
                val coinData: MutableList<DealDatasBean> = ArrayList()
                if (usdt != null) {
                    coinData.addAll(usdt)
                }
                if (innovation1 != null) {
                    coinData.addAll(innovation1)
                }
                val allCoinList: MutableList<DealDatasBean> = ArrayList()
                val keySet: Set<String> = stringListMap.keys
                for (key in keySet) {
                    if (key == "POC+") {
                        val innovation = stringListMap["POC+"]!!
                        Log.i("是否有创新区", "initData: " + innovation.size)
                        for (i in innovation.indices) {
                            innovation[i].isNew = 1
                        }
                        allCoinList.addAll(innovation)
                    } else {
                        allCoinList.addAll(stringListMap[key]!!)
                    }
                }
                requestCoinTradeRank(allCoinList)
                mainCoinsData.clear()
                for (datasBean in allCoinList) {
                    if (datasBean.fShortName == "BTC") {
                        mainCoinsData.add(datasBean)
                    } else if (datasBean.fShortName == "ETH") {
                        mainCoinsData.add(datasBean)
                    }
                }
                for (datasBean in allCoinList) {
                    if (datasBean.fShortName == "VBT") {
                        mainCoinsData.add(datasBean)
                    }
                }
                if (mainCoinPage.size == 0) {
                    return@Observer
                }

                // TODO: 2019/4/11 暂时只有第一页有数据
                //刷新数据
                val linearLayout = mainCoinPage.get(0)
                for (i in mainCoinsData.indices) {
                    val child = linearLayout.getChildAt(i) as? LinearLayout ?: continue
                    //底下子view可能不是linearlayout
                    if (mainCoinsData.get(i) != null) {
                        setMainCoinData(mainCoinsData.get(i), child)
                    }
                }
            }
        })
    }

    override fun createPresent(): BasePresent<BaseView>? {
        return null
    }

    //首页涨幅榜
    fun setMainCoinData(datasBean: DealDatasBean?, child: LinearLayout) {
        val tvCoinName = child.getChildAt(0) as TextView
        val tvCoinPrice = child.getChildAt(1) as TextView
        val tvTrend = child.getChildAt(2) as TextView
        val tvRmbPrice = child.getChildAt(3) as TextView
        tvCoinName.text = datasBean!!.getfShortName() + "/" + datasBean.group
        val ratio = datasBean.fupanddown
        val ratioDecimal = MoneyUtils.decimal2ByUp(BigDecimal(ratio))
        if ((ratio.toString() + "").toDouble() >= 0) {
            tvTrend.setTextColor(VBTApplication.RISE_COLOR)
            tvTrend.text = "+$ratioDecimal%"
            tvCoinPrice.setTextColor(VBTApplication.RISE_COLOR)
        } else {
            tvTrend.setTextColor(VBTApplication.FALL_COLOR)
            tvTrend.text = "$ratioDecimal%"
            tvCoinPrice.setTextColor(VBTApplication.FALL_COLOR)
        }
        val actualPrice = MoneyUtils.decimalByUp(datasBean.priceDecimals, BigDecimal(datasBean.lastDealPrize))
        tvCoinPrice.text = actualPrice.toPlainString()
        tvRmbPrice.text = "≈" + MoneyUtils.decimalByUp(if (LanguageUtils.currentLanguage == 1) 2 else 4, BigDecimal(MoneyUtils.mul(actualPrice.toDouble(), java.lang.Double.valueOf(datasBean.rate)))) + getString(R.string.money_unit)
        child.setOnClickListener { v: View? ->
            Constants.coinName = datasBean.fShortName
            //            Constants.coinGroup = datasBean.getIsNew() == 1 ? "POC+" : datasBean.getGroup();
            startActivity(Intent(context, TrendChartNewActivity::class.java).putExtra("coin", datasBean))
        }
    }

    private fun isLogin(i: Int) {
        val sessionInfo = SessionLiveData.getIns().value
        if (sessionInfo != null) {
            if (sessionInfo.isAuth) {
                val itCurrencyRecharge = Intent(activity, CurrencyListActivity::class.java)
                itCurrencyRecharge.putExtra("type", if (i == 1) getString(R.string.recharge) else getString(R.string.withdraw))
                startActivity(itCurrencyRecharge)
            } else {
                val itCertificatesInfo = Intent(activity, CertificatesInfoActivity::class.java)
                startActivity(itCertificatesInfo)
            }
        } else {
            val itLogin = Intent(activity, LoginActivity::class.java)
            itLogin.putExtra("type", "info")
            startActivity(itLogin)
        }
    }

    /**
     * 用来改变tabLayout选中后的字体大小及颜色
     * @param tab
     * @param isSelect
     */
    private fun updateTabView(tab: TabLayout.Tab, isSelect: Boolean) {
        //找到自定义视图的控件ID
        val tv_tab = tab.customView!!.findViewById<TextView>(R.id.tv_tab_name)
        if (isSelect) {
            //设置标签选中
            tv_tab.isSelected = true
            //选中后字体变大
            tv_tab.typeface = Typeface.DEFAULT_BOLD
        } else {
            //设置标签取消选中
            tv_tab.isSelected = false
            //恢复为默认字体大小
            tv_tab.typeface = Typeface.DEFAULT
        }
    }

    //首页公告
    private fun showNotice() {
        var noticeUrl = Constants.BASE_URL + "v1/articleList?page=1&pageSize=5&type=5"
//        if (LanguageUtils.currentLanguage != 1) {
//            noticeUrl = "https://vbt.zendesk.com/api/v2/help_center/en-001/categories/360001882394/articles.json?per_page=3&sort_by=created_at&sort_order=desc"
//        }
        Log.i("nidongliang", "noticeuri : $noticeUrl")
        OkHttpUtils
                .get()
                .url(noticeUrl)
                .addHeader("X-Requested-With", "XMLHttpReques")
                .addParams("type", "1")
                .addParams("page", "1")
                .addParams("pageSize", "10")
                .build()
                .execute(ResultModelCallback<Any?>(activity, object : ResponseCallBack<HomeNoticeBean> {
                    override fun onError(e: String) {}
                    override fun onResponse(response: HomeNoticeBean) {
                        val qBadge = QBadgeView(activity)
                                .bindTarget(ivNotice)
                        qBadge.setGravityOffset(0f, 0f, true)
                        qBadge.setBadgePadding(4f, true)
                        qBadge.badgeBackgroundColor = Color.parseColor("#e0514b")
                        qBadge.badgeNumber = 0
                        notices!!.clear()
                        response.data.forEach {
                            notices!!.add(it.title)
                            noticesUrlList!!.add(Constants.REALM_NAME + "/mobile/#/detail?type=${it.type}&id=${it.id}")
                        }
//                        for ( (_, _, title, _, _, _, _, _) in response.data) {
//                            notices!!.add(title)
////                            noticesUrlList!!.add(html_url)
//                        }
                        bulletinView!!.setAdapter(object : BulletinAdapter<String>(activity, notices) {
                            override fun getView(position: Int): View {
                                val rootView = getRootView(R.layout.item_notice_list)
                                val tvNotice = rootView.findViewById<TextView>(R.id.tv_notice)
                                tvNotice.text = mData[position] as String?
                                return rootView
                            }
                        })
                    }
                }))
    }

    //    把数据传给底部fragment
    fun requestCoinTradeRank(dealDatasBeans: List<DealDatasBean>?) {
//        mBottomVp.resetHeight(mIndex);

        if (mCoinTradeRankList != null) {
            mCoinTradeRankList!!.clear()
            val rxBusData = RxBusData()
            rxBusData.msgData = dealDatasBeans
            rxBusData.msgName = "HomeBottom"
            RxBus.get().post(rxBusData)

            var temp = dealDatasBeans?.sortedByDescending { t1 -> t1.fupanddown }
            temp = temp?.filter { ti -> ti.fupanddown >= 0 }
            listType1.clear()
            if (!temp.isNullOrEmpty()){
                if (temp.size > 10){
                    temp.subList(0, 10)
                }
                listType1.addAll(temp)
            }

            if (isFirst){
                listData.clear()
                if (!temp.isNullOrEmpty())
                    listData.addAll(temp)
                isFirst = false
            }

             temp = dealDatasBeans?.sortedBy { t1 -> t1.fupanddown }
            temp = temp?.filter { ti -> ti.fupanddown < 0 }
            listType2.clear()
            if (!temp.isNullOrEmpty()) {
                if (temp.size > 10){
                    temp.subList(0, 10)
                }
                listType2.addAll(temp)
            }


            currencyTrendAdapter!!.setNewData(listData)
//            lifecycleScope.launch {
//                delay(1000)
//                mBottomVp!!.resetHeight(0)
//            }
            }
        }

    private inner class MainCoinAdapter : PagerAdapter() {
        override fun getCount(): Int {
            return 1
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val rootView = LayoutInflater.from(activity).inflate(R.layout.item_home_main_coin, null)
            val llMainCoin = rootView.findViewById<LinearLayout>(R.id.ll_main_coin)
            mainCoinPage.add(position, llMainCoin)
            container.addView(rootView)
            return rootView
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }
    }

    //设置banner图片
    fun requestBannerImage() {
        OkHttpUtils
                .get()
                .addHeader("X-Requested-With", "XMLHttpReques")
                .url("https://biyoex.com/api/v1/image")
                .build()
                .execute(object : StringCallback() {
                    override fun onError(call: Call, e: Exception, id: Int) {
                        if (swipeRefreshLayout != null && swipeRefreshLayout!!.isRefreshing) {
                            swipeRefreshLayout!!.isRefreshing = false
                        }
                        ToastUtils.showToast(R.string.load_failed)
                    }

                    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
                    @Throws(JSONException::class)
                    override fun onResponse(response: String, id: Int) {
                        val jsonObject = JSONObject(response)
                        //使用treeMap进行排序
                        val bannerMap = TreeMap<String, BannerBean>()
                        val code = jsonObject.getInt("code")
                        if (200 == code) {
                            val data = jsonObject.getJSONObject("data")
                            Log.i("nidongliang", "data = $data")
                            val appBanner = data.getString("appBanner")
                            val bannerBeans = GsonUtil.jsonToList(appBanner, BannerBean::class.java)
                            for (bannerBean in bannerBeans) {
                                val key = bannerBean.key
                                if (key.contains("image_index_app")) {
                                    bannerBean.url =  bannerBean.url.replace("/#/announdetail", "/mobile/#/detail")
                                    bannerMap[key] = bannerBean
                                }
                            }
                            //倒序
//                            val descendMap: Map<String, BannerBean> = bannerMap.descendingMap()
                            val urlIterator = bannerMap.entries.iterator()
                            paths!!.clear()
                            this@HomeFragment.bannerBeans!!.clear()
                            //                            List<ImageData> imageData = new ArrayList<>();
                            while (urlIterator.hasNext()) {
                                val entry = urlIterator.next()
                                val bannerBean = entry.value
                                this@HomeFragment.bannerBeans!!.add(bannerBean)
                                paths!!.add(VBTApplication.appPictureUrl + bannerBean.value)
                                //                                ImageData b1 = new ImageData();
//                                b1.setImage(VBTApplication.appPictureUrl + bannerBean.getValue());
//                                b1.setMainTitle("");
//                                imageData.add(b1);
                            }
                            if (paths != null && vpHomeBanner != null) {
                                vpHomeBanner!!.setImages(paths)
                                        .setImageLoader(BannerImageLoader())
                                        .setDelayTime(4000)
                                        .start()
                            }
                            // TODO: 2019/4/26 加载banner
                            if (swipeRefreshLayout != null && swipeRefreshLayout!!.isRefreshing) {
                                swipeRefreshLayout!!.isRefreshing = false
                            }
                        }
                    }
                })
    }

    override fun OnBannerClick(position: Int) {
        if (bannerBeans!![position].url.contains("=")) {
            val itWebPageLoading = Intent(activity, WebPageLoadingActivity::class.java)
            itWebPageLoading.putExtra("url", bannerBeans!![position].url.split("=").toTypedArray()[1])
            itWebPageLoading.putExtra("type", "html")
            itWebPageLoading.putExtra("title", getString(R.string.detail))
            activity!!.startActivity(itWebPageLoading)
        }
    }

    @OnClick(R.id.iv_vipbuy, R.id.layout_rush)
    fun onClick(view: View) {
       if (SessionLiveData.getIns().value != null) {
            val jumpIntent = Intent()
            when (view.id) {
                R.id.layout_rush -> {
                    jumpIntent.setClass(requireContext(), CoinRushBuyActivity::class.java)
                    startActivity(jumpIntent)
                }
                R.id.iv_vipbuy -> {
                    jumpIntent.setClass(requireContext(), VipActivity::class.java)
                    startActivity(jumpIntent)
                }
            }
        } else {
            startActivity(Intent(activity, LoginActivity::class.java))
        }
    }

    private fun openPersonHelp() {
        Chat.INSTANCE.init(requireContext(), Constants.ZenkenAccountKey)
        val chatConfiguration = ChatConfiguration.builder()
                .withAgentAvailabilityEnabled(false)
                .withPreChatFormEnabled(true)
                .build()
        val profileProvider = Chat.INSTANCE.providers()!!.profileProvider()
        val chatProvider = Chat.INSTANCE.providers()!!.chatProvider()
        val visitorInfo = VisitorInfo.builder()
                .build()
        profileProvider.setVisitorInfo(visitorInfo, null)
        chatProvider.setDepartment("Department name", null)
        MessagingActivity.builder()
                .withEngines(ChatEngine.engine())
                .show(context!!, chatConfiguration)
    }

    override fun onRefresh() {
        requestBannerImage()
        showNotice()
    }
}
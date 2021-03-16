package com.biyoex.app.my.fragment

import com.biyoex.app.common.mvpbase.BaseFragment
import butterknife.BindView
import com.biyoex.app.R
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.biyoex.app.my.bean.ZGSessionInfoBean
import com.biyoex.app.common.utils.SharedPreferencesUtils
import androidx.recyclerview.widget.GridLayoutManager
import com.biyoex.app.common.utils.SpacesItemDecoration
import com.zhy.adapter.recyclerview.CommonAdapter
import android.content.Intent
import android.view.View
import androidx.lifecycle.Observer
import com.biyoex.app.common.activity.WebPageLoadingActivity
import com.biyoex.app.common.activity.LoginActivity
import com.biyoex.app.common.data.SessionLiveData
import zendesk.chat.Chat
import zendesk.chat.ChatConfiguration
import zendesk.chat.VisitorInfo
import zendesk.messaging.MessagingActivity
import zendesk.chat.ChatEngine
import com.biyoex.app.common.mvpbase.BasePresent
import com.biyoex.app.common.okhttp.OkHttpUtils
import com.biyoex.app.common.okhttp.callback.StringCallback
import butterknife.OnClick
import com.biyoex.app.common.Constants
import com.biyoex.app.common.mvpbase.BaseView
import com.biyoex.app.my.RegistInviteActivity
import com.biyoex.app.my.activity.*
import com.zhy.adapter.recyclerview.base.ViewHolder
import okhttp3.Call
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception

//import com.tencent.bugly.crashreport.CrashReport;
/**
 * 个人资产
 * Created by LG on 2017/5/17.
 */
class MyFragment : BaseFragment<BasePresent<BaseView>>() {
    //    @BindView(R.id.iv_user_logo)
    //    ImageView ivUserLogo;
    @JvmField
    @BindView(R.id.tv_number)
    var tvNumber: TextView? = null

    @JvmField
    @BindView(R.id.rv_function)
    var rvFunction: RecyclerView? = null

    @JvmField
    @BindView(R.id.tv_user_uid)
    var tvUserUid: TextView? = null

    //    @BindView(R.id.tv_night)
    //    TextView tvNight;
    private var mFunctionNmaeList = mutableListOf<String?>()
    private var mFunctionLogoList = mutableListOf<Int>()
    private var sessionInfo: ZGSessionInfoBean? = null

    //动态修改状态栏背景颜色
    override fun initImmersionBar() {
//        immersionBar!!.statusBarDarkFont(false).init()
    }

    override fun initComp() {
        //
//        tvNight.setOnClickListener(view -> {
//            String color = SharedPreferencesUtils.getInstance().getString("color", "0");//判断是夜间还是白天 0=白天  1=  夜间
//            if(color.equals("1")){
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//                SharedPreferencesUtils.getInstance().saveString("color","0");
//            }
//            else {
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//                SharedPreferencesUtils.getInstance().saveString("color","1");
//            }
//            openActivity(StartActivity.class);
//        });
    }

    override fun initData() {
        //        mFunctionNmaeList.add(getString(R.string.my_vip));
        mFunctionNmaeList.add("安全中心")
        mFunctionNmaeList.add("我的委托")
        mFunctionNmaeList.add(getString(R.string.help_center))
        mFunctionNmaeList.add("联系客服")
        mFunctionNmaeList.add(getString(R.string.setting))
        //        mFunctionLogoList.add(R.mipmap.ruoguan_btn);
//        mFunctionLogoList.add(R.mipmap.icon_vip);
        mFunctionLogoList.add(R.mipmap.icon_safe)
        mFunctionLogoList.add(R.mipmap.icon_myorder)
        mFunctionLogoList.add(R.mipmap.icon_help)
        mFunctionLogoList.add(R.mipmap.icon_my_online_server)
        mFunctionLogoList.add(R.mipmap.icon_setting)
        val color = SharedPreferencesUtils.getInstance().getString("color", "0") == "0"
        //        tvNight.setText(color?R.string.night:R.string.whileday);
        val functionLayoutManager = GridLayoutManager(activity, 1)
        rvFunction!!.addItemDecoration(SpacesItemDecoration(3, 1, 0x1642))
        rvFunction!!.layoutManager = functionLayoutManager
        val mFunctionCommonAdapter: CommonAdapter<*> = object : CommonAdapter<String?>(context, R.layout.item_function_modular, mFunctionNmaeList) {
            override fun convert(holder: ViewHolder, s: String?, position: Int) {
                holder.setText(R.id.tv_fuicon_name, mFunctionNmaeList.get(position))
                if (mFunctionLogoList.get(position) != 0) {
                    holder.setImageResource(R.id.iv_fuicon_icon, mFunctionLogoList.get(position))
                }
                holder.setOnClickListener(R.id.rl_layout_modular) { v: View? ->
                    if (sessionInfo != null) {
                        when (mFunctionNmaeList[position]) {
                            "安全中心" -> {
                                val itRealNameCertification = Intent(activity, RealNameCertificationActivity::class.java)
                                startActivity(itRealNameCertification)
                            }
                            "我的委托" -> {
                                val itEntrustSupervise = Intent(activity, NewsEntrustedManageActivity::class.java)
                                startActivity(itEntrustSupervise)
                            }
                            getString(R.string.help_center) -> {
                                val helpIntent = Intent(context, WebPageLoadingActivity::class.java)
                                helpIntent.putExtra("url", Constants.REALM_NAME + "/mobile/#/index")
                                helpIntent.putExtra("type", "url")
                                helpIntent.putExtra("title", getString(R.string.help_center))
                                startActivity(helpIntent)
                            }
                            "联系客服" -> {
//                                openPersonHelp()
                                startActivity(Intent(activity, ContactActivity::class.java))
                            }
                            getString(R.string.setting) -> {
                                val itSeting = Intent(activity, SetingActivity::class.java)
                                startActivity(itSeting)
                            }
                        }
                    } else {
                        when (mFunctionNmaeList[position]) {
                            getString(R.string.setting) -> {
                                val Seting = Intent(activity, SetingActivity::class.java)
                                startActivity(Seting)
                            }
                            else ->{
                                val itSeting = Intent(activity, LoginActivity::class.java)
                                startActivity(itSeting)
                            }
                        }
                    }
                }
            }
        }
        rvFunction!!.adapter = mFunctionCommonAdapter
        SessionLiveData.getIns().observe(this, Observer{ userinfoBean: ZGSessionInfoBean? ->
            if (userinfoBean == null) {
                sessionInfo = null
                if (tvNumber != null) {
                    tvNumber!!.setText(R.string.regist_login)
                }
                if (tvUserUid != null) {
                    tvUserUid!!.setText(R.string.welcome_app)
                }
            } else {
                sessionInfo = userinfoBean
                //
                tvUserUid!!.text = "UID：" + sessionInfo!!.id
                tvNumber!!.text = starNum(userinfoBean.loginName)
            }
        })
    }

    private fun openPersonHelp() {
        Chat.INSTANCE.init(context!!, Constants.ZenkenAccountKey)
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

    override fun createPresent(): BasePresent<BaseView>? {
        return null
    }

    private fun starNum(num: String): String {
        return if (num.contains("@")) {
            num.replace("(\\w?)(\\w+)(\\w)(@\\w+\\.[a-z]+(\\.[a-z]+)?)".toRegex(), "$1****$3$4")
        } else {
            num.replace("(\\d{3})\\d{4}(\\d{4})".toRegex(), "$1****$2")
        }
    }

    fun refreshUnreadinformation() {
        OkHttpUtils
                .get()
                .addHeader("X-Requested-With", "XMLHttpRequest")
                .url(Constants.BASE_URL + "v1/account/unreadInformation")
                .build()
                .execute(object : StringCallback() {
                    override fun onError(call: Call, e: Exception, id: Int) {}

                    @kotlin.jvm.Throws(JSONException::class)
                    override fun onResponse(response: String, id: Int) {
                        val jsonObject = JSONObject(response)
                        val code = jsonObject.getInt("code")
                        val data = jsonObject.getInt("data")
                        try {
                            if (200 == code) {
//                                qBadge.setBadgeNumber(data);
                            } else {
//                                qBadge.setBadgeNumber(0);
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            //                            qBadge.setBadgeNumber(0);
                        }
                    }
                })
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_new_my
    }

    @OnClick(R.id.iv_header, R.id.card_mine)
    fun onClick(view: View) {
        if (sessionInfo == null) {
            val itLogin = Intent(activity, LoginActivity::class.java)
            itLogin.putExtra("type", "info")
            startActivity(itLogin)
        } else {
            when (view.id) {
                R.id.card_mine, R.id.iv_header -> {
                    val itSeting1 = Intent(activity, SetingActivity::class.java)
                    startActivity(itSeting1)
                    activity!!.overridePendingTransition(R.anim.activity_translate_in, 0)
                }
                R.id.tv_cardid -> {
                    //                    if (sessionInfo.isAuth()) {
//                        ToastUtils.showToast(getString(R.string.certified));
//                    } else {
                    val itCertificatesInfo2 = Intent(activity, CertificatesInfoActivity::class.java)
                    startActivity(itCertificatesInfo2)
                }
                R.id.tv_security -> {
                    val itRealNameCertification = Intent(activity, RealNameCertificationActivity::class.java)
                    startActivity(itRealNameCertification)
                }
                R.id.tv_share_retrun -> //                    openActivity(InviteReturnDetailsActivity.class);
                    openActivity(RegistInviteActivity::class.java)
                R.id.iv_header_share -> {
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (SessionLiveData.getIns().value != null) {
            refreshUnreadinformation()
        }
    }

    companion object {
        const val INT = 2
    }
}
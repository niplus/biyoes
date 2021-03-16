package com.biyoex.app.my.activity

import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import butterknife.BindView
import butterknife.OnClick
import com.uber.autodispose.AutoDispose
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.biyoex.app.R
import com.biyoex.app.common.base.BaseActivity
import com.biyoex.app.common.bean.CoinNameListBean
import com.biyoex.app.common.bean.RequestResult
import com.biyoex.app.common.http.RetrofitHelper
import com.biyoex.app.common.mvpbase.BaseObserver
import com.biyoex.app.common.widget.LetterNavigation
import com.biyoex.app.my.adapter.CurrencyRechargeAdapter
import com.biyoex.app.my.bean.RechargeCoinBean
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

/**
 * 币种列表
 * Created by LG on 2017/6/7.
 */
class CurrencyListActivity : BaseActivity(), SwipeRefreshLayout.OnRefreshListener {
    @JvmField
    @BindView(R.id.tv_title)
    var tvTitle: TextView? = null

    @JvmField
    @BindView(R.id.p_recyclerview)
    var pRecyclerview: RecyclerView? = null

    @JvmField
    @BindView(R.id.sr_SwipeRefreshLayout)
    var srSwipeRefreshLayout: SwipeRefreshLayout? = null

    @JvmField
    @BindView(R.id.letterNavigation)
    var letterNavigation: LetterNavigation? = null

    @JvmField
    @BindView(R.id.ed_search)
    var mEdittext: EditText? = null
    private var mCurrencyRechargeList: MutableList<RechargeCoinBean>? = null
    private var mSearchList: MutableList<RechargeCoinBean>? = null

    //    private List<RechargeCoinBean> temp;
    private var mCurrencyRechargeAdapter: CurrencyRechargeAdapter? = null
    private var linearLayoutManager: LinearLayoutManager? = null
    override fun initView() {
        tvTitle!!.setText(R.string.coin_list)
        letterNavigation!!.setOnLetterChangeListener { letter: String ->
            if (mCurrencyRechargeList!!.size != 0) {
                for (i in mCurrencyRechargeList!!.indices) {
                    val firstLetter = mCurrencyRechargeList!![i].name[0].toString() + ""
                    if (firstLetter.toUpperCase() == letter) {
                        linearLayoutManager!!.scrollToPositionWithOffset(i, 0)
                        //                            linearLayoutManager.setStackFromEnd(true);
                        break
                    }
                }
            }
        }

//        pRecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//                if (mCurrencyRechargeList.size() != 0) {
//                    String name = mCurrencyRechargeList.get(linearLayoutManager.findFirstVisibleItemPosition()).getName();
//                    String firstLetter = name.charAt(0) + "";
//
//                    letterNavigation.setLetter(firstLetter);
//                }
//            }
//        });
        mEdittext!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (!mEdittext!!.text.toString().isEmpty()) {
                    mSearchList!!.clear()
                    for (i in mCurrencyRechargeList!!.indices) {
                        if (mCurrencyRechargeList!![i].name.toUpperCase().contains(mEdittext!!.text.toString().toUpperCase())) {
                            mSearchList!!.add(mCurrencyRechargeList!![i])
                        }
                    }
                    mCurrencyRechargeAdapter!!.setNewData(mSearchList)
                } else {
                    mCurrencyRechargeAdapter!!.setNewData(mCurrencyRechargeList)
                }
            }
        })
    }

    override fun initData() {
        val itCurrency = intent
        mCurrencyRechargeList = ArrayList()
        mSearchList = ArrayList()
        //        temp = new ArrayList<>();
//        tvTitle.setText("虚拟币"+itCurrency.getStringExtra("type")+"列表");
        linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager!!.orientation = LinearLayoutManager.VERTICAL
        pRecyclerview!!.layoutManager = linearLayoutManager
        mCurrencyRechargeAdapter = CurrencyRechargeAdapter(this, R.layout.item_currency, mCurrencyRechargeList, itCurrency.getStringExtra("type"))
        pRecyclerview!!.adapter = mCurrencyRechargeAdapter
        srSwipeRefreshLayout!!.setOnRefreshListener(this)
        srSwipeRefreshLayout!!.setColorSchemeColors(Color.rgb(44, 183, 227))
        showLoadingDialog()
        requestRechargeCoin()
    }

    override fun getLayoutId(): Int {
        return R.layout.public_search_title_recylerview
    }

    @OnClick(R.id.iv_menu)
    override fun onBackPressed() {
        super.onBackPressed()
    }

    fun requestRechargeCoin() {
        RetrofitHelper.getIns()
                .zgtopApi
                .getCoinList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .`as`(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this, Lifecycle.Event.ON_DESTROY)))
                .subscribe(object : BaseObserver<RequestResult<CoinNameListBean>>() {
                    protected override fun success(listRequestResult: RequestResult<CoinNameListBean>) {
                        hideLoadingDialog()
                        //                        if (mCurrencyRechargeList.size() > 0) {
                        mCurrencyRechargeList!!.clear()
                        //                        }
                        srSwipeRefreshLayout!!.isRefreshing = false
                        srSwipeRefreshLayout!!.isEnabled = true
                        //                        List<RechargeCoinBean> rechargeCoinBeen = GsonUtil.jsonToList(listRequestResult., RechargeCoinBean.class);
                        mCurrencyRechargeList!!.addAll(listRequestResult.data.balanceList)
                        Collections.sort(mCurrencyRechargeList) { o1: RechargeCoinBean, o2: RechargeCoinBean -> o1.name.compareTo(o2.name) }

//                            mCurrencyRechargeList.addAll(temp);
                        mCurrencyRechargeAdapter!!.notifyDataSetChanged()
                    }

                    override fun failed(code: Int, data: String?, msg: String?) {
                        super.failed(code, data, msg)
                        hideLoadingDialog()
                        //ToastUtils.showToast(e.toString());
                        srSwipeRefreshLayout!!.isRefreshing = false
                        srSwipeRefreshLayout!!.isEnabled = true
                    }
                })
        //
    }

    override fun onRefresh() {
        srSwipeRefreshLayout!!.isEnabled = false
        requestRechargeCoin()
    }
}
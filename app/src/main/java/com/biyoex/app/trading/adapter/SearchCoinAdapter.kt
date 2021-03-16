package com.biyoex.app.trading.adapter

import androidx.lifecycle.Lifecycle
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.kotlin.autoDisposable
import com.biyoex.app.R
import com.biyoex.app.common.activity.LoginActivity
import com.biyoex.app.common.bean.RequestResult
import com.biyoex.app.common.http.RetrofitHelper
import com.biyoex.app.common.mvpbase.BaseObserver
import com.biyoex.app.common.utils.SharedPreferencesUtils
import com.biyoex.app.common.utils.ToastUtils
import com.biyoex.app.home.bean.CoinTradeRankBean
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SearchCoinAdapter : BaseQuickAdapter<CoinTradeRankBean.DealDatasBean, BaseViewHolder>(R.layout.item_search_coin) {
    override fun convert(helper: BaseViewHolder?, item: CoinTradeRankBean.DealDatasBean?) {
        helper!!.setText(R.id.item_search_coin_name, item!!.fShortName)

        val coins = SharedPreferencesUtils.getInstance().getString("coin_id", "")
        if (coins.contains("" + item.fid)) {
            helper.setImageDrawable(R.id.item_search_coin_iv, mContext.resources.getDrawable(R.mipmap.icon_home_select))
        } else {
            helper.setImageDrawable(R.id.item_search_coin_iv, mContext.resources.getDrawable(R.mipmap.icon_home_normal))
        }
        helper.getView<View>(R.id.item_search_coin_iv).setOnClickListener { v -> addSelectCoin(item.fid, coins.contains("" + item.fid)) }
    }

    private fun addSelectCoin(fid: Int, type: Boolean) {
        var coinId = SharedPreferencesUtils.getInstance().getString("coin_id", "")
        if (type) {
            coinId = coinId.replace(",$fid,", ",")
            SharedPreferencesUtils.getInstance().saveString("coin_id", coinId)
        } else {
            if (coinId == "") {
                SharedPreferencesUtils.getInstance().saveString("coin_id", ",$fid,")
            } else {
                SharedPreferencesUtils.getInstance().saveString("coin_id", "$coinId$fid,")
            }
        }
        val SelectCoinId = SharedPreferencesUtils.getInstance().getString("coin_id", "")
        RetrofitHelper.getIns().zgtopApi.updateUserSelfToken(SelectCoinId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .autoDisposable(AndroidLifecycleScopeProvider.from(mContext as AppCompatActivity, Lifecycle.Event.ON_DESTROY))//OnDestory时自动解绑
                .subscribe(object : BaseObserver<RequestResult<Any>>() {
                    override fun success(requestResult: RequestResult<Any>) {
                        //                        String coins2 = SharedPreferencesUtils.getInstance().getString("coin_id", "");
                        if (type) {
                            ToastUtils.showToast(mContext.resources.getString(R.string.cancel_collect_success))
                        } else {
                            ToastUtils.showToast(mContext.resources.getString(R.string.add_collection_success))
                        }
                        notifyDataSetChanged()
                    }

                    override fun failed(code: Int, data: String?, msg: String?) {
//                        super.failed(code, data, msg)
                        var coinId = SharedPreferencesUtils.getInstance().getString("coin_id", "")
                        if (type) {
                            if (coinId == "") {
                                SharedPreferencesUtils.getInstance().saveString("coin_id", ",$fid,")
                            } else {
                                SharedPreferencesUtils.getInstance().saveString("coin_id", "$coinId$fid,")
                            }
                        } else {
                            coinId = coinId.replace(",$fid,", ",")
                            SharedPreferencesUtils.getInstance().saveString("coin_id", coinId)

                        }
                        if (code == 401) {
                            mContext.startActivity(Intent(mContext, LoginActivity::class.java))
                        }
                    }
                })
    }
}
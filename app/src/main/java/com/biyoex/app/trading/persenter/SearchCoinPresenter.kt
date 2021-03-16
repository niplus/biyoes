package com.biyoex.app.trading.persenter

import com.biyoex.app.common.mvpbase.BasePresent
import com.biyoex.app.home.bean.CoinTradeRankBean
import com.biyoex.app.trading.bean.CoinMarketLiveData
import com.biyoex.app.trading.view.SearchCoinView

class SearchCoinPresenter : BasePresent<SearchCoinView>() {

    fun getCoinData() {
        var mListData: MutableList<CoinTradeRankBean.DealDatasBean> = mutableListOf()
        val mCoinData = CoinMarketLiveData.getIns().value
        if (mCoinData != null) {
            val keys = mCoinData.dataMap.keys
            for (item in keys) {
                val value = mCoinData.dataMap.getValue(item)
                if (item == "POC+") {
                    for (item in value) {
                        item.isNew = 1
                        mListData.add(item)
                    }
                } else {
                    mListData.addAll(value)
                }
            }
            mView.getCoinData(mListData)
        }
    }
}
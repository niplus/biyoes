package com.biyoex.app.trading.view

import com.biyoex.app.common.bean.CoinInfoBean
import com.biyoex.app.common.mvpbase.BaseView


/**
 * time = 2019/9/10 0010
 * CreatedName =
 */
interface CoinDetailsView :BaseView {
    fun getCoinInfo(coinInfoBean: CoinInfoBean)
}
package com.biyoex.app.common.data

import android.content.Context
import androidx.lifecycle.LiveData
import com.biyoex.app.home.bean.CoinTradeRankBean

/**
 *
 * @ProjectName:    VBT-android$
 * @Package:        com.vbt.app.common.data$
 * @ClassName:      CoinLiveData$
 * @Description:    java类作用描述
 * @Author:         赵伟国
 * @CreateDate:     2020-05-18$ 11:15$
 * @Version:        1.0
 */
class CoinLiveData(var context: Context) :LiveData<CoinTradeRankBean.DealDatasBean>(){


    companion object {
        private var ins: CoinLiveData? = null
        fun getIns(mContext: Context): CoinLiveData {
            if (ins == null) {
                ins = CoinLiveData(mContext)
            }
            return ins as CoinLiveData
        }
    }

     fun setCoinValue(value:CoinTradeRankBean.DealDatasBean){
         setValue(value)
     }

    override fun getValue(): CoinTradeRankBean.DealDatasBean{
        return super.getValue()!!
    }
}
package com.biyoex.app.my.view

import com.biyoex.app.common.mvpbase.BaseView

/**
 *
 * @ProjectName:    VBT-android$
 * @Package:        com.vbt.app.my.view$
 * @ClassName:      OCRView$
 * @Description:    实名认证VIEw
 * @Author:         赵伟国
 * @CreateDate:     2020-04-10$ 12:47$
 * @Version:        1.0
 */
interface OCRView :BaseView{

    fun checkBankSuccess(biz_token:String)

    fun postAuthSuccess()
}
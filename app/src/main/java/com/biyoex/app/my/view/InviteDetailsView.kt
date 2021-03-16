package com.biyoex.app.my.view

import com.biyoex.app.common.bean.InviteReturnRankBean
import com.biyoex.app.common.bean.MineInviteReturnBean
import com.biyoex.app.common.mvpbase.BaseView


/**
 * time = 2019/8/22 0022
 * CreatedName = zhaoweiguo
 */
interface InviteDetailsView : BaseView {
    fun getInviteRankDataSuccess(mRankData: MutableList<InviteReturnRankBean>)
    fun getMyInviteSuccess(mInviteData: MutableList<MineInviteReturnBean>)
}
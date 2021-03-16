package com.biyoex.app.home.view

import com.biyoex.app.common.bean.PosRoleListBean
import com.biyoex.app.common.mvpbase.BaseView

interface PosRolesView :BaseView {

    fun getRoleListData(mListData: MutableList<PosRoleListBean>)
}
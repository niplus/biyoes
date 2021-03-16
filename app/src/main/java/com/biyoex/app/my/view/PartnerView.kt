package com.biyoex.app.my.view

import com.biyoex.app.common.bean.InvitationPartnerBean
import com.biyoex.app.common.mvpbase.BaseView

 interface   PartnerView :BaseView {

  fun getPartner(code:Int)
  fun postShareIdSuccess(data: InvitationPartnerBean)
}
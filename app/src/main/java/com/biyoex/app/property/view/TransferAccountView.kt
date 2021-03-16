package com.biyoex.app.property.view

import com.biyoex.app.common.mvpbase.BaseView

interface TransferAccountView :BaseView {
   fun getUserBalance(userBalance:String,CoidNmae:String)

   fun postTransferSuccess()

   fun postSmsSuccess()

}
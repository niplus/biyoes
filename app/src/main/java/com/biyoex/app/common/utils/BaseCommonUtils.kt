package com.biyoex.app.common.utils

import android.content.Context
import android.widget.Toast

/**
 *
 * @ProjectName:    VBT-android$
 * @Package:        com.vbt.app.common.utils$
 * @ClassName:      BaseCommonUtils$
 * @Description:    java类作用描述
 * @Author:         赵伟国
 * @CreateDate:     2020-05-19$ 09:15$
 * @Version:        1.0
 */


fun Context.toast(message:String){
    Toast.makeText(this,message,Toast.LENGTH_LONG).show()
}

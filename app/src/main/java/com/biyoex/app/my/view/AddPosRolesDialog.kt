package com.biyoex.app.my.view

import android.app.Activity
import android.text.Editable
import android.view.Gravity
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import com.biyoex.app.R
import com.biyoex.app.common.utils.Util
import com.biyoex.app.common.widget.BottomDialog

/**
 * Created by mac on 20/7/28.
 * 添加POS角色
 */


class AddPosRolesDialog(context: Activity, var listener: DialogAddPartner) : BottomDialog(context, R.layout.dialog_add_roles) {
    init {
        val window = window
        window!!.decorView.setPadding(100, 0, 100, 0)
        // 获取Window的LayoutParams
        val attributes = window.attributes
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT
        attributes.gravity = Gravity.CENTER
        // 一定要重新设置, 才能生效
        window.attributes = attributes

        //粘贴
        findViewById<TextView>(R.id.tv_addRoles_paste_InviteCode).setOnClickListener {
            findViewById<EditText>(R.id.edit_input_InviteCode).text = Editable.Factory.getInstance().newEditable(Util.getCopy(context))
        }

        //立即添加
        findViewById<TextView>(R.id.tv_Immediately_create).setOnClickListener {
            val name: String = Util.ReplaceAll(findViewById(R.id.edit_input_nickname))
            val inviteCode: String = Util.ReplaceAll(findViewById(R.id.edit_input_InviteCode))
            listener.onAddPosRoles(name, inviteCode)
            findViewById<EditText>(R.id.edit_input_nickname).text = Editable.Factory.getInstance().newEditable("")
        }
    }
}

interface DialogAddPartner {
    fun onAddPosRoles(name: String, inviteCode: String)
}
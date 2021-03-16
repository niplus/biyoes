package com.biyoex.app.home.activity

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.biyoex.app.R
import com.biyoex.app.common.bean.PosRoleListBean
import com.biyoex.app.common.data.AppData
import com.biyoex.app.common.mvpbase.BaseActivity
import com.biyoex.app.home.presenter.PosRolesPresenter
import com.biyoex.app.home.view.PosRolesView
import com.biyoex.app.my.view.AddPosRolesDialog
import com.biyoex.app.my.view.DialogAddPartner
import kotlinx.android.synthetic.main.activity_switching_pos_roles.*
import kotlinx.android.synthetic.main.layout_title_while_bar.*

/**
 * Created by mac on 20/7/28.
 * 切换POS角色
 */

class SwitchingPosRolesActivity : BaseActivity<PosRolesPresenter>(), DialogAddPartner, PosRolesView {

    var mAdapter: SwitchingPosRolesAdapter? = null
    var dialog: AddPosRolesDialog? = null

    @SuppressLint("SetTextI18n")
    override fun initData() {
        dialog = AddPosRolesDialog(this, this)
        iv_right.visibility = View.VISIBLE
        iv_right.setImageResource(R.mipmap.add_new_role)
        //添加新角色
        iv_right.setOnClickListener {
            dialog?.show()
        }
        rl_menu.setOnClickListener { finish() }
        tv_title.text = "切换POS角色"
        recyclerview_pos_roles.layoutManager = LinearLayoutManager(this)
        mAdapter = SwitchingPosRolesAdapter()
        mAdapter!!.emptyView = View.inflate(this, R.layout.view_empty, null)
        recyclerview_pos_roles.adapter = mAdapter

    }

    override fun getRoleListData(mListData: MutableList<PosRoleListBean>) {
        mAdapter?.setNewData(mListData)
    }

    override fun getLayoutId(): Int = R.layout.activity_switching_pos_roles


    //立即添加pos角色
    override fun onAddPosRoles(name: String, inviteCode: String) {
        dialog?.dismiss()
        mPresent.getPosCreatePosRoleData(name, inviteCode)//创建角色

    }

    override fun createPresent(): PosRolesPresenter = PosRolesPresenter()
    override fun initComp() {
        mPresent.getSwiPosRoleListData()//获取角色列表
    }

}

class SwitchingPosRolesAdapter : BaseQuickAdapter<PosRoleListBean, BaseViewHolder>(R.layout.item_user_swi_roles) {
    override fun convert(helper: BaseViewHolder?, item: PosRoleListBean) {
        val id = "（ID: " + item.id + "）"
        helper!!.setText(R.id.tv_user_account_name, item.nickName)
                .setText(R.id.tv_user_account_type, if (item.userId == 0) "主账号$id" else "子账号$id")
        val oneStatus: ImageView = helper.getView(R.id.iv_swi_roles_selected)
        if (item.userId == AppData.getRoleId() || item.id == AppData.getRoleId()) {
            oneStatus.visibility = View.VISIBLE
//            helper.setImageResource(R.id.iv_swi_roles_selected, R.mipmap.user_swi_roles_selected)
        } else {
            oneStatus.visibility = View.GONE
//            helper.setImageResource(R.id.iv_swi_roles_selected, R.mipmap.user_swi_roles_selecteds)
        }
        //点击刷新数据
        helper.itemView.setOnClickListener {
            notifyDataSetChanged();
            if (item.userId == 0) {
                AppData.setRoleId(0)
            } else {
                AppData.setRoleId(item.id)
            }
        }
    }
}

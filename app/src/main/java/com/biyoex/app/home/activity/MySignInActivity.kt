package com.biyoex.app.home.activity

import androidx.recyclerview.widget.LinearLayoutManager
import com.biyoex.app.R
import com.biyoex.app.common.base.BaseAppCompatActivity
import com.biyoex.app.common.bean.RequestResult
import com.biyoex.app.common.http.RetrofitHelper
import com.biyoex.app.common.mvpbase.BaseObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.base_refresh_recyclerview.*
import kotlinx.android.synthetic.main.layout_title_bar.*

class MySignInActivity : BaseAppCompatActivity() {
    var page = 1
    var pagesize = 20
    override fun getLayoutId(): Int = R.layout.activity_my_sign_in

    override fun initView() {
        tv_title.text = "签到记录"
        rl_menu.setOnClickListener { finish() }

        base_recyclerview.layoutManager = LinearLayoutManager(this)

    }

    override fun initData() {
        RetrofitHelper.getIns().zgtopApi
                .getSignRecord(page,pagesize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object:BaseObserver<RequestResult<String>>(this){
                    override fun success(t: RequestResult<String>) {

                    }

                    override fun failed(code: Int, data: String?, msg: String?) {
                        super.failed(code, data, msg)
                        msg?.let {
                            showToast(msg)
                        }
                    }
                })
    }


}

package com.biyoex.app.home.view

import android.content.Context
import android.view.Gravity
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.itfitness.nineluckpan.widgei.NineLuckPan
import com.biyoex.app.R
import com.biyoex.app.common.bean.RequestResult
import com.biyoex.app.common.bean.SignToCoin
import com.biyoex.app.common.http.RetrofitHelper
import com.biyoex.app.common.mvpbase.BaseObserver
import com.biyoex.app.common.widget.BottomDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.view_user_everyday.*

class HomeSignDialog(context: Context, var mData: List<SignToCoin>) : BottomDialog(context, R.layout.view_user_everyday) {
    var mAdapter = HomeSignAdapter()

    init {
        val window = window
        window!!.decorView.setPadding(0, 0, 0, 0)
        // 获取Window的LayoutParams
        val attributes = window.attributes
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT
        attributes.gravity = Gravity.CENTER
        // 一定要重新设置, 才能生效
        window.attributes = attributes
//        var mRecyclerView = findViewById<RecyclerView>(R.id.rv_home_sign)
        findViewById<ImageView>(R.id.iv_close).setOnClickListener {
            dismiss()
        }
        var luckyView = findViewById<NineLuckPan>(R.id.lucky_view)
        luckyView.mLuckStr.clear()
        luckyView.mLuckStr.addAll(mData)
        luckyView.onLuckPanAnimEndListener = object : NineLuckPan.OnLuckPanAnimEndListener {
            override fun onClickListener() {
                getLuckyIndex()
            }

            override fun onAnimEnd(position: Int, msg: SignToCoin) {
                dismiss()
                HomedetailsDialog(context,msg).show()
            }
        }
    }

    private fun getLuckyIndex() {
//        lucky_view.startAnim(5+1)
            RetrofitHelper.getIns().zgtopApi
                    .luckyIndex
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : BaseObserver<RequestResult<String>>() {

                        override fun failed(code: Int, data: String?, msg: String?) {
                            super.failed(code, data, msg)
                            Toast.makeText(context,data,Toast.LENGTH_LONG).show()
                        }

                        override fun success(t: RequestResult<String>) {
                            for (item in mData.indices){
                                t.data.let {
                                    if (it != null) {
                                        if(it == "${mData[item].id}"){
                                            lucky_view.startAnim(item+1)
                                        }
                                    }
                                }
                            }
                        }
                    })

    }
}

class HomeSignAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_home_sign) {
    var mSelectPosition = 100;
    override fun convert(helper: BaseViewHolder?, item: String) {
        helper?.setText(R.id.tv_item_name, item)
        if (helper?.adapterPosition == 4) {
            helper?.setBackgroundRes(R.id.tv_item_name, R.mipmap.icon_sign_commit)
                    .setText(R.id.tv_item_name, R.string.sign_home)
        }
    }

    fun setSelectPosition(position: Int) {
        mSelectPosition = position
        notifyDataSetChanged()
    }
}
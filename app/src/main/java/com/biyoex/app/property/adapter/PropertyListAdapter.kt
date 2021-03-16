package com.biyoex.app.property.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.View
import android.view.animation.AnimationSet
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.biyoex.app.R
import com.biyoex.app.VBTApplication
import com.biyoex.app.common.Constants
import com.biyoex.app.common.base.RxBus
import com.biyoex.app.common.base.RxBusData
import com.biyoex.app.common.data.SessionLiveData
import com.biyoex.app.common.utils.ActivityManagerUtils
import com.biyoex.app.common.utils.GlideUtils
import com.biyoex.app.common.utils.MoneyUtils
import com.biyoex.app.my.activity.CurrencyReChargeActivity
import com.biyoex.app.my.activity.FinanceDetailActivity
import com.biyoex.app.my.activity.WithdrawCurrencyActivity
import com.biyoex.app.property.LockPropertyActivity
import com.biyoex.app.property.PropertyRecordActivity
import com.biyoex.app.property.TransferAccountActivity
import com.biyoex.app.property.TransferActivity
import com.biyoex.app.property.datas.PropertyListBean
import java.math.BigDecimal

@Suppress("DEPRECATION")
class PropertyListAdapter(var mType: Int) : BaseQuickAdapter<PropertyListBean, BaseViewHolder>(R.layout.item_news_currency_capital) {

    @SuppressLint("ObjectAnimatorBinding")
    override fun convert(holder: BaseViewHolder?, data: PropertyListBean) {
        var item = data.rechargeCoinBean
        var tvRecharge = holder!!.itemView.findViewById<TextView>(R.id.item_property_recharge)
        var tvWithDraw = holder.itemView.findViewById<TextView>(R.id.item_property_withdraw)
        var layoutChild = holder.itemView.findViewById<LinearLayout>(R.id.item_layout_child)
        var animation = AnimationSet(true)

        //转出动画
        var transferAnimation = TranslateAnimation(TranslateAnimation.RELATIVE_TO_PARENT, 1f,
                TranslateAnimation.RELATIVE_TO_PARENT, 0f,
                TranslateAnimation.RELATIVE_TO_PARENT, 0f
                , TranslateAnimation.RELATIVE_TO_PARENT, 0f);
        transferAnimation.duration = 500
        animation.addAnimation(transferAnimation)
        var outAnimation = TranslateAnimation(TranslateAnimation.RELATIVE_TO_SELF, 0f,
                TranslateAnimation.RELATIVE_TO_SELF, 1f,
                TranslateAnimation.RELATIVE_TO_SELF, 0f
                , TranslateAnimation.RELATIVE_TO_SELF, 0f);
        outAnimation.duration = 500
        if (mType == 2) {
            holder.setVisible(R.id.item_property_recharge, false)
                    .setVisible(R.id.item_property_withdraw, false)
                    .setVisible(R.id.item_property_market, false)
        } else {
            holder.setVisible(R.id.item_property_transfer, false)
                    .setVisible(R.id.item_property_lock, false)
                    .setVisible(R.id.item_property_record, false)
        }
        layoutChild.visibility = if (data.isopen) View.VISIBLE else View.GONE
        tvRecharge.background = if (item.isRecharge) mContext.getDrawable(R.drawable.bg_item_property_btn) else mContext.getDrawable(R.drawable.bg_item_property_btn_normal)
        tvWithDraw.background = if (item.isWithDraw) mContext.getDrawable(R.drawable.bg_item_property_btn) else mContext.getDrawable(R.drawable.bg_item_property_btn_normal)
        val iamgeview = holder.itemView.findViewById<ImageView>(R.id.iv_coin_iamge)
//        Glide.with(mContext).

        GlideUtils.getInstance().displayCricleImage(mContext, "${VBTApplication.appPictureUrl}${item.url}", holder.itemView.findViewById(R.id.iv_coin_iamge))
        holder.setText(R.id.tv_shortName, MoneyUtils.add(item.frozen, item.total).toPlainString())
                .setText(R.id.tv_frozen_number, MoneyUtils.decimal4ByUp(BigDecimal(item.frozen)).toPlainString())
                .setText(R.id.tv_total_number, MoneyUtils.decimal4ByUp(BigDecimal(item.total)).toPlainString())
                .setText(R.id.tv_value, MoneyUtils.decimal2ByUp(BigDecimal(item.cnyValue)).toPlainString())
                .setText(R.id.tv_coin_name, item.name)
        holder.setImageDrawable(R.id.item_is_open, if (data.isopen) mContext.resources.getDrawable(R.mipmap.icon_open) else mContext.resources.getDrawable(R.mipmap.icon_no_open))
        //交易

        holder.setOnClickListener(R.id.item_property_market) {
            Constants.coinName = item.name

            val rxBusData = RxBusData()
            rxBusData.msgName = "MarketFragment"
            rxBusData.msgData = "refreshData"
            RxBus.get().post(rxBusData)
//            EventBus.getDefault().post("MarketFragmentRefresh");
            ActivityManagerUtils.getInstance().mainActivity.turnPage(2)
        }
        //锁仓
        holder.setOnClickListener(R.id.item_property_lock) {
            val intent = Intent(mContext, LockPropertyActivity::class.java)
            intent.putExtra("coidId", "" + item.id)
            mContext.startActivity(intent)
        }
        //划转
        holder.setOnClickListener(R.id.item_property_transfer) {
            val intent = Intent(mContext, TransferActivity::class.java)
            intent.putExtra("coidId", item.id)
            mContext.startActivity(intent)
        }
        //转账
        holder.setOnClickListener(R.id.item_property_transfer_account) {
            val intent = Intent(mContext, TransferAccountActivity::class.java)
            intent.putExtra("coidId", Integer.valueOf(item.id))
            intent.putExtra("coidName", item.name)
            mContext.startActivity(intent)
        }
        //记录
        holder.setOnClickListener(R.id.item_property_record) {
            val intent = Intent(mContext, PropertyRecordActivity::class.java)
            intent.putExtra("coidId", Integer.valueOf(item.id))
            mContext.startActivity(intent)
        }
        //充值
        holder.setOnClickListener(R.id.item_property_recharge) {
            if (item.isRecharge) {
                val itCurrencyReCharge = Intent(mContext, CurrencyReChargeActivity::class.java)
                itCurrencyReCharge.putExtra("currency_name", item.allName)
                itCurrencyReCharge.putExtra("symbol", item.id)
                itCurrencyReCharge.putExtra("shortName", item.name)
                itCurrencyReCharge.putExtra("img_url", item.url)
                mContext.startActivity(itCurrencyReCharge)
            } else {
                Toast.makeText(mContext, mContext.getString(R.string.stop_recharge), Toast.LENGTH_SHORT).show()
            }
        }
        //提现
        holder.setOnClickListener(R.id.item_property_withdraw) {
            SessionLiveData.getIns().value?.let {
                if (item.isWithDraw) {
                    val itWithDrawCurrency = Intent(mContext, WithdrawCurrencyActivity::class.java)
                    itWithDrawCurrency.putExtra("currency_name", item.allName)
                    itWithDrawCurrency.putExtra("symbol", item.id)
                    itWithDrawCurrency.putExtra("shortName", item.name)
                    itWithDrawCurrency.putExtra("frozen", item.frozen)
                    itWithDrawCurrency.putExtra("total", item.total)
                    itWithDrawCurrency.putExtra("img_url", item.url)
                    mContext.startActivity(itWithDrawCurrency)
//                    if (it.isGoogleBind) {
//                        val itWithDrawCurrency = Intent(mContext, WithdrawCurrencyActivity::class.java)
//                        itWithDrawCurrency.putExtra("currency_name", item.allName)
//                        itWithDrawCurrency.putExtra("symbol", item.id)
//                        itWithDrawCurrency.putExtra("shortName", item.name)
//                        itWithDrawCurrency.putExtra("frozen", item.frozen)
//                        itWithDrawCurrency.putExtra("total", item.total)
//                        itWithDrawCurrency.putExtra("img_url", item.url)
//                        mContext.startActivity(itWithDrawCurrency)
//                    } else {
//                        AlertDialog.Builder(mContext)
//                                .setTitle(R.string.hint_message)
//                                .setMessage(R.string.please_bind_google).setPositiveButton(R.string.go_bind) { dialog, _ ->
//                                    dialog.dismiss()
//                                    mContext.startActivity(Intent(mContext, BindGoogleActivity::class.java))
//                                }.setNegativeButton(R.string.cancel) { dialog, _ -> dialog.dismiss() }.show()
//                    }
                } else {
                    Toast.makeText(mContext, mContext.getString(R.string.stop_withdraw), Toast.LENGTH_SHORT).show()

                }
            }

        }
        holder.itemView.setOnClickListener {
//            if (!data.isopen) {
//                layoutChild.visibility = View.VISIBLE
//                layoutChild.startAnimation(animation)
//                animation.setAnimationListener(object : Animation.AnimationListener {
//                    override fun onAnimationRepeat(animation: Animation?) {
//
//                    }
//
//                    override fun onAnimationEnd(animation: Animation?) {
//                        for (item in 0 until mData.size) {
//                            if (item != holder.adapterPosition) {
//                                mData[item] = PropertyListBean(false, mData[item].rechargeCoinBean)
//                            } else {
//                                mData[item] = PropertyListBean(true, mData[item].rechargeCoinBean)
//                            }
//                        }
//                        setNewData(mData)
//                    }
//
//                    override fun onAnimationStart(animation: Animation?) {
//                    }
//                })
//            } else {
//                layoutChild.startAnimation(outAnimation)
//                outAnimation.setAnimationListener(object : Animation.AnimationListener {
//                    override fun onAnimationRepeat(animation: Animation?) {
//
//                    }
//
//                    override fun onAnimationEnd(animation: Animation?) {
//                        layoutChild.visibility = View.GONE
//                        holder.setVisible(R.id.item_layout_child, false)
//                        mData[holder.adapterPosition] = PropertyListBean(false, mData[holder.adapterPosition].rechargeCoinBean)
//                        setNewData(mData)
//                    }
//
//                    override fun onAnimationStart(animation: Animation?) {
//
//                    }
//                })
//            }

            val intent = Intent(mContext, FinanceDetailActivity::class.java).apply {
                putExtra("finance", item)
            }
            mContext.startActivity(intent)
        }
    }
}
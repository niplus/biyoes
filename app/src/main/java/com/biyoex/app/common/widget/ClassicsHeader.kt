package com.biyoex.app.common.widget

import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshKernel
import android.content.Context
import androidx.annotation.ColorInt
import androidx.annotation.NonNull
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.github.fujianlian.klinechart.utils.ViewUtil
import com.scwang.smartrefresh.layout.constant.RefreshState
import com.scwang.smartrefresh.layout.constant.SpinnerStyle
import com.scwang.smartrefresh.layout.internal.pathview.PathsView
import com.scwang.smartrefresh.layout.internal.ProgressDrawable
import com.scwang.smartrefresh.layout.api.RefreshHeader
import com.biyoex.app.R


class ClassicsHeader : LinearLayout, RefreshHeader {
    override fun onPullingDown(percent: Float, offset: Int, headerHeight: Int, extendHeight: Int) {

    }

    private var mHeaderText: TextView? = null//标题文本
    private var mArrowView: PathsView? = null//下拉箭头
    private var mProgressView: ImageView? = null//刷新动画视图
    private var mProgressDrawable: ProgressDrawable? = null//刷新动画

    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        this.initView(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        this.initView(context)
    }

    private fun initView(context: Context) {
        gravity = Gravity.CENTER
        mHeaderText = TextView(context)
        mProgressDrawable = ProgressDrawable()
        mArrowView = PathsView(context)
        mProgressView = ImageView(context)
        mProgressView!!.setImageDrawable(mProgressDrawable)
        mArrowView!!.parserPaths("M20,12l-1.41,-1.41L13,16.17V4h-2v12.17l-5.58,-5.59L4,12l8,8 8,-8z")
        addView(mProgressView, ViewUtil.Dp2Px(context,20f), ViewUtil.Dp2Px(context,20f))
        addView(mArrowView, ViewUtil.Dp2Px(context,20f), ViewUtil.Dp2Px(context,20f))
        addView(View(context),ViewUtil.Dp2Px(context,20f), ViewUtil.Dp2Px(context,20f))
        addView(mHeaderText, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        minimumHeight = ViewUtil.Dp2Px(context,60f)
    }

    @NonNull
    override fun getView(): View {
        return this//真实的视图就是自己，不能返回null
    }

    override fun getSpinnerStyle(): SpinnerStyle {
        return SpinnerStyle.Translate//指定为平移，不能null
    }

    override fun onStartAnimator(layout: RefreshLayout, headHeight: Int, maxDragHeight: Int) {
        mProgressDrawable!!.start()//开始动画
    }

    override fun onFinish(layout: RefreshLayout, success: Boolean): Int {
        mProgressDrawable!!.stop()//停止动画
        if (success) {
            mHeaderText!!.text = context.getString(R.string.refresh_commit)
        } else {
            mHeaderText!!.text = context.getString(R.string.refresh_faield)
        }
        return 500//延迟500毫秒之后再弹回
    }

    override fun onStateChanged(refreshLayout: RefreshLayout, oldState: RefreshState, newState: RefreshState) {
        when (newState) {
            RefreshState.None, RefreshState.PullDownToRefresh -> {
                mHeaderText!!.text = context.getString(R.string.down_start_refresh)
                mArrowView!!.visibility = VISIBLE//显示下拉箭头
                mProgressView!!.setVisibility(GONE)//隐藏动画
                mArrowView!!.animate().rotation(0f)//还原箭头方向
            }
            RefreshState.Refreshing -> {
                mHeaderText!!.text = context.getString(R.string.refreshing)
                mProgressView!!.visibility = VISIBLE//显示加载动画
                mArrowView!!.visibility = GONE//隐藏箭头
            }
            RefreshState.ReleaseToRefresh -> {
                mHeaderText!!.setText(context.getString(R.string.end_refresh))
                mArrowView!!.animate().rotation(180f)//显示箭头改为朝上
            }
        }
    }

    override fun isSupportHorizontalDrag(): Boolean {
        return false
    }

    override fun onInitialized(kernel: RefreshKernel, height: Int, maxDragHeight: Int) {}
    override fun onHorizontalDrag(percentX: Float, offsetX: Int, offsetMax: Int) {}
    fun onPulling(percent: Float, offset: Int, headHeight: Int, maxDragHeight: Int) {}
    override fun onReleasing(percent: Float, offset: Int, headHeight: Int, maxDragHeight: Int) {}
    override fun onRefreshReleased(layout: RefreshLayout, headerHeight: Int, maxDragHeight: Int) {}
    override fun setPrimaryColors(@ColorInt vararg colors: Int) {}
}
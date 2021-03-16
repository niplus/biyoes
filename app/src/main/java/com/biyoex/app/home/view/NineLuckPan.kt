package com.itfitness.nineluckpan.widgei

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.Nullable
import com.biyoex.app.R
import com.biyoex.app.common.bean.SignToCoin
import com.biyoex.app.common.utils.Util

import java.util.ArrayList
import kotlin.math.min


class NineLuckPan @JvmOverloads constructor(context: Context, @Nullable attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private var mPaint: Paint? = null
    private var mTextPaint: Paint? = null
    private var mRects: ArrayList<RectF>? = null//存储矩形的集合
    private val mStrokWidth = 10f//矩形的描边宽度
    private val mItemColor = intArrayOf(Color.GREEN, Color.YELLOW)//矩形的颜色
    private var mRectSize: Int = 0//矩形的宽和高（矩形为正方形）
    private var mClickStartFlag = false//是否点击中间矩形的标记
    private val mRepeatCount = 3//转的圈数
    private var mLuckNum = 6//最终中奖位置
    private var mPosition = -1//抽奖块的位置
    private var mStartLuckPosition = 0//开始抽奖的位置
    var mLuckStr = mutableListOf<SignToCoin>()
    var onLuckPanAnimEndListener: OnLuckPanAnimEndListener? = null

    /**
     * 初始化数据
     */
    init {
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint!!.style = Paint.Style.FILL
        mPaint!!.strokeWidth = mStrokWidth
        mRects = ArrayList()
        mTextPaint = Paint()
        mTextPaint!!.color = Color.RED
        mTextPaint!!.textSize = Util.Dp2Px(context, 12f).toFloat()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mRectSize = min(w, h) / 3//获取矩形的宽和高
        mRects!!.clear()//当控件大小改变的时候清空数据
        initRect()//重新加载矩形数据
    }

    /**
     * 加载矩形数据
     */
    private fun initRect() {
        //加载前三个矩形
        for (x in 0..2) {
            val left = (x * mRectSize).toFloat()
            val top = 0f
            val right = ((x + 1) * mRectSize).toFloat()
            val bottom = mRectSize.toFloat()
            val rectF = RectF(left, top, right, bottom)
            mRects!!.add(rectF)
        }
        //加载第四个
        mRects!!.add(RectF((width - mRectSize).toFloat(), mRectSize.toFloat(), width.toFloat(), (mRectSize * 2).toFloat()))
        //加载第五~七个
        for (y in 3 downTo 1) {
            val left = (width - (4 - y) * mRectSize).toFloat()
            val top = (mRectSize * 2).toFloat()
            val right = ((y - 3) * mRectSize + width).toFloat()
            val bottom = (mRectSize * 3).toFloat()
            val rectF = RectF(left, top, right, bottom)
            mRects!!.add(rectF)
        }
        //        加载第八个
        mRects!!.add(RectF(0f, mRectSize.toFloat(), mRectSize.toFloat(), (mRectSize * 2).toFloat()))
        //加载第九个
        mRects!!.add(RectF(mRectSize.toFloat(), mRectSize.toFloat(), (mRectSize * 2).toFloat(), (mRectSize * 2).toFloat()))
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            mClickStartFlag = mRects!![8].contains(event.x, event.y)
            return true
        }
        if (event.action == MotionEvent.ACTION_UP) {

            if (mClickStartFlag) {
                if (mRects!![8].contains(event.x, event.y)) {
                    onLuckPanAnimEndListener?.onClickListener()
                }
                mClickStartFlag = false
            }
        }
        return super.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawRects(canvas)
        drawImages(canvas)
    }

    private fun drawImages(canvas: Canvas) {
        mTextPaint!!.textAlign = Paint.Align.CENTER
        for (x in 0 until mRects!!.size) {
            if (x != 8) {
                val rectF = mRects!![x]
                val left = rectF.left + (rectF.width() / 2)
                val top = rectF.top + (rectF.height() / 2)
                var text = if (mLuckStr[x].amount.isNullOrEmpty()) mLuckStr[x].name else "${mLuckStr[x].amount}${mLuckStr[x].name}"
                canvas.drawText(text, left, top, mTextPaint!!)
            } else {
                canvas.drawText(context.getString(R.string.sign_home), width / 2f, height / 2f, mTextPaint!!)
            }
        }
    }

    /**
     * 画矩形
     * @param canvas
     */
    private fun drawRects(canvas: Canvas) {
        mPaint!!.color = Color.RED
        mPaint!!.style = Paint.Style.STROKE
        mPaint!!.strokeJoin = Paint.Join.ROUND
        for (x in mRects!!.indices) {
            val rectF = mRects!![x]
            if (x == 8) {
                mPaint!!.style = Paint.Style.STROKE
                mPaint!!.color = Color.parseColor("#EB4D4D")
                var rect = Rect()
                rect.left = 0
                rect.right = rectF.width().toInt()
                rect.top = 0
                rect.bottom = rectF.height().toInt()
                canvas.drawBitmap(BitmapFactory.decodeResource(resources, R.mipmap.icon_sign_commit), rect, rectF, mPaint)
            } else {
                if (mPosition == x) {
                    mPaint!!.style = Paint.Style.FILL
                    mPaint!!.color = Color.parseColor("#FBEC95")
                } else {
                    mPaint!!.style = Paint.Style.STROKE
                    mPaint!!.color = Color.parseColor("#EB4D4D")
                }
                canvas.drawRoundRect(rectF, 20f, 20f, mPaint!!)
            }
        }
    }

    fun setPosition(position: Int) {
        mPosition = position
        invalidate()
    }

    /**
     * 开始动画
     */
    fun startAnim(mLuckNum: Int) {
        val valueAnimator = ValueAnimator.ofInt(mStartLuckPosition, mRepeatCount * 8 + mLuckNum).setDuration(3000)
        valueAnimator.addUpdateListener { animation ->
            val position = animation.animatedValue as Int
            setPosition(position % 8)
        }
        valueAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                mStartLuckPosition = mLuckNum
                if (onLuckPanAnimEndListener != null&&mPosition!=0) {
                    onLuckPanAnimEndListener!!.onAnimEnd(mPosition, mLuckStr[mPosition - 1])
                }
            }
        })
        valueAnimator.start()
    }



    interface OnLuckPanAnimEndListener {
        fun onAnimEnd(position: Int, msg: SignToCoin)
        fun onClickListener()
    }
}

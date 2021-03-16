package com.biyoex.app.common.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import com.biyoex.app.R


class PercentTextLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    private var mText: String? = null
    var text: String?
        get() = mText
        set(mText) {
            this.mText = mText
            postInvalidate()
        }

    var defaultPercent: Int
        get() = mDefaultPercent
        set(mDefaultPercent) {
            this.mDefaultPercent = mDefaultPercent
            postInvalidate()
        }

    private var mDefaultPercent: Int = 0
    var backColorDefault: Int = 0
    var backColor: Int = 0
    var textColor: Int = 0
    var textSize: Int = 0
    private var mBound: Rect? = null
    var textAlignLeft: Int = 0

    init {
        initView(context, attrs, defStyleAttr)
    }

    /**
     * @param context
     * 上下文对象
     * @param attrs
     * 参数
     * @param defStyleAttr
     */
    private fun initView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.PercentText, defStyleAttr, 0)
        val n = a.indexCount
        for (i in 0 until n) {
            val attr = a.getIndex(i)
            when (attr) {
                R.styleable.PercentText_backColor -> backColor = a.getColor(attr, Color.GRAY)
                R.styleable.PercentText_backColorDefault -> backColorDefault = a.getColor(attr, Color.WHITE)
                R.styleable.PercentText_defaultPercent -> mDefaultPercent = a.getInt(attr, 50)
                R.styleable.PercentText_text -> mText = a.getString(attr)
                R.styleable.PercentText_textsize -> textSize = a.getDimensionPixelSize(attr, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16F, resources.displayMetrics) as Int)
                R.styleable.PercentText_textColor -> textColor = a.getColor(attr, Color.BLACK)
                R.styleable.PercentText_alignLeft -> textAlignLeft = a.getDimensionPixelSize(attr, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16F, resources.getDisplayMetrics()) as Int)
            }
        }
        a.recycle()
    }

    override fun onDraw(canvas: Canvas) {
        // 绘制背景
        val paint = Paint()
        paint.color = backColorDefault
        canvas.drawRect(0F, 0F, measuredWidth.toFloat(), measuredHeight.toFloat(), paint)
        // 测量字体
        mBound = Rect()
        paint.getTextBounds(mText, 0, mText!!.length, mBound)
        // 绘制已完成的进度
        val path = Path()
        path.moveTo(0F, 0F)
        if (mDefaultPercent < 0) {
            mDefaultPercent = 0
        }
        if (mDefaultPercent > 100) {
            mDefaultPercent = 100
        }
        if (mDefaultPercent >= 2 && mDefaultPercent <= 98) {
            // point2
            val p1_width = (mDefaultPercent + 2) * width / 100
            path.lineTo(p1_width.toFloat(), 0F)
            // point3
            val p3_width = (mDefaultPercent - 2) * width / 100
            path.lineTo(p3_width.toFloat(), height.toFloat())
        } else if (mDefaultPercent < 2) {
            val p1_width = mDefaultPercent * width / 100
            path.lineTo(p1_width.toFloat(), 0F)
        } else if (mDefaultPercent > 98) {
            // point2
            path.lineTo(width.toFloat(), 0F)
            // point3
            val p3_width = mDefaultPercent * width / 100
            path.lineTo(p3_width.toFloat(), height.toFloat())
        }
        // point4
        path.lineTo(0F, getHeight().toFloat())
        path.close()
        paint.color = backColor
        canvas.drawPath(path, paint)
        // 绘制文字
        paint.color = textColor
        paint.textSize = textSize.toFloat()
        paint.strokeWidth = 3F
        val fontMetrics = paint.fontMetricsInt
        // paint2.setTextSize(20);
        val baseline = (measuredHeight - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top
        canvas.drawText(mText!!, textAlignLeft.toFloat(), baseline.toFloat(), paint)
        invalidate()
    }
}
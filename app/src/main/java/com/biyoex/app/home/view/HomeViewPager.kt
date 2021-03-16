package com.biyoex.app.home.view

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import java.util.*


class HomeViewPager : ViewPager {
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    var timer: Timer = Timer()
    var mPosition: Int = 1
    var isTouch = false
    var time: Long = 3000
    lateinit var mAdapter:MyVpAdapter
    var mListener:OnPageScrolled? = null
    private var handler = @SuppressLint("HandlerLeak")
    //自动轮播
    object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            currentItem = mPosition
        }
    }
    //加载position的动画
    init {
        setOnPageChangeListener(object : OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                mPosition = position
                mListener?.pageScrolled(position)
            }
        })
    }

    //设置是否触摸
    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        when (ev?.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> isTouch = true

            MotionEvent.ACTION_UP -> isTouch = false
        }
        return super.onTouchEvent(ev)
    }
    //开启子线程循环播放


    fun setView(list: List<View>) {
        mAdapter = MyVpAdapter(list, context)
        adapter = mAdapter
//        mRunnable.run()
        var timerTask  = object :TimerTask(){
            override fun run() {
              if(!isTouch){
                  mPosition++
                  handler.sendEmptyMessage(1000)
              }
                else{
                  Thread.sleep(3000)
              }
            }
        }
        timer.schedule(timerTask,0,5000)
//        timer.cancel()
    }

    fun setOnCLickListener(listener: ClickListener){
        mAdapter?.listener = listener
    }

    interface  OnPageScrolled{
        fun pageScrolled(position: Int)
    }


    fun cancelLIstener(){
        mListener = null
    }
}
    /**
     * 循环播放
     * */
class MyVpAdapter(var data: List<View>, var context: Context) : PagerAdapter() {
    var listener:ClickListener ? =null

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val i = position % data.size
        val rootView = data[i]
            try{
                container.addView(rootView)
            }
            catch (e:Exception){

            }
        rootView.setOnClickListener {
           listener?.setItemOnClickListener(i%2)
        }
        return rootView
    }
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(data[position%data.size])
    }
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }
    override fun getCount(): Int = Int.MAX_VALUE

}
interface ClickListener{
     fun setItemOnClickListener(position:Int)
}

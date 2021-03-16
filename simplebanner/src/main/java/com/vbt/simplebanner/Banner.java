package com.biyoex.simplebanner;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.biyoex.simplebanner.loader.ImageLoaderInterface;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Banner extends FrameLayout implements ViewPager.OnPageChangeListener {

    /**
     * 是否自动播放
     */
    private boolean isAutoPlay;

    /**
     * 切换时间间隔
     */
    private int timeInterval = 4000;

    private ViewPager mViewPager;

    private List<String> paths;

    private BannerPagerAdapter mAdapter;

    private ImageLoaderInterface imageLoader;

    private Context context;

    private int currentPosition;

    private Handler mHandler;


    public Banner(@NonNull Context context) {
        this(context, null);
    }

    public Banner(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Banner(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.context = context;
        initAtters(attrs);
        initView();

        mHandler = new Handler();
    }

    private void initAtters(@Nullable AttributeSet attrs){
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Banner);
            typedArray.recycle();
        }
    }

    private void initView(){
        paths = new ArrayList<>();

        View rootView = LayoutInflater.from(context).inflate(R.layout.layout_banner, this, true);

        mViewPager = rootView.findViewById(R.id.viewpager);
    }

    private void initViewPager(){
        if (mAdapter != null){
            return;
        }
        mAdapter = new BannerPagerAdapter();
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setPageTransformer(true, pageTransformer);

        mViewPager.addOnPageChangeListener(this);

//        initViewpagerScroll();
    }

    /**
     * 开始播放
     */
    public void play(){
        //正在播放则直接返回
        if (isAutoPlay){
            return;
        }
        isAutoPlay = true;

        mHandler.postDelayed(playRunnable, timeInterval);
    }

    /**
     * 暂停播放
     */
    public void stop(){
        isAutoPlay = false;

        mHandler.removeCallbacks(playRunnable);
    }

    private final Runnable playRunnable = new Runnable() {
        @Override
        public void run() {
            if (isAutoPlay){
                mViewPager.setCurrentItem(currentPosition+1, true);
                mHandler.postDelayed(this, timeInterval);
            }
        }
    };

    private final ViewPager.PageTransformer pageTransformer = new ViewPager.PageTransformer() {
        @Override
        public void transformPage(@NonNull View page, float position) {
            position = Math.abs(position);
            if (position < 2){
                page.setScaleY(1f - 0.05f * position);
                page.setScaleX(1f - 0.05f * position);
            }
        }
    };

    public ViewPager getmViewPager() {
        return mViewPager;
    }

    /**
     * 控制viewpager滑动速度
     */
    private void initViewpagerScroll(){
        if (mViewPager == null){
            return;
        }
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            //使属性可以访问
            field.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(mViewPager.getContext(),
                    new AccelerateInterpolator());
            //设置属性
            field.set(mViewPager, scroller);
            scroller.setmDuration(1000);
        } catch (Exception e) {

        }
    }

    public void setImageLoader(ImageLoaderInterface imageLoader) {
        this.imageLoader = imageLoader;
    }

    public void setImages(List<String> imageUrls){
        initViewPager();
        boolean needTurnPage;
        if (paths.size() != 0) {
            needTurnPage = false;
            paths.clear();
        }else {
            needTurnPage = true;
        }

        paths.addAll(imageUrls);
        mAdapter.notifyDataSetChanged();
        if (needTurnPage && paths.size() != 0) {
            mViewPager.setCurrentItem(Integer.MAX_VALUE / 2 - (Integer.MAX_VALUE / 2 % paths.size()), false);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        currentPosition = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                stop();
                break;
            case MotionEvent.ACTION_UP:
                play();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    class BannerPagerAdapter extends PagerAdapter {
        private final LinkedList<View> viewCache = new LinkedList<>();

        @Override
        public int getCount() {
            if (paths.size() == 0){
                return 0;
            }
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            int index = position % paths.size();
            View convertView = null;
            if (viewCache.size() == 0){
                convertView = LayoutInflater.from(context).inflate(R.layout.layout_page, null);
            }else {
                convertView = viewCache.removeFirst();
            }

            ImageView ivDisplay = convertView.findViewById(R.id.iv_display);
            if (paths.size() != 0)
                imageLoader.displayImage(context, paths.get(index), ivDisplay);

            container.addView(convertView);
            return convertView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            viewCache.add((View) object);
        }
    }
}

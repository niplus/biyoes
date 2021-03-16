package com.biyoex.app.common.mvpbase;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import android.util.Log;

import com.biyoex.app.common.utils.SharedPreferencesUtils;

public abstract class BaseLazyFragment<T extends BasePresent> extends BaseFragment {

    /**
     * 懒加载过
     */
    private boolean isLazyLoaded;
    /**
     * Fragment的View加载完毕的标记
     */
    private boolean isPrepared;
    /**
     * 是否重新加載過
     */
    private boolean isReLazyed;

    public T mPresent;

    /**
     * 第一步,改变isPrepared标记
     * 当onViewCreated()方法执行时,表明View已经加载完毕,此时改变isPrepared标记为true,并调用lazyLoad()方法
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isPrepared = true;
        mPresent = createPresent();
        if (mPresent != null) {
            mPresent.attachView(this);
        }
        //只有Fragment onCreateView好了
        //另外这里调用一次lazyLoad(）
        Log.i("是否加載", "onActivityCreated: ");
        lazyLoad();
    }

    @Override
    public void initImmersionBar() {
//        immersionBar.transparentNavigationBar();
        String color = SharedPreferencesUtils.getInstance().getString("color", "0");
        immersionBar.statusBarDarkFont(color.equals("0")).init();
    }
    /**
     * 第二步
     * 此方法会在onCreateView(）之前执行
     * 当viewPager中fragment改变可见状态时也会调用
     * 当fragment 从可见到不见，或者从不可见切换到可见，都会调用此方法
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.i("是否课件", "setUserVisibleHint: ");
        lazyLoad();
    }

    /**
     * 调用懒加载
     * 第三步:在lazyLoad()方法中进行双重标记判断,通过后即可进行数据加载
     */
    private void lazyLoad() {
        if (getUserVisibleHint() && isPrepared && !isLazyLoaded) {
            onLazyLoad();
            isLazyLoaded = true;
        } else if (getUserVisibleHint() && isPrepared && !isReLazyed) {
            Log.i("懶加載", "lazyLoad: 再次加載");
            reLazyLoad();
        }
    }

    /**
     * 第四步:定义抽象方法onLazyLoad(),具体加载数据的工作,交给子类去完成
     */
    @UiThread
    public abstract void onLazyLoad();

    public abstract void reLazyLoad();

    protected abstract T createPresent();
}

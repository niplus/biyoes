package com.biyoex.app.common.adapter;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * 标签布局的适配器
 * Created by LG on 2017/3/10.
 */

public class TabLayoutAdapter extends FragmentPagerAdapter {

    private Context mContext;
    /**
     * 标签的item布局页面
     */
    private List<Fragment> mFragmentList;
    /**
     * 标签的名称
     */
    private List<String> mTitleList;

    public TabLayoutAdapter(Context mContext, FragmentManager fm, List<Fragment> mFragmentList, List<String> mTitleList) {
        super(fm);
        this.mContext = mContext;
        this.mFragmentList = mFragmentList;
        this.mTitleList = mTitleList;
    }

    /**
     * tablayout的标题的名称
     *
     * @param position
     * @return
     */
    @Override
    public CharSequence getPageTitle(int position) {

        if (mTitleList == null || mTitleList.size() == 0) {
            return "";
        } else {
            return mTitleList.get(position);
        }
    }

    /**
     * 返回当前的fragment
     *
     * @param position
     * @return
     */
    @Override
    public Fragment getItem(int position) {
        if (mFragmentList.size() != 0) {
            return mFragmentList.get(position);
        }
        return null;
    }


    /**
     * 返回总共的页面大小
     *
     * @return
     */
    @Override
    public int getCount() {
        return mFragmentList == null ? 0 : mFragmentList.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (position != 3) {
            super.destroyItem(container, position, object);
        }
    }
}

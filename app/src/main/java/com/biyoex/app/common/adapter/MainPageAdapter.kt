package com.biyoex.app.common.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 *
 * @ProjectName:    VBT-android$
 * @Package:        com.vbt.app.common.adapter$
 * @ClassName:      MainPageAdapter$
 * @Description:    java类作用描述
 * @Author:         赵伟国
 * @CreateDate:     2020-05-18$ 11:47$
 * @Version:        1.0
 */
class MainPageAdapter (var mutableList: MutableList<Fragment>,var fragmentManager: FragmentManager):FragmentPagerAdapter(fragmentManager) {
    override fun getItem(position: Int): Fragment =  mutableList[position]

    override fun getCount(): Int = mutableList.size
}
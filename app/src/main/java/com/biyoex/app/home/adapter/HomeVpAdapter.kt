package com.biyoex.app.home.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import android.view.ViewGroup

class HomeVpAdapter(fragmentManager: FragmentManager, var fragmentList: MutableList<Fragment>, var titleList: MutableList<String>) : FragmentPagerAdapter(fragmentManager) {
    override fun getItem(position: Int): Fragment = fragmentList[position]

    override fun getCount(): Int = fragmentList.size
    override fun getPageTitle(position: Int): CharSequence? = titleList[position]
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        super.destroyItem(container, position, `object`)
    }
}
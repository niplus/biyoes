package com.biyoex.app.property.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class PropertyVPAdapter(val fm: FragmentManager, val mContext:Context, val titles:List<String>, val fragments:List<Fragment>) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment = fragments[position]
    override fun getCount(): Int  = fragments.size
    override fun getPageTitle(position: Int): CharSequence?  = titles[position]
}
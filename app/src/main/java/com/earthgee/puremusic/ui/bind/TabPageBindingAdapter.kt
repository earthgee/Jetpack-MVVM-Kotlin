package com.earthgee.puremusic.ui.bind

import androidx.databinding.BindingAdapter
import androidx.viewpager.widget.ViewPager
import com.earthgee.architecture.ui.adapter.CommonViewPagerAdapter
import com.earthgee.puremusic.R
import com.google.android.material.tabs.TabLayout

/**
 *  Created by earthgee on 2024/1/5
 *  CopyRight (c) earthgee.com
 *  功能：
 */
object TabPageBindingAdapter {

    @JvmStatic
    @BindingAdapter(value = ["initTabAndPage"], requireAll = false)
    fun initTabAndPage(viewPager: ViewPager, initTabAndPage: Boolean) {
        val tabLayout = viewPager.rootView.findViewById<TabLayout>(R.id.tab_layout)
        val count = tabLayout.tabCount
        val title = arrayOfNulls<String>(count)
        for(i in 0 until count) {
            title[i] = tabLayout.getTabAt(i)?.text?.toString()
        }
        viewPager.adapter = CommonViewPagerAdapter(false, title)
        tabLayout.setupWithViewPager(viewPager)
    }

}
























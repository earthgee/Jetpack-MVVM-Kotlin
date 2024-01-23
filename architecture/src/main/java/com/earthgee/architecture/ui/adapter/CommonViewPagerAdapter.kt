package com.earthgee.architecture.ui.adapter

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter

/**
 *  Created by earthgee on 2024/1/5
 *  CopyRight (c) earthgee.com
 *  功能：
 */
class CommonViewPagerAdapter(val enableDestroyItem: Boolean, val title: Array<String?>) :
    PagerAdapter() {

    private var titleCount = 0

    init {
        titleCount = title.size
    }

    override fun getCount(): Int = titleCount

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        return container.getChildAt(position)
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        if(enableDestroyItem) {
            container.removeView(`object` as View)
        }
    }

    override fun getPageTitle(position: Int): CharSequence? = title[position]

}
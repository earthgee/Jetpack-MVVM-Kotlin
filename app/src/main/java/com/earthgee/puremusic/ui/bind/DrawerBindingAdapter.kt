package com.earthgee.puremusic.ui.bind

import androidx.core.view.GravityCompat
import androidx.databinding.BindingAdapter
import androidx.drawerlayout.widget.DrawerLayout

/**
 *  Created by earthgee on 2024/1/4
 *  CopyRight (c) earthgee.com
 *  功能：
 */
object DrawerBindingAdapter {

    @JvmStatic
    @BindingAdapter(value = ["isOpenDrawer"], requireAll = false)
    fun openDrawer(drawerLayout: DrawerLayout, isOpenDrawer: Boolean) {
        if (isOpenDrawer && !drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.openDrawer(GravityCompat.START)
        } else {
            drawerLayout.closeDrawer(GravityCompat.START)
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["allowDrawerOpen"], requireAll = false)
    fun allowDrawerOpen(drawerLayout: DrawerLayout, allowDrawerOpen: Boolean) {
        drawerLayout.setDrawerLockMode(if (allowDrawerOpen) DrawerLayout.LOCK_MODE_UNLOCKED else DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    @JvmStatic
    @BindingAdapter(value = ["bindDrawerListener"], requireAll = false)
    fun listenDrawerState(drawerLayout: DrawerLayout, listener: DrawerLayout.SimpleDrawerListener) {
        drawerLayout.addDrawerListener(listener)
    }

}
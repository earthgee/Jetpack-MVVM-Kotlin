package com.earthgee.puremusic.domain.message

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.kunminx.architecture.domain.message.MutableResult
import com.kunminx.architecture.domain.message.Result


/**
 *  Created by earthgee on 2024/1/8
 *  CopyRight (c) earthgee.com
 *  功能：
 */
object DrawerCoordinateManager : DefaultLifecycleObserver{

    private val tagOfSecondaryPages = ArrayList<String>()

    private val isNoneSecondaryPage: Boolean
        get() = tagOfSecondaryPages.isEmpty()

    private val _enableSwipeDrawer = MutableResult<Boolean>()
    var enableSwipeDrawer: Result<Boolean> = _enableSwipeDrawer

    fun requestToUpdateDrawerMode(pageOpened: Boolean, pageName: String) {
        if(pageOpened) {
            tagOfSecondaryPages.add(pageName)
        } else {
            tagOfSecondaryPages.remove(pageName)
        }
        _enableSwipeDrawer.value = isNoneSecondaryPage
    }

    override fun onCreate(owner: LifecycleOwner) {
        tagOfSecondaryPages.add(owner.javaClass.simpleName)
        _enableSwipeDrawer.value = isNoneSecondaryPage
    }

    override fun onDestroy(owner: LifecycleOwner) {
        tagOfSecondaryPages.remove(owner.javaClass.simpleName)
        _enableSwipeDrawer.value = isNoneSecondaryPage
    }

}
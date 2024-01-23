package com.earthgee.architecture.data.response.manager

import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.earthgee.architecture.utils.Utils

/**
 *  Created by earthgee on 2024/1/2
 *  CopyRight (c) earthgee.com
 *  功能：
 */
object NetworkStateManager : DefaultLifecycleObserver{

    private val mNetworkStateReceive = NetworkStateReceive()

    //TODO tip：让 NetworkStateManager 可观察页面生命周期，
    // 从而在页面失去焦点时，
    // 及时断开本页面对网络状态的监测，以避免重复回调和一系列不可预期的问题。

    // 关于 Lifecycle 组件的存在意义，可详见《为你还原一个真实的 Jetpack Lifecycle》篇的解析
    // https://xiaozhuanlan.com/topic/3684721950

    override fun onResume(owner: LifecycleOwner) {
        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        Utils.getApp().applicationContext.registerReceiver(mNetworkStateReceive, intentFilter)
    }

    override fun onPause(owner: LifecycleOwner) {
        Utils.getApp().applicationContext.unregisterReceiver(mNetworkStateReceive)
    }

}
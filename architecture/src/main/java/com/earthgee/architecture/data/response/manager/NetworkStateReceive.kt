package com.earthgee.architecture.data.response.manager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import com.earthgee.architecture.utils.NetworkUtils
import com.earthgee.architecture.utils.ToastUtils

/**
 *  Created by earthgee on 2024/1/2
 *  CopyRight (c) earthgee.com
 *  功能：
 */
class NetworkStateReceive : BroadcastReceiver(){

    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent?.action == ConnectivityManager.CONNECTIVITY_ACTION) {
            if(!NetworkUtils.isConnected()) {
                ToastUtils.showShortToast("网络不给力")
            }
        }
    }

}
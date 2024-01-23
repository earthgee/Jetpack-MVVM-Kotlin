package com.earthgee.architecture.ui.page

import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import androidx.lifecycle.ViewModel
import com.earthgee.architecture.data.response.manager.NetworkStateManager
import com.earthgee.architecture.utils.AdaptScreenUtils
import com.earthgee.architecture.utils.BarUtils
import com.earthgee.architecture.utils.ScreenUtils
import com.kunminx.architecture.ui.page.DataBindingActivity
import com.kunminx.architecture.ui.page.DataBindingConfig
import com.kunminx.architecture.ui.scope.ViewModelScope

/**
 *  Created by earthgee on 2024/1/2
 *  CopyRight (c) earthgee.com
 *  功能：
 */
abstract class BaseActivity : DataBindingActivity(){

    protected val mViewModelScope by lazy {
        ViewModelScope()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        BarUtils.setStatusBarColor(this, Color.TRANSPARENT)
        BarUtils.setStatusBarLightMode(this, true)

        super.onCreate(savedInstanceState)

        lifecycle.addObserver(NetworkStateManager)

        //TODO tip 1: DataBinding 严格模式（详见 DataBindingActivity - - - - - ）：
        // 将 DataBinding 实例限制于 base 页面中，默认不向子类暴露，
        // 通过这方式，彻底解决 View 实例 Null 安全一致性问题，
        // 如此，View 实例 Null 安全性将和基于函数式编程思想的 Jetpack Compose 持平。

        // 如这么说无体会，详见 https://xiaozhuanlan.com/topic/9816742350 和 https://xiaozhuanlan.com/topic/2356748910
    }

    //TODO tip 2: Jetpack 通过 "工厂模式" 实现 ViewModel 作用域可控，
    //目前我们在项目中提供了 Application、Activity、Fragment 三个级别的作用域，
    //值得注意的是，通过不同作用域 Provider 获得 ViewModel 实例非同一个，
    //故若 ViewModel 状态信息保留不符合预期，可从该角度出发排查 是否眼前 ViewModel 实例非目标实例所致。

    //如这么说无体会，详见 https://xiaozhuanlan.com/topic/6257931840

    protected inline fun <reified T : ViewModel> getActivityScopeViewModel(): T =
        mViewModelScope.getActivityScopeViewModel(this, T::class.java)

    protected inline fun <reified T : ViewModel> getApplicationScopeViewModel(): T =
        mViewModelScope.getApplicationScopeViewModel(T::class.java)

    override fun getResources(): Resources {
        if(ScreenUtils.isPortrait()) {
            return AdaptScreenUtils.adaptWidth(super.getResources(), 360)
        } else {
            return AdaptScreenUtils.adaptHeight(super.getResources(), 640)
        }
    }

}
package com.earthgee.puremusic.ui.page

import android.os.Bundle
import android.view.View
import com.earthgee.architecture.ui.page.BaseFragment
import com.earthgee.architecture.ui.page.StateHolder
import com.earthgee.puremusic.R
import com.earthgee.puremusic.BR
import com.earthgee.puremusic.data.bean.LibraryInfo
import com.earthgee.puremusic.data.config.Const
import com.earthgee.puremusic.domain.request.InfoRequester
import com.earthgee.puremusic.ui.page.adapter.DrawerAdapter
import com.kunminx.architecture.ui.page.DataBindingConfig
import com.kunminx.architecture.ui.state.State

/**
 *  Created by earthgee on 2024/1/4
 *  CopyRight (c) earthgee.com
 *  功能：
 */
class DrawerFragment : BaseFragment() {

    //TODO tip 1：基于 "单一职责原则"，应将 ViewModel 划分为 state-ViewModel 和 result-ViewModel，
    // state-ViewModel 职责仅限于托管、保存和恢复本页面 state，作用域仅限于本页面，
    // result-ViewModel 职责仅限于 "消息分发" 场景承担 "可信源"，作用域依 "数据请求" 或 "跨页通信" 消息分发范围而定

    // 如这么说无体会，详见 https://xiaozhuanlan.com/topic/8204519736

    private lateinit var mStates: DrawerStates
    private lateinit var mInfoRequester: InfoRequester

    override fun initViewModel() {
        mStates = getFragmentScopeViewModel()
        mInfoRequester = getFragmentScopeViewModel()
    }

    override fun getDataBindingConfig(): DataBindingConfig {
        //TODO tip 2: DataBinding 严格模式：
        // 将 DataBinding 实例限制于 base 页面中，默认不向子类暴露，
        // 通过这方式，彻底解决 View 实例 Null 安全一致性问题，
        // 如此，View 实例 Null 安全性将和基于函数式编程思想的 Jetpack Compose 持平。
        // 而 DataBindingConfig 就是在这样背景下，用于为 base 页面 DataBinding 提供绑定项。

        // 如这么说无体会，详见 https://xiaozhuanlan.com/topic/9816742350 和 https://xiaozhuanlan.com/topic/2356748910
        return DataBindingConfig(R.layout.fragment_drawer, BR.vm, mStates)
            .addBindingParam(BR.click, ClickProxy())
            .addBindingParam(BR.adapter, DrawerAdapter(requireContext()))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //TODO tip 3: 从 PublishSubject 接收回推的数据，并在回调中响应数据的变化，
        // 也即通过 BehaviorSubject（例如 ObservableField）通知控件属性重新渲染，并为其兜住最后一次状态，

        //如这么说无体会，详见 https://xiaozhuanlan.com/topic/6741932805

        mInfoRequester.mLibraryResult.observe(viewLifecycleOwner) { dataResult ->
            if(!dataResult.mResponseStatus.success) return@observe
            if(dataResult.mEntity != null) {
                mStates.list.set(dataResult.mEntity)
            }
        }

        mInfoRequester.requestLibraryInfo()

    }

    inner class ClickProxy {

        fun logoClick() {
            openUrlInBrowser(Const.PROJECT_LINK)
        }

    }

    class DrawerStates : StateHolder() {

        val list = State<List<LibraryInfo>>(arrayListOf())

    }

}
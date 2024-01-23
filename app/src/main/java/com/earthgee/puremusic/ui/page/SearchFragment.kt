package com.earthgee.puremusic.ui.page

import android.os.Bundle
import android.view.View
import com.earthgee.architecture.ui.page.BaseFragment
import com.earthgee.architecture.ui.page.StateHolder
import com.earthgee.puremusic.BR
import com.earthgee.puremusic.R
import com.earthgee.puremusic.data.config.Const
import com.earthgee.puremusic.domain.event.DownloadEvent
import com.earthgee.puremusic.domain.message.DrawerCoordinateManager
import com.earthgee.puremusic.domain.request.DownloadRequester
import com.kunminx.architecture.ui.page.DataBindingConfig
import com.kunminx.architecture.ui.state.State

/**
 *  Created by earthgee on 2024/1/8
 *  CopyRight (c) earthgee.com
 *  功能：
 */
class SearchFragment: BaseFragment() {

    //TODO tip 1：基于 "单一职责原则"，应将 ViewModel 划分为 state-ViewModel 和 result-ViewModel，
    // state-ViewModel 职责仅限于托管、保存和恢复本页面 state，作用域仅限于本页面，
    // result-ViewModel 职责仅限于 "消息分发" 场景承担 "可信源"，作用域依 "数据请求" 或 "跨页通信" 消息分发范围而定

    // 如这么说无体会，详见 https://xiaozhuanlan.com/topic/8204519736
    private lateinit var mStates: SearchStates
    private lateinit var mDownloadRequester: DownloadRequester
    private lateinit var mGlobalDownloadRequester: DownloadRequester

    override fun initViewModel() {
        mStates = getFragmentScopeViewModel()
        mDownloadRequester = getFragmentScopeViewModel()
        mGlobalDownloadRequester = getActivityScopeViewModel()
    }

    override fun getDataBindingConfig(): DataBindingConfig {
        //TODO tip 2: DataBinding 严格模式：
        // 将 DataBinding 实例限制于 base 页面中，默认不向子类暴露，
        // 通过这方式，彻底解决 View 实例 Null 安全一致性问题，
        // 如此，View 实例 Null 安全性将和基于函数式编程思想的 Jetpack Compose 持平。
        // 而 DataBindingConfig 就是在这样背景下，用于为 base 页面 DataBinding 提供绑定项。

        // 如这么说无体会，详见 https://xiaozhuanlan.com/topic/9816742350 和 https://xiaozhuanlan.com/topic/2356748910

        return DataBindingConfig(R.layout.fragment_search, BR.vm, mStates)
            .addBindingParam(BR.click, ClickProxy())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycle.addObserver(DrawerCoordinateManager)
        //TODO tip 3：绑定跟随视图控制器生命周期、可叫停、单独放在 UseCase 中处理的业务
        lifecycle.addObserver(mDownloadRequester)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //TODO tip 8: 此处演示使用 MVI-Dispatcher input-output 接口完成数据请求响应

        //如这么说无体会，详见《领域层设计》篇拆解 https://juejin.cn/post/7117498113983512589
        mDownloadRequester.output(this) { downloadEvent ->
            if(downloadEvent.eventId == DownloadEvent.EVENT_DOWNLOAD) {
                val state = downloadEvent.downloadState
                mStates.progress_cancelable.set(state.progress)
                mStates.enableDownload.set(state.progress == 100 || state.progress == 0)
            }
        }

        //TODO tip 9: 此处演示 "同一 Result-ViewModel 类，在不同作用域下实例化，造成的不同结果"
        mGlobalDownloadRequester.output(this) { downloadEvent ->
            if(downloadEvent.eventId == DownloadEvent.EVENT_DOWNLOAD_GLOBAL) {
                val state = downloadEvent.downloadState
                mStates.progress.set(state.progress)
                mStates.enableGlobalDownload.set(state.progress == 100 || state.progress == 0)
            }
        }

    }

    // TODO tip 4：此处通过 DataBinding 规避 setOnClickListener 时存在的 View 实例 Null 安全一致性问题，

    // 也即，有视图就绑定，无就无绑定，总之 不会因不一致性造成 View 实例 Null 安全问题。
    // 如这么说无体会，详见 https://xiaozhuanlan.com/topic/9816742350

    inner class ClickProxy {

        fun back() {
            nav().navigateUp()
        }

        fun testNav() {
            openUrlInBrowser(Const.COLUMN_LINK)
        }

        fun subscribe() {
            openUrlInBrowser(Const.COLUMN_LINK)
        }

        //TODO tip: 同 tip 8

        fun testDownload() {
            mGlobalDownloadRequester.input(DownloadEvent(DownloadEvent.EVENT_DOWNLOAD_GLOBAL))
        }

        //TODO tip 5: 在 UseCase 中 执行可跟随生命周期中止的下载任务
        fun testLifecycleDownload() {
            mDownloadRequester.input(DownloadEvent(DownloadEvent.EVENT_DOWNLOAD))
        }

    }

    //TODO tip 6：基于单一职责原则，抽取 Jetpack ViewModel "状态保存和恢复" 的能力作为 StateHolder，
    // 并使用 ObservableField 的改良版子类 State 来承担 BehaviorSubject，用作所绑定控件的 "可信数据源"，
    // 从而在收到来自 PublishSubject 的结果回推后，响应结果数据的变化，也即通知控件属性重新渲染，并为其兜住最后一次状态，

    //如这么说无体会，详见 https://xiaozhuanlan.com/topic/6741932805

    class SearchStates: StateHolder() {

        val progress = State(1)
        val progress_cancelable = State(1)
        val enableDownload = State(true)
        val enableGlobalDownload = State(true)

    }

}
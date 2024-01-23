package com.earthgee.puremusic

import android.os.Bundle
import android.view.View
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.Navigation
import com.earthgee.architecture.ui.page.BaseActivity
import com.earthgee.architecture.ui.page.StateHolder
import com.earthgee.puremusic.domain.event.Messages
import com.earthgee.puremusic.domain.message.DrawerCoordinateManager
import com.earthgee.puremusic.domain.message.PageMessenger
import com.earthgee.puremusic.domain.proxy.PlayerManager
import com.kunminx.architecture.ui.page.DataBindingConfig
import com.kunminx.architecture.ui.state.State

/**
 *  Created by earthgee on 2024/1/2
 *  CopyRight (c) earthgee.com
 *  功能：
 */
class MainActivity: BaseActivity(){

    //TODO tip 1：基于 "单一职责原则"，应将 ViewModel 划分为 state-ViewModel 和 result-ViewModel，
    // state-ViewModel 职责仅限于托管、保存和恢复本页面 state，作用域仅限于本页面，
    // result-ViewModel 职责仅限于 "消息分发" 场景承担 "可信源"，作用域依 "数据请求" 或 "跨页通信" 消息分发范围而定

    // 如这么说无体会，详见 https://xiaozhuanlan.com/topic/8204519736

    private lateinit var mStates: MainActivityStates

    private lateinit var mMessenger: PageMessenger

    private var mIsListened = false

    override fun initViewModel() {
        mStates = getActivityScopeViewModel()
        mMessenger = getApplicationScopeViewModel()
    }

    override fun getDataBindingConfig(): DataBindingConfig {
        //TODO tip 2: DataBinding 严格模式：
        // 将 DataBinding 实例限制于 base 页面中，默认不向子类暴露，
        // 通过这方式，彻底解决 View 实例 Null 安全一致性问题，
        // 如此，View 实例 Null 安全性将和基于函数式编程思想的 Jetpack Compose 持平。
        // 而 DataBindingConfig 就是在这样背景下，用于为 base 页面 DataBinding 提供绑定项。

        // 如这么说无体会，详见 https://xiaozhuanlan.com/topic/9816742350 和 https://xiaozhuanlan.com/topic/2356748910

        return DataBindingConfig(R.layout.activity_main, BR.vm, mStates)
            .addBindingParam(BR.listener, ListenerHandler())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        PlayerManager.init(this)

        //TODO tip 6: 从 PublishSubject 接收回推的数据，并在回调中响应数据的变化，
        // 也即通过 BehaviorSubject（例如 ObservableField）通知控件属性重新渲染，并为其兜住最后一次状态，

        //如这么说无体会，详见 https://xiaozhuanlan.com/topic/6741932805
        mMessenger.output(this) { messages ->
            when(messages.eventId) {
                Messages.EVENT_CLOSE_ACTIVITY_IF_ALLOWED -> {
                    val nav = Navigation.findNavController(this, R.id.main_fragment_host)
                    if(nav.currentDestination != null && nav.currentDestination?.id != R.id.mainFragment) {
                        nav.navigateUp()
                    } else if(mStates.isDrawerOpened.get() == true){
                        //TODO 同 tip 3
                        mStates.openDrawer.set(false)
                    } else {
                        super.onBackPressed()
                    }
                }
                Messages.EVENT_OPEN_DRAWER -> {
                    //TODO yes：同 tip 2:
                    // 此处将 drawer 的 open 和 close 都放在 drawerBindingAdapter 中操作，
                    // 规避 View 实例 Null 安全一致性问题，因为横屏布局无 drawerLayout。
                    // 此处如果用手动判空，很容易因疏忽而造成空引用。

                    //TODO 此外，此处为 drawerLayout 绑定状态 "openDrawer"，使用 "去防抖" ObservableField 子类，
                    // 主要考虑到 ObservableField 具有 "防抖" 特性，不适合该场景。

                    //如这么说无体会，详见 https://xiaozhuanlan.com/topic/9816742350
                    mStates.openDrawer.set(true)
                }
            }
        }

        DrawerCoordinateManager.enableSwipeDrawer.observe(this) {
            //TODO yes: 同 tip 2
            mStates.allowDrawerOpen.set(it)
        }

    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if(!mIsListened) {

            // TODO tip 3：此处演示向 "可信源" 发送请求，以便实现 "生命周期安全、消息分发可靠一致" 的通知。

            // 如这么说无体会，详见 https://xiaozhuanlan.com/topic/0168753249
            // --------
            // 与此同时，此处传达的另一思想是 "最少知道原则"，
            // Activity 内部事情在 Activity 内部消化，不要试图在 fragment 中调用和操纵 Activity 内部东西。
            // 因为 Activity 端的处理后续可能会改变，且可受用于更多 fragment，而不单单是本 fragment。
            mMessenger.input(Messages(Messages.EVENT_ADD_SLIDE_LISTENER))

            mIsListened = true
        }
    }

    override fun onBackPressed() {
        // TODO 同 tip 3
        mMessenger.input(Messages(Messages.EVENT_CLOSE_SLIDE_PANEL_IF_EXPANDED))
    }

    inner class ListenerHandler: DrawerLayout.SimpleDrawerListener() {

        override fun onDrawerOpened(drawerView: View) {
            super.onDrawerOpened(drawerView)
            mStates.isDrawerOpened.set(true)
        }

        override fun onDrawerClosed(drawerView: View) {
            super.onDrawerClosed(drawerView)
            mStates.isDrawerOpened.set(false)
            mStates.openDrawer.set(false)
        }

    }

    class MainActivityStates: StateHolder() {

        val isDrawerOpened = State<Boolean>(false)

        val openDrawer = State<Boolean>(false)

        val allowDrawerOpen = State<Boolean>(true)

    }

}
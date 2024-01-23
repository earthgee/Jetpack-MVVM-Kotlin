package com.earthgee.puremusic.ui.page

import android.os.Bundle
import android.view.View
import com.earthgee.architecture.ui.page.BaseFragment
import com.earthgee.architecture.ui.page.StateHolder
import com.earthgee.puremusic.R
import com.earthgee.puremusic.BR
import com.earthgee.puremusic.data.bean.TestMusic
import com.earthgee.puremusic.domain.event.Messages
import com.earthgee.puremusic.domain.message.PageMessenger
import com.earthgee.puremusic.domain.proxy.PlayerManager
import com.earthgee.puremusic.domain.request.MusicRequester
import com.earthgee.puremusic.ui.page.adapter.PlayListAdapter
import com.kunminx.architecture.ui.page.DataBindingConfig
import com.kunminx.architecture.ui.state.State

/**
 *  Created by earthgee on 2024/1/4
 *  CopyRight (c) earthgee.com
 *  功能：
 */
class MainFragment: BaseFragment() {

    //TODO tip 1：基于 "单一职责原则"，应将 ViewModel 划分为 state-ViewModel 和 result-ViewModel，
    // state-ViewModel 职责仅限于托管、保存和恢复本页面 state，作用域仅限于本页面，
    // result-ViewModel 职责仅限于 "消息分发" 场景承担 "可信源"，作用域依 "数据请求" 或 "跨页通信" 消息分发范围而定

    // 如这么说无体会，详见 https://xiaozhuanlan.com/topic/8204519736

    private lateinit var mStates: MainStates
    private lateinit var mMessenger: PageMessenger
    private lateinit var mMusicRequester: MusicRequester
    private lateinit var mAdapter: PlayListAdapter

    override fun initViewModel() {
        mStates = getFragmentScopeViewModel()
        mMessenger = getApplicationScopeViewModel()
        mMusicRequester = getFragmentScopeViewModel()
    }

    override fun getDataBindingConfig(): DataBindingConfig {
        mAdapter = PlayListAdapter(requireContext())
        mAdapter.setOnItemClickListener { viewId, item, position ->
            PlayerManager.playAudio(position)
        }

        //TODO tip 2: DataBinding 严格模式：
        // 将 DataBinding 实例限制于 base 页面中，默认不向子类暴露，
        // 通过这方式，彻底解决 View 实例 Null 安全一致性问题，
        // 如此，View 实例 Null 安全性将和基于函数式编程思想的 Jetpack Compose 持平。
        // 而 DataBindingConfig 就是在这样背景下，用于为 base 页面 DataBinding 提供绑定项。

        // 如这么说无体会，详见 https://xiaozhuanlan.com/topic/9816742350 和 https://xiaozhuanlan.com/topic/2356748910
        return DataBindingConfig(R.layout.fragment_main, BR.vm, mStates)
            .addBindingParam(BR.click, ClickProxy())
            .addBindingParam(BR.adapter, mAdapter)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        PlayerManager.uiStates.observe(viewLifecycleOwner) { uiStates ->
            mStates.musicId.set(uiStates.musicId) {
                mAdapter.notifyDataSetChanged()
            }
        }

        //TODO tip 4:
        // getViewLifeCycleOwner 是 2020 年新增特性，
        // 主要为了解决 getView() 生命长度 比 fragment 短（仅存活于 onCreateView 之后和 onDestroyView 之前），
        // 导致某些时候 fragment 其他成员还活着，但 getView() 为 null 的 生命周期安全问题，
        // 也即，在 fragment 场景下，请使用 getViewLifeCycleOwner 作为 liveData 观察者。
        // Activity 则不用改变。
        mMusicRequester.freeMusicsResult.observe(viewLifecycleOwner) { dataResult ->
            if(!dataResult.mResponseStatus.success) return@observe

            val musicAlbum = dataResult.mEntity

            // TODO tip 5：未作 UnPeek 处理的 LiveData，在视图控制器重建时会自动倒灌数据
            // 请记得这一点，因为如果没有妥善处理，这里就可能出现预期外错误（例如收到旧数据推送），
            // 所以，再一次，请记得它在重建时一定会倒灌。

            // 如这么说无体会，详见 https://xiaozhuanlan.com/topic/6719328450
            mStates.list.set(musicAlbum.musics)
            PlayerManager.loadAlbum(musicAlbum)
        }

        mMessenger.output(this) { messages ->
            if(messages.eventId == Messages.EVENT_LOGIN_SUCCESS) {
                //TODO tip:
                //loginFragment 登录成功后的后续处理，例如刷新页面状态等
            }
        }

        //request data
        mMusicRequester.requestFreeMusic()
    }

    // TODO tip 7：此处通过 DataBinding 规避 setOnClickListener 时存在的 View 实例 Null 安全一致性问题，

    // 也即，有视图就绑定，无就无绑定，总之 不会因不一致性造成 View 实例 Null 安全问题。
    // 如这么说无体会，详见 https://xiaozhuanlan.com/topic/9816742350

    inner class ClickProxy {

        fun openMenu() {
            // TODO tip 8：此处演示向 "可信源" 发送请求，以便实现 "生命周期安全、消息分发可靠一致" 的通知。

            // 如这么说无体会，详见 https://xiaozhuanlan.com/topic/6017825943 & https://juejin.cn/post/7117498113983512589
            // --------
            // 与此同时，此处传达的另一思想是 "最少知道原则"，
            // Activity 内部事情在 Activity 内部消化，不要试图在 fragment 中调用和操纵 Activity 内部东西。
            // 因为 Activity 端的处理后续可能会改变，且可受用于更多 fragment，而不单单是本 fragment。

            mMessenger.input(Messages(Messages.EVENT_OPEN_DRAWER))
        }

        fun login() {
            nav().navigate(R.id.action_mainFragment_to_loginFragment)
        }

        fun search() {
            nav().navigate(R.id.action_mainFragment_to_searchFragment)
        }

    }

    //TODO tip 9：每个页面都需单独准备一个 state-ViewModel，托管与 "控件属性" 发生绑定的 State，
    // 此外，state-ViewModel 职责仅限于状态托管和保存恢复，不建议在此处理 UI 逻辑，

    // UI 逻辑和业务逻辑，本质区别在于，前者是数据的消费者，后者是数据的生产者，
    // 数据总是来自领域层业务逻辑的处理，并单向回推至 UI 层，在 UI 层中响应数据的变化（也即处理 UI 逻辑），
    // 换言之，UI 逻辑只适合在 Activity/Fragment 等视图控制器中编写，将来升级到 Jetpack Compose 更是如此。

    //如这么说无体会，详见 https://xiaozhuanlan.com/topic/6741932805

    class MainStates: StateHolder() {

        //TODO tip 10：此处我们使用 "去除防抖特性" 的 ObservableField 子类 State，用以代替 MutableLiveData，

        //如这么说无体会，详见 https://xiaozhuanlan.com/topic/9816742350
        val musicId = State<String>("", true)
        val initTabAndPage = State<Boolean>(true)

        val pageAssetPath = State<String>("summary.html")
        val list = State<List<TestMusic>>(arrayListOf())

    }

}






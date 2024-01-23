package com.earthgee.puremusic.ui.page

import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import com.earthgee.architecture.ui.page.BaseFragment
import com.earthgee.architecture.ui.page.StateHolder
import com.earthgee.architecture.utils.Res
import com.earthgee.architecture.utils.ToastUtils
import com.earthgee.puremusic.R
import com.earthgee.puremusic.BR
import com.earthgee.puremusic.databinding.FragmentPlayerBinding
import com.earthgee.puremusic.domain.event.Messages
import com.earthgee.puremusic.domain.message.DrawerCoordinateManager
import com.earthgee.puremusic.domain.message.PageMessenger
import com.earthgee.puremusic.domain.proxy.PlayerManager
import com.earthgee.puremusic.ui.page.helper.OnSeekbarChangeListener
import com.earthgee.puremusic.ui.view.PlayerSlideListener
import com.kunminx.architecture.ui.page.DataBindingConfig
import com.kunminx.architecture.ui.state.State
import com.kunminx.player.domain.PlayingInfoManager
import com.kunminx.player.domain.PlayingInfoManager.RepeatMode
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import com.sothree.slidinguppanel.SlidingUpPanelLayout.SimplePanelSlideListener

/**
 *  Created by earthgee on 2024/1/8
 *  CopyRight (c) earthgee.com
 *  功能：
 */
class PlayerFragment : BaseFragment() {

    //TODO tip 1：基于 "单一职责原则"，应将 ViewModel 划分为 state-ViewModel 和 result-ViewModel，
    // state-ViewModel 职责仅限于托管、保存和恢复本页面 state，作用域仅限于本页面，
    // result-ViewModel 职责仅限于 "消息分发" 场景承担 "可信源"，作用域依 "数据请求" 或 "跨页通信" 消息分发范围而定

    // 如这么说无体会，详见 https://xiaozhuanlan.com/topic/8204519736

    private lateinit var mStates: PlayerStates
    private lateinit var mAnimatorStates: PlayerSlideListener.SlideAnimatorStates
    private lateinit var mMessenger: PageMessenger
    private lateinit var mListener: PlayerSlideListener

    override fun initViewModel() {
        mStates = getFragmentScopeViewModel()
        mAnimatorStates = getFragmentScopeViewModel()
        mMessenger = getApplicationScopeViewModel()
    }

    override fun getDataBindingConfig(): DataBindingConfig {
        //TODO tip 2: DataBinding 严格模式：
        // 将 DataBinding 实例限制于 base 页面中，默认不向子类暴露，
        // 通过这方式，彻底解决 View 实例 Null 安全一致性问题，
        // 如此，View 实例 Null 安全性将和基于函数式编程思想的 Jetpack Compose 持平。
        // 而 DataBindingConfig 就是在这样背景下，用于为 base 页面 DataBinding 提供绑定项。

        // 如这么说无体会，详见 https://xiaozhuanlan.com/topic/9816742350 和 https://xiaozhuanlan.com/topic/2356748910
        return DataBindingConfig(R.layout.fragment_player, BR.vm, mStates)
            .addBindingParam(BR.panelVm, mAnimatorStates)
            .addBindingParam(BR.click, ClickProxy())
            .addBindingParam(BR.listener, ListenerHandler())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //TODO tip 3: 此处演示使用 "可信源" MVI-Dispatcher input-output 接口完成消息收发

        //如这么说无体会，详见《领域层设计》篇拆解 https://juejin.cn/post/7117498113983512589

        mMessenger.output(this) { messages ->
            when (messages.eventId) {
                Messages.EVENT_ADD_SLIDE_LISTENER -> {
                    if (view.parent.parent is SlidingUpPanelLayout) {
                        val sliding = view.parent.parent as SlidingUpPanelLayout

                        //TODO tip 4: 警惕使用。非必要情况下，尽可能不在子类中拿到 binding 实例乃至获取 view 实例。使用即埋下隐患。
                        // 目前方案是于 debug 模式，对获取实例情况给予提示。

                        // 如这么说无体会，详见 https://xiaozhuanlan.com/topic/9816742350 和 https://xiaozhuanlan.com/topic/2356748910
                        mListener = PlayerSlideListener(
                            binding as FragmentPlayerBinding,
                            mAnimatorStates,
                            sliding
                        )
                        sliding.addPanelSlideListener(mListener)
                        sliding.addPanelSlideListener(object : SimplePanelSlideListener() {

                            override fun onPanelStateChanged(
                                panel: View?,
                                previousState: SlidingUpPanelLayout.PanelState?,
                                newState: SlidingUpPanelLayout.PanelState?
                            ) {
                                newState ?: return
                                DrawerCoordinateManager.requestToUpdateDrawerMode(
                                    newState == SlidingUpPanelLayout.PanelState.EXPANDED,
                                    javaClass.simpleName
                                )
                            }

                        })
                    }
                }
                Messages.EVENT_CLOSE_SLIDE_PANEL_IF_EXPANDED -> {
                    // 按下返回键，如果此时 slide 面板是展开的，那么只对面板进行 slide down

                    if (view.parent.parent is SlidingUpPanelLayout) {
                        val sliding = view.parent.parent as SlidingUpPanelLayout
                        if (sliding.panelState == SlidingUpPanelLayout.PanelState.EXPANDED) {
                            sliding.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
                        } else {
                            // TODO tip 5：此处演示向 "可信源" 发送请求，以便实现 "生命周期安全、消息分发可靠一致" 的通知。

                            // 如这么说无体会，详见 https://xiaozhuanlan.com/topic/0168753249
                            // --------
                            // 与此同时，此处传达的另一思想是 "最少知道原则"，
                            // Activity 内部事情在 Activity 内部消化，不要试图在 fragment 中调用和操纵 Activity 内部东西。
                            // 因为 Activity 端的处理后续可能会改变，且可受用于更多 fragment，而不单单是本 fragment。

                            // TODO: yes:

                            mMessenger.input(Messages(Messages.EVENT_CLOSE_ACTIVITY_IF_ALLOWED))
                        }
                    } else {
                        mMessenger.input(Messages(Messages.EVENT_CLOSE_ACTIVITY_IF_ALLOWED))
                    }
                }
            }
        }

        // TODO tip 6：所有播放状态的改变，皆来自 getUiStates() 统一分发，
        //  确保 "消息分发可靠一致"，避免不可预期推送和错误，

        // 细节 1： uiStates 回调只读，此处只可通过 getter 获取只读数据，避免数据被篡改，
        // 细节 2： uiStates 每次都是整个推送，progress 等属性会造成 uiStates 的高频回推，
        //         故此宜对低频变化属性做防抖处理，仅当属性值变化时，通知相关控件完成一次重绘，

        PlayerManager.uiStates.observe(viewLifecycleOwner) { uiStates ->
            mStates.musicId.set(uiStates.musicId) { changed ->
                mStates.title.set(uiStates.title)
                mStates.artist.set(uiStates.summary)
                mStates.coverImg.set(uiStates.img)
                mStates.maxSeekDuration.set(uiStates.duration)
                if(::mListener.isInitialized) {
                    view.post(mListener::calculateTitleAndArtist)
                }
            }
            mStates.currentSeekPosition.set(uiStates.progress)
            mStates.isPlaying.set(!uiStates.isPaused)
            mStates.repeatMode.set(uiStates.repeatMode) { changed ->
                mStates.playModeIcon.set(PlayerManager.getModeIcon(uiStates.repeatMode))
            }
        }

    }

    // TODO tip 7：此处通过 DataBinding 规避 setOnClickListener 时存在的 View 实例 Null 安全一致性问题，

    // 也即，有视图就绑定，无就无绑定，总之 不会因不一致性造成 View 实例 Null 安全问题。
    // 如这么说无体会，详见 https://xiaozhuanlan.com/topic/9816742350
    inner class ClickProxy {

        fun slideDown() {
            mMessenger.input(Messages(Messages.EVENT_CLOSE_SLIDE_PANEL_IF_EXPANDED))
        }

        fun more() {

        }

        fun next() {
            PlayerManager.playNext()
        }

        fun togglePlay() {
            PlayerManager.togglePlay()
        }

        fun previous() {
            PlayerManager.playPrevious()
        }

        fun playMode() {
            PlayerManager.changeMode()
        }

        fun showPlayList() {
            ToastUtils.showShortToast("暂不支持该功能")
        }

    }

    class ListenerHandler : OnSeekbarChangeListener() {

        override fun onStopTrackingTouch(seekBar: SeekBar?) {
            PlayerManager.setSeek(seekBar?.progress ?: 0)
        }

    }

    //TODO tip 8：基于单一职责原则，抽取 Jetpack ViewModel "状态保存和恢复" 的能力作为 StateHolder，
    // 并使用 ObservableField 的改良版子类 State 来承担 BehaviorSubject，用作所绑定控件的 "可信数据源"，
    // 从而在收到来自 PublishSubject 的结果回推后，响应结果数据的变化，也即通知控件属性重新渲染，并为其兜住最后一次状态，

    //如这么说无体会，详见 https://xiaozhuanlan.com/topic/6741932805

    class PlayerStates : StateHolder() {

        val musicId = State("", true)
        val repeatMode: State<Enum<RepeatMode>> =
            State(PlayingInfoManager.RepeatMode.LIST_CYCLE, true)
        val title = State("PureMusic", true)
        val artist = State("PureMusic", true)
        val coverImg = State("", true)
        val placeHolder = State(Res.getDrawable(R.drawable.bg_album_default), true)
        val maxSeekDuration = State(0, true)
        val currentSeekPosition = State(0, true)
        val isPlaying = State(false, true)
        val playModeIcon = State(PlayerManager.getModeIcon(), true)

    }

}




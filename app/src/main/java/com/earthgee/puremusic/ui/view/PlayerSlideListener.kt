package com.earthgee.puremusic.ui.view

import android.animation.ArgbEvaluator
import android.animation.FloatEvaluator
import android.animation.IntEvaluator
import android.graphics.Color
import android.graphics.Rect
import android.util.Log
import android.view.View
import android.widget.TextView
import com.earthgee.architecture.ui.page.StateHolder
import com.earthgee.architecture.utils.DisplayUtils
import com.earthgee.architecture.utils.ScreenUtils
import com.earthgee.puremusic.databinding.FragmentPlayerBinding
import com.kunminx.architecture.ui.state.State
import com.sothree.slidinguppanel.SlidingUpPanelLayout

/**
 *  Created by earthgee on 2024/1/8
 *  CopyRight (c) earthgee.com
 *  功能：
 */
class PlayerSlideListener(
    val mBinding: FragmentPlayerBinding,
    val mStates: SlideAnimatorStates,
    val mSlidingUpPanelLayout: SlidingUpPanelLayout
) : SlidingUpPanelLayout.PanelSlideListener {

    private val SCREEN_WIDTH: Int
    private val SCREEN_HEIGHT: Int

    private val NOW_PLAYING_CARD_COLOR: Int
    private val PLAY_PAUSE_DRAWABLE_COLOR: Int

    private val INT_EVALUATOR = IntEvaluator()
    private val FLOAT_EVALUATOR = FloatEvaluator()
    private val COLOR_EVALUATOR = ArgbEvaluator()

    private var mTitleEndTranslationX = 0
    private var mArtistEndTranslationX = 0
    private var mArtistNormalEndTranslationY = 0
    private var mContentNormalEndTranslationY = 0

    private var mModeStartX = 0
    private var mPreviousStartX = 0
    private var mPlayPauseStartX = 0
    private var mNextStartX = 0
    private var mPlayQueueStartX = 0
    private var mModeEndX = 0
    private var mPreviousEndX = 0
    private var mPlayPauseEndX = 0
    private var mNextEndX = 0
    private var mPlayQueueEndX = 0

    private var mIconContainerStartY = 0
    private var mIconContainerEndY = 0

    private var mStatus = Status.COLLAPSED

    enum class Status {
        EXPANDED,
        COLLAPSED
    }

    init {
        SCREEN_WIDTH = ScreenUtils.getScreenWidth()
        SCREEN_HEIGHT = ScreenUtils.getScreenHeight()
        PLAY_PAUSE_DRAWABLE_COLOR = Color.BLACK
        NOW_PLAYING_CARD_COLOR = Color.WHITE
        calculateTitleAndArtist()

        mModeStartX = mBinding.mode?.left ?: 0
        mPreviousStartX = mBinding.previous.left
        mPlayPauseStartX = mBinding.playPause.left
        mNextStartX = mBinding.next.left
        mPlayQueueStartX = mBinding.icPlayList?.left ?: 0
        val size = DisplayUtils.dp2px(36f)
        val gap = (SCREEN_WIDTH - 5 * size) / 6
        mPlayPauseEndX = (SCREEN_WIDTH / 2) - (size / 2)
        mPreviousEndX = mPlayPauseEndX - gap - size
        mModeEndX = mPreviousEndX - gap - size
        mNextEndX = mPlayPauseEndX + gap + size
        mPlayQueueEndX = mNextEndX + gap + size
        mIconContainerStartY = mBinding.iconContainer.top

        val tempImgSize = DisplayUtils.dp2px(55f)
        mStates.albumArtSize.set(Pair(tempImgSize, tempImgSize))
        mIconContainerEndY =
            SCREEN_HEIGHT - 3 * mBinding.iconContainer.height - mBinding.seekBottom.height
        mStates.playPauseDrawableColor.set(PLAY_PAUSE_DRAWABLE_COLOR)
        mStates.playCircleAlpha.set(INT_EVALUATOR.evaluate(0f, 0, 255))
        mStates.nextX.set(mNextStartX)
        mStates.modeX.set(0)
        mStates.previousX.set(0)
        mStates.playPauseX.set(mPlayPauseStartX)
        mStates.iconContainerY.set(mIconContainerStartY)
        mBinding.executePendingBindings()
    }

    fun calculateTitleAndArtist() {
        val titleWidth = getTextWidth(mBinding.title)
        val artistWidth = getTextWidth(mBinding.artist)
        mTitleEndTranslationX = (SCREEN_WIDTH / 2) - (titleWidth / 2) - DisplayUtils.dp2px(67f)
        mArtistEndTranslationX = (SCREEN_WIDTH / 2) - (artistWidth / 2) - DisplayUtils.dp2px(67f)
        mArtistNormalEndTranslationY = DisplayUtils.dp2px(12f)
        mContentNormalEndTranslationY = SCREEN_WIDTH + DisplayUtils.dp2px(32f)
        mStates.titleTranslationX.set(if (mStatus == Status.COLLAPSED) 0f else mTitleEndTranslationX.toFloat())
        mStates.artistTranslationX.set(if (mStatus == Status.COLLAPSED) 0f else mArtistEndTranslationX.toFloat())
    }

    private fun getTextWidth(textView: TextView?): Int {
        textView ?: return 0
        val bounds = Rect()
        textView.paint.getTextBounds(textView.text.toString(), 0, textView.text.length, bounds)
        return bounds.width()
    }

    /**
     * TODO tip：使用 ObservableField 绑定，尽可能减少 View 实例 Null 安全一致性问题
     * <p>
     *  如这么说无体会，详见 https://xiaozhuanlan.com/topic/9816742350 和 https://xiaozhuanlan.com/topic/2356748910
     */
    class SlideAnimatorStates : StateHolder() {

        val icPlayListX = State(0)
        val modeX = State(0)
        val modeVisibility = State(false)
        val modeAlpha = State(0f)
        val previousX = State(0)
        val previousVisibility = State(false)
        val previousAlpha = State(0f)
        val playPauseDrawableColor = State(0)
        val playCircleAlpha = State(0)
        val playPauseX = State(0)
        val nextX = State(0)
        val iconContainerY = State(0)
        val songProgressNormalVisibility = State(false)
        val artistTranslationX = State(0f)
        val artistTranslationY = State(0f)
        val titleTranslationX = State(0f)
        val summaryTranslationY = State(0f)
        val customToolbarVisibility = State(false)
        val albumArtSize = State(Pair(0, 0))

    }

    override fun onPanelSlide(panel: View?, slideOffset: Float) {
        calculateTitleAndArtist()
        val tempImgSize = INT_EVALUATOR.evaluate(slideOffset, DisplayUtils.dp2px(55f), SCREEN_WIDTH)
        mStates.albumArtSize.set(Pair(tempImgSize, tempImgSize))
        mStates.titleTranslationX.set(
            FLOAT_EVALUATOR.evaluate(
                slideOffset,
                0,
                mTitleEndTranslationX
            )
        )
        mStates.artistTranslationX.set(
            FLOAT_EVALUATOR.evaluate(
                slideOffset,
                0,
                mArtistEndTranslationX
            )
        )
        mStates.artistTranslationY.set(
            FLOAT_EVALUATOR.evaluate(
                slideOffset,
                0,
                mArtistNormalEndTranslationY
            )
        )
        mStates.summaryTranslationY.set(
            FLOAT_EVALUATOR.evaluate(
                slideOffset,
                0,
                mContentNormalEndTranslationY
            )
        )
        mStates.playPauseX.set(
            INT_EVALUATOR.evaluate(
                slideOffset,
                mPlayPauseStartX,
                mPlayPauseEndX
            )
        )
        mStates.previousX.set(INT_EVALUATOR.evaluate(slideOffset, mPreviousStartX, mPreviousEndX))
        mStates.modeX.set(INT_EVALUATOR.evaluate(slideOffset, mModeStartX, mModeEndX))
        mStates.nextX.set(INT_EVALUATOR.evaluate(slideOffset, mNextStartX, mNextEndX))
        mStates.icPlayListX.set(
            INT_EVALUATOR.evaluate(
                slideOffset,
                mPlayQueueStartX,
                mPlayQueueEndX
            )
        )
        mStates.iconContainerY.set(
            INT_EVALUATOR.evaluate(
                slideOffset,
                mIconContainerStartY,
                mIconContainerEndY
            )
        )

        mStates.playCircleAlpha.set(INT_EVALUATOR.evaluate(slideOffset, 0, 255))
        mStates.playPauseDrawableColor.set(
            COLOR_EVALUATOR.evaluate(
                slideOffset,
                PLAY_PAUSE_DRAWABLE_COLOR,
                NOW_PLAYING_CARD_COLOR
            ) as Int
        )
        mStates.previousAlpha.set(FLOAT_EVALUATOR.evaluate(slideOffset, 0, 1))
        mStates.modeAlpha.set(FLOAT_EVALUATOR.evaluate(slideOffset, 0, 1))
        mBinding.executePendingBindings()
    }

    override fun onPanelStateChanged(
        panel: View?,
        previousState: SlidingUpPanelLayout.PanelState?,
        newState: SlidingUpPanelLayout.PanelState?
    ) {
        if (previousState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
            mStates.songProgressNormalVisibility.set(false)
            mStates.modeVisibility.set(true)
            mStates.previousVisibility.set(true)
        }

        if (newState == SlidingUpPanelLayout.PanelState.EXPANDED) {
            mStatus = Status.EXPANDED
            mStates.customToolbarVisibility.set(true)
        } else if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
            mStatus = Status.COLLAPSED
            mStates.songProgressNormalVisibility.set(true)
            mStates.modeVisibility.set(false)
            mStates.previousVisibility.set(false)
            mBinding.topContainer.setOnClickListener {
                if (mSlidingUpPanelLayout.isTouchEnabled) {
                    mSlidingUpPanelLayout.panelState = SlidingUpPanelLayout.PanelState.EXPANDED
                }
            }
        } else if (newState == SlidingUpPanelLayout.PanelState.DRAGGING) {
            mStates.customToolbarVisibility.set(false)
        }
    }

}






















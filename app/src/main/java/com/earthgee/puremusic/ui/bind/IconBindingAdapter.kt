package com.earthgee.puremusic.ui.bind

import androidx.databinding.BindingAdapter
import com.earthgee.puremusic.ui.view.PlayPauseView
import net.steamcrafted.materialiconlib.MaterialDrawableBuilder.IconValue
import net.steamcrafted.materialiconlib.MaterialIconView

/**
 *  Created by earthgee on 2024/1/8
 *  CopyRight (c) earthgee.com
 *  功能：
 */
object IconBindingAdapter {

    @JvmStatic
    @BindingAdapter(value = ["isPlaying"], requireAll = false)
    fun isPlaying(pauseView: PlayPauseView, isPlaying: Boolean) {
        if (isPlaying) {
            pauseView.play()
        } else {
            pauseView.pause()
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["mdIcon"], requireAll = false)
    fun setIcon(view: MaterialIconView, iconValue: IconValue) {
        view.setIcon(iconValue)
    }

}
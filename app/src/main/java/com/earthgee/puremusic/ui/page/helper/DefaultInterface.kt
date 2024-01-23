package com.earthgee.puremusic.ui.page.helper

import android.widget.SeekBar

/**
 *  Created by earthgee on 2024/1/8
 *  CopyRight (c) earthgee.com
 *  功能：
 */
abstract class OnSeekbarChangeListener: SeekBar.OnSeekBarChangeListener {

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {}

    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

    override fun onStopTrackingTouch(seekBar: SeekBar?) {}

}
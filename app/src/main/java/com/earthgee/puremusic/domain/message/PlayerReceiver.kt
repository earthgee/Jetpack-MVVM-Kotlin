package com.earthgee.puremusic.domain.message

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.view.KeyEvent
import com.earthgee.puremusic.domain.proxy.PlayerManager
import com.earthgee.puremusic.ui.widget.PlayerService

/**
 *  Created by earthgee on 2024/1/15
 *  CopyRight (c) earthgee.com
 *  功能：
 */
class PlayerReceiver : BroadcastReceiver(){

    override fun onReceive(context: Context?, intent: Intent?) {
        context?: return
        intent?: return

        when(intent.action) {
            PlayerService.NOTIFY_PLAY -> {
                PlayerManager.playAudio()
            }
            PlayerService.NOTIFY_PAUSE -> {
                PlayerManager.pauseAudio()
            }
            PlayerService.NOTIFY_NEXT -> {
                PlayerManager.playNext()
            }
            PlayerService.NOTIFY_CLOSE -> {
                PlayerManager.clear()
            }
            PlayerService.NOTIFY_PREVIOUS -> {
                PlayerManager.playPrevious()
            }
        }
    }

}
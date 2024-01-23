package com.earthgee.puremusic.ui.widget

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.view.View
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.earthgee.architecture.domain.usecase.UseCase.UseCaseCallback
import com.earthgee.architecture.domain.usecase.UseCaseHandler
import com.earthgee.architecture.utils.ImageUtils
import com.earthgee.puremusic.MainActivity
import com.earthgee.puremusic.R
import com.earthgee.puremusic.data.bean.TestMusic
import com.earthgee.puremusic.data.config.Const
import com.earthgee.puremusic.domain.proxy.PlayerManager
import com.earthgee.puremusic.domain.usecase.DownloadUseCase
import java.io.File
import kotlin.math.exp

/**
 *  Created by earthgee on 2024/1/12
 *  CopyRight (c) earthgee.com
 *  功能：播放服务
 */
class PlayerService : Service() {

    companion object {

        const val GROUP_ID = "group_001"
        const val CHANNEL_ID = "channel_001"

        const val NOTIFY_PREVIOUS = "pure_music.earthgee.previous"
        const val NOTIFY_CLOSE = "pure_music.earthgee.close"
        const val NOTIFY_PAUSE = "pure_music.earthgee.pause"
        const val NOTIFY_PLAY = "pure_music.earthgee.play"
        const val NOTIFY_NEXT = "pure_music.earthgee.next"

    }

    private val mDownloadUseCase: DownloadUseCase by lazy {
        DownloadUseCase()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val results = PlayerManager.currentPlayingMusic
        if (results == null) {
            stopSelf()
            return START_NOT_STICKY
        }

        createNotification(results)
        return START_NOT_STICKY
    }

    private fun createNotification(testMusic: TestMusic) {
        try {
            val title = testMusic.title
            val album = PlayerManager.album
            val summary = album.summary

            val simpleContentView =
                RemoteViews(applicationContext.packageName, R.layout.notify_player_small)
            val expandedView =
                RemoteViews(applicationContext.packageName, R.layout.notify_player_big)

            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.action = "ShowPlayer"
            val pendingIntent = PendingIntent.getActivity(
                this, 0, intent,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_MUTABLE else 0
            )

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val notificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                val playGroup = NotificationChannelGroup(GROUP_ID, "播放")
                notificationManager.createNotificationChannelGroup(playGroup)

                val playChannel = NotificationChannel(
                    CHANNEL_ID,
                    "播放时的通知栏展示",
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                playChannel.group = GROUP_ID
                notificationManager.createNotificationChannel(playChannel)
            }

            val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_player)
                .setContentIntent(pendingIntent)
                .setOnlyAlertOnce(true)
                .setContentTitle(title)
                .setCustomContentView(simpleContentView)
                .setCustomBigContentView(expandedView)
                .build()

            setListeners(simpleContentView)
            setListeners(expandedView)

            simpleContentView.setViewVisibility(R.id.player_progress_bar, View.GONE)
            simpleContentView.setViewVisibility(R.id.player_next, View.VISIBLE)
            simpleContentView.setViewVisibility(R.id.player_previous, View.VISIBLE)
            expandedView.setViewVisibility(R.id.player_progress_bar, View.GONE)
            expandedView.setViewVisibility(R.id.player_next, View.VISIBLE)
            expandedView.setViewVisibility(R.id.player_previous, View.VISIBLE)

            val isPaused = PlayerManager.isPaused
            simpleContentView.setViewVisibility(R.id.player_pause, if(isPaused) View.GONE else View.VISIBLE)
            simpleContentView.setViewVisibility(R.id.player_play, if(isPaused) View.VISIBLE else View.GONE)
            expandedView.setViewVisibility(R.id.player_pause, if(isPaused) View.GONE else View.VISIBLE)
            expandedView.setViewVisibility(R.id.player_play, if(isPaused) View.VISIBLE else View.GONE)

            simpleContentView.setTextViewText(R.id.player_song_name, title)
            simpleContentView.setTextViewText(R.id.player_author_name, summary)
            expandedView.setTextViewText(R.id.player_song_name, title)
            expandedView.setTextViewText(R.id.player_author_name, summary)
            notification.flags = notification.flags or Notification.FLAG_ONGOING_EVENT

            val coverPath = "${Const.COVER_PATH}${File.separator}${testMusic.musicId}.jpg"
            val bitmap = ImageUtils.getBitmap(coverPath)

            if(bitmap != null) {
                simpleContentView.setImageViewBitmap(R.id.player_album_art, bitmap)
                expandedView.setImageViewBitmap(R.id.player_album_art, bitmap)
            } else {
                requestAlbumCover(testMusic.coverImg, testMusic.musicId)
                simpleContentView.setImageViewResource(R.id.player_album_art, R.drawable.bg_album_default)
                expandedView.setImageViewResource(R.id.player_album_art, R.drawable.bg_album_default)
            }

            startForeground(5, notification)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private fun setListeners(view: RemoteViews) {
        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        else
            PendingIntent.FLAG_UPDATE_CURRENT
        try {
            var pendingIntent = PendingIntent.getBroadcast(
                applicationContext, 0, Intent(
                    NOTIFY_PREVIOUS
                ).setPackage(packageName), flags
            )
            view.setOnClickPendingIntent(R.id.player_previous, pendingIntent)
            pendingIntent = PendingIntent.getBroadcast(
                applicationContext,
                0,
                Intent(NOTIFY_CLOSE).setPackage(packageName),
                flags
            )
            view.setOnClickPendingIntent(R.id.player_close, pendingIntent)
            pendingIntent = PendingIntent.getBroadcast(
                applicationContext,
                0,
                Intent(NOTIFY_NEXT).setPackage(packageName),
                flags
            )
            view.setOnClickPendingIntent(R.id.player_next, pendingIntent)
            pendingIntent = PendingIntent.getBroadcast(
                applicationContext,
                0,
                Intent(NOTIFY_PAUSE).setPackage(packageName),
                flags
            )
            view.setOnClickPendingIntent(R.id.player_pause, pendingIntent)
            pendingIntent = PendingIntent.getBroadcast(
                applicationContext,
                0,
                Intent(NOTIFY_PLAY).setPackage(packageName),
                flags
            )
            view.setOnClickPendingIntent(R.id.player_play, pendingIntent)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private fun requestAlbumCover(coverUrl: String, musicId: String) {
        UseCaseHandler.getInstance().execute(mDownloadUseCase,
            DownloadUseCase.RequestValues(coverUrl, "${musicId}.jpg")
        ) {
            startService(Intent(applicationContext, PlayerService::class.java))
        }
    }

    override fun onBind(intent: Intent?) = null

}






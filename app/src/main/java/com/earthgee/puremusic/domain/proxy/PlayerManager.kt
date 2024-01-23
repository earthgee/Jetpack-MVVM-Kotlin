package com.earthgee.puremusic.domain.proxy

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import com.danikula.videocache.HttpProxyCacheServer
import com.earthgee.puremusic.data.bean.TestAlbum
import com.earthgee.puremusic.data.bean.TestArtist
import com.earthgee.puremusic.data.bean.TestMusic
import com.earthgee.puremusic.ui.widget.PlayerService
import com.kunminx.player.contract.ICacheProxy
import com.kunminx.player.contract.IPlayController
import com.kunminx.player.contract.IServiceNotifier
import com.kunminx.player.domain.MusicDTO
import com.kunminx.player.domain.PlayerController
import com.kunminx.player.domain.PlayingInfoManager
import net.steamcrafted.materialiconlib.MaterialDrawableBuilder

/**
 *  Created by earthgee on 2024/1/3
 *  CopyRight (c) earthgee.com
 *  功能：
 */
object PlayerManager : IPlayController<TestAlbum, TestMusic, TestArtist> {

    val mController = PlayerController<TestAlbum, TestMusic, TestArtist>()

    private var mIsInit = false

    fun init(context: Context) {
        if(!mIsInit) {
            init(context, null, null)
            mIsInit = true
        }
    }

    override fun init(
        context: Context?,
        iServiceNotifier: IServiceNotifier?,
        iCacheProxy: ICacheProxy?
    ) {
        context?: return
        val applicationContext = context.applicationContext

        val proxyCacheServer = HttpProxyCacheServer.Builder(context)
            .fileNameGenerator { url ->
                val splitArray = url.split("/")
                splitArray[splitArray.size - 1]
            }
            .maxCacheSize(Int.MAX_VALUE.toLong() + 1)
            .build()

        mController.init(applicationContext, { startOrStop ->
            val intent = Intent(applicationContext, PlayerService::class.java)
            if(startOrStop) applicationContext.startService(intent)
            else applicationContext.stopService(intent)
        }, proxyCacheServer::getProxyUrl)
    }

    override fun getAlbum(): TestAlbum =
        mController.album

    override fun getAlbumMusics(): MutableList<TestMusic> {
        return mController.albumMusics
    }

    override fun setChangingPlayingMusic(changingPlayingMusic: Boolean) {
        mController.setChangingPlayingMusic(changingPlayingMusic)
    }

    override fun getAlbumIndex(): Int {
        return mController.albumIndex
    }

    override fun getRepeatMode(): Enum<PlayingInfoManager.RepeatMode> {
        return mController.repeatMode
    }

    override fun getCurrentPlayingMusic(): TestMusic {
        return mController.currentPlayingMusic
    }

    override fun playAudio() {
        mController.playAudio()
    }

    override fun playAudio(albumIndex: Int) {
        mController.playAudio(albumIndex)
    }

    override fun playNext() {
        mController.playNext()
    }

    override fun playPrevious() {
        mController.playPrevious()
    }

    override fun playAgain() {
        mController.playAgain()
    }

    override fun togglePlay() {
        mController.togglePlay()
    }

    override fun pauseAudio() {
        mController.pauseAudio()
    }

    override fun resumeAudio() {
        mController.resumeAudio()
    }

    override fun clear() {
        mController.clear()
    }

    override fun changeMode() {
        mController.changeMode()
    }

    override fun isPlaying(): Boolean {
        return mController.isPlaying
    }

    override fun isPaused(): Boolean {
        return mController.isPaused
    }

    override fun isInit(): Boolean {
        return mController.isInit
    }

    override fun setSeek(progress: Int) {
        mController.setSeek(progress)
    }

    override fun getTrackTime(progress: Int): String {
        return mController.getTrackTime(progress)
    }

    override fun getUiStates(): LiveData<MusicDTO<TestAlbum, TestMusic, TestArtist>> {
        return mController.uiStates
    }

    override fun loadAlbum(musicAlbum: TestAlbum?, playIndex: Int) {
        mController.loadAlbum(musicAlbum, playIndex)
    }

    override fun loadAlbum(musicAlbum: TestAlbum?) {
        val album = mController.album
        if(album == null || album.albumId != musicAlbum?.albumId) {
            mController.loadAlbum(musicAlbum)
        }
    }

    fun getModeIcon(mode: Enum<PlayingInfoManager.RepeatMode>): MaterialDrawableBuilder.IconValue =
        when(mode) {
            PlayingInfoManager.RepeatMode.LIST_CYCLE -> MaterialDrawableBuilder.IconValue.REPEAT
            PlayingInfoManager.RepeatMode.SINGLE_CYCLE -> MaterialDrawableBuilder.IconValue.REPEAT_ONCE
            else -> MaterialDrawableBuilder.IconValue.SHUFFLE
        }


    fun getModeIcon(): MaterialDrawableBuilder.IconValue {
        return getModeIcon(repeatMode)
    }

}











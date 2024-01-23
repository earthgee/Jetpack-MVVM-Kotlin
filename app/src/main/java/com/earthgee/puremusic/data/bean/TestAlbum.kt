package com.earthgee.puremusic.data.bean

import com.kunminx.player.bean.base.BaseAlbumItem
import com.kunminx.player.bean.base.BaseArtistItem
import com.kunminx.player.bean.base.BaseMusicItem

/**
 *  Created by earthgee on 2024/1/3
 *  CopyRight (c) earthgee.com
 *  功能：
 */
class TestAlbum(
    albumId: String,
    title: String,
    summary: String,
    artist: TestArtist,
    coverImg: String,
    musics: List<TestMusic>,
    val albumMid: String
) : BaseAlbumItem<TestMusic, TestArtist>(albumId, title, summary, artist, coverImg, musics)

class TestMusic(
    musicId: String,
    coverImg: String,
    url: String,
    title: String,
    artist: TestArtist,
    val songMid: String
) : BaseMusicItem<TestArtist>(musicId, coverImg, url, title, artist)

class TestArtist(name: String, val birthday: String) : BaseArtistItem(name)
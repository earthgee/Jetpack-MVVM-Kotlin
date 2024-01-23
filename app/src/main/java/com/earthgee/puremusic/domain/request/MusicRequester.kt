package com.earthgee.puremusic.domain.request

import android.annotation.SuppressLint
import com.earthgee.architecture.data.response.DataResult
import com.earthgee.architecture.domain.request.Requester
import com.earthgee.puremusic.data.bean.TestAlbum
import com.earthgee.puremusic.data.repository.DataRepository
import com.kunminx.architecture.domain.message.MutableResult
import com.kunminx.architecture.domain.message.Result

/**
 *  Created by earthgee on 2024/1/5
 *  CopyRight (c) earthgee.com
 *  功能：
 */
class MusicRequester : Requester() {

    val _mFreeMusicsResult = MutableResult<DataResult<TestAlbum>>()
    var freeMusicsResult: Result<DataResult<TestAlbum>> = _mFreeMusicsResult

    //TODO tip 5: requester 作为数据的生产者，职责应仅限于 "请求调度 和 结果分发"，
    //
    // 换言之，此处只关注数据的生成和回推，不关注数据的使用，
    // 改变 UI 状态的逻辑代码，只应在表现层页面中编写，例如 Jetpack Compose 的使用，

    @SuppressLint("CheckResult")
    fun requestFreeMusic() {
        DataRepository.getFreeMusic().subscribe(_mFreeMusicsResult::setValue)
    }

}












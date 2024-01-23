package com.earthgee.puremusic.domain.event

import com.earthgee.puremusic.data.bean.DownloadState

/**
 *  Created by earthgee on 2024/1/8
 *  CopyRight (c) earthgee.com
 *  功能：
 */
class DownloadEvent(val eventId: Int, val downloadState: DownloadState = DownloadState()) {

    companion object {

        const val EVENT_DOWNLOAD = 1
        const val EVENT_DOWNLOAD_GLOBAL = 2

    }

    fun copy(downloadState: DownloadState): DownloadEvent = DownloadEvent(eventId, downloadState)

}
package com.earthgee.puremusic.data.config

import android.os.Environment
import com.earthgee.architecture.utils.Utils
import com.earthgee.puremusic.R

/**
 *  Created by earthgee on 2024/1/4
 *  CopyRight (c) earthgee.com
 *  功能：
 */
class Const {

    companion object {

        val COVER_PATH = Utils.getApp().getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.absolutePath ?: ""

        val PROJECT_LINK = Utils.getApp().getString(R.string.github_project)

        val COLUMN_LINK = Utils.getApp().getString(R.string.article_navigation)

    }

}
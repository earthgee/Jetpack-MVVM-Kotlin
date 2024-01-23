package com.earthgee.puremusic.ui.page.adapter

import androidx.recyclerview.widget.DiffUtil
import com.earthgee.puremusic.data.bean.LibraryInfo
import com.earthgee.puremusic.data.bean.TestMusic

/**
 *  Created by earthgee on 2024/1/4
 *  CopyRight (c) earthgee.com
 *  功能：
 */
object DiffUtils {

    val mLibraryInfoItemCallback: DiffUtil.ItemCallback<LibraryInfo> by lazy {
        object : DiffUtil.ItemCallback<LibraryInfo>() {
            override fun areItemsTheSame(oldItem: LibraryInfo, newItem: LibraryInfo) =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: LibraryInfo, newItem: LibraryInfo) =
                oldItem.title == newItem.title

        }
    }

    val mTestMusicItemCallback: DiffUtil.ItemCallback<TestMusic> by lazy {
        object: DiffUtil.ItemCallback<TestMusic>() {

            override fun areItemsTheSame(oldItem: TestMusic, newItem: TestMusic) =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: TestMusic, newItem: TestMusic) =
                oldItem.musicId == newItem.musicId

        }
    }

}
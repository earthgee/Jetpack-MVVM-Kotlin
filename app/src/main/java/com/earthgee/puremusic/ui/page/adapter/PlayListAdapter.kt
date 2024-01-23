package com.earthgee.puremusic.ui.page.adapter

import android.content.Context
import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import com.earthgee.puremusic.R
import com.earthgee.puremusic.data.bean.TestMusic
import com.earthgee.puremusic.databinding.AdapterPlayItemBinding
import com.earthgee.puremusic.domain.proxy.PlayerManager
import com.kunminx.binding_recyclerview.adapter.SimpleDataBindingAdapter

/**
 *  Created by earthgee on 2024/1/5
 *  CopyRight (c) earthgee.com
 *  功能：
 */
class PlayListAdapter(context: Context) :
    SimpleDataBindingAdapter<TestMusic, AdapterPlayItemBinding>(
        context,
        R.layout.adapter_play_item,
        DiffUtils.mTestMusicItemCallback
    ) {

    override fun onBindItem(
        binding: AdapterPlayItemBinding?,
        item: TestMusic?,
        holder: RecyclerView.ViewHolder?
    ) {
        binding?.album = item
        val currentIndex = PlayerManager.albumIndex
        binding?.ivPlayStatus?.setColor(if(currentIndex == holder?.absoluteAdapterPosition)
            binding.root.context.getColor(R.color.gray) else Color.TRANSPARENT)
    }

}
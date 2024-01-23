package com.earthgee.puremusic.ui.page.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import com.earthgee.puremusic.R
import com.earthgee.puremusic.data.bean.LibraryInfo
import com.earthgee.puremusic.databinding.AdapterLibraryBinding
import com.kunminx.binding_recyclerview.adapter.SimpleDataBindingAdapter

/**
 *  Created by earthgee on 2024/1/4
 *  CopyRight (c) earthgee.com
 *  功能：
 */
class DrawerAdapter(context: Context) :
    SimpleDataBindingAdapter<LibraryInfo, AdapterLibraryBinding>(
        context,
        R.layout.adapter_library,
        DiffUtils.mLibraryInfoItemCallback
    ) {

    init {
        setOnItemClickListener { viewId, item, position ->
            val uri = Uri.parse(item.url)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            mContext.startActivity(intent)
        }
    }

    override fun onBindItem(
        binding: AdapterLibraryBinding?,
        item: LibraryInfo?,
        holder: RecyclerView.ViewHolder?
    ) {
        binding?.info = item
    }


}
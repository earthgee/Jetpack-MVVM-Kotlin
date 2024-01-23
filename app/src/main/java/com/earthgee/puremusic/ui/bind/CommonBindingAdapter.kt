package com.earthgee.puremusic.ui.bind

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.earthgee.architecture.utils.ClickUtils

/**
 *  Created by earthgee on 2024/1/4
 *  CopyRight (c) earthgee.com
 *  功能：
 */
object CommonBindingAdapter {

    @JvmStatic
    @BindingAdapter(value = ["imageUrl", "placeHolder"], requireAll = false)
    fun imageUrl(view: ImageView, url: String, placeHolder: Drawable?) {
        Glide.with(view.context).load(url).placeholder(placeHolder).into(view)
    }

    @JvmStatic
    @BindingAdapter(value = ["onClickWithDebouncing"], requireAll = false)
    fun onClickWithDebouncing(view: View, onClickListener: View.OnClickListener) {
        ClickUtils.applySingleDebouncing(view, onClickListener)
    }

    @JvmStatic
    @BindingAdapter(value = ["size"], requireAll = false)
    fun size(view: View, size: Pair<Int, Int>) {
        val params = view.layoutParams as CoordinatorLayout.LayoutParams
        params.width = size.first
        params.height = size.second
        view.layoutParams = params
    }

    @JvmStatic
    @BindingAdapter(value = ["visible"], requireAll = false)
    fun visible(view: View, visible: Boolean) {
        if (visible && view.visibility == View.GONE) {
            view.visibility = View.VISIBLE
        } else if (!visible && view.visibility == View.VISIBLE) {
            view.visibility = View.GONE
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["invisible"], requireAll = false)
    fun invisible(view: View, visible: Boolean) {
        if (visible && view.visibility == View.INVISIBLE) {
            view.visibility = View.VISIBLE
        } else if (!visible && view.visibility == View.VISIBLE) {
            view.visibility = View.INVISIBLE
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["transX"], requireAll = false)
    fun translationX(view: View, translationX: Float) {
        view.translationX = translationX
    }

    @JvmStatic
    @BindingAdapter(value = ["transY"], requireAll = false)
    fun translationY(view: View, translationY: Float) {
        view.translationY = translationY
    }

    @JvmStatic
    @BindingAdapter(value = ["alpha"], requireAll = false)
    fun alpha(view: View, alpha: Float) {
        view.alpha = alpha
    }

    @JvmStatic
    @BindingAdapter(value = ["x"], requireAll = false)
    fun x(view: View, x: Float) {
        view.x = x
    }

    @JvmStatic
    @BindingAdapter(value = ["y"], requireAll = false)
    fun y(view: View, y: Float) {
        view.y = y
    }

}








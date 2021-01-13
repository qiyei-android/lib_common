package com.qiyei.android.common.listener

import android.view.View

interface OnItemClickListener<T> {

    /**
     * 点击到item
     * @param view
     * @param item
     * @param position
     */
    fun click(view: View, item: T, position: Int)
}
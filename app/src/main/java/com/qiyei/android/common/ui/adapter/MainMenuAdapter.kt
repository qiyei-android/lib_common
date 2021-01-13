package com.qiyei.android.common.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.qiyei.android.common.databinding.RvMainMenuItemBinding
import com.qiyei.android.common.entity.MainMenu
import com.qiyei.android.common.listener.OnItemClickListener


class MainMenuAdapter(private val mContext: Context, var mDatas: MutableList<MainMenu>) :
    RecyclerView.Adapter<MainMenuAdapter.MainMenuViewHolder>() {

    /**
     * item点击事件
     */
    var mClickListener: OnItemClickListener<MainMenu>? = null

    override fun getItemCount(): Int {
        return mDatas.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainMenuViewHolder {
        val binding = RvMainMenuItemBinding.inflate(LayoutInflater.from(mContext),parent,false)
        return MainMenuViewHolder(binding,binding.root)
    }

    override fun onBindViewHolder(holder: MainMenuViewHolder, position: Int) {
        holder.binding.tv.text = mDatas[position].name
        holder.binding.tv.setOnClickListener {
            mClickListener?.let {
                it.click(holder.binding.tv,mDatas[position],position)
            }
        }
    }

    inner class MainMenuViewHolder(val binding: RvMainMenuItemBinding,itemView: View) : RecyclerView.ViewHolder(itemView)
}
package com.example.moviesflickrapp.base.view

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter


/**
 * Utilize creating and binding data into viewHolder with a default [DiffUtil.ItemCallback]
 */
abstract class BaseAdapter<T : BaseEntity>(callback: DiffUtil.ItemCallback<T> = BaseDiffCallback()) :
    ListAdapter<T, BaseViewHolder<ViewDataBinding>>(callback) {


    override fun onBindViewHolder(holder: BaseViewHolder<ViewDataBinding>, position: Int) {
        bind(holder.binding, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        getViewHolder(parent, viewType)

    open fun getViewHolder(parent: ViewGroup, viewType: Int) =
        BaseViewHolder(createBinding(parent, viewType))

    abstract fun createBinding(parent: ViewGroup, viewType: Int): ViewDataBinding

    protected abstract fun bind(binding: ViewDataBinding, position: Int)
}

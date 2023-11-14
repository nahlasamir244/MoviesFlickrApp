package com.example.moviesflickrapp.base.view

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

/**
 * A generic diff callback that checks if items are the same by default equal implementation.
 */
open class BaseDiffCallback<T : BaseEntity> : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T) = oldItem.entityId() == newItem.entityId()

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: T, newItem: T) = oldItem == newItem
}
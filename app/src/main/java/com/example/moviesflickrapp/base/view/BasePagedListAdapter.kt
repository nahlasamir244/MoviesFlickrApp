package com.example.photoapp.base.view

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesflickrapp.base.view.BaseDiffCallback
import com.example.moviesflickrapp.base.view.BaseEntity
import com.example.moviesflickrapp.base.view.BaseViewHolder


/**
 * Base pagination list adapter
 *
 * Utilize creating and binding data into viewHolder
 * @see BaseEntity
 * @see BaseViewHolder
 */
abstract class BasePagedListAdapter<T : BaseEntity>(diffCallback: DiffUtil.ItemCallback<T> = BaseDiffCallback()) :
    PagingDataAdapter<T, RecyclerView.ViewHolder>(diffCallback) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return getViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        bind((holder as BaseViewHolder<*>).binding, position)

    }


    open fun getViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        BaseViewHolder(createBinding(parent, viewType))

    abstract fun createBinding(parent: ViewGroup, viewType: Int): ViewDataBinding

    protected abstract fun bind(binding: ViewDataBinding, position: Int)

}
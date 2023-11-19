package com.example.moviesflickrapp.presentation.movies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.moviesflickrapp.R
import com.example.moviesflickrapp.databinding.ItemMovieBinding
import com.example.moviesflickrapp.databinding.ItemOwnerBinding
import com.example.moviesflickrapp.presentation.ext.loadImage

class MovieAdapter(private val movieAdapterHandler: MovieAdapterHandler) :
    ListAdapter<BaseUIModel, ViewHolder>(MovieDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        when (viewType) {
            OWNER_VIEW_TYPE -> {
                OwnerViewHolder(
                    ItemOwnerBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                )
            }

            PHOTO_VIEW_TYPE -> {
                MovieViewHolder(
                    ItemMovieBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    ), movieAdapterHandler
                )
            }

            else -> InvalidHolder(View(parent.context))
        }

    override fun getItemViewType(position: Int): Int =
        when (getItem(position)) {
            is OwnerUIModel -> OWNER_VIEW_TYPE
            is PhotoUIModel -> PHOTO_VIEW_TYPE
        }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is OwnerViewHolder -> {
                (getItem(position) as? OwnerUIModel)?.let { ownerUIModel -> holder.bind(ownerUIModel) }
            }

            is MovieViewHolder -> {
                (getItem(position) as? PhotoUIModel)?.let { photoUIModel -> holder.bind(photoUIModel) }
            }
        }
    }

    inner class MovieViewHolder(
        private val itemMovieBinding: ItemMovieBinding,
        private val movieAdapterHandler: MovieAdapterHandler
    ) : ViewHolder(itemMovieBinding.root) {

        init {
            itemMovieBinding.apply {
                imageViewPhotoImage.setOnClickListener {
                    val currentPosition = layoutPosition
                    if (currentPosition != RecyclerView.NO_POSITION) {
                        (getItem(currentPosition) as? PhotoUIModel)?.let { photoUIModel ->
                            movieAdapterHandler.onMovieClicked(
                                photoUIModel
                            )
                        }
                    }
                }
            }
        }

        fun bind(photoUIModel: PhotoUIModel) {
            itemMovieBinding.apply {
                imageViewPhotoImage.loadImage(
                    photoUIModel.imageUrl,
                    R.drawable.ic_image_default_grey
                )
                textViewPhotoTitle.text = photoUIModel.title
            }
        }
    }


    inner class OwnerViewHolder(private val itemOwnerBinding: ItemOwnerBinding) :
        ViewHolder(itemOwnerBinding.root) {
        fun bind(ownerUIModel: OwnerUIModel) {
            itemOwnerBinding.apply {
                materialTextOwner.text = ownerUIModel.owner
            }
        }

    }

    private class MovieDiffUtil : DiffUtil.ItemCallback<BaseUIModel>() {
        override fun areItemsTheSame(
            oldItem: BaseUIModel,
            newItem: BaseUIModel
        ): Boolean =
            (oldItem is PhotoUIModel && newItem is PhotoUIModel && oldItem.id == newItem.id) ||
                    (oldItem is OwnerUIModel && newItem is OwnerUIModel && oldItem.owner == newItem.owner)


        override fun areContentsTheSame(
            oldItem: BaseUIModel,
            newItem: BaseUIModel
        ): Boolean =
            (oldItem is PhotoUIModel && newItem is PhotoUIModel && oldItem == newItem) ||
                    (oldItem is OwnerUIModel && newItem is OwnerUIModel && oldItem == newItem)
    }

    companion object {
        private const val OWNER_VIEW_TYPE = 1
        private const val PHOTO_VIEW_TYPE = 2
    }

    class InvalidHolder(v: View) : ViewHolder(v)
}
package com.example.moviesflickrapp.presentation.ext

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide

fun ImageView.loadImage(url: String, placeholderResourceId: Int) =
    Glide.with(this).load(url)
        .placeholder(
            ContextCompat.getDrawable(
                this.context,
                placeholderResourceId
            )
        ).into(this)

fun ImageView.loadImageRound(url: String, placeholder: Drawable) =
    Glide.with(this).load(url)
        .placeholder(placeholder)
        .circleCrop()
        .into(this)
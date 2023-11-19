package com.example.moviesflickrapp.presentation.ext

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


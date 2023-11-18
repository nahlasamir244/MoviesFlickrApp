package com.example.moviesflickrapp.presentation.movies

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PhotoUIModel(
    val id: String,
    val title: String,
    val imageUrl: String
) : Parcelable
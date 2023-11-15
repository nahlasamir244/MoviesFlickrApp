package com.example.moviesflickrapp.data.mappers

import com.example.moviesflickrapp.data.network.model.response.Photo
import com.example.moviesflickrapp.presentation.movies.PhotoUIModel

fun Photo.toPhotoUiModel() = PhotoUIModel(
    id = this.id,
    title = this.title ?: "",
    imageUrl = "https://farm${this.farm}.static.flickr.com/${this.server}/" +
            "${this.id}_${this.secret}.jpg"
)
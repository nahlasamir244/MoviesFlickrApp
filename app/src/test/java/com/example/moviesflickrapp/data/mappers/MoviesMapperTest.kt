package com.example.moviesflickrapp.data.mappers

import com.example.moviesflickrapp.data.network.model.response.Photo
import com.example.moviesflickrapp.presentation.movies.PhotoUIModel
import org.junit.Assert
import org.junit.Test

class MoviesMapperTest {
    private val photoDto = Photo(
        id = "123", owner = "nahla", secret = "123",
        server = "124", farm = 44, title = "hello", isPublic = 1, isFamily = 1, isFriend = 0,
        rate = 5
    )

    @Test
    fun toPhotoUiModel() {
        val photoUIModel = photoDto.toPhotoUiModel()
        val expectedPhotoUIModel = PhotoUIModel(
            id = "123",
            title = "hello",
            imageUrl = "https://farm44.static.flickr.com/124/123_123.jpg",
        )
        Assert.assertEquals(expectedPhotoUIModel, photoUIModel)
    }
}
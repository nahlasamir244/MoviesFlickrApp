package com.example.moviesflickrapp.domain

import com.example.moviesflickrapp.data.network.model.response.Photo
import com.example.moviesflickrapp.presentation.movies.OwnerUIModel
import com.example.moviesflickrapp.presentation.movies.PhotoUIModel
import org.junit.Assert
import org.junit.Test

class MapToUIModelListUseCaseTest {
    private val photosMap: Map<String, List<Photo>> = mapOf(
        "nahla" to listOf(
            Photo(
                id = "123",
                owner = "nahla",
                secret = "123",
                server = "124",
                farm = 44,
                title = "hello",
                isPublic = 1,
                isFamily = 1,
                isFriend = 0,
                rate = 5
            ), Photo(
                id = "123",
                owner = "nahla",
                secret = "123",
                server = "124",
                farm = 44,
                title = "hello",
                isPublic = 1,
                isFamily = 1,
                isFriend = 0,
                rate = 5
            ), Photo(
                id = "123",
                owner = "nahla",
                secret = "123",
                server = "124",
                farm = 44,
                title = "hello",
                isPublic = 1,
                isFamily = 1,
                isFriend = 0,
                rate = 5
            )
        )
    )

    @Test
    fun `MapToUIModelListUseCase should form a list of ui models from owner,movies map`() {
        val useCase = MapToUIModelListUseCase()
        val uiModelList = useCase(photosMap)
        val expectedUiModelList = listOf(
            OwnerUIModel("nahla"),
            PhotoUIModel(
                id = "123",
                title = "hello",
                imageUrl = "https://farm44.static.flickr.com/124/123_123.jpg",
            ),
            PhotoUIModel(
                id = "123",
                title = "hello",
                imageUrl = "https://farm44.static.flickr.com/124/123_123.jpg",
            ), PhotoUIModel(
                id = "123",
                title = "hello",
                imageUrl = "https://farm44.static.flickr.com/124/123_123.jpg",
            )
        )
        Assert.assertEquals(expectedUiModelList.size, uiModelList.size)
        Assert.assertEquals(expectedUiModelList, uiModelList)
    }

}
package com.example.moviesflickrapp.domain

import com.example.moviesflickrapp.data.network.model.response.Photo
import org.junit.Assert
import org.junit.Test

class CategorizeMoviesUseCaseTest {

    private val photoList: List<Photo> = listOf(
        Photo(
            id = "123", owner = "nahla", secret = "123",
            server = "124", farm = 44, title = "hello", isPublic = 1, isFamily = 1, isFriend = 0,
            rate = 5
        ),
        Photo(
            id = "123", owner = "nahla", secret = "123",
            server = "124", farm = 44, title = "hello 4", isPublic = 1, isFamily = 1, isFriend = 0,
            rate = 4
        ),
        Photo(
            id = "123", owner = "nahla", secret = "123",
            server = "124", farm = 44, title = "hello 0", isPublic = 1, isFamily = 1, isFriend = 0,
            rate = 0
        ),
        Photo(
            id = "123", owner = "nahla", secret = "123",
            server = "124", farm = 44, title = "hello 3", isPublic = 1, isFamily = 1, isFriend = 0,
            rate = 3
        ),
        Photo(
            id = "123", owner = "yellow", secret = "123",
            server = "124", farm = 44, title = "yellow", isPublic = 1, isFamily = 1, isFriend = 0,
            rate = 2
        ),
        Photo(
            id = "123", owner = "yellow", secret = "123",
            server = "124", farm = 44, title = "yellow 4", isPublic = 1, isFamily = 1, isFriend = 0,
            rate = 5
        ),
        Photo(
            id = "123", owner = "yellow", secret = "123",
            server = "124", farm = 44, title = "yellow 3", isPublic = 1, isFamily = 1, isFriend = 0,
            rate = 3
        ),
        Photo(
            id = "123", owner = "yellow", secret = "123",
            server = "124", farm = 44, title = "yellow 3", isPublic = 1, isFamily = 1, isFriend = 0,
            rate = 3
        ),
    )
    private val maximumNumberPerCategory = 3

    @Test
    operator fun invoke() {
        val useCase = CategorizeMoviesUseCase()
        val categorizedPhotos = useCase(photoList, maximumNumberPerCategory)
        val expectedCategorizedPhotos = mapOf<String?, List<Photo>>(
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
                    title = "hello 4",
                    isPublic = 1,
                    isFamily = 1,
                    isFriend = 0,
                    rate = 4
                ), Photo(
                    id = "123",
                    owner = "nahla",
                    secret = "123",
                    server = "124",
                    farm = 44,
                    title = "hello 3",
                    isPublic = 1,
                    isFamily = 1,
                    isFriend = 0,
                    rate = 3
                )
            ), "yellow" to listOf(
                Photo(
                    id = "123",
                    owner = "yellow",
                    secret = "123",
                    server = "124",
                    farm = 44,
                    title = "yellow 4",
                    isPublic = 1,
                    isFamily = 1,
                    isFriend = 0,
                    rate = 5
                ),
                Photo(
                    id = "123",
                    owner = "yellow",
                    secret = "123",
                    server = "124",
                    farm = 44,
                    title = "yellow 3",
                    isPublic = 1,
                    isFamily = 1,
                    isFriend = 0,
                    rate = 3
                ),
                Photo(
                    id = "123",
                    owner = "yellow",
                    secret = "123",
                    server = "124",
                    farm = 44,
                    title = "yellow 3",
                    isPublic = 1,
                    isFamily = 1,
                    isFriend = 0,
                    rate = 3
                ),
            )
        )
        Assert.assertEquals(expectedCategorizedPhotos, categorizedPhotos)
        Assert.assertEquals(categorizedPhotos.values.first().size, maximumNumberPerCategory)
    }
}
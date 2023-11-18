package com.example.moviesflickrapp.domain

import app.cash.turbine.test
import com.example.moviesflickrapp.MainCoroutineRule
import com.example.moviesflickrapp.base.data.ApiException
import com.example.moviesflickrapp.base.data.Resource
import com.example.moviesflickrapp.data.network.model.response.Photo
import com.example.moviesflickrapp.data.repository.movie.MovieRepo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.spy
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class GetMoviesUseCaseTest {
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private var closeableMockito: AutoCloseable? = null

    @Mock
    private lateinit var movieRepo: MovieRepo

    private lateinit var getMoviesUseCase: GetMoviesUseCase


    @Before
    fun beforeEach() {
        closeableMockito = MockitoAnnotations.openMocks(this)
        getMoviesUseCase =
            spy(GetMoviesUseCase(movieRepo))
    }


    @Test
    fun `GetMoviesUseCase should return error when request fails`() =
        runTest {
            val error = Resource.Error(ApiException(401,"Resource not found !!"))

            whenever(movieRepo.searchMovies(anyString())).doReturn(flowOf(error))

            getMoviesUseCase(anyString()).test {
                awaitItem() shouldBeEqualTo error
                awaitComplete()
            }

        }


    @Test
    fun `GetMoviesUseCase should return photo list when request is successful`() =
        runTest {
            val photoList = listOf<Photo>()

            whenever(movieRepo.searchMovies(anyString())).doReturn(
                flowOf(
                    Resource.Success(
                        photoList
                    )
                )
            )
            getMoviesUseCase(anyString()).test {
                awaitItem() shouldBeEqualTo Resource.Success(photoList)
                awaitComplete()
            }
        }


    @After
    @Throws(Exception::class)
    fun afterEach() {
        closeableMockito?.close()

    }

}
package com.example.moviesflickrapp.data.repository.movie

import app.cash.turbine.test
import com.example.moviesflickrapp.MainCoroutineRule
import com.example.moviesflickrapp.base.data.Resource
import com.example.moviesflickrapp.base.utils.NetworkConnectivityHelper
import com.example.moviesflickrapp.data.network.model.response.MoviesSearchResponse
import com.example.moviesflickrapp.data.network.model.response.Photo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeInstanceOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers.anyList
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class MovieRepositoryTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var movieLocalDataSource: MovieLocalDataSource

    @Mock
    private lateinit var movieRemoteDataSource: MovieRemoteDataSource

    @Mock
    private lateinit var networkConnectivityHelper: NetworkConnectivityHelper

    private lateinit var movieRepository: MovieRepositoryImpl

    private var closeable: AutoCloseable? = null


    @Before
    fun setUp() {
        closeable = MockitoAnnotations.openMocks(this)

        movieRepository = Mockito.spy(
            MovieRepositoryImpl(
                movieLocalDataSource,
                movieRemoteDataSource,
                networkConnectivityHelper,
                Dispatchers.Main
            )
        )
    }

    @Test
    fun `searchMovies should call MovieRemoteDataSource to get the matched photo list when Internet is Connected and save Result locally`() =
        runTest {
            val response: MoviesSearchResponse = mock()
            whenever(networkConnectivityHelper.isConnected()).doReturn(true)

            whenever(movieRemoteDataSource.searchMovies(anyString())).thenReturn(response)

            movieRepository.searchMovies(anyString()).collect {

            }
            verify(movieRemoteDataSource, times(1)).searchMovies(anyString())
            verify(movieLocalDataSource, times(1)).saveMovies(anyList())
        }

    @Test
    fun `searchMovies should call MovieLocalDataSource to get the matched photo list when Internet is Not Connected`() =
        runTest {
            val photoList: List<Photo> = mock()
            whenever(networkConnectivityHelper.isConnected()).doReturn(false)

            whenever(movieLocalDataSource.searchMovies(anyString())).thenReturn(photoList)

            movieRepository.searchMovies(anyString()).collect {

            }
            verify(movieLocalDataSource, times(1)).searchMovies(anyString())
        }

    @Test
    fun `when searchMovies triggered should return response with loading then success`() = runTest {

        val response: MoviesSearchResponse = mock()

        whenever(networkConnectivityHelper.isConnected()).doReturn(true)

        `when`(movieRemoteDataSource.searchMovies(anyString())).thenReturn(response)

        movieRepository.searchMovies(anyString()).test {
            awaitItem() shouldBeInstanceOf Resource.Loading::class.java
            val successResult = awaitItem()
            successResult shouldBeInstanceOf Resource.Success::class.java
            (successResult as? Resource.Success)?.data shouldBe response
            awaitComplete()
        }
    }

    @Test
    fun testSearchMoviesShouldReturnErrorIfErrorOccurred() = runTest {
        whenever(networkConnectivityHelper.isConnected()).doReturn(true)

        `when`(movieRemoteDataSource.searchMovies(anyString())).thenThrow(
            Exception()
        )

        movieRepository.searchMovies(anyString()).test {
            awaitItem() shouldBeInstanceOf Resource.Loading::class.java
            awaitItem() shouldBeInstanceOf Resource.Error::class.java
            awaitComplete()
        }
    }

    @After
    @Throws(Exception::class)
    fun afterEach() {
        closeable?.close()

    }
}
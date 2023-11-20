package com.example.moviesflickrapp.presentation.movies

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.moviesflickrapp.MainCoroutineRule
import com.example.moviesflickrapp.base.data.ApiException
import com.example.moviesflickrapp.base.data.Resource
import com.example.moviesflickrapp.domain.CategorizeMoviesUseCase
import com.example.moviesflickrapp.domain.GetMoviesUseCase
import com.example.moviesflickrapp.domain.InputType
import com.example.moviesflickrapp.domain.MapToUIModelListUseCase
import com.example.moviesflickrapp.domain.ValidateInputUseCase
import com.example.moviesflickrapp.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import org.amshove.kluent.internal.assertNotEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class MoviesViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutineRule = MainCoroutineRule()

    private lateinit var viewModel: MoviesViewModel

    private val getMoviesUseCase = mock<GetMoviesUseCase>()
    private val validateInputUseCase = mock<ValidateInputUseCase>()
    private val categorizeMoviesUseCase = mock<CategorizeMoviesUseCase>()
    private val mapToUIModelListUseCase = mock<MapToUIModelListUseCase>()

    @Before
    fun setup() {
        viewModel = MoviesViewModel(
            getMoviesUseCase, validateInputUseCase, categorizeMoviesUseCase, mapToUIModelListUseCase
        )
        viewModel.moviesEvent.observeForever {}
    }

    @Test
    fun `when searchMovie should invoke validateInputUseCase`() {
        coroutineRule.run {
            viewModel.invokeAction(MoviesScreenContract.Action.SearchMovies("yellow"))
            verify(validateInputUseCase).invoke("yellow", InputType.MOVIE_SEARCH_QUERY)
        }
    }

    @Test
    fun `when onMovieClicked should post NavigateToMoviePreview event`() {
        coroutineRule.run {
            viewModel.invokeAction(
                MoviesScreenContract.Action.OnMovieClicked(
                    PhotoUIModel(
                        id = "123",
                        title = "nahla",
                        imageUrl = "https://farm44.static.flickr.com/124/123_123.jpg",
                    )
                )
            )
            val expected = MoviesScreenContract.Event.NavigateToMoviePreview
            assertEquals(expected, viewModel.moviesEvent.getOrAwaitValue())
            assertNotEquals(viewModel.selectedMovie, null)
        }
    }

    @Test
    fun `when searchMovie with valid input should invoke getMoviesUseCase`() {
        coroutineRule.run {
            whenever(validateInputUseCase(any(), any())).thenReturn(true)
            viewModel.invokeAction(MoviesScreenContract.Action.SearchMovies("yellow"))
            verify(getMoviesUseCase).invoke("yellow")
        }
    }

    @Test
    fun `when searchMovie with invalid input should invoke getMoviesUseCase`() {
        coroutineRule.run {
            whenever(validateInputUseCase(any(), any())).thenReturn(false)
            viewModel.invokeAction(MoviesScreenContract.Action.SearchMovies(""))
            assertTrue(viewModel.moviesEvent.getOrAwaitValue() is MoviesScreenContract.Event.Warning)
        }
    }

    @Test
    fun `when GetMovies resource is error should return ErrorState`() {
        coroutineRule.run {
            whenever(getMoviesUseCase(any())).doReturn(flow {
                emit(
                    Resource.Error(
                        ApiException(404)
                    )
                )
            })
            whenever(validateInputUseCase(any(), any())).thenReturn(true)
            viewModel.invokeAction(MoviesScreenContract.Action.SearchMovies("yellow"))
            val expected = MoviesScreenContract.State.Error("Something went wrong")
            assertEquals(expected, viewModel.moviesState.getOrAwaitValue())
        }
    }

}

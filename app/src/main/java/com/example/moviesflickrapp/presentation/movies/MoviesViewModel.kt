package com.example.moviesflickrapp.presentation.movies

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesflickrapp.base.data.Resource
import com.example.moviesflickrapp.base.utils.livedata.SingleLiveEvent
import com.example.moviesflickrapp.data.network.model.response.Photo
import com.example.moviesflickrapp.domain.CategorizeMoviesUseCase
import com.example.moviesflickrapp.domain.GetMoviesUseCase
import com.example.moviesflickrapp.domain.InputType
import com.example.moviesflickrapp.domain.MapToUIModelListUseCase
import com.example.moviesflickrapp.domain.ValidateInputUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val getMoviesUseCase: GetMoviesUseCase,
    private val validateInputUseCase: ValidateInputUseCase,
    private val categorizeMoviesUseCase: CategorizeMoviesUseCase,
    private val mapToUIModelListUseCase: MapToUIModelListUseCase
) : ViewModel(), MoviesScreenContract.ViewModel {

    override val moviesState = MediatorLiveData<MoviesScreenContract.State>()
    override val moviesEvent = SingleLiveEvent<MoviesScreenContract.Event>()

    var selectedMovie: PhotoUIModel? = null

    override fun invokeAction(action: MoviesScreenContract.Action) {
        when (action) {
            is MoviesScreenContract.Action.OnMovieClicked -> {
                selectedMovie = action.photoUIModel.copy()
                moviesEvent.postValue(MoviesScreenContract.Event.NavigateToMoviePreview)
            }

            is MoviesScreenContract.Action.SearchMovies -> {
                if (validateInputUseCase(action.searchQuery, InputType.MOVIE_SEARCH_QUERY)) {
                    searchMovies(action)
                } else {
                    moviesEvent.postValue(MoviesScreenContract.Event.Warning("invalid search query")) // this better to be localized
                }

            }
        }
    }

    private fun searchMovies(action: MoviesScreenContract.Action.SearchMovies) {
        viewModelScope.launch {
            getMoviesUseCase(action.searchQuery).collectLatest { resource ->
                when (resource) {
                    is Resource.Error -> {
                        moviesState.postValue(MoviesScreenContract.State.Error("Something went wrong")) // this better to be localized
                    }

                    Resource.Loading -> {
                        moviesState.postValue(MoviesScreenContract.State.Loading)
                    }

                    is Resource.Success -> {
                        if (resource.data.isNullOrEmpty()) {
                            moviesState.postValue(MoviesScreenContract.State.EmptySearchResult)
                        } else {
                            handleSearchMoviesSuccess(resource)
                        }

                    }
                }
            }
        }
    }

    private fun handleSearchMoviesSuccess(resource: Resource.Success<List<Photo>?>) {
        resource.data?.let { photoList ->
            val categorizedMovies =
                categorizeMoviesUseCase(photoList, 5)
            val uiModelList = mapToUIModelListUseCase(categorizedMovies)
            moviesState.postValue(
                MoviesScreenContract.State.Success(
                    uiModelList
                )
            )
        }
    }
}
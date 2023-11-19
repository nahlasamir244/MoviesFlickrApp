package com.example.moviesflickrapp.presentation.movies

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.example.moviesflickrapp.base.utils.livedata.SingleLiveEvent
import com.example.moviesflickrapp.domain.CategorizeMoviesUseCase
import com.example.moviesflickrapp.domain.GetMoviesUseCase
import com.example.moviesflickrapp.domain.ValidateInputUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val getMoviesUseCase: GetMoviesUseCase,
    private val validateInputUseCase: ValidateInputUseCase,
    private val categorizeMoviesUseCase: CategorizeMoviesUseCase
) : ViewModel(), MoviesScreenContract.ViewModel {

    override val moviesState = MediatorLiveData<MoviesScreenContract.State>()
    override val moviesEvent = SingleLiveEvent<MoviesScreenContract.Event>()

    private var selectedMovie: PhotoUIModel? = null

    override fun invokeAction(action: MoviesScreenContract.Action) {
        when (action) {
            is MoviesScreenContract.Action.OnMovieClicked -> {
                moviesEvent.postValue(MoviesScreenContract.Event.NavigateToMoviePreview)
            }
            is MoviesScreenContract.Action.SearchMovies -> {

            }
        }
    }
}
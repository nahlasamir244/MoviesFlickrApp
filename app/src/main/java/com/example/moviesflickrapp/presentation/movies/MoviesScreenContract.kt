package com.example.moviesflickrapp.presentation.movies

import androidx.lifecycle.LiveData

sealed class MoviesScreenContract {

    interface ViewModel {
        val moviesState: LiveData<State>
        val moviesEvent: LiveData<Event>
        fun invokeAction(action: Action)
    }

    sealed class Action {
        data class SearchMovies(val searchQuery: String) : Action()
        data class OnMovieClicked(val photoUIModel: PhotoUIModel) : Action()
    }

    sealed class State {
        object Loading : State()
        data class Success(val uiModelList: List<BaseUIModel>) :
            State()

        object EmptySearchResult : State()
        data class Error(val message: String) : State()
    }

    sealed class Event {
        //to display error toast message
        data class Warning(val message: String) : Event()
        object NavigateToMoviePreview : Event()
    }
}


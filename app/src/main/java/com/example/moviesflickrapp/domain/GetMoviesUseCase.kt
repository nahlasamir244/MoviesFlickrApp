package com.example.moviesflickrapp.domain

import com.example.moviesflickrapp.data.repository.movie.MovieRepo
import javax.inject.Inject

class GetMoviesUseCase @Inject constructor(private val movieRepo: MovieRepo) {

    operator fun invoke(searchQuery: String) = movieRepo.searchMovies(searchQuery)
}
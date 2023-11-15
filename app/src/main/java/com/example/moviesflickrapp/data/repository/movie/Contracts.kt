package com.example.moviesflickrapp.data.repository.movie

import com.example.moviesflickrapp.base.data.Resource
import com.example.moviesflickrapp.data.network.model.response.MoviesSearchResponse
import com.example.moviesflickrapp.data.network.model.response.Photo
import kotlinx.coroutines.flow.Flow

interface MovieLocalDataSource {
    suspend fun searchMovies(searchQuery: String): List<Photo>?

    suspend fun saveMovies(movies: List<Photo>)
}

interface MovieRemoteDataSource {
    suspend fun searchMovies(searchQuery: String): MoviesSearchResponse
}

interface MovieRepo {
    fun searchMovies(searchQuery: String): Flow<Resource<List<Photo>?>>
}
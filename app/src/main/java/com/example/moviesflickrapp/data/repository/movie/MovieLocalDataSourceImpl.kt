package com.example.moviesflickrapp.data.repository.movie

import com.example.moviesflickrapp.data.local.dao.MovieDao
import com.example.moviesflickrapp.data.network.model.response.Photo
import javax.inject.Inject

class MovieLocalDataSourceImpl @Inject constructor(private val movieDao: MovieDao) :
    MovieLocalDataSource {
    override suspend fun searchMovies(searchQuery: String): List<Photo>? =
        movieDao.getMoviesByTitle(searchQuery)

    override suspend fun saveMovies(movies: List<Photo>) {
        movieDao.insertAll(movies)
    }
}
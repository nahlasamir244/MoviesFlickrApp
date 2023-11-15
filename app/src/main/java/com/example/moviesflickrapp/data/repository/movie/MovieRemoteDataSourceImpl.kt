package com.example.moviesflickrapp.data.repository.movie

import com.example.moviesflickrapp.base.data.BaseRemoteDataSource
import com.example.moviesflickrapp.data.network.api.Api
import com.example.moviesflickrapp.data.network.model.response.MoviesSearchResponse
import javax.inject.Inject

class MovieRemoteDataSourceImpl @Inject constructor(
    private val api: Api
) : BaseRemoteDataSource(), MovieRemoteDataSource {
    override suspend fun searchMovies(searchQuery: String): MoviesSearchResponse {
        return makeRequest {
            api.getPhotoSearchList(searchQuery)
        }
    }

}
package com.example.moviesflickrapp.data.network.api

import com.example.moviesflickrapp.data.network.model.response.MoviesSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET(".")
    suspend fun getPhotoSearchList(
        @Query("text") searchQuery: String = "",
        @Query("method") method: String = SEARCH_METHOD,
        @Query("nojsoncallback") noJson: Int = 1,
        @Query("format") format: String = RESPONSE_FORMAT,
        @Query("api_key") apiKey: String = FLICKR_API_KEY,
    ): Response<MoviesSearchResponse>

    companion object {
        const val FLICKR_API_KEY = "2b75e1530c10659582f25bcf189ee867"
        const val FLICKR_SECRET = "b82125eb01c9a822"
        const val RESPONSE_FORMAT = "json"
        const val SEARCH_METHOD = "flickr.photos.search"
    }
}

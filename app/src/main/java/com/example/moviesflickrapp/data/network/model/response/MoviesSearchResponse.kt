package com.example.moviesflickrapp.data.network.model.response

import com.google.gson.annotations.SerializedName

data class MoviesSearchResponse(

    @SerializedName("photos") var searchResult: SearchResult? = null,
    @SerializedName("stat") var state: String? = null,
    @SerializedName("code") var code: Int? = null,
    @SerializedName("message") var message: String? = null

)
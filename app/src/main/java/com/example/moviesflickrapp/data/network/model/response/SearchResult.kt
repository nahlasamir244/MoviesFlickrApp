package com.example.moviesflickrapp.data.network.model.response

import com.google.gson.annotations.SerializedName


data class SearchResult(

    @SerializedName("page") var page: Int? = null,
    @SerializedName("pages") var pages: Int? = null,
    @SerializedName("perpage") var perPage: Int? = null,
    @SerializedName("total") var total: Int? = null,
    @SerializedName("photo") var photoList: ArrayList<Photo>? = null

)
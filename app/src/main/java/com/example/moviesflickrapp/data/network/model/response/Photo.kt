package com.example.moviesflickrapp.data.network.model.response

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity(tableName = "photo_table")
@Parcelize
data class Photo(
    @PrimaryKey
    @SerializedName("id") var id: String,
    @SerializedName("owner") var owner: String? = null,
    @SerializedName("secret") var secret: String? = null,
    @SerializedName("server") var server: String? = null,
    @SerializedName("farm") var farm: Int? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("ispublic") var isPublic: Int? = null,
    @SerializedName("isfriend") var isFriend: Int? = null,
    @SerializedName("isfamily") var isFamily: Int? = null

) : Parcelable
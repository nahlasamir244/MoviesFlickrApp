package com.example.moviesflickrapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.moviesflickrapp.data.network.model.response.Photo
import javax.annotation.Nonnull

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(@Nonnull photos: List<Photo>)

    @Query("SELECT * FROM  photo_table WHERE title LIKE :queryString ORDER BY title ASC")
    fun getMoviesByTitle(queryString: String): List<Photo>?
}
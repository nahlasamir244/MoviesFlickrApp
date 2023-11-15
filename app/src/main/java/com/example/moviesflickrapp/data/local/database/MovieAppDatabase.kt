package com.example.moviesflickrapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.moviesflickrapp.data.local.dao.MovieDao
import com.example.moviesflickrapp.data.network.model.response.Photo

@Database(
    entities = [Photo::class],
    version = 1,
    exportSchema = false
)
abstract class MovieAppDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao

}
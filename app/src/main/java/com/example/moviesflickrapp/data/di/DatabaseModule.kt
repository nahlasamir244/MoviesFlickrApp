package com.example.moviesflickrapp.data.di

import android.content.Context
import androidx.room.Room
import com.example.moviesflickrapp.data.local.database.MovieAppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    @DatabaseName
    @Singleton
    fun provideDatabaseName() = "Flickr_Search.db"

    @Provides
    @Singleton
    fun provideMoviesAppDatabase(
        @ApplicationContext applicationContext: Context,
        @DatabaseName databaseName: String,
    ): MovieAppDatabase {
        return Room.databaseBuilder(
            applicationContext,
            MovieAppDatabase::class.java,
            databaseName
        ).build()
    }

    @Provides
    fun provideMoviesDao(flickrSearchAppDatabase: MovieAppDatabase) =
        flickrSearchAppDatabase.movieDao()

}
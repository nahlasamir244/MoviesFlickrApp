package com.example.moviesflickrapp.data.di

import com.example.moviesflickrapp.data.repository.movie.MovieLocalDataSource
import com.example.moviesflickrapp.data.repository.movie.MovieLocalDataSourceImpl
import com.example.moviesflickrapp.data.repository.movie.MovieRemoteDataSource
import com.example.moviesflickrapp.data.repository.movie.MovieRemoteDataSourceImpl
import com.example.moviesflickrapp.data.repository.movie.MovieRepo
import com.example.moviesflickrapp.data.repository.movie.MovieRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class MovieLocalDataSourceModule {

    @Binds
    abstract fun bindMovieLocalDataSource(
        movieLocalDataSource: MovieLocalDataSourceImpl
    ): MovieLocalDataSource
}


@Module
@InstallIn(ViewModelComponent::class)
abstract class MovieRemoteDataSourceModule {

    @Binds
    abstract fun bindMovieRemoteDataSource(
        movieRemoteDataSource: MovieRemoteDataSourceImpl
    ): MovieRemoteDataSource
}

@Module
@InstallIn(ViewModelComponent::class)
abstract class MovieRepositoryModule {

    @Binds
    abstract fun bindMovieRepository(
        movieRepository: MovieRepositoryImpl
    ): MovieRepo
}

package com.example.moviesflickrapp.data.repository.movie

import com.example.moviesflickrapp.base.data.BaseRepo
import com.example.moviesflickrapp.base.data.Resource
import com.example.moviesflickrapp.base.utils.NetworkConnectivityHelper
import com.example.moviesflickrapp.data.di.IODispatcher
import com.example.moviesflickrapp.data.network.model.response.Photo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val movieLocalDataSource: MovieLocalDataSource,
    private val movieRemoteDataSource: MovieRemoteDataSource,
    networkConnectivityHelper: NetworkConnectivityHelper,
    @IODispatcher coroutineDispatcher: CoroutineDispatcher
) : BaseRepo(networkConnectivityHelper, coroutineDispatcher), MovieRepo {
    override fun searchMovies(searchQuery: String): Flow<Resource<List<Photo>?>> {
        return networkWithCacheFlow(
            remoteCall = { movieRemoteDataSource.searchMovies(searchQuery).searchResult?.photoList },
            localCall = { movieLocalDataSource.searchMovies(searchQuery) },
            saveLocal = { movies ->
                if (movies != null) {
                    movieLocalDataSource.saveMovies(
                        movies
                    )
                }
            }
        )
    }
}
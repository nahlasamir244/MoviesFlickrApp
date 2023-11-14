package com.example.moviesflickrapp.base.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

abstract class BaseMockRepo(private val coroutineDispatcher: CoroutineDispatcher) {

    fun <T> mockFlow(localCall: suspend () -> T): Flow<Resource<T>> {
        return flow {
            emit(Resource.Loading)
            delay(2000)
            try {
                val data = localCall()
                emit(Resource.Success(data))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error(e))
            }
        }.flowOn(coroutineDispatcher)
    }
}
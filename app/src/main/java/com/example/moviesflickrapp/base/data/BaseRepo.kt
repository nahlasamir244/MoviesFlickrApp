package com.example.moviesflickrapp.base.data

import android.util.Log
import com.example.moviesflickrapp.base.utils.NetworkConnectivityHelper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

open class BaseRepo(
    private val networkConnectivityHelper: NetworkConnectivityHelper,
    val ioDispatcher: CoroutineDispatcher
) {

    fun <T> networkOnlyFlow(remoteCall: suspend () -> T): Flow<Resource<T>> {
        suspend fun fetchFromNetwork(): T {
            if (networkConnectivityHelper.isConnected()) return remoteCall()
            else throw NoInternetException()
        }

        return flow<Resource<T>> {
            emit(Resource.Loading)

            try {
                emit(Resource.Success(fetchFromNetwork()))
            } catch (e: Exception) {
                handleApiException(e, this)
            }
        }.flowOn(ioDispatcher)
    }

    private suspend fun <T> handleApiException(
        e: Exception,
        flowCollector: FlowCollector<Resource<T>>
    ) {
        Log.d(TAG, "Exception: ", e)
        flowCollector.emit(Resource.Error(e))
    }

    protected fun <T> networkWithCacheFlow(
        remoteCall: suspend () -> T,
        localCall: suspend () -> T?,
        saveLocal: suspend (T) -> Unit
    ): Flow<Resource<T>> {
        suspend fun fetchFromNetwork(): T {
            return remoteCall().also { response ->
                saveLocal(response)
            }
        }

        return flow<Resource<T>> {
            emit(Resource.Loading)

            try {
                val loadDataFromDB = localCall()
                val isLocalDataAvailable = (loadDataFromDB != null)
                if (isLocalDataAvailable) emit(Resource.Success(loadDataFromDB!!))

                if (networkConnectivityHelper.isConnected()) {
                    emit(Resource.Success(fetchFromNetwork()))
                } else {
                    if (!isLocalDataAvailable) emit(
                        Resource.Error(NoInternetException())
                    )
                }

            } catch (e: Exception) {
                handleApiException(e, this)
            }
        }.flowOn(ioDispatcher)
    }


    inline fun <reified T> localOnlyFlow(crossinline localCall: suspend () -> T): Flow<Resource<T>> {
        return flow {
            emit(Resource.Loading)
            try {
                val data = localCall()
                emit(Resource.Success(data))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error(e))
            }
        }.flowOn(ioDispatcher)
    }

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
        }.flowOn(Dispatchers.IO)
    }

    companion object {

        private val TAG = BaseRepo::class.simpleName

    }
}
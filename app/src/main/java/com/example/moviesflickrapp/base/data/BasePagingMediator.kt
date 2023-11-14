package com.example.moviesflickrapp.base.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.moviesflickrapp.base.data.BaseIndexPagingSource.Companion.DEFAULT_PAGE_INDEX
import com.example.moviesflickrapp.base.utils.NetworkConnectivityHelper
import com.example.moviesflickrapp.base.view.BaseEntity


/**
 * Mediator for caching paginated list from api
 */
@OptIn(ExperimentalPagingApi::class)
abstract class BasePagingMediator<Entity : BaseEntity, RemoteKey : BaseRemoteKey>(private val networkHelper: NetworkConnectivityHelper) :
    RemoteMediator<Int, Entity>() {

    override suspend fun load(
        loadType: LoadType, state: PagingState<Int, Entity>
    ): MediatorResult {

        val page = when (val pageKeyData = getKeyPageData(loadType, state)) {
            is MediatorResult.Success -> {
                return pageKeyData
            }

            else -> {
                pageKeyData as Int
            }
        }

        if (!networkHelper.isConnected())
            return MediatorResult.Error(NoInternetException())

        try {

            val response = getRemoteData(page, state.config.pageSize)
            val isEndOfList = response.isNullOrEmpty()

            val prevPage = if (page == DEFAULT_PAGE_INDEX) null else page - 1
            val nextPage = if (isEndOfList) null else page + 1


            if (!isEndOfList) {
                response?.let {
                    val indexes = it.map { newRemoteKey(it.entityId(), prevPage, nextPage) }

                    saveToLocal(loadType == LoadType.REFRESH, response, indexes)

                }
            }

            return MediatorResult.Success(endOfPaginationReached = isEndOfList)

        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }

    abstract suspend fun saveToLocal(
        isRefresh: Boolean,
        response: List<Entity>,
        indexes: List<RemoteKey>
    )

    abstract fun newRemoteKey(entityId: String, prevPage: Int?, nextPage: Int?): RemoteKey

    abstract suspend fun getRemoteData(page: Int, pageSize: Int): List<Entity>?

    /**
     * this returns the page key or the final end of list success result
     */
    private suspend fun getKeyPageData(loadType: LoadType, state: PagingState<Int, Entity>): Any {

        return when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getClosestRemoteKey(state)
                remoteKeys?.nextKey?.minus(1) ?: DEFAULT_PAGE_INDEX
            }

            LoadType.APPEND -> {
                val remoteKeys = getLastRemoteKey(state)

                // If remoteKeys is null, that means the refresh result is not in the database yet.
                // We can return Success with endOfPaginationReached = false because Paging
                // will call this method again if RemoteKeys becomes non-null.
                // If remoteKeys is NOT NULL but its nextKey is null, that means we've reached
                // the end of pagination for append.

                return remoteKeys?.nextKey
                    ?: MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
            }

            LoadType.PREPEND -> {
                val remoteKeys = getFirstRemoteKey(state)

                // If remoteKeys is null, that means the refresh result is not in the database yet.
                return remoteKeys?.prevKey
                    ?: //end of list condition reached
                    MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
            }
        }
    }

    /**
     * get the last remote key inserted which had the data
     */
    private suspend fun getLastRemoteKey(state: PagingState<Int, Entity>): RemoteKey? {
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { item -> getRemoteKeyById(item.entityId()) }
    }

    /**
     * get the first remote key inserted which had the data
     */
    private suspend fun getFirstRemoteKey(state: PagingState<Int, Entity>): RemoteKey? {
        return state.pages
            .firstOrNull { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { item -> getRemoteKeyById(item.entityId()) }
    }

    /**
     * get the closest remote key inserted which had the data
     */
    private suspend fun getClosestRemoteKey(state: PagingState<Int, Entity>): RemoteKey? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.entityId()?.let { id ->
                getRemoteKeyById(id)
            }
        }
    }

    abstract suspend fun getRemoteKeyById(id: String): RemoteKey?

}
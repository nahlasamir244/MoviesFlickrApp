package com.example.moviesflickrapp.base.data

import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState

abstract class BaseIndexPagingSource<T : Any> : PagingSource<Int, T>() {


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {

        val currentLoadingPageKey = params.key ?: DEFAULT_PAGE_INDEX

        return try {

            val list = getData(currentLoadingPageKey, params.loadSize)

            val nextKey = if (list?.size == params.loadSize) currentLoadingPageKey.plus(1) else null

            LoadResult.Page(
                data = list.orEmpty(),
                prevKey = null,  // Only paging forward
                nextKey = nextKey
            )

        } catch (e: Exception) {
            if (e is NoContentException) emptyPage()
            else LoadResult.Error(e)
        }
    }

    private fun emptyPage() = LoadResult.Page<Int, T>(
        data = emptyList(),
        prevKey = null,
        nextKey = null
    )

    abstract suspend fun getData(currentLoadingPageKey: Int, loadSize: Int): List<T>?

    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    companion object {
        const val DEFAULT_PAGE_INDEX = 0
        const val DEFAULT_PAGE_SIZE = 8
        const val DEFAULT_PREFETCH_DISTANCE = 2

        fun getDefaultPagingConfig(
            pageSize: Int = DEFAULT_PAGE_SIZE,
            initialLoadSize: Int = DEFAULT_PAGE_SIZE,
            prefetchDistance: Int = DEFAULT_PREFETCH_DISTANCE
        ) = PagingConfig(
            pageSize = pageSize,
            initialLoadSize = initialLoadSize,
            prefetchDistance = prefetchDistance
        )

    }
}
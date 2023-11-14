package com.example.moviesflickrapp.base.utils

import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter


/**
 * Handle Pagination first page states.
 *
 * Managing (loading, error & success) states of [PagingDataAdapter].
 * @param hasHeader whether this adapter has header item or not.
 * @param onInitialLoading the block to execute on initial loading.
 * @param onInitialEmpty the block to execute on initial empty.
 * @param onInitialError the block to execute on initial error.
 * @param onEachPageSuccess the block to execute on every page load success.
 */

fun PagingDataAdapter<*, *>.onStates(
    hasHeader: Boolean = false,
    onInitialLoading: () -> Unit = {},
    onInitialEmpty: () -> Unit = {},
    onInitialError: (Throwable) -> Unit = {},
    onEachPageSuccess: (isLastPage: Boolean) -> Unit = {}
) {
    addLoadStateListener { loadState ->

        val hasItems = itemCount > (if (hasHeader) 1 else 0)

        val refreshState = loadState.refresh
        val appendState = loadState.append
        val prependState = loadState.prepend

        val areAllStatesIdle =
            refreshState is LoadState.NotLoading && appendState is LoadState.NotLoading && prependState is LoadState.NotLoading

        when {
            refreshState is LoadState.NotLoading && !hasItems && appendState.endOfPaginationReached -> onInitialEmpty()
            refreshState is LoadState.Error -> onInitialError(refreshState.error)
            refreshState is LoadState.Loading && !hasItems -> onInitialLoading()
            areAllStatesIdle && hasItems -> onEachPageSuccess(appendState.endOfPaginationReached)
        }

    }
}
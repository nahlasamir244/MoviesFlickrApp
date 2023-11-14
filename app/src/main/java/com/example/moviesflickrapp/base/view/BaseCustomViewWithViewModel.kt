package com.example.moviesflickrapp.base.view

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.CallSuper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

abstract class BaseCustomViewWithViewModel<STATE : UiState, VM : BaseViewModel<STATE>>(
    context: Context,
    attrs: AttributeSet? = null
) : BaseCustomView(context, attrs) {

    protected lateinit var viewModel: VM

    protected abstract fun getVM(): VM

    protected val isViewModelInitialized
        get() = ::viewModel.isInitialized

    @CallSuper
    override fun onViewAttach() {
        viewModel = getVM()
        subscribeUiState()
        subscribeEvents()
    }

    private fun subscribeUiState() {
        viewModel.uiStateFlow
            .flowWithLifecycle(parentViewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach {
                renderState(it)
            }
            .launchIn(parentViewLifecycleOwner.lifecycleScope)
    }

    private fun subscribeEvents() {
        viewModel.singleEventFlow
            .flowWithLifecycle(parentViewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach {
                handleSingleEvent(it)
            }.launchIn(parentViewLifecycleOwner.lifecycleScope)
    }

    protected abstract fun renderState(uiState: STATE)

    protected abstract fun handleSingleEvent(event: SingleEvent)
}
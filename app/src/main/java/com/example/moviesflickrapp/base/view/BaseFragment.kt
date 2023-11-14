package com.example.photoapp.base.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.moviesflickrapp.R
import com.example.moviesflickrapp.base.view.BaseViewModel
import com.example.moviesflickrapp.base.view.SingleEvent
import com.example.moviesflickrapp.base.view.UiState
import com.gturedi.views.StatefulLayout
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


/**
 * Base fragment.
 *
 * @param DB the data binding class.
 */
abstract class BaseFragment<DB : ViewDataBinding, STATE : UiState, VM : BaseViewModel<STATE>> :
    Fragment() {

    protected open var onSingleEvent: ((SingleEvent) -> Unit)? = null

    lateinit var viewModel: VM

    abstract fun getVM(): VM

    private var _binding: DB? = null
    protected val binding: DB
        get() {
            return _binding ?: error("data binding should not be null")
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getVM()
    }

    @LayoutRes
    abstract fun getLayoutRes(): Int

    protected open fun bindViewModel() {}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = DataBindingUtil.inflate(inflater, getLayoutRes(), container, false)
        super.onCreateView(inflater, container, savedInstanceState)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeEvents()
        onViewAttach()
        subscribeUiState()
        bindViewModel()
    }

    private fun subscribeUiState() {
        viewModel.uiStateFlow
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach {
                renderState(it)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    abstract fun renderState(uiState: STATE)

    private fun subscribeEvents() {
        viewModel.singleEventFlow
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach {

            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.errorMsgEventFlow
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach {
                // applicationContext not activity context to avoid activity leak on back pressed while toast is still showing
                when (it) {
                    is BaseViewModel.ErrorMessage.StringErrorMessage -> Toast.makeText(
                        activity?.applicationContext,
                        it.message,
                        Toast.LENGTH_SHORT
                    ).show()

                    is BaseViewModel.ErrorMessage.StringResErrorMessage -> Toast.makeText(
                        activity?.applicationContext,
                        it.messageResId,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        onSingleEvent?.let {
            viewModel.singleEventFlow
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .onEach { event ->
                    it(event)
                }.launchIn(lifecycleScope)
        }
    }

    override fun onResume() {
        super.onResume()
        onViewRefresh()
    }


    /**
     * Called in [Fragment.onResume].
     */
    open fun onViewRefresh() {
    }

    /**
     * Called after the view is created (in [Fragment.onViewCreated]).
     */
    open fun onViewAttach() {
    }

    fun showLoading(stateFullLayout: StatefulLayout) {
        stateFullLayout.showLoading()
    }

    fun showEmpty(stateFullLayout: StatefulLayout) {
        stateFullLayout.showEmpty()
    }

    fun showError(stateFullLayout: StatefulLayout, error: String?, retry: () -> Unit) {
        stateFullLayout.showError(error ?: getString(R.string.something_went_wrong)) { retry() }
    }

    fun showContent(stateFullLayout: StatefulLayout) {
        stateFullLayout.showContent()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
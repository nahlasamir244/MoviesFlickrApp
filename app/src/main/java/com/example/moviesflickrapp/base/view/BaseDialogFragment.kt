package com.example.photoapp.base.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.moviesflickrapp.R
import com.example.moviesflickrapp.base.view.BaseViewModel
import com.example.moviesflickrapp.base.view.UiState
import com.gturedi.views.StatefulLayout
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


/**
 * Base class for dialog fragments.
 *
 * @param DB the data binding class.
 */
abstract class BaseDialogFragment<DB : ViewDataBinding, STATE : UiState, VM : BaseViewModel<STATE>> :
    DialogFragment() {

    lateinit var viewModel: VM

    private var _binding: DB? = null
    protected val binding: DB
        get() {
            return _binding ?: throw IllegalStateException(
                "data binding should not be null"
            )
        }

    @LayoutRes
    abstract fun getLayoutRes(): Int

    abstract fun getVM(): VM

    protected fun bindViewModel() {}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = DataBindingUtil.inflate(inflater, getLayoutRes(), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = getVM()
        subscribeEvents()
        onViewAttach()
        subscribeUiState()
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
            .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach {

            }
            .launchIn(lifecycleScope)

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
    }

    override fun onResume() {
        super.onResume()
        onViewRefresh()
    }

    open fun onViewAttach() {

    }

    open fun onViewRefresh() {

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
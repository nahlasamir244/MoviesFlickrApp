package com.example.moviesflickrapp.presentation.movies

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviesflickrapp.R
import com.example.moviesflickrapp.databinding.FragmentMoviesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoviesFragment : Fragment(), MovieAdapterHandler {

    private val viewModel: MoviesViewModel by activityViewModels()
    private var _binding: FragmentMoviesBinding? = null
    private val binding get() = _binding!!

    private val movieAdapter by lazy {
        MovieAdapter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_movies, container, false)
        super.onCreateView(inflater, container, savedInstanceState)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.moviesState.observe(viewLifecycleOwner, Observer(::renderViewState))
        viewModel.moviesEvent.observe(viewLifecycleOwner, Observer(::renderViewEvent))
        initListeners()
    }

    private fun initListeners() {
        binding.apply {
            buttonRetry.setOnClickListener {
                viewModel.invokeAction(
                    MoviesScreenContract.Action.SearchMovies(
                        textInputEditTextSearch.text.toString()
                    )
                )
            }
            textInputEditTextSearch.setOnEditorActionListener { _: TextView?, actionId: Int, _: KeyEvent? ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    viewModel.invokeAction(
                        MoviesScreenContract.Action.SearchMovies(
                            textInputEditTextSearch.text.toString()
                        )
                    )
                }
                false
            }
        }
    }

    private fun renderViewState(state: MoviesScreenContract.State) {
        when (state) {
            MoviesScreenContract.State.EmptySearchResult -> {
                handleEmptyState()
            }

            is MoviesScreenContract.State.Error -> {
                handleErrorState()
            }

            MoviesScreenContract.State.Loading -> {
                handleLoadingState()
            }

            is MoviesScreenContract.State.Success -> {
                handleSuccessState(state)
            }
        }
    }

    private fun handleSuccessState(state: MoviesScreenContract.State.Success) {
        binding.apply {
            recyclerViewMovies.apply {
                visibility = View.VISIBLE
                adapter = movieAdapter
                layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                movieAdapter.submitList(state.uiModelList)
            }
            buttonRetry.visibility = View.GONE
            progressBar.visibility = View.GONE
            textViewErrorMessage.visibility = View.GONE
        }
    }

    private fun handleLoadingState() {
        binding.apply {
            recyclerViewMovies.visibility = View.GONE
            buttonRetry.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
            textViewErrorMessage.visibility = View.GONE
        }
    }

    private fun handleErrorState() {
        binding.apply {
            recyclerViewMovies.visibility = View.GONE
            buttonRetry.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
            textViewErrorMessage.visibility = View.VISIBLE
            textViewErrorMessage.text = getString(R.string.something_went_wrong)
        }
    }

    private fun handleEmptyState() {
        binding.apply {
            recyclerViewMovies.visibility = View.GONE
            buttonRetry.visibility = View.GONE
            progressBar.visibility = View.GONE
            textViewErrorMessage.visibility = View.VISIBLE
            textViewErrorMessage.text = getString(R.string.no_search_result)
        }
    }

    private fun renderViewEvent(event: MoviesScreenContract.Event) {
        when (event) {
            MoviesScreenContract.Event.NavigateToMoviePreview -> {
                findNavController().navigate(MoviesFragmentDirections.actionMoviesFragmentToMoviePreviewFragment())
            }

            is MoviesScreenContract.Event.Warning -> {
                Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onMovieClicked(photoUiModel: PhotoUIModel) {
        viewModel.invokeAction(MoviesScreenContract.Action.OnMovieClicked(photoUiModel))
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}
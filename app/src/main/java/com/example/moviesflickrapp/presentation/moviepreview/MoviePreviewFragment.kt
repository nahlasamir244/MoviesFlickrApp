package com.example.moviesflickrapp.presentation.moviepreview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.moviesflickrapp.R
import com.example.moviesflickrapp.databinding.FragmentMoviePreviewBinding
import com.example.moviesflickrapp.presentation.movies.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoviePreviewFragment : Fragment() {
    private var binding: FragmentMoviePreviewBinding? = null
    private val moviesViewModel: MoviesViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movie_preview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMoviePreviewBinding.bind(view)
        binding?.apply {
            viewmodel = moviesViewModel
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

}
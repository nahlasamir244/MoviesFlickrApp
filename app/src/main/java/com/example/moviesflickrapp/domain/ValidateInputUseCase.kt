package com.example.moviesflickrapp.domain

import javax.inject.Inject

typealias IsValid = ValidateInputUseCase

class ValidateInputUseCase @Inject constructor() {
    operator fun invoke(input: String, inputType: InputType): Boolean {
        return when (inputType) {
            InputType.MOVIE_SEARCH_QUERY -> isMovieSearchQueryValid(input)
        }
    }

    private fun isMovieSearchQueryValid(input: String): Boolean
    //we can add any validation logic here
            = input.isNotBlank() && input.isNotEmpty()

}

//each inputType has a different validation criteria
enum class InputType {
    MOVIE_SEARCH_QUERY
}
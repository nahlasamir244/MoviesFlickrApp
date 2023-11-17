package com.example.moviesflickrapp.domain

import com.example.moviesflickrapp.data.network.model.response.Photo

class CategorizeMoviesUseCase {
    operator fun invoke(
        photos: List<Photo>,
        maximumNumberPerCategory: Int
    ): Map<String, List<Photo>> {
        val categoryToTopRatedPhotoListMap: MutableMap<String, List<Photo>> = mutableMapOf()
        photos.groupBy { photo ->
            photo.owner ?: "No Owner"
        }.forEach { categoryToPhotoListMap ->
            categoryToPhotoListMap.value.apply {
                val photosSortedByRate: MutableList<Photo> = this.toMutableList()
                photosSortedByRate.sortByDescending { photo -> photo.rate }
                val topRatedPhotos = photosSortedByRate.take(maximumNumberPerCategory)
                categoryToTopRatedPhotoListMap[categoryToPhotoListMap.key] = topRatedPhotos
            }
        }
        return categoryToTopRatedPhotoListMap
    }
}
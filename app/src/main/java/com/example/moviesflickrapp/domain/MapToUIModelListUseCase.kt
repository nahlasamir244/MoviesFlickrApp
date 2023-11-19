package com.example.moviesflickrapp.domain

import com.example.moviesflickrapp.data.mappers.toPhotoUiModel
import com.example.moviesflickrapp.data.network.model.response.Photo
import com.example.moviesflickrapp.presentation.movies.BaseUIModel
import com.example.moviesflickrapp.presentation.movies.OwnerUIModel
import javax.inject.Inject

class MapToUIModelListUseCase @Inject constructor() {
    operator fun invoke(photosMap: Map<String, List<Photo>>): List<BaseUIModel> {
        val uiModelList = mutableListOf<BaseUIModel>()
        photosMap.forEach { entry ->
            uiModelList.add(OwnerUIModel(entry.key))
            entry.value.forEach { photo ->
                uiModelList.add(photo.toPhotoUiModel())
            }
        }
        return uiModelList
    }
}
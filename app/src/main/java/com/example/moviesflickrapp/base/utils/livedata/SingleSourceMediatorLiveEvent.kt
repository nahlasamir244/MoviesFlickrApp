package com.example.moviesflickrapp.base.utils.livedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

class SingleSourceMediatorLiveEvent<T> : SingleMediatorLiveEvent<T>() {

    private var lastSource: LiveData<*>? = null

    override fun <S> addSource(source: LiveData<S>, onChanged: Observer<in S>) {
        lastSource?.let { removeSource(it) }
        lastSource = source
        super.addSource(source, onChanged)
    }
}
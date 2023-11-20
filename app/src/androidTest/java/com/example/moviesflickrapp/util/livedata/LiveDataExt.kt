package com.example.moviesflickrapp.util.livedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

/**
 * LiveData utils for tests
 *
 * @author joaocevada
 */

fun <T> LiveData<T>.getValue(): T? {
    var response: T? = null
    val observer = Observer<T> { response = it }
    observeForever(observer)
    removeObserver(observer)
    return response
}

package com.example.moviesflickrapp.data.di

import javax.inject.Qualifier

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class BaseUrl

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class DatabaseName

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class IODispatcher
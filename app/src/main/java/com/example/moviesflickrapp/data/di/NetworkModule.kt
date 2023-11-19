package com.example.moviesflickrapp.data.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.moviesflickrapp.data.network.api.Api
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideApplicationScope() = CoroutineScope(SupervisorJob())

    @Provides
    @Singleton
    @IODispatcher
    fun provideIODispatcher() = Dispatchers.IO

    @Provides
    @BaseUrl
    @Singleton
    fun provideBaseUrl() = "https://www.flickr.com/services/rest/"

    @Provides
    @Singleton
    fun provideOkHttpClient(@ApplicationContext context: Context): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(ChuckerInterceptor(context))
            .build()
    }

    @Singleton
    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }


    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient, @BaseUrl baseUrl: String,
        gsonConverterFactory: GsonConverterFactory
    ): Api =
        Retrofit.Builder()
            .addConverterFactory(gsonConverterFactory)
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .build().create(Api::class.java)

}
package com.example.moviesflickrapp.base.utils

import android.content.Context
import com.google.gson.Gson

class JsonUtils(val context: Context) {

    inline fun <reified T> fromJson(jsonString: String?): T? =
        Gson().fromJson(jsonString, T::class.java)

    inline fun <reified T> toJson(model: T?): String? = Gson().toJson(model, T::class.java)

    inline fun <reified T> fromJsonArray(jsonString: String?): List<T>? = Gson().fromJson<List<T>>(
        jsonString,
        object : com.google.gson.reflect.TypeToken<List<T?>?>() {}.type
    )

    inline fun <reified T> toJsonArray(models: List<T>?): String? =
        Gson().toJson(models, object : com.google.gson.reflect.TypeToken<List<T?>?>() {}.type)

    inline fun <reified T> jsonAssetFileToObj(assetFilePath: String): T? {
        val jsonString = context.assets.open(assetFilePath).bufferedReader().use { it.readText() }
        return fromJson(jsonString)
    }

    inline fun <reified T> jsonAssetFileToArray(assetFilePath: String): List<T>? {
        val jsonString = context.assets.open(assetFilePath).bufferedReader().use { it.readText() }
        return fromJsonArray(jsonString)
    }

}

package com.example.moviesflickrapp.base.utils

import android.annotation.SuppressLint
import com.blankj.utilcode.util.NetworkUtils

open class NetworkConnectivityHelper {
    @SuppressLint("MissingPermission")
    open fun isConnected() = NetworkUtils.isConnected()
}
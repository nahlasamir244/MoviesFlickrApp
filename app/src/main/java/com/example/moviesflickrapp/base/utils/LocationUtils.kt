package com.example.moviesflickrapp.base.utils

import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.util.Log
import android.widget.Toast
import com.example.moviesflickrapp.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale

suspend fun getCountryCodeFromLocation(context: Context, location: Location) =
    withContext(Dispatchers.IO) {
        val geocoder = Geocoder(context)

        try {
            val addresses = geocoder.getFromLocation(
                location.latitude,
                location.longitude,
                1
            )
            return@withContext addresses?.get(0)?.countryCode
        } catch (e: Exception) {
            Log.e(
                TAG,
                "An error occurred while getting country code from location with Geocoder",
                e
            )
            return@withContext null
        }
    }

suspend fun getLocationAddress(context: Context, location: Location) =
    withContext(Dispatchers.IO) {
        val locale = Locale.getDefault()
        val geocoder = Geocoder(context, locale)
        if (!Geocoder.isPresent()) {
            Toast.makeText(
                context,
                context.getString(R.string.error_no_geocoder_service),
                Toast.LENGTH_LONG
            ).show()
        }
        try {
            val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            addresses?.get(0)
        } catch (e: Exception) {
            // Catch if no location address are found
            Log.e(
                TAG,
                "An error occurred while getting country code from location with Geocoder",
                e
            )
            null
        }
    }

private const val TAG = "LocationUtils"
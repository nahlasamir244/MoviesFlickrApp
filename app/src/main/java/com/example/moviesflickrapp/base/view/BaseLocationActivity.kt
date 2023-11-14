package com.example.moviesflickrapp.base.view

import android.content.Intent
import android.location.Address
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.Utils
import com.example.moviesflickrapp.R
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.yayandroid.locationmanager.LocationManager
import com.yayandroid.locationmanager.configuration.DefaultProviderConfiguration
import com.yayandroid.locationmanager.configuration.GooglePlayServicesConfiguration
import com.yayandroid.locationmanager.configuration.LocationConfiguration
import com.yayandroid.locationmanager.configuration.PermissionConfiguration
import com.yayandroid.locationmanager.constants.ProcessType
import com.yayandroid.locationmanager.listener.LocationListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Suppress("DEPRECATION")
abstract class BaseLocationActivity<DB : ViewDataBinding, STATE : UiState, VM : BaseViewModel<STATE>> :
    BaseActivity<DB, STATE, VM>(), LocationListener {
    private var locationManager: LocationManager? = null


    open fun getLocationConfiguration(): LocationConfiguration = BASE_LOCATION_CONF

    protected open fun getLocation() {
        return locationManager?.get() ?: throw IllegalStateException(
            "locationManager is null. "
                    + "Make sure you call 'super.initialize' before attempting to getLocation"
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        locationManager = LocationManager.Builder(applicationContext)
            .configuration(getLocationConfiguration())
            .activity(this)
            .notify(this)
            .build()

        super.onCreate(savedInstanceState)
    }

    override fun onDestroy() {
        locationManager?.onDestroy()
        super.onDestroy()
    }

    override fun onPause() {
        locationManager?.onPause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        locationManager?.onResume()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        locationManager?.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        locationManager?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    override fun onProcessTypeChanged(@ProcessType processType: Int) {

    }

    override fun onPermissionGranted(alreadyHadPermission: Boolean) {

    }

    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
        Log.d(javaClass.simpleName, "onStatusChanged $provider $status")
    }

    override fun onProviderEnabled(provider: String) {
        Log.d(javaClass.simpleName, "onProviderEnabled $provider")
    }

    override fun onProviderDisabled(provider: String) {
        Log.e(javaClass.simpleName, "onProviderDisabled $provider")
    }

    final override fun onLocationChanged(location: Location?) {
        location?.let {
            onGetLocation(it)
        }
    }

    /**
     * override if don't need the DetailedAddress from Geocoder
     * otherwise override [onDetailedAddressChanged]
     */
    open fun onGetLocation(location: Location) {
        lifecycleScope.launch {
            withContext(Dispatchers.Main) {
            }
        }
    }

    open fun onDetailedAddressChanged(address: Address?) {
    }

    companion object {
        val BASE_LOCATION_CONF: LocationConfiguration
            get() {
                val isGooglePlayAvailable = GoogleApiAvailability.getInstance()
                    .isGooglePlayServicesAvailable(Utils.getApp()) == ConnectionResult.SUCCESS

                val conf = LocationConfiguration.Builder()
                    .askForPermission(
                        PermissionConfiguration.Builder()
                            .rationaleMessage(StringUtils.getString(R.string.enable_location_rational_message))
                            .build()
                    )

                return conf.also {
                    if (isGooglePlayAvailable) {
                        it.useGooglePlayServices(
                            GooglePlayServicesConfiguration.Builder()
                                .failOnSettingsApiSuspended(true)
                                .build()
                        )
                    } else {
                        it.useDefaultProviders(
                            DefaultProviderConfiguration.Builder()
                                .gpsMessage(StringUtils.getString(R.string.location_gps_message))
                                .build()
                        )
                    }
                }.build()
            }

    }

}
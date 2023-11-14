package com.example.moviesflickrapp.base.view

import android.content.Intent
import android.location.Location
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.lifecycleScope
import com.yayandroid.locationmanager.LocationManager
import com.yayandroid.locationmanager.configuration.LocationConfiguration
import com.yayandroid.locationmanager.constants.ProcessType
import com.yayandroid.locationmanager.listener.LocationListener
import kotlinx.coroutines.launch

@Suppress("OVERRIDE_DEPRECATION", "DEPRECATION")
abstract class BaseLocationBottomSheetFragment<DB : ViewDataBinding, STATE : UiState, VM : BaseViewModel<STATE>> :
    BaseBottomSheetFragment<DB, STATE, VM>(), LocationListener {
    private var locationManager: LocationManager? = null

    open fun getLocationConfiguration(): LocationConfiguration =
        BaseLocationActivity.BASE_LOCATION_CONF


    protected open fun getLocation() {
        return locationManager?.get() ?: throw IllegalStateException(
            "locationManager is null. "
                    + "Make sure onCreate was called before attempting to getLocation"
        )
    }

    fun stopLocationUpdates() {
        locationManager?.cancel()
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locationManager = LocationManager.Builder(requireActivity().application)
            .configuration(getLocationConfiguration())
            .fragment(this)
            .notify(this)
            .build()
    }

    @CallSuper
    override fun onResume() {
        super.onResume()
        locationManager?.onResume()
    }

    @CallSuper
    override fun onPause() {
        locationManager?.onPause()
        super.onPause()
    }

    @CallSuper
    override fun onDestroy() {
        locationManager?.onDestroy()
        super.onDestroy()
    }

    @CallSuper
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        locationManager?.onActivityResult(requestCode, resultCode, data)
    }

    @CallSuper
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        locationManager?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onProcessTypeChanged(@ProcessType processType: Int) {
        // override if needed
    }

    override fun onPermissionGranted(alreadyHadPermission: Boolean) {
        // override if needed
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        // override if needed
    }

    override fun onProviderEnabled(provider: String?) {
        // override if needed
    }

    override fun onProviderDisabled(provider: String?) {
        // override if needed
    }

    /**
     * override if don't need the DetailedAddress from Geocoder
     * otherwise override [onDetailedAddressChanged]
     */
    override fun onLocationChanged(location: Location?) {
        lifecycleScope.launch {
            location?.let { location ->

            }
        }
    }
}
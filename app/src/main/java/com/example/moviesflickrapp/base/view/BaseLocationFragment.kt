package com.example.moviesflickrapp.base.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.annotation.CallSuper
import androidx.databinding.ViewDataBinding
import com.example.photoapp.base.view.BaseFragment
import com.yayandroid.locationmanager.LocationManager
import com.yayandroid.locationmanager.configuration.LocationConfiguration
import com.yayandroid.locationmanager.constants.ProcessType
import com.yayandroid.locationmanager.listener.LocationListener

@Suppress("OVERRIDE_DEPRECATION")
abstract class BaseLocationFragment<DB : ViewDataBinding, STATE : UiState, VM : BaseViewModel<STATE>> :
    BaseFragment<DB, STATE, VM>(), LocationListener {

    private var locationManager: LocationManager? = null

    open fun getLocationConfiguration(): LocationConfiguration =
        BaseLocationActivity.BASE_LOCATION_CONF


    protected open fun getLocation() {
        if (locationManager != null) {
            locationManager?.get()
        } else {
            throw IllegalStateException(
                "locationManager is null. "
                        + "Make sure onCreate was called before attempting to getLocation"
            )
        }
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
        Log.d(javaClass.simpleName, "onStatusChanged $provider $status")
    }

    override fun onProviderEnabled(provider: String?) {
        Log.d(javaClass.simpleName, "onProviderEnabled $provider")
    }

    override fun onProviderDisabled(provider: String?) {
        Log.e(javaClass.simpleName, "onProviderDisabled $provider")
    }

}
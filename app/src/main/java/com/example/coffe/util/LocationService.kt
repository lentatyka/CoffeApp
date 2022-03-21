package com.example.coffe.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.example.coffe.ui.MainActivity
import com.google.android.gms.location.*
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@FragmentScoped
class LocationService @Inject constructor(
    @ActivityContext private val activity: Context,
    private val callback: Callback
) : LocationCallback() {

    companion object {
        private const val INTERVAL = 20000L // 20 seconds
    }

    interface Callback {
        fun invoke(location: Location?)
    }

    private val locationManager: FusedLocationProviderClient
    private val locationRequest: LocationRequest

    init {
        locationManager = LocationServices.getFusedLocationProviderClient(activity)
        locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    //
    fun stopUpdate(){
        locationManager.removeLocationUpdates(this)
    }
    fun startUpdate() {
        checkPermission()
        locationManager.lastLocation.addOnSuccessListener {
            callback.invoke(it)
        }.addOnFailureListener {
            locationManager.requestLocationUpdates(
                locationRequest, this, Looper.getMainLooper()
            )
        }
    }

    //
    private fun checkPermission() {
        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                (activity as MainActivity),
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                101
            )
            return
        }
    }

    override fun onLocationResult(locationResult: LocationResult) {
        var lastLocation: Location? = null
        locationResult.locations.forEach { location ->
            location?.let {
                if (lastLocation == null || location.accuracy < lastLocation!!.accuracy)
                    lastLocation = location
            }
        }
        callback.invoke(lastLocation)
    }

    override fun onLocationAvailability(loc: LocationAvailability) {
        if(!loc.isLocationAvailable){
            stopUpdate()
            callback.invoke(null)
        }
    }

}
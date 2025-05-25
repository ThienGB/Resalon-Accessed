package com.example.reservationdemo.helper

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.Locale

fun getCurrentLocation(
    context: Context,
    fusedLocationClient: FusedLocationProviderClient,
    onLocationReceived: (city: String) -> Unit,
    onError: ((Exception) -> Unit)? = null
) {
    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
    != PackageManager.PERMISSION_GRANTED
    ) {
        Toast.makeText(context, "Location permission not granted", Toast.LENGTH_SHORT).show()
        onError?.invoke(SecurityException("Location permission not granted"))
        return
    }
    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
        if (location != null) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val geocoder = Geocoder(context, Locale.getDefault())
                    val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                    val city = if (!addresses.isNullOrEmpty()) {
                        addresses[0].locality ?: addresses[0].adminArea ?: "City unknown"
                    } else {
                        "City unknown"
                    }
                    withContext(Dispatchers.Main) {
                        onLocationReceived(city)
                    }
                } catch (e: IOException) {
                    withContext(Dispatchers.Main) {
                        onError?.invoke(e)
                    }
                }
            }
        } else {
            // Location null
            onError?.invoke(NullPointerException("Location is null"))
        }
    }.addOnFailureListener {
        onError?.invoke(it)
    }
}
package com.example.myapplication.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow


class LocationHelper(private val context: Context) {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)


    fun tienePermisoUbicacion(): Boolean {
        return ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
        ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun obtenerUltimaUbicacion(onResult: (Location?) -> Unit) {
        if (!tienePermisoUbicacion()) {
            onResult(null)
            return
        }

        try {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    onResult(location)
                }
                .addOnFailureListener {
                    onResult(null)
                }
        } catch (e: SecurityException) {
            onResult(null)
        }
    }

    fun obtenerUbicacionEnTiempoReal(intervaloMs: Long = 10_000L): Flow<Location> = callbackFlow {
        if (!tienePermisoUbicacion()) {
            close()
            return@callbackFlow
        }

        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            intervaloMs
        )
            .setMinUpdateIntervalMillis(intervaloMs / 2)
            .setWaitForAccurateLocation(true)
            .build()

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let { location ->
                    trySend(location)
                }
            }
        }

        try {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        } catch (e: SecurityException) {
            close(e)
        }

        awaitClose {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }

    fun calcularDistanciaKm(
        latOrigen: Double, lngOrigen: Double,
        latDestino: Double, lngDestino: Double
    ): Double {
        val resultado = FloatArray(1)
        Location.distanceBetween(latOrigen, lngOrigen, latDestino, lngDestino, resultado)
        return resultado[0].toDouble() / 1000.0
    }
}

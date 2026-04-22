package com.smartx.rfidreader.core.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.resume

class GpsHelper(private val context: Context) {

    private val TAG = "GpsHelper"
    private val fusedClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    private fun hasPermission(): Boolean =
        ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
        ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

    /**
     * Tenta obter a última localização conhecida.
     * Em seguida, se nula ou antiga, solicita uma atualização rápida.
     * Timeout total de 4 segundos.
     * Retorna null se permissão negada ou falhar.
     */
    suspend fun getLocation(): Pair<Double, Double>? {
        if (!hasPermission()) {
            Log.w(TAG, "Permissão de localização não concedida")
            return null
        }
        return withTimeoutOrNull(4_000L) {
            try {
                val lastKnown = getLastKnownLocation()
                if (lastKnown != null) return@withTimeoutOrNull lastKnown
                requestSingleUpdate()
            } catch (e: SecurityException) {
                Log.e(TAG, "SecurityException ao obter localização", e)
                null
            }
        }
    }

    private suspend fun getLastKnownLocation(): Pair<Double, Double>? =
        suspendCancellableCoroutine { cont ->
            try {
                fusedClient.lastLocation
                    .addOnSuccessListener { loc: Location? ->
                        if (loc != null) cont.resume(Pair(loc.latitude, loc.longitude))
                        else cont.resume(null)
                    }
                    .addOnFailureListener {
                        Log.w(TAG, "lastLocation falhou", it)
                        cont.resume(null)
                    }
            } catch (e: SecurityException) {
                cont.resume(null)
            }
        }

    private suspend fun requestSingleUpdate(): Pair<Double, Double>? =
        suspendCancellableCoroutine { cont ->
            val req = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000L)
                .setMaxUpdateAgeMillis(2000L)
                .setMaxUpdates(1)
                .build()

            val cb = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    fusedClient.removeLocationUpdates(this)
                    val loc = result.lastLocation
                    if (loc != null) cont.resume(Pair(loc.latitude, loc.longitude))
                    else cont.resume(null)
                }
            }
            cont.invokeOnCancellation { fusedClient.removeLocationUpdates(cb) }
            try {
                fusedClient.requestLocationUpdates(req, cb, Looper.getMainLooper())
            } catch (e: SecurityException) {
                cont.resume(null)
            }
        }
}

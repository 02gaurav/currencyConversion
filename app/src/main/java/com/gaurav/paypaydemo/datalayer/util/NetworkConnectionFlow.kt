package com.gaurav.paypaydemo.datalayer.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkConnectionFlow
@Inject
constructor (val context: Context) {

    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    fun observeNetworkStatus(): Flow<Boolean> = callbackFlow {
        // Emit the initial state of the network connection
        trySend(isNetworkAvailable())

        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                trySend(true) // Emit true when network is available
            }

            override fun onLost(network: Network) {
                trySend(false) // Emit false when network is lost
            }
        }

        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)

        awaitClose { // Cleanup on close
            connectivityManager.unregisterNetworkCallback(networkCallback)
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val networkCapabilities =
            connectivityManager.activeNetwork?.let {
                connectivityManager.getNetworkCapabilities(it)
            }
        return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }
}
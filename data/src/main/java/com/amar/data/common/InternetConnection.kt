package com.amar.data.common

import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.content.Context

object InternetConnection {
    fun isAvailable(context: Context): Boolean {
        // TODO deprecated in 29 API use networkCallback for > API 10
        val connectivity = context.getSystemService(
            Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivity.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true
        println(" <<<<<<<<<<<<<<<< Check Connection >>>>>>>>>>>>>>>>> " + (activeNetwork?.isConnectedOrConnecting == true))
        return true;
    }
}
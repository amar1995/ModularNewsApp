package com.amar.data.common

import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.content.Context
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketAddress

object InternetConnection {
    fun isAvailable(context: Context): Boolean {
        /* --------- old version code
//        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        val networkInfo = connectivityManager.activeNetworkInfo
//        return networkInfo != null && networkInfo.isConnectedOrConnecting
         */
        // final result
        var result = false

        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?

        // checking if wifi or other cellular network is connected or not
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            cm?.run {
                cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                    result = when {
                        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                        else -> false
                    }
                }
            }
        } else {
            cm?.run {
                cm.activeNetworkInfo?.run {
                    if (type == ConnectivityManager.TYPE_WIFI) {
                        result = true
                    } else if (type == ConnectivityManager.TYPE_MOBILE) {
                        result = true
                    }
                }
            }
        }

        if (!result) {
            return result
        }

        // checking if connected to wifi or cellular network but there is no internet

        try {
            val timeoutMs = 2000
            val socket: Socket = Socket()

            runBlocking {
                withContext(Dispatchers.IO) {
                    val socketAddr: SocketAddress = InetSocketAddress("8.8.8.8", 53);
                    socket.connect(socketAddr, timeoutMs);
                    socket.close();
                }
            }
            return result;
        } catch (e: Exception) {
            return !result;
        }
    }
}
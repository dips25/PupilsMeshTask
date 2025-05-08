package com.task.pupilsmeshtask.NetworkUtils

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkRequest
import androidx.lifecycle.LiveData

class NetworkConnectionLiveData(c:Context) : LiveData<Boolean>() {

    private val connectivityManager:ConnectivityManager = c.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    
    private val networkCallback = object : NetworkCallback() {

        override fun onAvailable(network: Network) {

            postValue(true)
        }

        override fun onLost(network: Network) {

            postValue(false)
        }
        
    }



    override fun onActive() {
        super.onActive()

        val request = NetworkRequest.Builder().build()
        connectivityManager.registerNetworkCallback(request,networkCallback)
        postValue(connectivityManager.activeNetworkInfo?.isConnected==true)
    }

    override fun onInactive() {
        super.onInactive()
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }


}
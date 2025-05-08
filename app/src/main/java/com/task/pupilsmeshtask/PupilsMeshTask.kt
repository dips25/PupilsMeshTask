package com.task.pupilsmeshtask

import android.app.Application
import com.task.pupilsmeshtask.NetworkUtils.NetworkConnectionLiveData

class PupilsMeshTask: Application() {

    var networkStatus: NetworkConnectionLiveData?=null
    override fun onCreate() {
        super.onCreate()

        networkStatus = NetworkConnectionLiveData(baseContext)
    }
}
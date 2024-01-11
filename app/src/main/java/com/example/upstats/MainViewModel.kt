package com.example.upstats


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class MainViewModel(application: Application) : AndroidViewModel(application) {
    val totalTimeForeground: MutableLiveData<Long> = MutableLiveData()
    val totalTimeBackground: MutableLiveData<Long> = MutableLiveData()
    val currentTimeForeground: MutableLiveData<Long> = MutableLiveData()
    val currentTimeBackground: MutableLiveData<Long> = MutableLiveData()


    private val lifecycleListener = AppLifecycleListener(application) { newForegroundTime, newBackgroundTime, newCurrentForegroundTime, newCurrentBackgroundTime   ->
        totalTimeForeground.value= (newForegroundTime)
        totalTimeBackground.postValue(newBackgroundTime)

        currentTimeForeground.postValue(newCurrentForegroundTime)
        currentTimeBackground.postValue(newCurrentBackgroundTime)
    }

    init {
        application.registerActivityLifecycleCallbacks(lifecycleListener)
    }
}

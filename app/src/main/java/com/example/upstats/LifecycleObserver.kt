package com.example.upstats

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AppLifecycleListener(private val context: Application, private val updateTime: (Long, Long, Long, Long) -> Unit) : Application.ActivityLifecycleCallbacks {
    private var incrementForegroundScalaInMs: Long = 10
    private var lastBackgroundTime: Long = 0

    private var currentForegroundTime: Long = 0


    private var updateJob: Job? = null

    init {

        updateJob?.cancel()
        startOrUpdateForegroundTimeTracking()

    }
    private fun startOrUpdateForegroundTimeTracking() {

        currentForegroundTime = 0
        updateJob = CoroutineScope(Dispatchers.Default).launch {
            while (isActive) {
                withContext(Dispatchers.Main) {

                    saveForegroundTime(loadForegroundTime() + incrementForegroundScalaInMs)
                    currentForegroundTime += incrementForegroundScalaInMs

                    updateTime(loadForegroundTime(), loadBackgroundTime(), currentForegroundTime, loadBackgroundCurrentTime() )
                }
                delay(incrementForegroundScalaInMs)
            }
        }
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
    override fun onActivityStarted(activity: Activity) {}
    override fun onActivityResumed(activity: Activity) {


        val elapsedBackgroundTime = System.currentTimeMillis() - lastBackgroundTime
        saveBackgroundTime(loadBackgroundTime() + elapsedBackgroundTime)
        saveBackgroundCurrentTime(elapsedBackgroundTime)


        updateJob?.cancel()
        startOrUpdateForegroundTimeTracking()

    }

    override fun onActivityPaused(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {
        updateJob?.cancel()
        lastBackgroundTime = System.currentTimeMillis()

    }
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {}

    private fun saveForegroundTime(foregroundTime: Long) {
        val sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putLong("totalForegroundTime", foregroundTime).apply()
    }

    private fun loadForegroundTime(): Long {
        val sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getLong("totalForegroundTime", 0)
    }

    private fun saveBackgroundTime(backgroundTime: Long) {
        val sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putLong("totalBackgroundTime", backgroundTime).apply()
    }

    private fun loadBackgroundTime(): Long {
        val sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getLong("totalBackgroundTime", 0)
    }

    private fun saveBackgroundCurrentTime(backgroundTime: Long) {
        val sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putLong("totalBackgroundCurrentTime", backgroundTime).apply()
    }

    private fun loadBackgroundCurrentTime(): Long {
        val sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getLong("totalBackgroundCurrentTime", 0)
    }
}

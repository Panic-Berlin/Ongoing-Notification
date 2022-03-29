package com.exemple.testongoingnotification

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.work.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        workManager()
    }

    private fun workManager() {
        applicationContext
        val constraints = Constraints.Builder()
            .setRequiresCharging(false)
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .setRequiresBatteryNotLow(false)
            .build()
        val request = PeriodicWorkRequest.Builder(
            InfoNotifyReceiver::class.java,
            2,
            TimeUnit.HOURS
        ).setConstraints(constraints)
            .build()
        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(
                "work_id",
                ExistingPeriodicWorkPolicy.REPLACE,
                request
            )
    }
}

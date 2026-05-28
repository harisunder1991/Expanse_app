package com.expanse.autopilot

import android.app.Application
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.expanse.autopilot.worker.AutoSweepWorker
import java.util.concurrent.TimeUnit

class ExpanseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        
        // Enqueue automated background savings goal audit sweeps
        val autoSweepRequest = PeriodicWorkRequestBuilder<AutoSweepWorker>(
            12, TimeUnit.HOURS
        ).build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "AutoSweepWorkerWork",
            ExistingPeriodicWorkPolicy.KEEP,
            autoSweepRequest
        )
    }
}

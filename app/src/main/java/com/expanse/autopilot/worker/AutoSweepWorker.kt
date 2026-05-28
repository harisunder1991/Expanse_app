package com.expanse.autopilot.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.expanse.autopilot.data.repository.FinanceRepository
import kotlinx.coroutines.flow.firstOrNull

class AutoSweepWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val repo = FinanceRepository(applicationContext)
            
            // Reconcile and audit goal completions
            val activeGoals = repo.getActiveGoals().firstOrNull() ?: emptyList()
            for (goal in activeGoals) {
                if (goal.currentAmount >= goal.targetAmount) {
                    val db = com.expanse.autopilot.data.local.AppDatabase.getDatabase(applicationContext)
                    db.savingsGoalDao().updateCompletion(goal.id, true)
                }
            }
            
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }
}

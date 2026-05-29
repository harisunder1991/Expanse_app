package com.expanse.autopilot.data.repository

import android.content.Context
import com.expanse.autopilot.data.local.AppDatabase
import com.expanse.autopilot.data.local.entity.BudgetCategoryEntity
import com.expanse.autopilot.data.local.entity.SavingsGoalEntity
import com.expanse.autopilot.data.local.entity.TransactionEntity
import com.expanse.autopilot.domain.engine.BudgetEngine
import com.expanse.autopilot.domain.engine.SweepEngine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class FinanceRepository(context: Context) {

    private val db = AppDatabase.getDatabase(context)
    private val transactionDao = db.transactionDao()
    private val budgetDao = db.budgetDao()
    private val savingsGoalDao = db.savingsGoalDao()

    private val budgetEngine = BudgetEngine()
    private val sweepEngine = SweepEngine()

    fun getAllTransactions(): Flow<List<TransactionEntity>> = transactionDao.getAllTransactionsFlow()
    fun getAllBudgets(): Flow<List<BudgetCategoryEntity>> = budgetDao.getAllBudgetsFlow()
    fun getActiveGoals(): Flow<List<SavingsGoalEntity>> = savingsGoalDao.getActiveGoalsFlow()

    suspend fun addTransaction(
        amount: Double,
        type: String, // "DEBIT", "CREDIT", "SWEEP"
        category: String, // "FIXED", "FLEXIBLE", "SAVINGS"
        description: String,
        isAutoScraped: Boolean,
        subCategory: String = "General",
        account: String = "Secure Bank"
    ) = withContext(Dispatchers.IO) {
        val timestamp = System.currentTimeMillis()
        val newTx = TransactionEntity(
            amount = amount,
            type = type,
            category = category,
            description = description,
            timestamp = timestamp,
            isAutoScraped = isAutoScraped,
            subCategory = subCategory,
            account = account
        )

        // Insert primary transaction
        transactionDao.insertTransaction(newTx)

        if (type == "CREDIT") {
            // Apply 50/30/20 splitting engine rules
            val split = budgetEngine.calculateEnvelopeSplit(amount)
            
            // Adjust allocations
            budgetDao.incrementLimit("FIXED", split.fixedAmount)
            budgetDao.incrementLimit("FLEXIBLE", split.flexibleAmount)
            budgetDao.incrementLimit("SAVINGS", split.savingsAmount)

        } else if (type == "DEBIT") {
            // Increment the spent balance
            budgetDao.incrementSpent(category, amount)

            // If it is flexible budget daily expense, calculate and trigger micro-savings sweep
            if (category == "FLEXIBLE") {
                val sweepAmt = sweepEngine.calculateSweepAmount(amount)
                if (sweepAmt > 0) {
                    // 1. Create SWEEP Transaction
                    val sweepTx = TransactionEntity(
                        amount = sweepAmt,
                        type = "SWEEP",
                        category = "SAVINGS",
                        description = "Round-up Sweep ($description)",
                        timestamp = System.currentTimeMillis(),
                        isAutoScraped = isAutoScraped,
                        subCategory = "Savings Sweep",
                        account = account
                    )
                    transactionDao.insertTransaction(sweepTx)

                    // 2. Increment spent of Flexible category by sweep amount to subtract from Safe-to-Spend
                    budgetDao.incrementSpent("FLEXIBLE", sweepAmt)

                    // 3. Add to the first active Savings Goal if exists
                    db.runInTransaction {
                        // Using a simple transaction runner block to fetch active goals
                        // and apply the increment
                        db.openHelper.writableDatabase.execSQL(
                            "UPDATE savings_goals SET currentAmount = currentAmount + ? " +
                                    "WHERE id = (SELECT id FROM savings_goals WHERE isCompleted = 0 ORDER BY targetDate ASC LIMIT 1)",
                            arrayOf(sweepAmt)
                        )
                    }
                }
            }
        }
    }

    suspend fun deleteTransaction(transaction: TransactionEntity) = withContext(Dispatchers.IO) {
        // Delete primary transaction from db
        transactionDao.deleteTransaction(transaction)

        val type = transaction.type
        val amount = transaction.amount
        val category = transaction.category

        if (type == "CREDIT") {
            // Reverse 50/30/20 splitting rules
            val split = budgetEngine.calculateEnvelopeSplit(amount)
            budgetDao.incrementLimit("FIXED", -split.fixedAmount)
            budgetDao.incrementLimit("FLEXIBLE", -split.flexibleAmount)
            budgetDao.incrementLimit("SAVINGS", -split.savingsAmount)
        } else if (type == "DEBIT") {
            // Reverse spent balance
            budgetDao.incrementSpent(category, -amount)

            // Reverse micro-savings sweep if it was generated
            if (category == "FLEXIBLE") {
                val sweepAmt = sweepEngine.calculateSweepAmount(amount)
                if (sweepAmt > 0) {
                    db.runInTransaction {
                        db.openHelper.writableDatabase.execSQL(
                            "DELETE FROM transactions WHERE type = 'SWEEP' AND description = ? AND amount = ?",
                            arrayOf("Round-up Sweep (${transaction.description})", sweepAmt)
                        )
                        db.openHelper.writableDatabase.execSQL(
                            "UPDATE budget_categories SET currentSpent = currentSpent - ? WHERE categoryId = 'FLEXIBLE'",
                            arrayOf(sweepAmt)
                        )
                        db.openHelper.writableDatabase.execSQL(
                            "UPDATE savings_goals SET currentAmount = currentAmount - ? " +
                                    "WHERE id = (SELECT id FROM savings_goals WHERE isCompleted = 0 ORDER BY targetDate ASC LIMIT 1)",
                            arrayOf(sweepAmt)
                        )
                    }
                }
            }
        } else if (type == "SWEEP") {
            // Deleting a sweep directly (rare manual deletion)
            budgetDao.incrementSpent("FLEXIBLE", -amount)
            db.runInTransaction {
                db.openHelper.writableDatabase.execSQL(
                    "UPDATE savings_goals SET currentAmount = currentAmount - ? " +
                            "WHERE id = (SELECT id FROM savings_goals WHERE isCompleted = 0 ORDER BY targetDate ASC LIMIT 1)",
                    arrayOf(amount)
                )
            }
        }
    }

    suspend fun createSavingsGoal(name: String, target: Double, durationMonths: Int) = withContext(Dispatchers.IO) {
        val targetTime = System.currentTimeMillis() + (durationMonths * 30L * 24 * 60 * 60 * 1000)
        val goal = SavingsGoalEntity(
            goalName = name,
            targetAmount = target,
            currentAmount = 0.0,
            targetDate = targetTime
        )
        savingsGoalDao.insertGoal(goal)
    }

    suspend fun clearAllData() = withContext(Dispatchers.IO) {
        transactionDao.deleteAllTransactions()
        budgetDao.deleteAllBudgets()
        budgetDao.insertBudgets(
            listOf(
                BudgetCategoryEntity("FIXED", 0.0, 0.0),
                BudgetCategoryEntity("FLEXIBLE", 0.0, 0.0),
                BudgetCategoryEntity("SAVINGS", 0.0, 0.0)
            )
        )
    }
}

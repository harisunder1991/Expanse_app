package com.expanse.autopilot.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.expanse.autopilot.data.local.dao.BudgetDao
import com.expanse.autopilot.data.local.dao.SavingsGoalDao
import com.expanse.autopilot.data.local.dao.TransactionDao
import com.expanse.autopilot.data.local.entity.BudgetCategoryEntity
import com.expanse.autopilot.data.local.entity.SavingsGoalEntity
import com.expanse.autopilot.data.local.entity.TransactionEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.zetetic.database.sqlcipher.SupportOpenHelperFactory

@Database(
    entities = [TransactionEntity::class, BudgetCategoryEntity::class, SavingsGoalEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun transactionDao(): TransactionDao
    abstract fun budgetDao(): BudgetDao
    abstract fun savingsGoalDao(): SavingsGoalDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "expanse_secure.db"
                ).apply {
                    // SQLCipher Factory setup for encryption
                    try {
                        val keyManager = DatabaseKeyManager(context.applicationContext)
                        val passphrase = keyManager.getOrCreatePassphrase()
                        val factory = SupportOpenHelperFactory(passphrase)
                        openHelperFactory(factory)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // Prepopulate default envelope budget categories
                        CoroutineScope(Dispatchers.IO).launch {
                            val database = getDatabase(context)
                            database.budgetDao().insertBudgets(
                                listOf(
                                    BudgetCategoryEntity("FIXED", allocatedLimit = 0.0, currentSpent = 0.0),
                                    BudgetCategoryEntity("FLEXIBLE", allocatedLimit = 0.0, currentSpent = 0.0),
                                    BudgetCategoryEntity("SAVINGS", allocatedLimit = 0.0, currentSpent = 0.0)
                                )
                            )
                            database.savingsGoalDao().insertGoal(
                                SavingsGoalEntity(
                                    goalName = "Emergency Fund",
                                    targetAmount = 50000.0,
                                    currentAmount = 0.0,
                                    targetDate = System.currentTimeMillis() + 31536000000L // 1 year
                                )
                            )
                        }
                    }
                })
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

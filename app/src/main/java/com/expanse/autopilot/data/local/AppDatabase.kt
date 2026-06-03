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
import androidx.room.migration.Migration

@Database(
    entities = [TransactionEntity::class, BudgetCategoryEntity::class, SavingsGoalEntity::class],
    version = 2,
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
                val MIGRATION_1_2 = object : Migration(1, 2) {
                    override fun migrate(db: SupportSQLiteDatabase) {
                        // Check if columns already exist (in case of dev inconsistencies)
                        try {
                            db.execSQL("ALTER TABLE transactions ADD COLUMN subCategory TEXT NOT NULL DEFAULT 'General'")
                        } catch (e: Exception) { e.printStackTrace() }
                        try {
                            db.execSQL("ALTER TABLE transactions ADD COLUMN account TEXT NOT NULL DEFAULT 'Secure Bank'")
                        } catch (e: Exception) { e.printStackTrace() }
                    }
                }

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "expanse_secure.db"
                )
                .addMigrations(MIGRATION_1_2)
                .fallbackToDestructiveMigration()
                .apply {
                    // SQLCipher Factory setup for encryption
                    try {
                        System.loadLibrary("sqlcipher")
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
                        // Prepopulate default envelope budget categories using raw SQL on the SupportSQLiteDatabase
                        // to avoid infinite recursion deadlock, running synchronously in onCreate to prevent race conditions.
                        try {
                            db.execSQL("INSERT INTO budget_categories (categoryId, allocatedLimit, currentSpent) VALUES ('FIXED', 0.0, 0.0)")
                            db.execSQL("INSERT INTO budget_categories (categoryId, allocatedLimit, currentSpent) VALUES ('FLEXIBLE', 0.0, 0.0)")
                            db.execSQL("INSERT INTO budget_categories (categoryId, allocatedLimit, currentSpent) VALUES ('SAVINGS', 0.0, 0.0)")
                            
                            val targetDate = System.currentTimeMillis() + 31536000000L
                            db.execSQL("INSERT INTO savings_goals (goalName, targetAmount, currentAmount, targetDate, isCompleted) VALUES ('Emergency Fund', 50000.0, 0.0, $targetDate, 0)")
                        } catch (e: Exception) {
                            e.printStackTrace()
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

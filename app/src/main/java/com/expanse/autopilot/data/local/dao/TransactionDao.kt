package com.expanse.autopilot.data.local.dao

import androidx.room.*
import com.expanse.autopilot.data.local.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions ORDER BY timestamp DESC")
    fun getAllTransactionsFlow(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE category = :category ORDER BY timestamp DESC")
    fun getTransactionsByCategoryFlow(category: String): Flow<List<TransactionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransactions(transactions: List<TransactionEntity>)

    @Query("SELECT SUM(amount) FROM transactions WHERE type = :type AND category = :category")
    suspend fun getSumAmountByTypeAndCategory(type: String, category: String): Double?

    @Query("SELECT SUM(amount) FROM transactions WHERE type = :type")
    suspend fun getSumAmountByType(type: String): Double?

    @Query("DELETE FROM transactions")
    suspend fun deleteAllTransactions()
}

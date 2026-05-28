package com.expanse.autopilot.data.local.dao

import androidx.room.*
import com.expanse.autopilot.data.local.entity.BudgetCategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {
    @Query("SELECT * FROM budget_categories")
    fun getAllBudgetsFlow(): Flow<List<BudgetCategoryEntity>>

    @Query("SELECT * FROM budget_categories WHERE categoryId = :categoryId")
    suspend fun getBudgetById(categoryId: String): BudgetCategoryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBudget(budget: BudgetCategoryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBudgets(budgets: List<BudgetCategoryEntity>)

    @Query("UPDATE budget_categories SET currentSpent = currentSpent + :amount WHERE categoryId = :categoryId")
    suspend fun incrementSpent(categoryId: String, amount: Double)

    @Query("UPDATE budget_categories SET allocatedLimit = allocatedLimit + :amount WHERE categoryId = :categoryId")
    suspend fun incrementLimit(categoryId: String, amount: Double)

    @Query("UPDATE budget_categories SET currentSpent = 0")
    suspend fun resetSpending()

    @Query("DELETE FROM budget_categories")
    suspend fun deleteAllBudgets()
}

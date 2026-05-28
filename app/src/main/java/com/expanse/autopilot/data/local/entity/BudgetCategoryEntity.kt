package com.expanse.autopilot.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "budget_categories")
data class BudgetCategoryEntity(
    @PrimaryKey val categoryId: String, // "FIXED", "FLEXIBLE", "SAVINGS"
    val allocatedLimit: Double,
    val currentSpent: Double
) {
    val remaining: Double
        get() = allocatedLimit - currentSpent
}

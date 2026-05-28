package com.expanse.autopilot.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "savings_goals")
data class SavingsGoalEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val goalName: String,
    val targetAmount: Double,
    val currentAmount: Double,
    val targetDate: Long,
    val isCompleted: Boolean = false
)

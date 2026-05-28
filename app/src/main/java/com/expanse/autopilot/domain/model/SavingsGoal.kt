package com.expanse.autopilot.domain.model

data class SavingsGoal(
    val id: Long = 0,
    val goalName: String,
    val targetAmount: Double,
    val currentAmount: Double,
    val targetDate: Long,
    val isCompleted: Boolean
) {
    val progress: Float
        get() = if (targetAmount > 0) (currentAmount / targetAmount).toFloat().coerceIn(0f, 1f) else 0f
}

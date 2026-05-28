package com.expanse.autopilot.ui.dashboard

import com.expanse.autopilot.data.local.entity.BudgetCategoryEntity
import com.expanse.autopilot.data.local.entity.SavingsGoalEntity
import com.expanse.autopilot.data.local.entity.TransactionEntity

data class DashboardState(
    val transactions: List<TransactionEntity> = emptyList(),
    val budgets: List<BudgetCategoryEntity> = emptyList(),
    val activeGoals: List<SavingsGoalEntity> = emptyList(),
    val isQuickEntryOpen: Boolean = false,
    val isAddingGoalOpen: Boolean = false
)

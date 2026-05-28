package com.expanse.autopilot.domain.model

data class Transaction(
    val id: Long = 0,
    val amount: Double,
    val type: TransactionType,
    val category: BudgetCategory,
    val description: String,
    val timestamp: Long,
    val isAutoScraped: Boolean
)

enum class TransactionType {
    DEBIT, CREDIT, SWEEP
}

enum class BudgetCategory {
    FIXED, FLEXIBLE, SAVINGS
}

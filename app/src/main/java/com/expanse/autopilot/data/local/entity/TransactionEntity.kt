package com.expanse.autopilot.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val amount: Double,
    val type: String, // "DEBIT", "CREDIT", "SWEEP"
    val category: String, // "FIXED", "FLEXIBLE", "SAVINGS"
    val description: String, // Merchant name / Source (e.g. "HDFC Bank", "Zomato")
    val timestamp: Long,
    val isAutoScraped: Boolean, // true if SMS, false if manual
    val subCategory: String = "General",
    val account: String = "Secure Bank"
)

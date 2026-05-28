package com.expanse.autopilot.domain.engine

data class EnvelopeSplit(
    val fixedAmount: Double,
    val flexibleAmount: Double,
    val savingsAmount: Double
)

class BudgetEngine {
    
    fun calculateEnvelopeSplit(incomeAmount: Double): EnvelopeSplit {
        if (incomeAmount <= 0) return EnvelopeSplit(0.0, 0.0, 0.0)
        
        val fixed = incomeAmount * 0.50
        val flexible = incomeAmount * 0.30
        val savings = incomeAmount * 0.20
        
        return EnvelopeSplit(
            fixedAmount = fixed,
            flexibleAmount = flexible,
            savingsAmount = savings
        )
    }
}

package com.expanse.autopilot.ui.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.expanse.autopilot.data.repository.FinanceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = FinanceRepository(application)

    private val _uiState = MutableStateFlow(DashboardState())
    val uiState: StateFlow<DashboardState> = _uiState.asStateFlow()

    init {
        // Collect DB changes reactively
        viewModelScope.launch {
            combine(
                repository.getAllTransactions(),
                repository.getAllBudgets(),
                repository.getActiveGoals()
            ) { txs, bgts, goals ->
                DashboardState(
                    transactions = txs,
                    budgets = bgts,
                    activeGoals = goals
                )
            }.collect { combinedState ->
                _uiState.value = combinedState
            }
        }
    }

    fun openQuickEntry() {
        _uiState.value = _uiState.value.copy(isQuickEntryOpen = true)
    }

    fun closeQuickEntry() {
        _uiState.value = _uiState.value.copy(isQuickEntryOpen = false)
    }

    fun openAddingGoal() {
        _uiState.value = _uiState.value.copy(isAddingGoalOpen = true)
    }

    fun closeAddingGoal() {
        _uiState.value = _uiState.value.copy(isAddingGoalOpen = false)
    }

    fun addManualTransaction(
        amount: Double,
        type: String,
        category: String,
        description: String,
        subCategory: String,
        account: String
    ) {
        viewModelScope.launch {
            repository.addTransaction(
                amount = amount,
                type = type,
                category = category,
                description = description,
                isAutoScraped = false,
                subCategory = subCategory,
                account = account
            )
            closeQuickEntry()
        }
    }

    fun deleteTransaction(transaction: TransactionEntity) {
        viewModelScope.launch {
            repository.deleteTransaction(transaction)
        }
    }

    fun createGoal(name: String, target: Double, durationMonths: Int) {
        viewModelScope.launch {
            repository.createSavingsGoal(name, target, durationMonths)
            closeAddingGoal()
        }
    }

    fun resetDataForPrivacy() {
        viewModelScope.launch {
            repository.clearAllData()
        }
    }
}

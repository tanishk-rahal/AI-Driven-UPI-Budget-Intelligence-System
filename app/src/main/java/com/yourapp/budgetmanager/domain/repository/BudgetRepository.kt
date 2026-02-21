package com.yourapp.budgetmanager.domain.repository

import com.yourapp.budgetmanager.domain.model.Budget
import kotlinx.coroutines.flow.Flow

/**
 * Abstraction for budget data.
 * Placeholder for future budget limits per category.
 */
interface BudgetRepository {

    fun getAllBudgets(): Flow<List<Budget>>
    suspend fun getBudgetByCategory(category: String): Budget?
    suspend fun insertBudget(budget: Budget)
    suspend fun updateBudget(budget: Budget)
}

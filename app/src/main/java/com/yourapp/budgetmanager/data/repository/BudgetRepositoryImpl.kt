package com.yourapp.budgetmanager.data.repository

import com.yourapp.budgetmanager.data.local.dao.BudgetDao
import com.yourapp.budgetmanager.data.local.entity.BudgetEntity
import com.yourapp.budgetmanager.domain.model.Budget
import com.yourapp.budgetmanager.domain.repository.BudgetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BudgetRepositoryImpl @Inject constructor(
    private val dao: BudgetDao
) : BudgetRepository {

    override fun getAllBudgets(): Flow<List<Budget>> =
        dao.getAllBudgets().map { list -> list.map { it.toDomain() } }

    override suspend fun getBudgetByCategory(category: String): Budget? =
        dao.getByCategory(category)?.toDomain()

    override suspend fun insertBudget(budget: Budget) {
        dao.insert(BudgetEntity.fromDomain(budget))
    }

    override suspend fun updateBudget(budget: Budget) {
        dao.update(BudgetEntity.fromDomain(budget))
    }
}

package com.yourapp.budgetmanager.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yourapp.budgetmanager.domain.model.Budget

/**
 * Room entity for budget limits per category.
 */
@Entity(tableName = "budgets")
data class BudgetEntity(
    @PrimaryKey val id: String,
    val category: String,
    val limitAmount: Double,
    val spentAmount: Double,
    val periodStartMillis: Long,
    val periodEndMillis: Long
) {
    fun toDomain(): Budget = Budget(
        id = id,
        category = category,
        limitAmount = limitAmount,
        spentAmount = spentAmount,
        periodStartMillis = periodStartMillis,
        periodEndMillis = periodEndMillis
    )

    companion object {
        fun fromDomain(b: Budget): BudgetEntity = BudgetEntity(
            id = b.id,
            category = b.category,
            limitAmount = b.limitAmount,
            spentAmount = b.spentAmount,
            periodStartMillis = b.periodStartMillis,
            periodEndMillis = b.periodEndMillis
        )
    }
}

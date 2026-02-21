package com.yourapp.budgetmanager.domain.model

/**
 * Domain model for a budget (e.g. monthly category budget).
 * Entity in Room maps to this for offline-first.
 */
data class Budget(
    val id: String,
    val category: String,
    val limitAmount: Double,
    val spentAmount: Double,
    val periodStartMillis: Long,
    val periodEndMillis: Long
) {
    val remaining: Double get() = (limitAmount - spentAmount).coerceAtLeast(0.0)
    val usagePercent: Float get() = if (limitAmount <= 0) 0f else (spentAmount / limitAmount * 100).toFloat()
}

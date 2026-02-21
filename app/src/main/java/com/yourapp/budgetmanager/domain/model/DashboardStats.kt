package com.yourapp.budgetmanager.domain.model

/**
 * Aggregated stats for dashboard.
 * Use case builds this from transactions and budgets.
 */
data class DashboardStats(
    val totalSpentThisMonth: Double,
    val pendingCount: Int,
    val recentTransactions: List<Transaction>,
    val categoryBreakdown: Map<String, Double>
)

package com.yourapp.budgetmanager.domain.usecase

import com.yourapp.budgetmanager.domain.model.DashboardStats
import com.yourapp.budgetmanager.domain.model.TransactionState
import com.yourapp.budgetmanager.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.util.Calendar
import javax.inject.Inject

/**
 * Builds dashboard statistics from transactions.
 * Uses current month for "total spent" and provides pending count and recent list.
 */
class GetDashboardStatsUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    operator fun invoke(): Flow<DashboardStats> = transactionRepository.getAllTransactions().map { transactions ->
        val calendar = Calendar.getInstance()
        val startOfMonth = calendar.apply { set(Calendar.DAY_OF_MONTH, 1); set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0); set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0) }.timeInMillis
        val successInMonth = transactions.filter { it.state == TransactionState.SUCCESS && it.timestamp >= startOfMonth }
        val totalSpentThisMonth = successInMonth.sumOf { it.amount }
        val pendingCount = transactions.count { it.state == TransactionState.PENDING || it.state == TransactionState.INITIATED }
        val recent = transactions.sortedByDescending { it.timestamp }.take(10)
        val categoryBreakdown = successInMonth.groupingBy { it.category }.fold(0.0) { acc, t -> acc + t.amount }
        DashboardStats(
            totalSpentThisMonth = totalSpentThisMonth,
            pendingCount = pendingCount,
            recentTransactions = recent,
            categoryBreakdown = categoryBreakdown
        )
    }
}

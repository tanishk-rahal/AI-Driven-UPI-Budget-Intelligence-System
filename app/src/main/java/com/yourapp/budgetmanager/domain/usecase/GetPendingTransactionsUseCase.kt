package com.yourapp.budgetmanager.domain.usecase

import com.yourapp.budgetmanager.domain.model.Transaction
import com.yourapp.budgetmanager.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Exposes pending (and initiated) transactions for recovery UI when app resumes.
 */
class GetPendingTransactionsUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    operator fun invoke(): Flow<List<Transaction>> = transactionRepository.getPendingTransactions()
}

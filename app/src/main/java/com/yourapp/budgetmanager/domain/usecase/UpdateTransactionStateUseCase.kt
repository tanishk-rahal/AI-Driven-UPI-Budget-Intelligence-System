package com.yourapp.budgetmanager.domain.usecase

import com.yourapp.budgetmanager.domain.model.TransactionState
import com.yourapp.budgetmanager.domain.repository.TransactionRepository
import javax.inject.Inject

/**
 * Updates a transaction's state (e.g. PENDING -> SUCCESS/FAILED from recovery UI).
 */
class UpdateTransactionStateUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(transactionId: String, state: TransactionState) {
        transactionRepository.updateTransactionState(transactionId, state)
    }
}

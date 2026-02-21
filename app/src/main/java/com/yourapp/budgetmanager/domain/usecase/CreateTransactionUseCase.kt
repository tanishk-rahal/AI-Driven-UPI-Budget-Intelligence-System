package com.yourapp.budgetmanager.domain.usecase

import com.yourapp.budgetmanager.domain.model.Transaction
import com.yourapp.budgetmanager.domain.model.TransactionState
import com.yourapp.budgetmanager.domain.repository.TransactionRepository
import java.util.UUID
import javax.inject.Inject

/**
 * Creates a new transaction in INITIATED state and persists it.
 * Called after user completes "Purpose of Payment" and before launching UPI app.
 */
class CreateTransactionUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(
        amount: Double,
        receiverName: String,
        upiId: String,
        category: String,
        note: String,
        aiConfidence: Float? = null
    ): Transaction {
        val transaction = Transaction(
            id = UUID.randomUUID().toString(),
            amount = amount,
            receiverName = receiverName,
            upiId = upiId,
            category = category,
            note = note,
            state = TransactionState.INITIATED,
            aiConfidence = aiConfidence,
            timestamp = System.currentTimeMillis(),
            isSynced = false
        )
        transactionRepository.insertTransaction(transaction)
        return transaction
    }
}

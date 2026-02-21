package com.yourapp.budgetmanager.domain.model

/**
 * Domain model for a transaction.
 * Independent of persistence (Room entity maps to this).
 */
data class Transaction(
    val id: String,
    val amount: Double,
    val receiverName: String,
    val upiId: String,
    val category: String,
    val note: String,
    val state: TransactionState,
    val aiConfidence: Float?,
    val timestamp: Long,
    val isSynced: Boolean
) {
    val isPending: Boolean get() = state == TransactionState.PENDING || state == TransactionState.INITIATED
}

package com.yourapp.budgetmanager.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yourapp.budgetmanager.domain.model.Transaction
import com.yourapp.budgetmanager.domain.model.TransactionState

/**
 * Room entity for transactions.
 * Maps to domain [Transaction] in repository.
 */
@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey val id: String,
    val amount: Double,
    val receiverName: String,
    val upiId: String,
    val category: String,
    val note: String,
    val state: String,
    val aiConfidence: Float?,
    val timestamp: Long,
    val isSynced: Boolean
) {
    fun toDomain(): Transaction = Transaction(
        id = id,
        amount = amount,
        receiverName = receiverName,
        upiId = upiId,
        category = category,
        note = note,
        state = TransactionState.valueOf(state),
        aiConfidence = aiConfidence,
        timestamp = timestamp,
        isSynced = isSynced
    )

    companion object {
        fun fromDomain(t: Transaction): TransactionEntity = TransactionEntity(
            id = t.id,
            amount = t.amount,
            receiverName = t.receiverName,
            upiId = t.upiId,
            category = t.category,
            note = t.note,
            state = t.state.name,
            aiConfidence = t.aiConfidence,
            timestamp = t.timestamp,
            isSynced = t.isSynced
        )
    }
}

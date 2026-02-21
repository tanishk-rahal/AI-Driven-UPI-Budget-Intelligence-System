package com.yourapp.budgetmanager.domain.repository

import com.yourapp.budgetmanager.domain.model.Transaction
import com.yourapp.budgetmanager.domain.model.TransactionState
import kotlinx.coroutines.flow.Flow

/**
 * Abstraction for transaction data.
 * Data layer implements this; domain/use cases depend only on this interface.
 */
interface TransactionRepository {

    fun getAllTransactions(): Flow<List<Transaction>>
    fun getPendingTransactions(): Flow<List<Transaction>>
    suspend fun getTransactionById(id: String): Transaction?
    suspend fun insertTransaction(transaction: Transaction)
    suspend fun updateTransactionState(id: String, state: TransactionState)
    suspend fun updateTransaction(transaction: Transaction)
    suspend fun getUnsyncedTransactions(): List<Transaction>
    suspend fun markAsSynced(id: String)
}

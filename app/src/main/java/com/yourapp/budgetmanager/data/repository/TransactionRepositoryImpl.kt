package com.yourapp.budgetmanager.data.repository

import com.yourapp.budgetmanager.data.local.dao.TransactionDao
import com.yourapp.budgetmanager.data.local.entity.TransactionEntity
import com.yourapp.budgetmanager.domain.model.Transaction
import com.yourapp.budgetmanager.domain.model.TransactionState
import com.yourapp.budgetmanager.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Offline-first implementation of [TransactionRepository].
 * All writes go to Room; SyncManager handles push to Supabase when integrated.
 */
class TransactionRepositoryImpl @Inject constructor(
    private val dao: TransactionDao
) : TransactionRepository {

    override fun getAllTransactions(): Flow<List<Transaction>> =
        dao.getAllTransactions().map { list -> list.map { it.toDomain() } }

    override fun getPendingTransactions(): Flow<List<Transaction>> =
        dao.getPendingTransactions().map { list -> list.map { it.toDomain() } }

    override suspend fun getTransactionById(id: String): Transaction? =
        dao.getById(id)?.toDomain()

    override suspend fun insertTransaction(transaction: Transaction) {
        dao.insert(TransactionEntity.fromDomain(transaction))
    }

    override suspend fun updateTransactionState(id: String, state: TransactionState) {
        dao.updateState(id, state.name)
    }

    override suspend fun updateTransaction(transaction: Transaction) {
        dao.update(TransactionEntity.fromDomain(transaction))
    }

    override suspend fun getUnsyncedTransactions(): List<Transaction> =
        dao.getUnsynced().map { it.toDomain() }

    override suspend fun markAsSynced(id: String) {
        dao.markSynced(id)
    }
}

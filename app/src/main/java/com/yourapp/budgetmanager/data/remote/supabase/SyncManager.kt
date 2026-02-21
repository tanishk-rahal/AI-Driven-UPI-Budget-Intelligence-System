package com.yourapp.budgetmanager.data.remote.supabase

import com.yourapp.budgetmanager.core.constants.AppConstants
import com.yourapp.budgetmanager.domain.repository.TransactionRepository
import javax.inject.Inject

/**
 * Handles offline-first sync with Supabase.
 * - Push: upload unsynced local transactions in batches.
 * - Pull: fetch remote updates and merge (future).
 * Call from WorkManager or on app foreground when backend is integrated.
 */
class SyncManager @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val supabaseClient: SupabaseClient
) {

    /**
     * Push local unsynced transactions to Supabase.
     * Marks as synced on success.
     */
    suspend fun pushUnsyncedTransactions(): SyncResult {
        if (!supabaseClient.isAvailable()) return SyncResult.Skipped("Supabase not available")
        val unsynced = transactionRepository.getUnsyncedTransactions()
        if (unsynced.isEmpty()) return SyncResult.Success(0)
        var pushed = 0
        var lastError: Throwable? = null
        unsynced.chunked(AppConstants.SYNC_BATCH_SIZE).forEach { batch ->
            batch.forEach { transaction ->
                supabaseClient.upsertTransaction(transaction)
                    .onSuccess { transactionRepository.markAsSynced(transaction.id); pushed++ }
                    .onFailure { lastError = it }
            }
        }
        return if (lastError != null && pushed == 0) SyncResult.Error(lastError!!)
        else SyncResult.Success(pushed)
    }

    /**
     * Pull remote updates and merge into local (placeholder).
     * When implemented: fetch by timestamp, insert/update local, resolve conflicts.
     */
    suspend fun pullRemoteUpdates(): SyncResult {
        if (!supabaseClient.isAvailable()) return SyncResult.Skipped("Supabase not available")
        // Placeholder: in real impl, fetch and upsert into repository
        return SyncResult.Success(0)
    }

    sealed class SyncResult {
        data class Success(val count: Int) : SyncResult()
        data class Error(val cause: Throwable) : SyncResult()
        data class Skipped(val reason: String) : SyncResult()
    }
}

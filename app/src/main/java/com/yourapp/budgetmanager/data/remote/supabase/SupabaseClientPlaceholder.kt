package com.yourapp.budgetmanager.data.remote.supabase

import com.yourapp.budgetmanager.domain.model.Transaction
import javax.inject.Inject

/**
 * No-op implementation until Supabase SDK is added.
 * SyncManager will call this; when backend is integrated, replace with real client.
 */
class SupabaseClientPlaceholder @Inject constructor() : SupabaseClient {

    override suspend fun upsertTransaction(transaction: Transaction): Result<Unit> =
        Result.failure(UnsupportedOperationException("Supabase not yet integrated"))

    override suspend fun fetchTransactionsAfter(timestampMillis: Long): Result<List<Transaction>> =
        Result.failure(UnsupportedOperationException("Supabase not yet integrated"))

    override fun isAvailable(): Boolean = false
}

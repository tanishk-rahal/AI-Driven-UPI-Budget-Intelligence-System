package com.yourapp.budgetmanager.data.remote.supabase

import com.yourapp.budgetmanager.domain.model.Transaction

/**
 * Placeholder for Supabase client.
 * When integrated: auth, realtime, and REST for transactions/budgets.
 */
interface SupabaseClient {

    /** Push a single transaction to remote (e.g. transactions table). */
    suspend fun upsertTransaction(transaction: Transaction): Result<Unit>

    /** Pull remote transactions updated after given timestamp. */
    suspend fun fetchTransactionsAfter(timestampMillis: Long): Result<List<Transaction>>

    /** Whether backend is configured and reachable. */
    fun isAvailable(): Boolean
}

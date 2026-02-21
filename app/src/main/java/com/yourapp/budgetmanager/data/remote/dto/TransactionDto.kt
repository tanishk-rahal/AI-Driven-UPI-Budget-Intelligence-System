package com.yourapp.budgetmanager.data.remote.dto

/**
 * DTO for Supabase transactions table (when integrated).
 * Maps to/from [com.yourapp.budgetmanager.domain.model.Transaction].
 */
data class TransactionDto(
    val id: String,
    val amount: Double,
    val receiver_name: String,
    val upi_id: String,
    val category: String,
    val note: String,
    val state: String,
    val ai_confidence: Float?,
    val timestamp: Long,
    val user_id: String? = null
)

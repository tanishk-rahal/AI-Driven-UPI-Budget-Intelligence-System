package com.yourapp.budgetmanager.domain.model

/**
 * Result of AI-based intent analysis (purpose of payment).
 * Used when user enters a note and we predict category.
 */
data class IntentResult(
    val predictedCategory: String,
    val confidenceScore: Float
)

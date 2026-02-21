package com.yourapp.budgetmanager.data.repository

import com.yourapp.budgetmanager.domain.model.IntentResult
import com.yourapp.budgetmanager.domain.usecase.IntentAnalyzer
import kotlinx.coroutines.delay
import javax.inject.Inject

/**
 * Mock implementation of [IntentAnalyzer].
 * Uses simple keyword matching; replace with ML/API when AI is integrated.
 */
class MockIntentAnalyzer @Inject constructor() : IntentAnalyzer {

    private val categoryKeywords = mapOf(
        "Food" to listOf("food", "lunch", "dinner", "restaurant", "cafe", "groceries", "zomato", "swiggy"),
        "Travel" to listOf("travel", "petrol", "fuel", "uber", "ola", "metro", "bus"),
        "Shopping" to listOf("shopping", "amazon", "flipkart", "store"),
        "Bills" to listOf("bill", "electricity", "rent", "recharge", "broadband"),
        "Transfer" to listOf("transfer", "send", "self", "savings")
    )

    override suspend fun analyzeIntent(note: String): IntentResult {
        delay(100) // Simulate async call
        val lower = note.lowercase().trim()
        if (lower.isBlank()) return IntentResult(predictedCategory = "Other", confidenceScore = 0f)
        for ((category, keywords) in categoryKeywords) {
            if (keywords.any { lower.contains(it) }) {
                return IntentResult(predictedCategory = category, confidenceScore = 0.85f)
            }
        }
        return IntentResult(predictedCategory = "Other", confidenceScore = 0.5f)
    }
}

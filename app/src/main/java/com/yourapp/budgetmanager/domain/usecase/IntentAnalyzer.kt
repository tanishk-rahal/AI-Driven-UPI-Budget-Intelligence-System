package com.yourapp.budgetmanager.domain.usecase

import com.yourapp.budgetmanager.domain.model.IntentResult

/**
 * Analyzes user-entered payment note to predict category (intent).
 * Real implementation will call ML/API; for now use mock.
 */
interface IntentAnalyzer {

    /**
     * @param note User's "Purpose of Payment" / note text
     * @return Predicted category and confidence in [0, 1]
     */
    suspend fun analyzeIntent(note: String): IntentResult
}

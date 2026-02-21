package com.yourapp.budgetmanager.core.constants

/**
 * Application-wide constants.
 * Keeps magic strings and numbers in one place for maintainability.
 */
object AppConstants {

    const val DATABASE_NAME = "budget_manager_db"
    const val DATABASE_VERSION = 1

    /** UPI Intent action for launching external UPI apps */
    const val UPI_INTENT_ACTION = "android.intent.action.VIEW"

    /** Minimum confidence score (0-1) for AI-predicted category to be auto-applied */
    const val AI_CONFIDENCE_THRESHOLD = 0.7

    /** Sync batch size when pushing to Supabase */
    const val SYNC_BATCH_SIZE = 50
}

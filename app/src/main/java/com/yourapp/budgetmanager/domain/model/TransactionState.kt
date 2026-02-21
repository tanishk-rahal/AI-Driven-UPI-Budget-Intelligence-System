package com.yourapp.budgetmanager.domain.model

/**
 * Lifecycle state of a UPI transaction from our app's perspective.
 * INITIATED: Saved locally, UPI app not yet launched.
 * PENDING: UPI app was launched; we're waiting for user to return and confirm outcome.
 * SUCCESS: User confirmed payment / we detected success.
 * FAILED: User marked as failed or payment failed.
 */
enum class TransactionState {
    INITIATED,
    PENDING,
    SUCCESS,
    FAILED
}

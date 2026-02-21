package com.yourapp.budgetmanager.presentation.navigation

/**
 * Sealed class for Navigation Compose routes.
 * Single source of truth for screen routes and optional arguments.
 */
sealed class Screen(val route: String) {
    data object Auth : Screen("auth")
    data object Dashboard : Screen("dashboard")
    data object TransactionList : Screen("transactions")
    data object IntentCapture : Screen("intent_capture")
    data object TransactionDetail : Screen("transaction/{transactionId}") {
        fun createRoute(transactionId: String) = "transaction/$transactionId"
    }
    data object Recovery : Screen("recovery")
    data object Insights : Screen("insights")
    data object Profile : Screen("profile")
    data object Notifications : Screen("notifications")
}

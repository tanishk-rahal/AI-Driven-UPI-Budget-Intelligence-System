package com.yourapp.budgetmanager.presentation.state

import com.yourapp.budgetmanager.domain.model.DashboardStats

/**
 * UI state for Dashboard screen.
 * Wraps [DashboardStats] in [UiState] for loading/error handling.
 */
data class DashboardState(
    val uiState: UiState<DashboardStats> = UiState.Loading,
    val refreshing: Boolean = false
)

package com.yourapp.budgetmanager.presentation.state

/**
 * Base UI state for screens.
 * Keeps UI stateless: ViewModel holds state, composables only display and send events.
 */
sealed class UiState<out T> {
    data object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String, val throwable: Throwable? = null) : UiState<Nothing>()
}

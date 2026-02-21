package com.yourapp.budgetmanager.core.result

/**
 * Sealed class representing operation results across the app.
 * Used in repository/use-case layer to avoid throwing exceptions in business logic.
 * Presentation layer maps this to UiState.
 */
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Throwable? = null, val message: String? = null) : Result<Nothing>()
    data object Loading : Result<Nothing>()
}

inline fun <T> Result<T>.onSuccess(action: (T) -> Unit): Result<T> {
    if (this is Result.Success) action(data)
    return this
}

inline fun <T> Result<T>.onError(action: (Result.Error) -> Unit): Result<T> {
    if (this is Result.Error) action(this)
    return this
}

fun <T> Result<T>.getOrNull(): T? = (this as? Result.Success)?.data
fun <T> Result<T>.isSuccess(): Boolean = this is Result.Success
fun <T> Result<T>.isError(): Boolean = this is Result.Error

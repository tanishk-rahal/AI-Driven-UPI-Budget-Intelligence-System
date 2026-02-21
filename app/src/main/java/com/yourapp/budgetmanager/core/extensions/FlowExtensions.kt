package com.yourapp.budgetmanager.core.extensions

import com.yourapp.budgetmanager.core.result.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

/**
 * Maps a Flow of domain/data types to Result wrapper.
 * Use in repository implementations when exposing flows to use cases.
 */
fun <T> Flow<T>.asResult(): Flow<Result<T>> = map { Result.Success(it) }
    .catch { e -> kotlinx.coroutines.flow.flowOf(Result.Error(e)) }

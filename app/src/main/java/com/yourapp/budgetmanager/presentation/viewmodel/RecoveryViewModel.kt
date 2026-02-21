package com.yourapp.budgetmanager.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yourapp.budgetmanager.domain.model.Transaction
import com.yourapp.budgetmanager.domain.model.TransactionState
import com.yourapp.budgetmanager.domain.usecase.GetPendingTransactionsUseCase
import com.yourapp.budgetmanager.domain.usecase.UpdateTransactionStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecoveryViewModel @Inject constructor(
    private val getPendingTransactionsUseCase: GetPendingTransactionsUseCase,
    private val updateTransactionStateUseCase: UpdateTransactionStateUseCase
) : ViewModel() {

    private val _pendingTransactions = MutableStateFlow<List<Transaction>>(emptyList())
    val pendingTransactions: StateFlow<List<Transaction>> = _pendingTransactions.asStateFlow()

    init {
        viewModelScope.launch {
            getPendingTransactionsUseCase()
                .catch { }
                .collect { _pendingTransactions.value = it }
        }
    }

    fun confirmPaid(transactionId: String) {
        viewModelScope.launch {
            updateTransactionStateUseCase(transactionId, TransactionState.SUCCESS)
        }
    }

    fun markFailed(transactionId: String) {
        viewModelScope.launch {
            updateTransactionStateUseCase(transactionId, TransactionState.FAILED)
        }
    }
}

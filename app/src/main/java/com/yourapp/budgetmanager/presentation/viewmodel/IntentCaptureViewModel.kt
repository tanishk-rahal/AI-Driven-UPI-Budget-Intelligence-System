package com.yourapp.budgetmanager.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yourapp.budgetmanager.domain.model.Transaction
import com.yourapp.budgetmanager.domain.usecase.CreateTransactionUseCase
import com.yourapp.budgetmanager.domain.usecase.IntentAnalyzer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IntentCaptureViewModel @Inject constructor(
    private val createTransactionUseCase: CreateTransactionUseCase,
    private val intentAnalyzer: IntentAnalyzer
) : ViewModel() {

    private val _predictedCategory = MutableStateFlow<String?>(null)
    val predictedCategory: StateFlow<String?> = _predictedCategory.asStateFlow()

    private val _createdTransaction = MutableStateFlow<Transaction?>(null)
    val createdTransaction: StateFlow<Transaction?> = _createdTransaction.asStateFlow()

    fun analyzeNote(note: String) {
        viewModelScope.launch {
            val result = intentAnalyzer.analyzeIntent(note)
            _predictedCategory.value = result.predictedCategory
        }
    }

    fun createTransaction(
        amount: Double,
        receiverName: String,
        upiId: String,
        category: String,
        note: String,
        aiConfidence: Float?
    ) {
        viewModelScope.launch {
            val t = createTransactionUseCase(amount, receiverName, upiId, category, note, aiConfidence)
            _createdTransaction.value = t
        }
    }

    fun clearCreatedTransaction() {
        _createdTransaction.value = null
    }
}

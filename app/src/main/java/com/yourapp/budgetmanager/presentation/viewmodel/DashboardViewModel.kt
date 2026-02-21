package com.yourapp.budgetmanager.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yourapp.budgetmanager.domain.model.DashboardStats
import com.yourapp.budgetmanager.domain.usecase.GetDashboardStatsUseCase
import com.yourapp.budgetmanager.presentation.state.DashboardState
import com.yourapp.budgetmanager.presentation.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getDashboardStatsUseCase: GetDashboardStatsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state: StateFlow<DashboardState> = _state.asStateFlow()

    init {
        loadDashboard()
    }

    fun loadDashboard() {
        viewModelScope.launch {
            _state.update { it.copy(uiState = UiState.Loading) }
            getDashboardStatsUseCase()
                .catch { e -> _state.update { st -> st.copy(uiState = UiState.Error(e.message ?: "Error", e)) } }
                .collect { stats ->
                    _state.update { it.copy(uiState = UiState.Success(stats)) }
                }
        }
    }

    fun refresh() {
        _state.update { it.copy(refreshing = true) }
        loadDashboard()
        _state.update { it.copy(refreshing = false) }
    }
}

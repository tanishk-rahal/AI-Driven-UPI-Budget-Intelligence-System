package com.yourapp.budgetmanager.presentation.screens.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yourapp.budgetmanager.presentation.state.UiState
import com.yourapp.budgetmanager.presentation.viewmodel.DashboardViewModel

@Composable
fun DashboardScreen(
    onNavigateToIntentCapture: () -> Unit,
    onNavigateToTransactions: () -> Unit,
    onNavigateToRecovery: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Budget Manager", style = MaterialTheme.typography.headlineMedium)
        when (val ui = state.uiState) {
            is UiState.Loading -> CircularProgressIndicator(Modifier.padding(24.dp))
            is UiState.Error -> Text("Error: ${ui.message}", color = MaterialTheme.colorScheme.error)
            is UiState.Success -> {
                val stats = ui.data
                Text("Spent this month: ₹${stats.totalSpentThisMonth}", style = MaterialTheme.typography.titleMedium)
                Text("Pending: ${stats.pendingCount}", style = MaterialTheme.typography.bodyMedium)
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(stats.recentTransactions.take(5)) { t ->
                        Card(modifier = Modifier.padding(vertical = 4.dp)) {
                            Text("${t.receiverName} ₹${t.amount} - ${t.state}", modifier = Modifier.padding(12.dp))
                        }
                    }
                }
            }
        }
        Button(onClick = onNavigateToIntentCapture, modifier = Modifier.padding(8.dp)) { Text("New Payment (Intent)") }
        Button(onClick = onNavigateToTransactions, modifier = Modifier.padding(8.dp)) { Text("All Transactions") }
        Button(onClick = onNavigateToRecovery, modifier = Modifier.padding(8.dp)) { Text("Pending / Recovery") }
    }
}

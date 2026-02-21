package com.yourapp.budgetmanager.presentation.screens.transaction

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yourapp.budgetmanager.core.util.DateUtils
import com.yourapp.budgetmanager.presentation.viewmodel.TransactionViewModel

@Composable
fun TransactionListScreen(
    onNavigateBack: () -> Unit,
    onNavigateToRecovery: () -> Unit,
    viewModel: TransactionViewModel = hiltViewModel()
) {
    val transactions by viewModel.allTransactions.collectAsState()

    LazyColumn(Modifier.fillMaxSize().padding(16.dp)) {
        item {
            Button(onClick = onNavigateBack) { Text("Back") }
            Button(onClick = onNavigateToRecovery) { Text("Recovery") }
        }
        items(transactions) { t ->
            Card(Modifier.padding(8.dp)) {
                Text("${t.receiverName} | ₹${t.amount} | ${t.state} | ${DateUtils.formatTimestamp(t.timestamp)}", Modifier.padding(12.dp))
            }
        }
    }
}

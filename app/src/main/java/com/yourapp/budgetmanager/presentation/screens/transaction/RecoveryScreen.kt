package com.yourapp.budgetmanager.presentation.screens.transaction

import androidx.compose.foundation.layout.Column
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
import com.yourapp.budgetmanager.presentation.viewmodel.RecoveryViewModel

@Composable
fun RecoveryScreen(
    onNavigateBack: () -> Unit,
    viewModel: RecoveryViewModel = hiltViewModel()
) {
    val pending by viewModel.pendingTransactions.collectAsState()

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Button(onClick = onNavigateBack) { Text("Back") }
        Text("Pending transactions — Confirm or mark failed")
        LazyColumn {
            items(pending) { t ->
                Card(Modifier.padding(8.dp)) {
                    Text("${t.receiverName} ₹${t.amount}", Modifier.padding(8.dp))
                    Button(onClick = { viewModel.confirmPaid(t.id) }) { Text("Confirm Paid") }
                    Button(onClick = { viewModel.markFailed(t.id) }) { Text("Mark Failed") }
                }
            }
        }
    }
}

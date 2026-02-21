package com.yourapp.budgetmanager.presentation.screens.intentcapture

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yourapp.budgetmanager.presentation.viewmodel.IntentCaptureViewModel

@Composable
fun IntentCaptureScreen(
    onNavigateBack: () -> Unit,
    viewModel: IntentCaptureViewModel = hiltViewModel()
) {
    var amount by remember { mutableStateOf("") }
    var receiverName by remember { mutableStateOf("") }
    var upiId by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    val predictedCategory by viewModel.predictedCategory.collectAsState()

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Purpose of Payment")
        OutlinedTextField(value = note, onValueChange = { note = it; viewModel.analyzeNote(it) }, label = { Text("Note") }, modifier = Modifier.padding(8.dp))
        if (predictedCategory != null) Text("Suggested category: $predictedCategory")
        OutlinedTextField(value = amount, onValueChange = { amount = it }, label = { Text("Amount") }, modifier = Modifier.padding(8.dp))
        OutlinedTextField(value = receiverName, onValueChange = { receiverName = it }, label = { Text("Receiver") }, modifier = Modifier.padding(8.dp))
        OutlinedTextField(value = upiId, onValueChange = { upiId = it }, label = { Text("UPI ID") }, modifier = Modifier.padding(8.dp))
        Button(onClick = {
            viewModel.createTransaction(
                amount = amount.toDoubleOrNull() ?: 0.0,
                receiverName = receiverName,
                upiId = upiId,
                category = predictedCategory ?: "Other",
                note = note,
                aiConfidence = 0.85f
            )
        }, modifier = Modifier.padding(8.dp)) { Text("Save & Launch UPI") }
        Button(onClick = onNavigateBack, modifier = Modifier.padding(8.dp)) { Text("Back") }
    }
}

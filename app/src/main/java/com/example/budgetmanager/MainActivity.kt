package com.example.budgetmanager

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.budgetmanager.ui.dashboard.*
import com.example.budgetmanager.ui.scan.ScanActivity
import com.example.budgetmanager.ui.theme.BudgetManagerTheme

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {

    private val scanLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val scannedData = data?.getStringExtra("scanned_data")
            if (scannedData != null) {
                Toast.makeText(this, "Scanned: $scannedData", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BudgetManagerTheme {
                var currentRoute by remember { mutableStateOf(BottomNavItem.Dashboard.route) }
                val context = LocalContext.current

                Scaffold(
                    bottomBar = {
                        BottomNavBar(currentRoute = currentRoute) { route ->
                            if (route == BottomNavItem.Pay.route) {
                                val intent = Intent(context, ScanActivity::class.java)
                                scanLauncher.launch(intent)
                            } else {
                                currentRoute = route
                            }
                        }
                    }
                ) { paddingValues ->
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        item {
                            DashboardTopBar(
                                userName = "Arjun Mehta",
                                isSynced = true,
                                notificationCount = 3,
                                onNotificationClick = {}
                            )
                        }
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                            QuickPaymentSection(
                                onScanQrClick = {},
                                onSendContactClick = {},
                                onSendUpiClick = {}
                            )
                        }
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                            WeeklySpendingCard(
                                amount = "₹20,740",
                                previousAmount = "₹18,450",
                                percentageChange = 12.4f
                            )
                        }
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                            SavingsCard(
                                savingsAmount = "+₹3,840",
                                percentageChange = -15.2f,
                                savedCategories = "Transport, Bills",
                                highestOverspend = "Shopping +870"
                            )
                        }
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                            CategoryBudgetsSection()
                        }
                    }
                }
            }
        }
    }
}
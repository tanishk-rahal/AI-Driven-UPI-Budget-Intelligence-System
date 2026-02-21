package com.yourapp.budgetmanager.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yourapp.budgetmanager.presentation.screens.dashboard.DashboardScreen
import com.yourapp.budgetmanager.presentation.screens.intentcapture.IntentCaptureScreen
import com.yourapp.budgetmanager.presentation.screens.transaction.TransactionListScreen
import com.yourapp.budgetmanager.presentation.screens.transaction.RecoveryScreen

/**
 * Root navigation graph. Auth flow can wrap this when login is required.
 */
@Composable
fun BudgetManagerNavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.route
    ) {
        composable(Screen.Dashboard.route) {
            DashboardScreen(
                onNavigateToIntentCapture = { navController.navigate(Screen.IntentCapture.route) },
                onNavigateToTransactions = { navController.navigate(Screen.TransactionList.route) },
                onNavigateToRecovery = { navController.navigate(Screen.Recovery.route) }
            )
        }
        composable(Screen.IntentCapture.route) {
            IntentCaptureScreen(onNavigateBack = { navController.popBackStack() })
        }
        composable(Screen.TransactionList.route) {
            TransactionListScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToRecovery = { navController.navigate(Screen.Recovery.route) }
            )
        }
        composable(Screen.Recovery.route) {
            RecoveryScreen(onNavigateBack = { navController.popBackStack() })
        }
    }
}

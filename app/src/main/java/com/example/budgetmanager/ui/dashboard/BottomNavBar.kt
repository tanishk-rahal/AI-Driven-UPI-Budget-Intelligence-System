
package com.example.budgetmanager.ui.dashboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.budgetmanager.ui.theme.BudgetManagerTheme

sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    object Dashboard : BottomNavItem("dashboard", Icons.Default.GridView, "Dashboard")
    object Transactions : BottomNavItem("transactions", Icons.Default.SwapHoriz, "Transactions")
    object Pay : BottomNavItem("pay", Icons.Default.QrCodeScanner, "Pay")
    object Insights : BottomNavItem("insights", Icons.AutoMirrored.Filled.TrendingUp, "Insights")
    object Profile : BottomNavItem("profile", Icons.Default.Person, "Profile")
}

@Composable
fun BottomNavBar(
    currentRoute: String,
    onItemSelected: (String) -> Unit
) {
    val items = listOf(
        BottomNavItem.Dashboard,
        BottomNavItem.Transactions,
        BottomNavItem.Pay,
        BottomNavItem.Insights,
        BottomNavItem.Profile
    )

    Box {
        NavigationBar(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)),
            tonalElevation = 8.dp
        ) {
            items.forEachIndexed { index, item ->
                if (item.route == "pay") {
                    // Placeholder for the floating action button
                    NavigationBarItem(
                        selected = false,
                        onClick = { onItemSelected(item.route) },
                        icon = { },
                        enabled = false
                    )
                } else {
                    NavigationBarItem(
                        selected = currentRoute == item.route,
                        onClick = { onItemSelected(item.route) },
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { if (currentRoute == item.route) Text(item.label) else null },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                        )
                    )
                }
            }
        }

        FloatingActionButton(
            onClick = { onItemSelected(BottomNavItem.Pay.route) },
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-24).dp),
            shape = CircleShape,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            elevation = FloatingActionButtonDefaults.elevation(4.dp)
        ) {
            Icon(
                imageVector = BottomNavItem.Pay.icon,
                contentDescription = BottomNavItem.Pay.label,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0B0F1A)
@Composable
fun BottomNavBarPreview() {
    var currentRoute by remember { mutableStateOf(BottomNavItem.Dashboard.route) }
    BudgetManagerTheme {
        BottomNavBar(currentRoute = currentRoute) { route ->
            currentRoute = route
        }
    }
}

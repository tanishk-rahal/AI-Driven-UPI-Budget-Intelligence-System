
package com.example.budgetmanager.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.budgetmanager.ui.theme.*

@Composable
fun DashboardTopBar(
    userName: String,
    isSynced: Boolean,
    notificationCount: Int,
    onNotificationClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = "GOOD MORNING",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Text(
                text = userName,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            SyncBadge(isSynced = isSynced)
            Spacer(modifier = Modifier.width(8.dp))
            NotificationIcon(notificationCount = notificationCount, onClick = onNotificationClick)
        }
    }
}

@Composable
private fun SyncBadge(isSynced: Boolean) {
    val backgroundColor = if (isSynced) SuccessGreen.copy(alpha = 0.1f) else MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
    val contentColor = if (isSynced) SuccessGreen else MaterialTheme.colorScheme.primary
    val text = if (isSynced) "Synced" else "Syncing"

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(backgroundColor)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            color = contentColor,
            style = MaterialTheme.typography.labelMedium
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NotificationIcon(notificationCount: Int, onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        if (notificationCount > 0) {
            BadgedBox(
                badge = {
                    Badge(containerColor = ErrorRed) {
                        Text(text = notificationCount.toString())
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notifications"
                )
            }
        } else {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notifications"
            )
        }
    }
}

@Composable
fun QuickPaymentSection(
    onScanQrClick: () -> Unit,
    onSendContactClick: () -> Unit,
    onSendUpiClick: () -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "QUICK PAYMENT",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            QuickActionCard(icon = Icons.Default.QrCodeScanner, text = "Scan QR", onClick = onScanQrClick, modifier = Modifier.weight(1f))
            QuickActionCard(icon = Icons.Default.Person, text = "Send to Contact", onClick = onSendContactClick, modifier = Modifier.weight(1f))
            QuickActionCard(icon = Icons.AutoMirrored.Filled.Send, text = "Send to UPI ID", onClick = onSendUpiClick, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun QuickActionCard(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(imageVector = icon, contentDescription = text, modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = text, style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun WeeklySpendingCard(
    amount: String,
    previousAmount: String,
    percentageChange: Float
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Money Spent This Week",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                PercentageBadge(percentageChange = percentageChange)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = amount,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "vs $previousAmount last week",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
fun SavingsCard(
    savingsAmount: String,
    percentageChange: Float,
    savedCategories: String,
    highestOverspend: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Savings vs Last Month",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                PercentageBadge(percentageChange = percentageChange)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = savingsAmount,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = if (savingsAmount.startsWith("+")) SuccessGreen else ErrorRed
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                PillCard(
                    title = "Where you saved",
                    subtitle = savedCategories,
                    modifier = Modifier.weight(1f)
                )
                PillCard(
                    title = "Highest overspend",
                    subtitle = highestOverspend,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun PillCard(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun PercentageBadge(percentageChange: Float) {
    val isPositive = percentageChange > 0
    val color = if (isPositive) ErrorRed else SuccessGreen
    val icon = if (isPositive) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward
    val text = "${if (isPositive) "+" else ""}${"%.1f".format(percentageChange)}%"

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(color.copy(alpha = 0.1f))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            color = color,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun CategoryBudgetsSection() {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "CATEGORY BUDGETS",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            CategoryBudgetCard(
                categoryName = "Food & Dining",
                spentAmount = "₹5,840",
                totalBudget = "₹6,000",
                percentageUsed = 97,
                icon = Icons.Default.Restaurant
            )
            CategoryBudgetCard(
                categoryName = "Transport",
                spentAmount = "₹3,280",
                totalBudget = "₹4,000",
                percentageUsed = 82,
                icon = Icons.Default.DirectionsCar
            )
            CategoryBudgetCard(
                categoryName = "Shopping",
                spentAmount = "₹4,370",
                totalBudget = "₹3,500",
                percentageUsed = 125,
                icon = Icons.Default.ShoppingBag
            )
            CategoryBudgetCard(
                categoryName = "Healthcare",
                spentAmount = "₹1,460",
                totalBudget = "₹2,000",
                percentageUsed = 73,
                icon = Icons.Default.Favorite
            )
            CategoryBudgetCard(
                categoryName = "Entertainment",
                spentAmount = "₹2,190",
                totalBudget = "₹2,000",
                percentageUsed = 109,
                icon = Icons.Default.Theaters
            )
            CategoryBudgetCard(
                categoryName = "Bills & Utilities",
                spentAmount = "₹1,095",
                totalBudget = "₹1,500",
                percentageUsed = 73,
                icon = Icons.Default.Bolt
            )
        }
    }
}

@Composable
fun CategoryBudgetCard(
    categoryName: String,
    spentAmount: String,
    totalBudget: String,
    percentageUsed: Int,
    icon: ImageVector
) {
    val progressColor = when {
        percentageUsed < 80 -> MaterialTheme.colorScheme.primary
        percentageUsed <= 100 -> Amber
        else -> MaterialTheme.colorScheme.error
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = categoryName,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = categoryName,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "$percentageUsed%",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = progressColor
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "$spentAmount / $totalBudget",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { (percentageUsed / 100f).coerceAtMost(1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = progressColor,
                trackColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0B0F1A)
@Composable
fun DashboardTopBarPreview() {
    BudgetManagerTheme {
        DashboardTopBar(
            userName = "Arjun Mehta",
            isSynced = true,
            notificationCount = 3,
            onNotificationClick = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0B0F1A)
@Composable
fun QuickPaymentSectionPreview() {
    BudgetManagerTheme {
        QuickPaymentSection({}, {}, {})
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0B0F1A)
@Composable
fun WeeklySpendingCardPreview() {
    BudgetManagerTheme {
        WeeklySpendingCard(amount = "₹20,740", previousAmount = "₹18,450", percentageChange = 12.4f)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0B0F1A)
@Composable
fun SavingsCardPreview() {
    BudgetManagerTheme {
        SavingsCard(
            savingsAmount = "+₹3,840",
            percentageChange = -15.2f,
            savedCategories = "Transport, Bills",
            highestOverspend = "Shopping +870"
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0B0F1A)
@Composable
fun CategoryBudgetsSectionPreview() {
    BudgetManagerTheme {
        CategoryBudgetsSection()
    }
}

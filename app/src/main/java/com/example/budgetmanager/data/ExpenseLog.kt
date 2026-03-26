package com.example.budgetmanager.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expense_logs")
data class ExpenseLog(
    @PrimaryKey val id: String,
    val upiId: String,
    val payeeName: String,
    val amount: Double,
    val note: String,
    val category: String,
    val status: String,
    val paymentId: String,
    val orderId: String,
    val timestamp: Long,
    val isConfirmed: Boolean
)

package com.example.budgetmanager.network.models

data class CreateOrderRequest(
    val amount: Double,
    val note: String
)

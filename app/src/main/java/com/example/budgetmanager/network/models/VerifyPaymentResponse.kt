package com.example.budgetmanager.network.models

data class VerifyPaymentResponse(
    val success: Boolean,
    val verified: Boolean,
    val message: String
)

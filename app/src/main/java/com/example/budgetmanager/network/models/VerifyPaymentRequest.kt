package com.example.budgetmanager.network.models

data class VerifyPaymentRequest(
    val razorpay_order_id: String,
    val razorpay_payment_id: String,
    val razorpay_signature: String
)

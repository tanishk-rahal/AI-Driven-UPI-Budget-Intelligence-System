package com.example.budgetmanager.network.models

import com.google.gson.annotations.SerializedName

data class CreateOrderResponse(
    @SerializedName("success")  val success: Boolean,
    @SerializedName("orderId")  val orderId: String,
    @SerializedName("amount")   val amount: Int,
    @SerializedName("currency") val currency: String,
    @SerializedName("receipt")  val receipt: String
)

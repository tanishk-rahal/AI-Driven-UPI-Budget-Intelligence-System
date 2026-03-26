package com.example.budgetmanager.network

import com.example.budgetmanager.network.models.CreateOrderRequest
import com.example.budgetmanager.network.models.CreateOrderResponse
import com.example.budgetmanager.network.models.VerifyPaymentRequest
import com.example.budgetmanager.network.models.VerifyPaymentResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("create-order")
    suspend fun createOrder(
        @Body body: CreateOrderRequest
    ): CreateOrderResponse

    @POST("verify-payment")
    suspend fun verifyPayment(
        @Body body: VerifyPaymentRequest
    ): VerifyPaymentResponse
}

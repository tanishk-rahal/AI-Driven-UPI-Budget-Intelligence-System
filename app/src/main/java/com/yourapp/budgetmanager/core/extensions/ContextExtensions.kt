package com.yourapp.budgetmanager.core.extensions

import android.content.Context
import android.content.Intent
import android.net.Uri

/**
 * Extension to build UPI payment intent.
 * Used when launching external UPI app from IntentCapture flow.
 */
fun Context.createUpiIntent(
    upiId: String,
    amount: Double,
    name: String,
    note: String? = null
): Intent {
    val uriBuilder = Uri.parse("upi://pay").buildUpon()
        .appendQueryParameter("pa", upiId)
        .appendQueryParameter("pn", name)
        .appendQueryParameter("am", amount.toString())
    note?.let { uriBuilder.appendQueryParameter("tn", it) }
    return Intent(Intent.ACTION_VIEW).setData(uriBuilder.build())
}

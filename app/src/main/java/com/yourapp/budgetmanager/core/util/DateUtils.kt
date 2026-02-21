package com.yourapp.budgetmanager.core.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Centralized date formatting utilities.
 * Keeps date display consistent across the app.
 */
object DateUtils {

    private val displayFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
    private val dateOnlyFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    fun formatTimestamp(timestamp: Long): String = displayFormat.format(Date(timestamp))
    fun formatDateOnly(timestamp: Long): String = dateOnlyFormat.format(Date(timestamp))
}

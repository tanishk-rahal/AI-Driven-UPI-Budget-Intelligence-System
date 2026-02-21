package com.yourapp.budgetmanager.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yourapp.budgetmanager.core.constants.AppConstants
import com.yourapp.budgetmanager.data.local.dao.BudgetDao
import com.yourapp.budgetmanager.data.local.dao.TransactionDao
import com.yourapp.budgetmanager.data.local.entity.BudgetEntity
import com.yourapp.budgetmanager.data.local.entity.TransactionEntity

@Database(
    entities = [TransactionEntity::class, BudgetEntity::class],
    version = AppConstants.DATABASE_VERSION,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun budgetDao(): BudgetDao
}

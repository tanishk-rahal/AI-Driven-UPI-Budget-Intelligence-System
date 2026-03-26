package com.example.budgetmanager.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

data class CategoryTotal(
    val category: String,
    val total: Double
)

@Dao
interface ExpenseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(expense: ExpenseLog)

    @Query("SELECT * FROM expense_logs ORDER BY timestamp DESC")
    fun getAllExpenses(): Flow<List<ExpenseLog>>

    @Query("SELECT * FROM expense_logs WHERE status = :status ORDER BY timestamp DESC")
    fun getExpensesByStatus(status: String): Flow<List<ExpenseLog>>

    @Query("SELECT SUM(amount) FROM expense_logs WHERE timestamp > :startOfMonth AND status = 'SUCCESS'")
    fun getMonthlyTotal(startOfMonth: Long): Flow<Double?>

    @Query("SELECT category, SUM(amount) as total FROM expense_logs WHERE status = 'SUCCESS' GROUP BY category ORDER BY total DESC")
    fun getCategoryTotals(): Flow<List<CategoryTotal>>

    @Query("UPDATE expense_logs SET status = :status, isConfirmed = :isConfirmed WHERE id = :id")
    suspend fun updateStatus(id: String, status: String, isConfirmed: Boolean)

    @Query("DELETE FROM expense_logs WHERE id = :id")
    suspend fun deleteExpense(id: String)
}

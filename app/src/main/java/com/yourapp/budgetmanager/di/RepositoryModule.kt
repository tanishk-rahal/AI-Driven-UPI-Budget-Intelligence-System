package com.yourapp.budgetmanager.di

import com.yourapp.budgetmanager.data.repository.BudgetRepositoryImpl
import com.yourapp.budgetmanager.data.repository.TransactionRepositoryImpl
import com.yourapp.budgetmanager.domain.repository.BudgetRepository
import com.yourapp.budgetmanager.domain.repository.TransactionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindTransactionRepository(impl: TransactionRepositoryImpl): TransactionRepository

    @Binds
    @Singleton
    abstract fun bindBudgetRepository(impl: BudgetRepositoryImpl): BudgetRepository
}

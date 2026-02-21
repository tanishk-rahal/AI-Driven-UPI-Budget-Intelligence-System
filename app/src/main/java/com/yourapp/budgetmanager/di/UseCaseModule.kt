package com.yourapp.budgetmanager.di

import com.yourapp.budgetmanager.data.repository.MockIntentAnalyzer
import com.yourapp.budgetmanager.domain.usecase.IntentAnalyzer
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {

    @Binds
    @Singleton
    abstract fun bindIntentAnalyzer(mock: MockIntentAnalyzer): IntentAnalyzer
}

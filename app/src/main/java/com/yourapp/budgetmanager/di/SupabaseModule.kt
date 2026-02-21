package com.yourapp.budgetmanager.di

import com.yourapp.budgetmanager.data.remote.supabase.SupabaseClient
import com.yourapp.budgetmanager.data.remote.supabase.SupabaseClientPlaceholder
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Placeholder module. When Supabase is integrated, provide real [SupabaseClient] here.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class SupabaseModule {

    @Binds
    @Singleton
    abstract fun bindSupabaseClient(placeholder: SupabaseClientPlaceholder): SupabaseClient
}

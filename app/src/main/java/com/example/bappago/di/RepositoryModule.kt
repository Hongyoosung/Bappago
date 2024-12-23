package com.example.bappago.di

import com.example.bappago.data.repository.AuthRepositoryImpl
import com.example.bappago.data.repository.MealPlanRepositoryImpl
import com.example.bappago.domain.repository.AuthRepository
import com.example.bappago.domain.repository.MealPlanRepository
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
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindMealPlanRepository(
        mealPlanRepositoryImpl: MealPlanRepositoryImpl
    ): MealPlanRepository
}
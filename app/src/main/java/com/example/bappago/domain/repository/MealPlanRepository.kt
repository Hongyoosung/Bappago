package com.example.bappago.domain.repository

import com.example.bappago.domain.model.MealPlan

interface MealPlanRepository {
    suspend fun generateMealPlan(prompt: String): Result<MealPlan>
}
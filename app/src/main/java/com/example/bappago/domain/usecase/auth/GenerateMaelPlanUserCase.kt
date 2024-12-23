package com.example.bappago.domain.usecase.auth

import com.example.bappago.domain.model.MealPlan
import com.example.bappago.domain.repository.MealPlanRepository
import javax.inject.Inject

class GenerateMealPlanUseCase @Inject constructor(
    private val mealPlanRepository: MealPlanRepository
) {
    suspend operator fun invoke(prompt: String): Result<MealPlan> {
        return mealPlanRepository.generateMealPlan(prompt)
    }
}
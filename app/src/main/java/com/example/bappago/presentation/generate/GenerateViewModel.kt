package com.example.bappago.presentation.generate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bappago.domain.model.MealPlan
import com.example.bappago.domain.usecase.auth.GenerateMealPlanUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GenerateViewModel @Inject constructor(
    private val generateMealPlanUseCase: GenerateMealPlanUseCase  // UseCase 주입
) : ViewModel() {
    private val _uiState = MutableStateFlow<GenerateUiState>(GenerateUiState.Initial)
    val uiState: StateFlow<GenerateUiState> = _uiState

    fun generateMealPlan(prompt: String) {
        viewModelScope.launch {
            _uiState.value = GenerateUiState.Loading
            generateMealPlanUseCase(prompt)
                .onSuccess { mealPlan ->
                    _uiState.value = GenerateUiState.Success(mealPlan)
                }
                .onFailure { error ->
                    _uiState.value = GenerateUiState.Error(error.message ?: "Unknown error")
                }
        }
    }
}

sealed class GenerateUiState {
    object Initial : GenerateUiState()
    object Loading : GenerateUiState()
    data class Success(val mealPlan: MealPlan) : GenerateUiState()
    data class Error(val message: String) : GenerateUiState()
}

data class MealPlan(
    val id: String,
    val startDate: String,
    val endDate: String,
    val meals: List<Meal>
)

data class Meal(
    val date: String,
    val dishName: String,
    val recipe: Recipe
)

data class Recipe(
    val ingredients: List<String>,
    val steps: List<String>,
    val cookingTime: Int,
    val difficulty: String
)
package com.example.bappago.domain.model

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
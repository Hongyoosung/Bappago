package com.example.bappago.util

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.example.bappago.domain.model.MealPlan

object B_JsonUtil {
    fun parseMealPlan(json: String, gson: Gson): Result<MealPlan> {
        return try {
            val mealPlan = gson.fromJson(json, MealPlan::class.java)
            Result.success(mealPlan)
        } catch (e: JsonSyntaxException) {
            Result.failure(e)
        }
    }
}
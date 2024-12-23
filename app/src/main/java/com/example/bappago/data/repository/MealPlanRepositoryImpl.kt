package com.example.bappago.data.repository

import androidx.media3.transformer.JsonUtil
import com.example.bappago.data.remote.ClaudeApi
import com.example.bappago.data.remote.ClaudeRequest
import com.example.bappago.domain.model.MealPlan
import com.example.bappago.domain.repository.MealPlanRepository
import com.example.bappago.util.B_JsonUtil
import com.google.gson.Gson
import javax.inject.Inject

class MealPlanRepositoryImpl @Inject constructor(
    private val claudeApi: ClaudeApi,
    private val gson: Gson
) : MealPlanRepository {

    override suspend fun generateMealPlan(prompt: String): Result<MealPlan> {
        return try {
            val request = ClaudeRequest(
                messages = listOf(
                    ClaudeRequest.Message(
                        content = buildPrompt(prompt)
                    )
                )
            )

            val response = claudeApi.generateMealPlan(request)
            if (response.isSuccessful) {
                response.body()?.let { claudeResponse ->
                    val mealPlanJson = extractJsonFromResponse(claudeResponse.content.first().text)
                    com.example.bappago.util.B_JsonUtil.parseMealPlan(mealPlanJson, gson)
                } ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception("API call failed: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun extractJsonFromResponse(text: String): String {
        // Claude가 JSON 외에 다른 텍스트를 포함할 수 있으므로 JSON 부분만 추출
        val jsonStart = text.indexOf("{")
        val jsonEnd = text.lastIndexOf("}") + 1
        return if (jsonStart != -1 && jsonEnd != -1) {
            text.substring(jsonStart, jsonEnd)
        } else {
            throw IllegalStateException("Invalid JSON response")
        }
    }

    private fun buildPrompt(userPrompt: String): String {
        return """
        당신은 전문 요리사입니다. 다음 조건에 맞는 식단을 JSON 형식으로 생성해주세요.
        
        조건:
        1. 모든 날짜는 "YYYY-MM-DD" 형식을 사용합니다.
        2. 각 요리에는 구체적인 레시피와 조리시간, 난이도가 포함되어야 합니다.
        3. 재료는 구체적인 양과 함께 명시해야 합니다.
        
        사용자 요청: $userPrompt
        
        다음 JSON 형식으로 응답해주세요:
        {
            "id": "고유ID",
            "startDate": "시작날짜",
            "endDate": "종료날짜",
            "meals": [
                {
                    "date": "날짜",
                    "dishName": "요리명",
                    "recipe": {
                        "ingredients": ["재료1 (계량)", "재료2 (계량)"],
                        "steps": ["조리단계1", "조리단계2"],
                        "cookingTime": 조리시간(분),
                        "difficulty": "난이도(쉬움/보통/어려움)"
                    }
                }
            ]
        }
        
        JSON 형식만 응답하고 다른 텍스트는 포함하지 마세요.
        """.trimIndent()
    }
}
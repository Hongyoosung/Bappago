package com.example.bappago.data.remote

import com.example.bappago.BuildConfig
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST


interface ClaudeApi {
    @Headers(
        "Content-Type: application/json",
        "x-api-key: ${BuildConfig.CLAUDE_API_KEY}"
    )
    @POST("v1/messages")
    suspend fun generateMealPlan(
        @Body request: ClaudeRequest
    ): Response<ClaudeResponse>
}

data class ClaudeRequest(
    val model: String = "claude-3-opus-20240229",
    val max_tokens: Int = 4096,
    val messages: List<Message>
) {
    data class Message(
        val role: String = "user",
        val content: String
    )
}

data class ClaudeResponse(
    val id: String,
    val model: String,
    val role: String,
    val content: List<Content>
) {
    data class Content(
        val type: String,
        val text: String
    )
}
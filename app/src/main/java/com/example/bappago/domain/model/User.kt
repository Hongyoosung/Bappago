package com.example.bappago.domain.model

data class User(
    val id: String,
    val email: String,
    val name: String,
    val profileImage: String? = null,
    val loginType: LoginType
)

enum class LoginType {
    KAKAO,
    NAVER,
    GUEST
}
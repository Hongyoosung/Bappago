package com.example.bappago.domain.usecase.auth

import com.example.bappago.domain.model.LoginType
import com.example.bappago.domain.model.User
import com.example.bappago.domain.repository.AuthRepository
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(loginType: LoginType): Result<User> {
        return when (loginType) {
            LoginType.KAKAO -> authRepository.signInWithKakao()
            LoginType.NAVER -> authRepository.signInWithNaver()
            else -> Result.failure(IllegalArgumentException("Unsupported login type"))
        }
    }
}
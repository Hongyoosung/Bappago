package com.example.bappago.data.repository

import com.example.bappago.domain.model.User
import com.example.bappago.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor() : AuthRepository {
    private val userFlow = MutableStateFlow<User?>(null)

    override suspend fun signInWithKakao(): Result<User> {
        // TODO: Implement Kakao login
        return Result.failure(IllegalStateException("Not implemented yet"))
    }

    override suspend fun signInWithNaver(): Result<User> {
        // TODO: Implement Naver login
        return Result.failure(IllegalStateException("Not implemented yet"))
    }

    override suspend fun signOut() {
        userFlow.value = null
    }

    override fun getUserFlow(): Flow<User?> = userFlow
}
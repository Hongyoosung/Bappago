package com.example.bappago.domain.repository

import com.example.bappago.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun signInWithKakao(): Result<User>
    suspend fun signInWithNaver(): Result<User>
    suspend fun signOut()
    fun getUserFlow(): Flow<User?>
}
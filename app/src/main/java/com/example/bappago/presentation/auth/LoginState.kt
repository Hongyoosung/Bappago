package com.example.bappago.presentation.auth

import com.example.bappago.domain.model.User

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val user: Result<User>) : LoginState()
    data class Error(val message: String) : LoginState()
}
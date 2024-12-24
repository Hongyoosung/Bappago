package com.example.bappago.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bappago.domain.model.LoginType
import com.example.bappago.domain.usecase.auth.SignInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    fun signIn(loginType: LoginType) {
        viewModelScope.launch(Dispatchers.IO) {  // Dispatcher 명시
            try {
                _loginState.value = LoginState.Loading
                val result = signInUseCase(loginType)
                withContext(Dispatchers.Main) {  // UI 업데이트는 메인 스레드에서
                    _loginState.value = LoginState.Success(result)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _loginState.value = LoginState.Error(e.message ?: "Unknown error")
                }
            }
        }
    }
}
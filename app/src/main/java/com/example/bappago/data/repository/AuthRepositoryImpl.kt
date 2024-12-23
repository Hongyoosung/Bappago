package com.example.bappago.data.repository

import android.content.Context
import com.example.bappago.domain.model.LoginType
import com.example.bappago.domain.model.User
import com.example.bappago.domain.repository.AuthRepository
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val context: Context
) : AuthRepository {
    private val userFlow = MutableStateFlow<User?>(null)

    override suspend fun signInWithKakao(): Result<User> = try {
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            handleKakaoTalkLogin()
        } else {
            handleKakaoAccountLogin()
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    private suspend fun handleKakaoTalkLogin(): Result<User> = suspendCancellableCoroutine { continuation ->
        UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
            when {
                error != null -> continuation.resume(Result.failure(error))
                token != null -> {
                    // Get user info
                    UserApiClient.instance.me { user, error ->
                        when {
                            error != null -> continuation.resume(Result.failure(error))
                            user != null -> {
                                val mappedUser = User(
                                    id = user.id.toString(),
                                    email = user.kakaoAccount?.email ?: "",
                                    name = user.kakaoAccount?.profile?.nickname ?: "",
                                    profileImage = user.kakaoAccount?.profile?.profileImageUrl,
                                    loginType = LoginType.KAKAO
                                )
                                userFlow.value = mappedUser
                                continuation.resume(Result.success(mappedUser))
                            }
                        }
                    }
                }
            }
        }
    }

    private suspend fun handleKakaoAccountLogin(): Result<User> = suspendCancellableCoroutine { continuation ->
        UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
            // handleKakaoTalkLogin과 동일한 처리
            // 코드 중복을 피하기 위해 실제 구현시에는 공통 함수로 분리하는 것이 좋습니다
        }
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
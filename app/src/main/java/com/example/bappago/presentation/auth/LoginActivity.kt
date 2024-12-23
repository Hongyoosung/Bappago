package com.example.bappago.presentation.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.bappago.databinding.ActivityLoginBinding
import com.example.bappago.domain.model.LoginType
import com.example.bappago.presentation.MainActivity
import com.kakao.sdk.common.util.Utility
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickListeners()
        observeLoginState()

        val keyHash = Utility.getKeyHash(this)
        Log.d("KeyHash", keyHash)
    }

    private fun setupClickListeners() {
        binding.btnKakaoLogin.setOnClickListener {
            viewModel.signIn(LoginType.KAKAO)
        }

        binding.btnNaverLogin.setOnClickListener {
            viewModel.signIn(LoginType.NAVER)
        }
    }

    private fun observeLoginState() {
        lifecycleScope.launch {
            try {
                viewModel.loginState.collect { state ->
                    when (state) {
                        is LoginState.Idle -> {
                            binding.progressBar.visibility = View.GONE
                        }
                        is LoginState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is LoginState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            finish()  // LoginActivity 종료
                        }
                        is LoginState.Error -> {
                            binding.progressBar.visibility = View.GONE
                            handleError(state.message)
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("LoginActivity", "Error in login state observation", e)
            }

        }
    }

    private fun handleError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
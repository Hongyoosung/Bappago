package com.example.bappago.presentation.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.bappago.databinding.ActivityLoginBinding
import com.example.bappago.domain.model.LoginType
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
                        // TODO: Navigate to MainActivity
                    }
                    is LoginState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(this@LoginActivity, state.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
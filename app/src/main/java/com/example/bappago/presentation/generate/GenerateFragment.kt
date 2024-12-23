package com.example.bappago.presentation.generate

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bappago.databinding.FragmentGenerateBinding
import com.example.bappago.domain.model.MealPlan
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GenerateFragment : Fragment() {
    private var _binding: FragmentGenerateBinding? = null
    private val binding get() = _binding!!

    private val viewModel: GenerateViewModel by viewModels()
    private lateinit var mealPlanAdapter: MealPlanAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupViews()
        observeState()
    }

    private fun setupViews() {
        binding.btnGenerate.setOnClickListener {
            val prompt = binding.etPrompt.text.toString()
            if (prompt.isNotEmpty()) {
                viewModel.generateMealPlan(prompt)
            }
        }
    }

    private fun setupRecyclerView() {
        mealPlanAdapter = MealPlanAdapter { meal ->
            // TODO: 레시피 상세 화면으로 이동
        }

        binding.rvMealPlan.apply {
            adapter = mealPlanAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when (state) {
                    is GenerateUiState.Initial -> {
                        binding.progressBar.visibility = View.GONE
                        binding.groupResult.visibility = View.GONE
                    }
                    is GenerateUiState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.groupResult.visibility = View.GONE
                    }
                    is GenerateUiState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.groupResult.visibility = View.VISIBLE
                        displayMealPlan(state.mealPlan)
                    }
                    is GenerateUiState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.groupResult.visibility = View.GONE
                        showError(state.message)
                    }
                }
            }
        }
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun displayMealPlan(mealPlan: MealPlan) {
        binding.tvPeriod.text = "기간: ${mealPlan.startDate} ~ ${mealPlan.endDate}"
        mealPlanAdapter.submitList(mealPlan.meals)
    }
}
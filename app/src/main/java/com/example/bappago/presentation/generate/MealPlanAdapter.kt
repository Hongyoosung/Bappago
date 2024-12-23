package com.example.bappago.presentation.generate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bappago.databinding.ItemMealBinding
import com.example.bappago.domain.model.Meal

class MealPlanAdapter(
    private val onMealClick: (Meal) -> Unit
) : ListAdapter<Meal, MealPlanAdapter.MealViewHolder>(MealDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        return MealViewHolder(
            ItemMealBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MealViewHolder(
        private val binding: ItemMealBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onMealClick(getItem(position))
                }
            }
        }

        fun bind(meal: Meal) {
            binding.tvDate.text = meal.date
            binding.tvDishName.text = meal.dishName
            binding.tvCookingTime.text = "${meal.recipe.cookingTime}분"
            binding.tvDifficulty.text = meal.recipe.difficulty
        }
    }

    private class MealDiffCallback : DiffUtil.ItemCallback<Meal>() {
        override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem.date == newItem.date
        }

        override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem == newItem
        }
    }
}
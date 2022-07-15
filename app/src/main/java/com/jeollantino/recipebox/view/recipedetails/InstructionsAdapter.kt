package com.jeollantino.recipebox.view.recipedetails

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jeollantino.recipebox.R
import com.jeollantino.recipebox.models.Step

class InstructionsAdapter : RecyclerView.Adapter<InstructionsAdapter.ViewHolder>() {

    private var steps: List<Step> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_steps, parent, false)
        return ViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val step = steps[position]

        holder.tvStepCount.text = "${step.number}"

        var ingredientsStr = "Ingredients:"
        for (ingredient in step.ingredients) {
            ingredientsStr = "$ingredientsStr ${ingredient.name},"
        }
        ingredientsStr = ingredientsStr.dropLast(1).trim()
        holder.tvIngredients.text = "$ingredientsStr"
        holder.tvDirections.text = "${step.step}"
    }

    override fun getItemCount(): Int {
        return steps.size
    }

    fun setSteps(steps: List<Step>) {
        this.steps = steps
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvStepCount: TextView
        val tvIngredients: TextView
        val tvDirections: TextView

        init {
            tvStepCount = itemView.findViewById(R.id.tv_stepCount)
            tvIngredients = itemView.findViewById(R.id.tv_ingredients)
            tvDirections = itemView.findViewById(R.id.tv_directions)
        }
    }

    interface OnSelectItemCallback {
        fun onSelect(item: Step)
    }
}

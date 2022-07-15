package com.jeollantino.recipebox.view.recipelist

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.jeollantino.recipebox.R
import com.jeollantino.recipebox.models.Recipe

class DishTypesAdapter : RecyclerView.Adapter<DishTypesAdapter.ViewHolder>() {

    private var dishTypes: List<String> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_dish_type, parent, false)
        return ViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dishType = dishTypes[position]

        holder.btnDishType.text = "$dishType"
    }

    override fun getItemCount(): Int {
        return dishTypes.size
    }

    fun setDishTypes(dishTypes: List<String>) {
        this.dishTypes = dishTypes
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val btnDishType: Button

        init {
            btnDishType = itemView.findViewById(R.id.btn_dishType)
        }
    }

    interface OnSelectItemCallback {
        fun onSelect(item: Recipe)
    }
}

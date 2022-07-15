package com.jeollantino.recipebox.view.recipedetails

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.jeollantino.recipebox.R
import com.jeollantino.recipebox.common.Constants.Companion.IMAGE_BASE_URL
import com.jeollantino.recipebox.models.Ingredient
import com.jeollantino.recipebox.models.Recipe

class IngredientsAdapter : RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {

    private var ingredients: List<Ingredient> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ingredients, parent, false)
        return ViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ingredient = ingredients[position]

        holder.tvTitle.text = "${ingredient.original}"

        holder.ivThumbnail.scaleType = ImageView.ScaleType.CENTER_CROP
        val options: RequestOptions = RequestOptions()
            .centerCrop()
            .placeholder(R.drawable.ic_logo)
            .error(R.drawable.ic_logo)
        Glide.with(holder.itemView)
            .load("$IMAGE_BASE_URL${ingredient.image}")
            .apply(options)
            .into(holder.ivThumbnail)
    }

    override fun getItemCount(): Int {
        return ingredients.size
    }

    fun setIngredients(ingredients: List<Ingredient>) {
        this.ingredients = ingredients
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivThumbnail: ImageView
        val tvTitle: TextView

        init {
            ivThumbnail = itemView.findViewById(R.id.iv_thumbnail)
            tvTitle = itemView.findViewById(R.id.tv_title)
        }
    }

    interface OnSelectItemCallback {
        fun onSelect(item: Recipe)
    }
}

package com.jeollantino.recipebox.view.recipelist

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.jeollantino.recipebox.R
import com.jeollantino.recipebox.models.Recipe

class RecipeListAdapter(
    private val context: Context,
    private val callback: OnSelectItemCallback
) : RecyclerView.Adapter<RecipeListAdapter.ViewHolder>() {

    private var recipeList: List<Recipe> = ArrayList()
    private lateinit var adapter: DishTypesAdapter

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recipe_list, parent, false)
        return ViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe = recipeList[position]

        holder.tvTitle.text = "${recipe.title}"
        holder.tvMinutes.text = "${recipe.readyInMinutes} MIN"
        holder.tvLikes.text = "${recipe.aggregateLikes} LIKES"
        holder.tvServings.text = "${recipe.servings} SERVINGS(S)"
        holder.tvPrice.text = "$ ${String.format("%.2f", recipe.pricePerServing)}"

        holder.ivThumbnail.scaleType = ImageView.ScaleType.CENTER_CROP
        val options: RequestOptions = RequestOptions()
            .centerCrop()
            .placeholder(R.drawable.ic_logo)
            .error(R.drawable.ic_logo)
        Glide.with(holder.itemView)
            .load(recipe.image)
            .apply(options)
            .into(holder.ivThumbnail)

        adapter = DishTypesAdapter()
        holder.rvDishType.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        holder.rvDishType.setHasFixedSize(true)
        holder.rvDishType.adapter = adapter

        adapter.setDishTypes(recipe.dishTypes)

        holder.ivSave.setOnClickListener {
            callback.onSave(recipe)
        }

        holder.cvContainer.setOnClickListener {
            callback.onSelect(recipe)
        }
    }

    override fun getItemCount(): Int {
        return recipeList.size
    }

    fun setResults(recipeList: List<Recipe>) {
        this.recipeList = recipeList
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivThumbnail: ImageView
        val ivSave: ImageView
        val tvTitle: TextView
        val tvMinutes: TextView
        val tvLikes: TextView
        val tvServings: TextView
        val tvPrice: TextView
        val rvDishType: RecyclerView
        val cvContainer: CardView

        init {
            ivThumbnail = itemView.findViewById(R.id.iv_thumbnail)
            ivSave = itemView.findViewById(R.id.iv_save)
            tvTitle = itemView.findViewById(R.id.tv_title)
            tvMinutes = itemView.findViewById(R.id.tv_minutes)
            tvLikes = itemView.findViewById(R.id.tv_likes)
            tvServings = itemView.findViewById(R.id.tv_servings)
            tvPrice = itemView.findViewById(R.id.tv_price)
            rvDishType = itemView.findViewById(R.id.rv_dishType)
            cvContainer = itemView.findViewById(R.id.cv_container)
        }
    }

    interface OnSelectItemCallback {
        fun onSelect(item: Recipe)
        fun onSave(recipe: Recipe)
    }
}

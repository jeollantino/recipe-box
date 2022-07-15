package com.jeollantino.recipebox.view.recipedetails

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import com.jeollantino.recipebox.R
import com.jeollantino.recipebox.base.BaseActivity
import com.jeollantino.recipebox.models.Recipe
import com.jeollantino.recipebox.view.RecipeViewModel
import com.jeollantino.recipebox.view.recipelist.DishTypesAdapter
import com.jeollantino.recipebox.view.recipelist.RecipeListActivity
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_recipe_details.*
import kotlinx.android.synthetic.main.activity_recipe_details.btn_seeMore
import kotlinx.android.synthetic.main.activity_recipe_details.iv_seeMore
import kotlinx.android.synthetic.main.activity_recipe_details.ll_backButton
import kotlinx.android.synthetic.main.activity_recipe_details.tv_seeMore

/**
 * The RecipeDetails activity displays the detailed view of
 * of a specific recipe
 *
 * @author Jeo Llantino
 * @version 1.0
 * @since 2022-07-14
 */
class RecipeDetailsActivity : BaseActivity() {

    val TAG: String? = RecipeDetailsActivity::class.java.simpleName

    private lateinit var viewModel: RecipeViewModel
    private lateinit var ingredientsAdapter: IngredientsAdapter
    private lateinit var instructionsAdapter: InstructionsAdapter
    private lateinit var dishTypesAdapter: DishTypesAdapter
    private lateinit var extraContentData: String
    private lateinit var recipeData: Recipe

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_details)

        init()
    }

    /**
     * Initialization method upon onCreate()
     */
    private fun init() {
        viewModel = ViewModelProviders.of(this).get(RecipeViewModel::class.java)

        getExtraData()
        setupUI()
    }

    /**
     * Retrieve intent extras from previous activity here and set to extraContentData
     */
    private fun getExtraData() {
        if (intent.extras != null) {
            extraContentData = intent.extras?.getString(EXTRA_CONTENT_DATA).toString()

            val gson = Gson()
            recipeData = gson.fromJson(extraContentData, Recipe::class.java)
        }
    }

    /**
     * Set all data from contentData to UI widgets and initialize listeners
     */
    private fun setupUI() {
        ll_backButton.setOnClickListener {
            onBackPressed()
        }

        iv_thumbnail.scaleType = ImageView.ScaleType.CENTER_CROP
        val options: RequestOptions = RequestOptions()
            .centerCrop()
            .placeholder(R.drawable.ic_logo)
            .error(R.drawable.ic_logo)
        Glide.with(this)
            .load(recipeData.image)
            .apply(options)
            .into(iv_thumbnail)

        tv_title.text = recipeData.title
        tv_minutes.text = "${recipeData.readyInMinutes} MIN"
        tv_likes.text = "${recipeData.aggregateLikes} LIKES"
        tv_servings.text = "${recipeData.servings} SERVINGS(S)"
        tv_price.text = "$ ${String.format("%.2f", recipeData.pricePerServing)}"
        tv_desc.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(recipeData.summary, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(recipeData.summary)
        }
        tv_ingredients.text = "Ingredients (${recipeData.extendedIngredients.size})"

        dishTypesAdapter = DishTypesAdapter()
        rv_dishType.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rv_dishType.setHasFixedSize(true)
        rv_dishType.adapter = dishTypesAdapter

        dishTypesAdapter.setDishTypes(recipeData.dishTypes)

        if (recipeData.dishTypes.isNotEmpty()) {
            tv_lblDishType.visibility = VISIBLE
            rv_dishType.visibility = VISIBLE
        } else {
            tv_lblDishType.visibility = GONE
            rv_dishType.visibility = GONE
        }

        if (recipeData.cuisines.isNotEmpty()) {
            tv_lblCuisines.visibility = VISIBLE
            tv_cuisines.visibility = VISIBLE

            var cuisinesStr = ""
            for (cuisine in recipeData.cuisines) {
                cuisinesStr = "$cuisinesStr $cuisine,"
            }
            cuisinesStr = cuisinesStr.dropLast(1).trim()
            tv_cuisines.text = cuisinesStr
        } else {
            tv_lblCuisines.visibility = GONE
            tv_cuisines.visibility = GONE
        }

        if (recipeData.diets.isNotEmpty()) {
            tv_lblDiets.visibility = VISIBLE
            tv_diets.visibility = VISIBLE

            var dietsStr = ""
            for (diet in recipeData.diets) {
                dietsStr = "$dietsStr $diet,"
            }
            dietsStr = dietsStr.dropLast(1).trim()
            tv_diets.text = dietsStr
        } else {
            tv_lblDiets.visibility = GONE
            tv_diets.visibility = GONE
        }

        if (recipeData.occasions.isNotEmpty()) {
            tv_lblOccasions.visibility = VISIBLE
            tv_occasions.visibility = VISIBLE

            var occasionsStr = ""
            for (occasions in recipeData.occasions) {
                occasionsStr = "$occasionsStr $occasions,"
            }
            occasionsStr = occasionsStr.dropLast(1).trim()
            tv_occasions.text = occasionsStr
        } else {
            tv_lblOccasions.visibility = GONE
            tv_occasions.visibility = GONE
        }

        ingredientsAdapter = IngredientsAdapter()
        rv_ingredients.layoutManager = LinearLayoutManager(this)
        rv_ingredients.setHasFixedSize(true)
        rv_ingredients.adapter = ingredientsAdapter
        ingredientsAdapter.setIngredients(recipeData.extendedIngredients)

        instructionsAdapter = InstructionsAdapter()
        rv_directions.layoutManager = LinearLayoutManager(this)
        rv_directions.setHasFixedSize(true)
        rv_directions.adapter = instructionsAdapter
        if (recipeData.analyzedInstructions.isNotEmpty()) {
            instructionsAdapter.setSteps(recipeData.analyzedInstructions[0].steps)
        }

        btn_seeMore.setOnClickListener {
            switchNoreContent()
        }

        iv_save.setOnClickListener {
            insertSavedRecipeToDB(recipeData.id.toLong(), Gson().toJson(recipeData).toString())
            Toast.makeText(this@RecipeDetailsActivity, "Successfully added to Saved Recipes!", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Set trigger for See More / See Less button
     */
    private fun switchNoreContent() {
        if (ll_seeMoreContents.isVisible) {
            ll_seeMoreContents.visibility = GONE
            tv_seeMore.text = "SEE MORE"
            iv_seeMore.setImageResource(R.drawable.ic_dropdown)
        } else {
            ll_seeMoreContents.visibility = VISIBLE
            tv_seeMore.text = "SEE LESS"
            iv_seeMore.setImageResource(R.drawable.ic_liftup)
        }
    }

    /**
     * Saves the last content being viewed by the user; set param value to "" for reset
     *
     * @param recipeData  The data of the recipe in JSON format converted to String
     */
    private fun insertSavedRecipeToDB(recipeId: Long, recipeData: String) {
        viewModel.insertSavedRecipe(this@RecipeDetailsActivity, recipeId, recipeData)
    }

    companion object {
        const val EXTRA_CONTENT_DATA = "EXTRA_CONTENT_TYPE"
    }

    override fun onBackPressed() {
        super.onBackPressed()

        val intent = Intent(this, RecipeListActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }
}

package com.jeollantino.recipebox.view.savedrecipes

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.google.gson.Gson
import com.jeollantino.recipebox.R
import com.jeollantino.recipebox.base.BaseActivity
import com.jeollantino.recipebox.models.Recipe
import com.jeollantino.recipebox.view.RecipeViewModel
import com.jeollantino.recipebox.view.recipedetails.RecipeDetailsActivity
import com.jeollantino.recipebox.view.recipelist.RecipeListActivity
import com.jeollantino.recipebox.view.recipelist.RecipeListAdapter
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_recipe_list.btn_tryAgain
import kotlinx.android.synthetic.main.activity_recipe_list.layoutRefresh
import kotlinx.android.synthetic.main.activity_recipe_list.ll_contentEmpty
import kotlinx.android.synthetic.main.activity_recipe_list.rv_contentList
import kotlinx.android.synthetic.main.activity_saved_recipes.*

/**
 * The RecipeList activity displays the list view of
 * of random recipes
 *
 * @author Jeo Llantino
 * @version 1.0
 * @since 2022-07-14
 */
class SavedRecipesActivity :
    BaseActivity(),
    RecipeListAdapter.OnSelectItemCallback {

    val TAG: String? = SavedRecipesActivity::class.java.simpleName

    private lateinit var viewModel: RecipeViewModel
    private lateinit var adapter: RecipeListAdapter
    private lateinit var progressDialog: MaterialDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_recipes)

        init()
    }

    /**
     * Initialization method upon onCreate()
     */
    private fun init() {
        viewModel = ViewModelProviders.of(this).get(RecipeViewModel::class.java)

        setupDialogs()
        setupUI()
        subscribeUI()
        getAllSavedRecipesFromDB()
    }

    private fun setupDialogs() {
        progressDialog = MaterialDialog(this)
            .title(R.string.loading_title)
            .message(R.string.loading_msg)
            .cancelable(false)
    }

    /**
     * Initialize and setup the page's UI including
     * recyclerviews, adapters and listeners
     */
    private fun setupUI() {
        adapter = RecipeListAdapter(this, this)
        rv_contentList.layoutManager = LinearLayoutManager(this)
        rv_contentList.setHasFixedSize(true)
        rv_contentList.adapter = adapter

        layoutRefresh.setColorSchemeColors(resources.getColor(R.color.pale_spring_bud), resources.getColor(R.color.pale_spring_bud_light))
        layoutRefresh.setOnRefreshListener {
            getAllSavedRecipesFromDB()
            layoutRefresh.isRefreshing = false
        }

        iv_home.setOnClickListener {
            onBackPressed()
        }

        btn_tryAgain.setOnClickListener {
            getAllSavedRecipesFromDB()
        }
    }

    /**
     * Observes changes of livedata from the MainViewModel
     * in order to consume these changes to the UI of ContentListActivity
     */
    private fun subscribeUI() {
        viewModel.recipeList.observe(this) { recipeList ->
            if (recipeList != null) {
                setResultsEmptyList(recipeList)
                adapter.setResults(recipeList)
            }
        }

        viewModel.errorMsg.observe(this) { errorMsg ->
            if (errorMsg.isNotEmpty()) {
                rv_contentList.visibility = GONE
                ll_contentEmpty.visibility = VISIBLE
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading != null) {
                if (isLoading) {
                    progressDialog.show()
                } else {
                    progressDialog.dismiss()
                }
            }
        }
    }

    /**
     * Determines the size of list and update visibility of recyclerview and linearlayout to UI
     *
     * @param list  The list of Results observed from livedata
     */
    private fun setResultsEmptyList(list: List<Recipe>) {
        if (list.isEmpty()) {
            rv_contentList.visibility = GONE
            ll_contentEmpty.visibility = VISIBLE
        } else {
            rv_contentList.visibility = VISIBLE
            ll_contentEmpty.visibility = GONE
        }
    }

    /**
     * Retrieves all the saved recipes (via database)
     * where the user requested
     */
    private fun getAllSavedRecipesFromDB() {
        viewModel.getAllSavedRecipes(this@SavedRecipesActivity)!!.observe(this@SavedRecipesActivity) { savedRecipes ->
            Log.d("", "getAllSavedRecipesFromDB: $savedRecipes")

            if (savedRecipes != null) {
                var recipes: MutableList<Recipe> = arrayListOf()
                for (recipe in savedRecipes) {
                    var recipeJsonStr = recipe.recipeData
                    Log.d("", "getAllSavedRecipesFromDB: recipeJsonStr $recipeJsonStr")
                    val recipeJson = Gson().fromJson(recipeJsonStr, Recipe::class.java)
                    recipes.add(recipeJson)
                }

                setResultsEmptyList(recipes)
                adapter.setResults(recipes)
            } else {
            }
        }
    }

    /**
     * Overrides the click/select action of user from the list via RecipeListAdapter
     *
     * @param recipe  The recipe data (clicked by user) in Recipe data class format to pass onto the RecipeDetailsActivity
     */
    override fun onSelect(recipe: Recipe) {
        val gson = Gson()
        val itemToJson = gson.toJson(recipe)

        val intent = Intent(this, RecipeDetailsActivity::class.java)
        intent.putExtra(RecipeDetailsActivity.EXTRA_CONTENT_DATA, itemToJson)
        startActivity(intent)
        finish()
    }

    /**
     * Overrides the click/select action of user from the save button via RecipeListAdapter
     *
     * @param recipe  The recipe data (clicked by user) in Recipe data class format to save to database
     */
    override fun onSave(recipe: Recipe) {
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

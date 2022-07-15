package com.jeollantino.recipebox.view

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jeollantino.recipebox.datasource.MainDataSource
import com.jeollantino.recipebox.models.Recipe
import com.jeollantino.recipebox.models.RecipesResponse
import com.jeollantino.recipebox.repositories.RecipeRepository
import com.jeollantino.recipebox.room.SavedRecipes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecipeViewModel(application: Application) : AndroidViewModel(application) {

    val TAG: String? = RecipeViewModel::class.java.simpleName

    val recipeList = MutableLiveData<List<Recipe>>()
    val errorMsg = MutableLiveData<String>()
    val isLoading = MutableLiveData<Boolean>()

    var savedRecipes: LiveData<List<SavedRecipes>>? = null

    /**
     * Searches for content via SearchRepository api trigger
     *
     * @param term      The URL-encoded text string you want to search for. For example: jack+johnson.
     * @param country   The two-letter country code for the store you want to search. The search uses the default store front for the specified country. For example: US.
     * @param media     The media type you want to search for. For example: movie.
     *
     * @return On success, returns List<Results> values and stores in resultsList MutableLiveData
     * @return On failed, returns String error message
     */
    suspend fun getRandomRecipes(number: Int) {
        withContext(Main) {
            isLoading.value = true
        }

        val recipeRepository = RecipeRepository(getApplication())
        recipeRepository.getRandomRecipes(
            number,
            object : MainDataSource.GetRandomRecipes {
                override fun success(response: RecipesResponse) {
                    isLoading.value = false
                    errorMsg.value = ""
                    recipeList.value = response.recipes
                }

                override fun failed(errorMessage: String) {
                    isLoading.value = false
                    errorMsg.value = errorMessage
                }
            }
        )
    }

    /**
     * Saves the last content data (via database)
     * visited by the user in the app
     *
     * @param context      The current context of the app
     * @param recipeData   The id of the recipe being saved
     * @param recipeData   The data of the recipe stored in JSON format converted to String
     */
    fun insertSavedRecipe(context: Context, recipeId: Long, recipeData: String) {
        val recipeRepository = RecipeRepository(getApplication())
        recipeRepository.insertSavedRecipe(context, recipeId, recipeData)
    }

    /**
     * Retrieves the list of saved recipes (via database)
     * where the user requested
     *
     * @param context   The current context of the app
     *
     * @return The LiveData of List<SavedRecipes> stored in visitedDatesList
     */
    fun getAllSavedRecipes(context: Context): LiveData<List<SavedRecipes>>? {
        val recipeRepository = RecipeRepository(getApplication())
        savedRecipes = recipeRepository.getSavedRecipes(context)
        return savedRecipes
    }
}

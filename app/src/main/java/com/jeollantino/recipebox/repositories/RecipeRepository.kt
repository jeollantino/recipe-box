package com.jeollantino.recipebox.repositories

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.jeollantino.recipebox.api.APIService
import com.jeollantino.recipebox.common.Constants.Companion.API_KEY
import com.jeollantino.recipebox.common.Constants.Companion.BASE_URL
import com.jeollantino.recipebox.datasource.MainDataSource
import com.jeollantino.recipebox.models.RecipesResponse
import com.jeollantino.recipebox.room.AppDatabase
import com.jeollantino.recipebox.room.SavedRecipes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RecipeRepository(context: Context) {

    val TAG: String? = RecipeRepository::class.java.simpleName

    protected var apiService: APIService

    /**
     * Gets random recipes via APIService
     *
     * @param number (required) The number of random recipes to be returned (between 1 and 100).
     *
     * @return onResponse, triggers callback.success() with the Response<RecipeResponse> values
     * @return onFailure, triggers callback.failed() with the error message
     */
    suspend fun getRandomRecipes(number: Int, callback: MainDataSource.GetRandomRecipes) {
        val call = apiService.getRandomRecipes(number, API_KEY)
        call.enqueue(object : Callback<RecipesResponse> {
            override fun onResponse(call: Call<RecipesResponse>, response: Response<RecipesResponse>) {
                if (response.body() != null) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        responseBody?.let {
                            callback.success(it)
                            return
                        }
                    } else {
                        callback.failed("Something went wrong! Please try again.")
                    }
                } else {
                    callback.failed("Something went wrong! Please try again.")
                }
            }

            override fun onFailure(call: Call<RecipesResponse?>, t: Throwable) {
                Log.d(TAG, "Fail: " + t.message)
                callback.failed(t.message.toString())
            }
        })
    }

    /**
     * Initializes the AppDatabase
     *
     * @param context   The current context of the app
     *
     * @return Returns the AppDatabase instance
     */
    fun initializeDB(context: Context): AppDatabase {
        return AppDatabase.getDatabaseClient(context)
    }

    /**
     * Saves the requested recipe data (via database)
     * by the user in the app
     *
     * @param context       The current context of the app
     * @param recipeId      The id of the recipe being saved
     * @param recipeData    The data of the recipe stored in JSON format converted to String
     */
    fun insertSavedRecipe(context: Context, recipeId: Long, recipeData: String) {
        appDatabase = initializeDB(context)
        CoroutineScope(IO).launch {
            val savedRecipe = SavedRecipes(recipeId, recipeData)
            appDatabase!!.savedRecipesDao().insert(savedRecipe)
        }
    }

    /**
     * Retrieves the list of saved recipes (via database)
     * where the user requested
     *
     * @param context   The current context of the app
     *
     * @return The LiveData of List<SavedRecipes> stored in savedRecipes
     */
    fun getSavedRecipes(context: Context): LiveData<List<SavedRecipes>>? {
        appDatabase = initializeDB(context)
        savedRecipes = appDatabase!!.savedRecipesDao().getAll()

        return savedRecipes
    }

    companion object {
        var appDatabase: AppDatabase? = null
        var savedRecipes: LiveData<List<SavedRecipes>>? = null
    }

    init {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
        apiService = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIService::class.java)
    }
}

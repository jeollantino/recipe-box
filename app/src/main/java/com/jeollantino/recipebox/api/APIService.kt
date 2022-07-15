package com.jeollantino.recipebox.api

import com.jeollantino.recipebox.models.RecipesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {

    /**
     * Usage of the Get Random Recipes API service endpoint
     *
     * @param number (required) The number of random recipes to be returned (between 1 and 100).
     * @param apiKey (required) The api key to be used as access for Spoonacular API triggers
     *
     * @return SearchResponse data class for results from endpoint call
     */
    @GET("/recipes/random")
    fun getRandomRecipes(
        @Query("number") number: Int?,
        @Query("apiKey") apiKey: String?
    ): Call<RecipesResponse>
}

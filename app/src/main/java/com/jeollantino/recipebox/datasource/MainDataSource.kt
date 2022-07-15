package com.jeollantino.recipebox.datasource

import com.jeollantino.recipebox.models.RecipesResponse

interface MainDataSource {
    interface GetRandomRecipes {
        fun success(response: RecipesResponse)
        fun failed(errorMessage: String)
    }
}

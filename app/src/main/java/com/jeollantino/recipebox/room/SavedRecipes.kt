package com.jeollantino.recipebox.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_recipes")
data class SavedRecipes(

    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: Long? = null,

    @ColumnInfo(name = "recipe_data")
    var recipeData: String

)

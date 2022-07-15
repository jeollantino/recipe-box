package com.jeollantino.recipebox.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SavedRecipesDao {

    @Query("SELECT * FROM saved_recipes")
    fun getAll(): LiveData<List<SavedRecipes>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg savedRecipes: SavedRecipes)
}

package com.jeollantino.recipebox.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [SavedRecipes::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun savedRecipesDao(): SavedRecipesDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Creates an instance of the AppDatabase
         * If already exists, return the instance
         *
         * @param context   The current context of the app
         *
         * @return Returns the AppDatabase instance
         */
        fun getDatabaseClient(context: Context): AppDatabase {
            if (INSTANCE != null) return INSTANCE!!

            synchronized(this) {
                INSTANCE = Room
                    .databaseBuilder(context, AppDatabase::class.java, "recipe-box-db")
                    .fallbackToDestructiveMigration()
                    .build()

                return INSTANCE!!
            }
        }
    }
}

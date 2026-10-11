package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MonumentEntity::class], version = 2, exportSchema = false)
abstract class TourDatabase : RoomDatabase() {

    abstract fun monumentDao(): MonumentDao

    companion object {
        @Volatile
        private var INSTANCE: TourDatabase? = null

        fun getDatabase(context: Context): TourDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TourDatabase::class.java,
                    "om_el_donia_tour_cache.db"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

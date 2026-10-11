package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MonumentDao {

    @Query("SELECT * FROM cached_monuments ORDER BY approximateYearBce ASC")
    fun getAllCachedMonuments(): Flow<List<MonumentEntity>>

    @Query("SELECT * FROM cached_monuments WHERE eraCategory = :eraCategory ORDER BY approximateYearBce ASC")
    fun getCachedMonumentsByEra(eraCategory: String): Flow<List<MonumentEntity>>

    @Query("SELECT * FROM cached_monuments WHERE id = :id LIMIT 1")
    suspend fun getMonumentById(id: String): MonumentEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(monuments: List<MonumentEntity>)

    @Query("SELECT COUNT(*) FROM cached_monuments")
    suspend fun getCount(): Int

    @Query("DELETE FROM cached_monuments")
    suspend fun clearAll()
}

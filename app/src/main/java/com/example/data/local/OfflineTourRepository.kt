package com.example.data.local

import android.content.Context
import com.example.data.EgyptTourRepository
import com.example.data.Monument
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class OfflineTourRepository(context: Context) {

    private val database = TourDatabase.getDatabase(context)
    private val monumentDao = database.monumentDao()

    val cachedMonuments: Flow<List<Monument>> = monumentDao.getAllCachedMonuments().map { entities ->
        if (entities.isEmpty()) {
            EgyptTourRepository.monuments.sortedBy { it.approximateYearBce }
        } else {
            entities.map { entity ->
                // Preserve drawable references from static repo
                val originalMonument = EgyptTourRepository.getMonumentById(entity.id)
                entity.toMonument(originalMonument?.drawableResId)
            }
        }
    }

    fun getMonumentsByEraFlow(eraKey: String): Flow<List<Monument>> {
        return if (eraKey == "all" || eraKey.isBlank()) {
            cachedMonuments
        } else {
            monumentDao.getCachedMonumentsByEra(eraKey).map { entities ->
                if (entities.isEmpty()) {
                    EgyptTourRepository.monuments
                        .filter { it.eraCategory.equals(eraKey, ignoreCase = true) }
                        .sortedBy { it.approximateYearBce }
                } else {
                    entities.map { entity ->
                        val originalMonument = EgyptTourRepository.getMonumentById(entity.id)
                        entity.toMonument(originalMonument?.drawableResId)
                    }
                }
            }
        }
    }

    suspend fun initializeCacheIfNeeded() = withContext(Dispatchers.IO) {
        val count = monumentDao.getCount()
        if (count < EgyptTourRepository.monuments.size) {
            refreshCache()
        }
    }

    suspend fun refreshCache() = withContext(Dispatchers.IO) {
        val entities = EgyptTourRepository.monuments.map { MonumentEntity.fromMonument(it) }
        monumentDao.insertAll(entities)
    }

    suspend fun getCachedCount(): Int = withContext(Dispatchers.IO) {
        monumentDao.getCount()
    }
}

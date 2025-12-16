package com.bazooka.inventory.data.repository

import com.bazooka.inventory.data.local.dao.BikePartDao
import com.bazooka.inventory.data.local.entity.BikePart
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BikePartRepository @Inject constructor(
    private val bikePartDao: BikePartDao
) {
    fun getAllBikeParts(): Flow<List<BikePart>> = bikePartDao.getAllBikeParts()

    fun getBikePartById(id: Long): Flow<BikePart?> = bikePartDao.getBikePartById(id)

    fun getBikePartsByCategory(category: String): Flow<List<BikePart>> =
        bikePartDao.getBikePartsByCategory(category)

    fun getLowStockBikeParts(): Flow<List<BikePart>> = bikePartDao.getLowStockBikeParts()

    fun searchBikeParts(query: String): Flow<List<BikePart>> =
        bikePartDao.searchBikeParts(query)

    suspend fun insertBikePart(bikePart: BikePart): Long =
        bikePartDao.insertBikePart(bikePart)

    suspend fun updateBikePart(bikePart: BikePart) =
        bikePartDao.updateBikePart(bikePart)

    suspend fun deleteBikePart(bikePart: BikePart) =
        bikePartDao.deleteBikePart(bikePart)

    suspend fun deleteBikePartById(id: Long) =
        bikePartDao.deleteBikePartById(id)

    fun getBikePartsCount(): Flow<Int> = bikePartDao.getBikePartsCount()

    fun getTotalQuantity(): Flow<Int?> = bikePartDao.getTotalQuantity()
}

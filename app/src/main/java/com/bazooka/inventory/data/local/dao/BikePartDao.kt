package com.bazooka.inventory.data.local.dao

import androidx.room.*
import com.bazooka.inventory.data.local.entity.BikePart
import kotlinx.coroutines.flow.Flow

@Dao
interface BikePartDao {

    @Query("SELECT * FROM bike_parts ORDER BY dateAdded DESC")
    fun getAllBikeParts(): Flow<List<BikePart>>

    @Query("SELECT * FROM bike_parts WHERE id = :id")
    fun getBikePartById(id: Long): Flow<BikePart?>

    @Query("SELECT * FROM bike_parts WHERE category = :category ORDER BY dateAdded DESC")
    fun getBikePartsByCategory(category: String): Flow<List<BikePart>>

    @Query("SELECT * FROM bike_parts WHERE quantity < minimalStock ORDER BY dateAdded DESC")
    fun getLowStockBikeParts(): Flow<List<BikePart>>

    @Query("SELECT * FROM bike_parts WHERE name LIKE '%' || :searchQuery || '%' OR description LIKE '%' || :searchQuery || '%'")
    fun searchBikeParts(searchQuery: String): Flow<List<BikePart>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBikePart(bikePart: BikePart): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBikeParts(bikeParts: List<BikePart>)

    @Update
    suspend fun updateBikePart(bikePart: BikePart)

    @Delete
    suspend fun deleteBikePart(bikePart: BikePart)

    @Query("DELETE FROM bike_parts WHERE id = :id")
    suspend fun deleteBikePartById(id: Long)

    @Query("SELECT COUNT(*) FROM bike_parts")
    fun getBikePartsCount(): Flow<Int>

    @Query("SELECT SUM(quantity) FROM bike_parts")
    fun getTotalQuantity(): Flow<Int?>
}

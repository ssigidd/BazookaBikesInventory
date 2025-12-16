package com.bazooka.inventory.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bike_parts")
data class BikePart(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String = "",
    val category: String,
    val quantity: Int = 0,
    val minimalStock: Int = 0,
    val price: Double = 0.0,
    val dateAdded: Long = System.currentTimeMillis(),
    val imageUrl: String? = null,
    val manufacturer: String? = null,
    val serialNumber: String? = null
)

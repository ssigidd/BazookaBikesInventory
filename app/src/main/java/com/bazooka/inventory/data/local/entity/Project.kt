package com.bazooka.inventory.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "projects")
data class Project(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String = "",
    val dateCreated: Long = System.currentTimeMillis(),
    val targetCompletionDate: Long? = null,
    val budget: Double? = null,
    val isArchived: Boolean = false
)

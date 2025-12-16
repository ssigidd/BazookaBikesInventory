package com.bazooka.inventory.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bazooka.inventory.data.local.dao.BikePartDao
import com.bazooka.inventory.data.local.dao.ProjectDao
import com.bazooka.inventory.data.local.dao.ProjectPartDao
import com.bazooka.inventory.data.local.entity.BikePart
import com.bazooka.inventory.data.local.entity.Project
import com.bazooka.inventory.data.local.entity.ProjectPart

@Database(
    entities = [BikePart::class, Project::class, ProjectPart::class],
    version = 2,
    exportSchema = false
)
abstract class BazookaDatabase : RoomDatabase() {

    abstract fun bikePartDao(): BikePartDao
    abstract fun projectDao(): ProjectDao
    abstract fun projectPartDao(): ProjectPartDao

    companion object {
        const val DATABASE_NAME = "bazooka_database"
    }
}

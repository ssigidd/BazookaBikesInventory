package com.bazooka.inventory.di

import android.content.Context
import androidx.room.Room
import com.bazooka.inventory.data.local.BazookaDatabase
import com.bazooka.inventory.data.local.dao.BikePartDao
import com.bazooka.inventory.data.local.dao.ProjectDao
import com.bazooka.inventory.data.local.dao.ProjectPartDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideBazookaDatabase(
        @ApplicationContext context: Context
    ): BazookaDatabase {
        return Room.databaseBuilder(
            context,
            BazookaDatabase::class.java,
            BazookaDatabase.DATABASE_NAME
        )
        .fallbackToDestructiveMigration()
        .build()
    }

    @Provides
    @Singleton
    fun provideBikePartDao(database: BazookaDatabase): BikePartDao {
        return database.bikePartDao()
    }

    @Provides
    @Singleton
    fun provideProjectDao(database: BazookaDatabase): ProjectDao {
        return database.projectDao()
    }

    @Provides
    @Singleton
    fun provideProjectPartDao(database: BazookaDatabase): ProjectPartDao {
        return database.projectPartDao()
    }
}

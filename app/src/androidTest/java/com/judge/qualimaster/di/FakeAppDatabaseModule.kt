package com.judge.qualimaster.di

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.judge.qualimaster.data.AppDatabase
import com.judge.qualimaster.data.AthleteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppDatabaseModule::class]
)
object FakeAppDatabaseModule {

    @Provides
    fun provideFakeInMemoryAppDatabase() : AppDatabase {
        return Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                AppDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @Provides
    fun provideFakeAthleteDao(db: AppDatabase) : AthleteDao {
        return db.athleteDao()
    }

}
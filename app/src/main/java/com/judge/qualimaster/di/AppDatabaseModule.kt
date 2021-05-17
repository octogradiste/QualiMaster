package com.judge.qualimaster.di

import android.content.Context
import com.judge.qualimaster.data.AppDatabase
import com.judge.qualimaster.data.AthleteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppDatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context) : AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    fun provideAthleteDao(db: AppDatabase) : AthleteDao {
        return db.athleteDao()
    }

}
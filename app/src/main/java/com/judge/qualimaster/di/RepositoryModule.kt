package com.judge.qualimaster.di

import com.judge.qualimaster.data.AthleteDao
import com.judge.qualimaster.data.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import javax.inject.Singleton

@InstallIn(ActivityComponent::class)
@Module
object RepositoryModule {

    @Singleton
    @Provides
    fun provideRepository(athleteDao: AthleteDao) : Repository {
        return Repository(athleteDao)
    }

}
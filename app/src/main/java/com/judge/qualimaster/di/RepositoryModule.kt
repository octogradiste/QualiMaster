package com.judge.qualimaster.di

import com.judge.core.data.BaseRepository
import com.judge.core.data.Repository
import com.judge.core.domain.Location
import com.judge.qualimaster.data.AthleteDao
import com.judge.qualimaster.data.BaseRepositoryImpl
//import com.judge.qualimaster.data.LocalBaseRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Singleton
    @Provides
    fun provideBaseRepository(athleteDao: AthleteDao): BaseRepository {
        return BaseRepositoryImpl(athleteDao)
    }

    @Singleton
    @Provides
    fun provideRepository(baseRepository: BaseRepository) : Repository {
        return Repository(baseRepository, Location)
    }

}
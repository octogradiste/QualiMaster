package com.judge.qualimaster.di

import com.judge.qualimaster.data.AppDatabase
import com.judge.qualimaster.data.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepositoryModule::class]
)
object FakeRepositoryModule {

    @Provides
    fun provideFakeRepository(db: AppDatabase) : Repository {
        return Repository(db.athleteDao())
    }

}
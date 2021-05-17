package com.judge.qualimaster.di

import com.judge.qualimaster.util.SimpleIntervalTimer
import com.judge.qualimaster.util.IntervalTimer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object IntervalTimerModule {

    @Provides
    fun provideIntervalTimer(): IntervalTimer {
        return SimpleIntervalTimer()
    }

}
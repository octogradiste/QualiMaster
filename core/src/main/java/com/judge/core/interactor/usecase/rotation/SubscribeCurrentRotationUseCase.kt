package com.judge.core.interactor.usecase.rotation

import com.judge.core.interactor.usecase.SubscribeCurrentTimeUseCase
import com.judge.core.interactor.usecase.competition.SubscribeCompetitionUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SubscribeCurrentRotationUseCase(
        private val subscribeCompetition: SubscribeCompetitionUseCase,
        private val currentTime: SubscribeCurrentTimeUseCase,
        private val getRotation: GetRotationUseCase,
        private val externalScope: CoroutineScope,
) {

    private val currentRotation = MutableStateFlow(0)

    operator fun invoke(competitionId: Int): StateFlow<Int> {
        val competition = subscribeCompetition(competitionId)
        val time = currentTime()

        externalScope.launch {
            time.collect { time ->
                val rotation = getRotation(time, competition.value)
                if (currentRotation.value != rotation) {
                    currentRotation.value = rotation
                }
            }
        }
        return currentRotation.asStateFlow()
    }

}
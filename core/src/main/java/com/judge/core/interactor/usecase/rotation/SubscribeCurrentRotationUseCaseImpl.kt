package com.judge.core.interactor.usecase.rotation

import com.judge.core.domain.result.Result
import com.judge.core.interactor.usecase.SubscribeCurrentTimeUseCase
import com.judge.core.interactor.usecase.competition.SubscribeCompetitionUseCase
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SubscribeCurrentRotationUseCaseImpl(
    private val subscribeCompetition: SubscribeCompetitionUseCase,
    private val currentTime: SubscribeCurrentTimeUseCase,
    private val getRotation: GetRotationUseCase,
) : SubscribeCurrentRotationUseCase {

    private val currentRotation = MutableStateFlow<Result<Int>>(Result.Success(0))

    override suspend operator fun invoke(competitionId: String, externalScope: CoroutineScope): StateFlow<Result<Int>> {
        val competition = subscribeCompetition(competitionId, externalScope)
        val time = currentTime(externalScope)

        externalScope.launch(CoroutineName("Current Rotation Coroutine")) {
            time.collect { time ->
                when (val result = competition.value) {
                    is Result.Success -> {
                        val rotation = getRotation(time, result.value)
                        currentRotation.value = Result.Success(rotation)
                    }
                    is Result.Error -> currentRotation.value = result
                }
            }
        }
        return currentRotation.asStateFlow()
    }

}
package com.judge.core.interactor

import com.judge.core.domain.model.Competition
import com.judge.core.domain.result.Result
import com.judge.core.interactor.usecase.*
import com.judge.core.interactor.usecase.athlete.AthleteBoulderBlockUseCase
import com.judge.core.interactor.usecase.athlete.AthleteTransitBlockUseCase
import com.judge.core.interactor.usecase.competition.RefreshCompetitionUseCase
import com.judge.core.interactor.usecase.competition.SetStartTimeUseCase
import com.judge.core.interactor.usecase.competition.SubscribeCompetitionUseCase
import com.judge.core.interactor.usecase.rotation.SubscribeCurrentRotationUseCase
import com.judge.core.presentation.AthleteListItem
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class CrucialInformationInteractor(
        private val athleteBoulderBlock: AthleteBoulderBlockUseCase,
        private val athleteTransitBlock: AthleteTransitBlockUseCase,
        val setStartTime: SetStartTimeUseCase,
        val subscribeCurrentTime: SubscribeCurrentTimeUseCase,
        val subscribeCurrentRotation: SubscribeCurrentRotationUseCase,
        val subscribeCompetition: SubscribeCompetitionUseCase,
        val refreshCompetition: RefreshCompetitionUseCase,
) {

    suspend fun createCrucialInformation(
            rotation: Result<Int>, comp: Result<Competition>
    ): Result<List<AthleteListItem>> = coroutineScope {
            val crucialInformation = mutableListOf<AthleteListItem>()

            if (comp is Result.Error) return@coroutineScope comp
            if (rotation is Result.Error) return@coroutineScope rotation

            comp as Result.Success
            rotation as Result.Success

            val currentDeferred = async { athleteBoulderBlock(rotation.value, comp.value, "Current") }
            val nextDeferred = async { athleteBoulderBlock(rotation.value + 1, comp.value, "Next") }
            val transitDeferred = async { athleteTransitBlock(rotation.value, comp.value, "Transit Zone") }

            when(val current = currentDeferred.await()) {
                is Result.Success -> crucialInformation.addAll(current.value)
                is Result.Error -> return@coroutineScope current
            }

            when(val next = nextDeferred.await()) {
                is Result.Success -> crucialInformation.addAll(next.value)
                is Result.Error -> return@coroutineScope next
            }

            when(val transit = transitDeferred.await()) {
                is Result.Success -> crucialInformation.addAll(transit.value)
                is Result.Error -> return@coroutineScope transit
            }

            return@coroutineScope Result.Success(crucialInformation)
        }
}

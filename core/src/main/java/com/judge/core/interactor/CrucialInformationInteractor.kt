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
            rotation: Int, comp: Competition
    ): Result<List<AthleteListItem>> = coroutineScope {
            val crucialInformation = mutableListOf<AthleteListItem>()

            val currentDeferred = async { athleteBoulderBlock(rotation, comp, "Current") }
            val nextDeferred = async { athleteBoulderBlock(rotation + 1, comp, "Next") }
            val transitDeferred = async { athleteTransitBlock(rotation, comp, "Transit Zone") }

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

package com.judge.core.interactor

import com.judge.core.domain.model.Competition
import com.judge.core.domain.result.Result
import com.judge.core.interactor.usecase.competition.RefreshCompetitionUseCase
import com.judge.core.interactor.usecase.athlete.AthleteBoulderBlockUseCase
import com.judge.core.interactor.usecase.athlete.AthleteTransitBlockUseCase
import com.judge.core.interactor.usecase.competition.SubscribeCompetitionUseCase
import com.judge.core.interactor.usecase.rotation.SubscribeCurrentRotationUseCase
import com.judge.core.interactor.usecase.athlete.AthleteIsolationBlockUseCase
import com.judge.core.interactor.usecase.athlete.AthleteMovingBlockUseCase
import com.judge.core.presentation.AthleteListItem
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class AthleteLocationInteractor(
        private val athleteBoulderBlock: AthleteBoulderBlockUseCase,
        private val athleteTransitBlock: AthleteTransitBlockUseCase,
        private val athleteMovingBlock: AthleteMovingBlockUseCase,
        private val athleteIsolationBlock: AthleteIsolationBlockUseCase,
        val subscribeCurrentRotation: SubscribeCurrentRotationUseCase,
        val subscribeCompetition: SubscribeCompetitionUseCase,
        val refreshCompetition: RefreshCompetitionUseCase,
) {

    suspend fun createAthleteLocationBlock(
            rotation: Int, comp: Competition
    ): Result<List<AthleteListItem>> = coroutineScope {
        val athleteLocation = mutableListOf<AthleteListItem>()

        val climbingDeferred = async { athleteBoulderBlock(rotation, comp, "Climbing") }
        val transitDeferred = async { athleteTransitBlock(rotation, comp, "Transit Zone") }
        val movingDeferred = async { athleteMovingBlock(rotation, comp,"Moving") }
        val isolationDeferred = async { athleteIsolationBlock(rotation, comp,"Isolation") }

        when(val climbing = climbingDeferred.await()) {
            is Result.Success -> athleteLocation.addAll(climbing.value)
            is Result.Error -> return@coroutineScope climbing
        }

        when(val transit = transitDeferred.await()) {
            is Result.Success -> athleteLocation.addAll(transit.value)
            is Result.Error -> return@coroutineScope transit
        }

        when(val moving = movingDeferred.await() ) {
            is Result.Success -> athleteLocation.addAll(moving.value)
            is Result.Error -> return@coroutineScope moving
        }

        when(val isolation = isolationDeferred.await()) {
            is Result.Success -> athleteLocation.addAll(isolation.value)
            is Result.Error -> return@coroutineScope isolation
        }

        return@coroutineScope Result.Success(athleteLocation)
    }
}
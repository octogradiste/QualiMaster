package com.judge.core.interactor

import com.judge.core.domain.model.Competition
import com.judge.core.domain.result.Result
import com.judge.core.interactor.usecase.competition.RefreshCompetitionUseCase
import com.judge.core.interactor.usecase.athlete.AthleteBoulderBlockUseCase
import com.judge.core.interactor.usecase.athlete.AthleteTransitBlockUseCase
import com.judge.core.interactor.usecase.competition.SubscribeCompetitionUseCaseImpl
import com.judge.core.interactor.usecase.rotation.SubscribeCurrentRotationUseCaseImpl
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
    val subscribeCurrentRotation: SubscribeCurrentRotationUseCaseImpl,
    val subscribeCompetition: SubscribeCompetitionUseCaseImpl,
    val refreshCompetition: RefreshCompetitionUseCase,
) {

    suspend fun createAthleteLocationBlock(
            rotation: Result<Int>, comp: Result<Competition>
    ): Result<List<AthleteListItem>> = coroutineScope {
        val athleteLocation = mutableListOf<AthleteListItem>()

        if (comp is Result.Error) return@coroutineScope comp
        if (rotation is Result.Error) return@coroutineScope rotation

        comp as Result.Success
        rotation as Result.Success

        val climbingDeferred = async { athleteBoulderBlock(rotation.value, comp.value, "Climbing") }
        val transitDeferred = async { athleteTransitBlock(rotation.value, comp.value, "Transit Zone") }
        val movingDeferred = async { athleteMovingBlock(rotation.value, comp.value,"Moving") }
        val isolationDeferred = async { athleteIsolationBlock(rotation.value, comp.value,"Isolation") }

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
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
            rotation: Int, comp: Competition): Result<List<AthleteListItem>> {
        val list = mutableListOf<AthleteListItem>()

        when(val block = athleteBoulderBlock(rotation, comp, "Current")) {
            is Result.Success -> list.addAll(block.value)
            is Result.Error -> return block
        }

        when(val block = athleteBoulderBlock(rotation + 1, comp, "Next")) {
            is Result.Success -> list.addAll(block.value)
            is Result.Error -> return block
        }

        when(val block = athleteTransitBlock(rotation, comp,"In Transit Zone")) {
            is Result.Success -> list.addAll(block.value)
            is Result.Error -> return block
        }

        when(val block = athleteMovingBlock(rotation, comp,"Moving")) {
            is Result.Success -> list.addAll(block.value)
            is Result.Error -> return block
        }

        when(val block = athleteIsolationBlock(rotation, comp,"Isolation")) {
            is Result.Success -> list.addAll(block.value)
            is Result.Error -> return block
        }

        return Result.Success(list)
    }
}
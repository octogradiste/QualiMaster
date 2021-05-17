package com.judge.core.interactor

import com.judge.core.domain.RotationPeriod
import com.judge.core.domain.model.Competition
import com.judge.core.domain.result.Result
import com.judge.core.interactor.usecase.athlete.AthleteBoulderBlockUseCase
import com.judge.core.interactor.usecase.competition.RefreshCompetitionUseCase
import com.judge.core.interactor.usecase.competition.SubscribeCompetitionUseCase
import com.judge.core.presentation.AthleteListItem

class RotationHistoryInteractor(
        private val athleteBoulderBlock: AthleteBoulderBlockUseCase,
        val subscribeCompetition: SubscribeCompetitionUseCase,
        val refreshCompetition: RefreshCompetitionUseCase,
) {
    suspend fun createRotationHistory(comp: Competition): Result<List<AthleteListItem>> {
        val list = mutableListOf<AthleteListItem>()

        val maxAthletes = comp.categories.maxOf { it.numOfAthletes }
        val maxRotation = RotationPeriod.maxRotation(comp.numOfAthletesClimbing, maxAthletes)

        for (rotation in 1..maxRotation) {
            when(val block = athleteBoulderBlock(rotation, comp, "Rotation $rotation")) {
                is Result.Success -> list.addAll(block.value)
                is Result.Error -> return block
            }
        }

        return Result.Success(list)
    }
}
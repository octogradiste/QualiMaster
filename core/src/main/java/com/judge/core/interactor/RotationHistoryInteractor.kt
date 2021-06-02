package com.judge.core.interactor

import com.judge.core.domain.RotationPeriod
import com.judge.core.domain.model.Competition
import com.judge.core.domain.result.Result
import com.judge.core.interactor.usecase.athlete.AthleteBoulderBlockUseCase
import com.judge.core.interactor.usecase.competition.RefreshCompetitionUseCase
import com.judge.core.interactor.usecase.competition.SubscribeCompetitionUseCaseImpl
import com.judge.core.presentation.AthleteListItem
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class RotationHistoryInteractor(
    private val athleteBoulderBlock: AthleteBoulderBlockUseCase,
    val subscribeCompetition: SubscribeCompetitionUseCaseImpl,
    val refreshCompetition: RefreshCompetitionUseCase,
) {
    suspend fun createRotationHistory(comp: Result<Competition>): Result<List<AthleteListItem>> {
        val list = mutableListOf<AthleteListItem>()

        if (comp is Result.Error) return comp

        comp as Result.Success

        val maxAthletes = if (comp.value.categories.isEmpty()) 0 else comp.value.categories.maxOf { it.numOfAthletes }
        val maxRotation = RotationPeriod.maxRotation(comp.value.numOfAthletesClimbing, maxAthletes)

        val blocksDeferred = mutableListOf<Deferred<Result<List<AthleteListItem>>>>()

        coroutineScope {
            for (rotation in 1..maxRotation) {
                blocksDeferred.add(async{ athleteBoulderBlock(rotation, comp.value, "Rotation $rotation") })
            }
        }

        for (blockDeferred in blocksDeferred) {
            when(val block = blockDeferred.await()) {
                is Result.Success -> list.addAll(block.value)
                is Result.Error -> return block
            }
        }

        return Result.Success(list)
    }
}
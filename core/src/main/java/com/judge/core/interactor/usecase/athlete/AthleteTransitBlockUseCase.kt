package com.judge.core.interactor.usecase.athlete

import com.judge.core.data.Repository
import com.judge.core.domain.model.Competition
import com.judge.core.domain.result.Result
import com.judge.core.presentation.AthleteListItem

class AthleteTransitBlockUseCase(
        private val repository: Repository,
        private val athleteBlock: AthleteBlockUseCase
) {

    suspend operator fun invoke(
            rotation: Int, comp: Competition, title: String
    ): Result<List<AthleteListItem>> {
        return when(val result = repository.getAthletesInTransitZone(rotation, comp)) {
            is Result.Success -> Result.Success(athleteBlock(title, result.value, comp))
            is Result.Error -> result
        }
    }
}
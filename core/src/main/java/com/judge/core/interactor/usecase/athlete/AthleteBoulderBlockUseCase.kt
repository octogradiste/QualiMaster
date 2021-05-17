package com.judge.core.interactor.usecase.athlete

import com.judge.core.data.Repository
import com.judge.core.domain.model.Competition
import com.judge.core.domain.result.Result
import com.judge.core.presentation.AthleteListItem

class AthleteBoulderBlockUseCase(
        private val repository: Repository
) {

    suspend operator fun invoke(
            rotation: Int, comp: Competition, title: String
    ): Result<List<AthleteListItem>> {

        // TODO add boulder

        val list = mutableListOf<AthleteListItem>()
        for (category in comp.categories) {
            when (val result = repository.getAthletesClimbing(rotation, comp, category)) {
                is Result.Success -> {
                    if (result.value.isNotEmpty()) {
                        list.add(AthleteListItem.CategoryItem(category))
                        list.addAll(result.value.map { AthleteListItem.AthleteItem(it) })
                    }
                }
                is Result.Error -> return result
            }
        }
        if (list.isNotEmpty()) {
            list.add(0, AthleteListItem.HeaderItem(title))
        }
        return Result.Success(list)
    }
}
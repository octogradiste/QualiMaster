package com.judge.core.interactor.usecase.athlete

import com.judge.core.data.Repository
import com.judge.core.domain.Location
import com.judge.core.domain.model.Boulder
import com.judge.core.domain.model.Competition
import com.judge.core.domain.result.Result
import com.judge.core.presentation.AthleteListItem

class AthleteBoulderBlockUseCase(
        private val repository: Repository,
        private val athleteBlock: AthleteBlockUseCase
) {

    suspend operator fun invoke(
            rotation: Int, comp: Competition, title: String
    ): Result<List<AthleteListItem>> {
        return when(val result = repository.getAthletesClimbing(rotation, comp)){
            is Result.Success -> Result.Success(athleteBlock(title, result.value, comp).map { item ->
               if (item is AthleteListItem.AthleteItem) {
                   val boulder = Location.athleteOnBoulder(
                       rotation, item.athlete.startOrder, comp.numOfAthletesClimbing)
                   AthleteListItem.BoulderItem(item.athlete, Boulder("B$boulder"))
               } else {
                   item
               }
            })
            is Result.Error -> result
        }
    }
}
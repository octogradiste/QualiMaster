package com.judge.core.interactor.usecase.competition

import com.judge.core.data.Repository
import com.judge.core.domain.model.Competition

class SetStartTimeUseCase(
        private val repository: Repository
) {

    suspend operator fun invoke(competition: Competition, time: Long) =
            repository.setStartTime(competition, time)

}
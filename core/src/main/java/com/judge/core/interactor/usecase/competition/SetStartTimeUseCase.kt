package com.judge.core.interactor.usecase.competition

import com.judge.core.data.Repository

class SetStartTimeUseCase(
        private val repository: Repository
) {

    suspend operator fun invoke(competitionId: Int, time: Long) =
            repository.setStartTime(competitionId, time)

}
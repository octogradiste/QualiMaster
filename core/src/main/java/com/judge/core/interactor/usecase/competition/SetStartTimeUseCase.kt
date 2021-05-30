package com.judge.core.interactor.usecase.competition

import com.judge.core.data.Repository
import com.judge.core.domain.model.Competition
import com.judge.core.domain.result.Response
import com.judge.core.domain.result.Result
import com.judge.core.domain.result.asResponse

class SetStartTimeUseCase(
        private val repository: Repository
) {

    suspend operator fun invoke(competition: Result<Competition>, time: Long): Response {
        return when (competition) {
            is Result.Success -> {
                repository.setStartTime(competition.value, time)
            }
            is Result.Error -> competition.asResponse()
        }
    }
}
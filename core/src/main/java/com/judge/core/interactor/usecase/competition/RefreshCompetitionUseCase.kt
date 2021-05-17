package com.judge.core.interactor.usecase.competition

import com.judge.core.data.Repository
import com.judge.core.domain.model.Competition
import com.judge.core.domain.result.Response
import com.judge.core.domain.result.Result
import com.judge.core.domain.result.asResult

class RefreshCompetitionUseCase(
        private val repository: Repository
) {

    suspend operator fun invoke(competitionId: Int) = repository.refreshCompetition(competitionId)

}
package com.judge.core.interactor.usecase.competition

import com.judge.core.data.Repository
import kotlinx.coroutines.flow.StateFlow

class SubscribeCompetitionUseCase(
        private val repository: Repository
) {

    operator fun invoke(competitionId: Int) = repository.subscribeCompetition(competitionId)

}
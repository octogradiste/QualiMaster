package com.judge.core.interactor.usecase.competition

import com.judge.core.data.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

class SubscribeCompetitionUseCase(
        private val repository: Repository,
        private val externalScope: CoroutineScope,
) {

    suspend operator fun invoke(competitionId: Long) =
        repository.subscribeCompetition(competitionId, externalScope)

}
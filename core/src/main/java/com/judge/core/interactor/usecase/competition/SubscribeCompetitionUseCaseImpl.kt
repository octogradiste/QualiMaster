package com.judge.core.interactor.usecase.competition

import com.judge.core.data.Repository
import kotlinx.coroutines.CoroutineScope

class SubscribeCompetitionUseCaseImpl(
    private val repository: Repository,
) : SubscribeCompetitionUseCase {

    override suspend operator fun invoke(competitionId: Long, externalScope: CoroutineScope) =
        repository.subscribeCompetition(competitionId, externalScope)

}
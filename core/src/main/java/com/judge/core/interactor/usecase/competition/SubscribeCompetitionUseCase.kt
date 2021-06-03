package com.judge.core.interactor.usecase.competition

import com.judge.core.domain.model.Competition
import com.judge.core.domain.result.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

interface SubscribeCompetitionUseCase {
    suspend operator fun invoke(competitionId: String, externalScope: CoroutineScope): StateFlow<Result<Competition>>
}
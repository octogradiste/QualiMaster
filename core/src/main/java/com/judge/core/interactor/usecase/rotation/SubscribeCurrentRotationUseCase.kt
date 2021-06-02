package com.judge.core.interactor.usecase.rotation

import com.judge.core.domain.result.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

interface SubscribeCurrentRotationUseCase {
    suspend operator fun invoke(competitionId: Long, externalScope: CoroutineScope): StateFlow<Result<Int>>
}
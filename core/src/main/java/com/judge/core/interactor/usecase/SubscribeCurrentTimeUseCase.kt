package com.judge.core.interactor.usecase

import com.judge.core.util.tick.TickHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SubscribeCurrentTimeUseCase(
        private val tickHandler: TickHandler,
) {

    operator fun invoke(externalScope: CoroutineScope) = tickHandler.subscribe(externalScope)

}
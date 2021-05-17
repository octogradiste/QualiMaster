package com.judge.core.interactor.usecase

import com.judge.core.util.tick.TickHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SubscribeCurrentTimeUseCase(
        externalScope: CoroutineScope,
        tickHandler: TickHandler,
) {

    private val tick = tickHandler.subscribe()
    private val currentTime = MutableStateFlow(0L)

    init {
        externalScope.launch {
            tick.collect { time ->
                currentTime.value = time
            }
        }
    }

    operator fun invoke() = currentTime.asStateFlow()

}
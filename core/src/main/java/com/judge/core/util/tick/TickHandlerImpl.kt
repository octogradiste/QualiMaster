package com.judge.core.util.tick

import com.judge.core.util.Clock
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TickHandlerImpl (
        externalScope: CoroutineScope,
        clock: Clock,
        tickIntervalMs: Long = 200
) : TickHandler {

    private val _tick = MutableStateFlow(0L)

    init {
        externalScope.launch {
            while(true) {
                _tick.emit(clock.currentTimeMs())
                delay(tickIntervalMs)
            }
        }
    }

    override fun subscribe() = _tick.asStateFlow()
}
